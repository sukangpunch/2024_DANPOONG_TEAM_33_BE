import os
import random

from flask import Flask, jsonify, request, Blueprint, g
from flask_restx import Api, Resource
from flask_cors import CORS

from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi

from dotenv import load_dotenv
load_dotenv()

JWT_SECRET_KEY = os.getenv('JWT_SECRET_KEY')
########################################################################################################################################################################################################################    
# Utis 함수

from utils.jwt import decode_token
from utils.user import user_certifications_classification_by_essential
from utils.simplify import simplify_certifications_to_list_names
from utils.mysql.user import get_user_certifications_mysql
from utils.date import add_d_day_to_companies, serialize_company_list
########################################################################################################################################################################################################################    
# MySQL 연결

from utils.mysql.connection import get_db_connection, get_cursor
########################################################################################################################################################################################################################    
# MongoDB 연결

MONGODB_URI = os.environ.get('MONGODB_URI')
MONGODB_DB = os.environ.get('MONGODB_DB')

# MongoDB Collections
COLLECTION_CMI = os.environ.get('COLLECTION_CMI')
COLLECTION_CERT = os.environ.get('COLLECTION_CERT')
COLLECTION_CMI_FLAT = os.environ.get('COLLECTION_CMI_FLAT')

client = MongoClient(MONGODB_URI, server_api=ServerApi('1'))
db = client[MONGODB_DB]
########################################################################################################################################################################################################################    
# FLASK 설정

FLASK_PORT = os.environ.get('FLASK_PORT')

app = Flask(__name__)
CORS(app)

blueprint = Blueprint('api', __name__, url_prefix='/flask')

# API 인스턴스 생성
api = Api(
    blueprint,
    version='1.00',
    title='Onet Flask API',
    description='Flask API for Onet',
    doc='/docs'
)

app.register_blueprint(blueprint)
########################################################################################################################################################################################################################    
# 네임스페이스 설정  

ping = api.namespace('ping', description = '상태 확인')
company_list = api.namespace('company/list', description = '회사 리스트')
# search = api.namespace('search', description = '검색')
########################################################################################################################################################################################################################
# 필드 설정
ordinary = {
            '_id': 0,
            }

minimul = {
            '_id': 0,
            'infoNo': 1,
            'companyName': 1, 
            'certificationsEssential': 1,
        }

require_end_date = {
            '_id': 0,
            'infoNo': 1,
            'companyName': 1, 
            'certificationsEssential': 1,
            'hiringPeriodEndDate': 1,
        }
########################################################################################################################################################################################################################
@ping.route('/')
class Ping(Resource):
    @ping.doc('Ping - Pong')
    def get(self):
        return {
            "status": "success",
        }

########################################################################################################################################################################################################################    
@ping.route('/mongo')
class PingMongo(Resource):
    @ping.doc('Ping - MongoDB')
    def get(self):
        return {
            "status": "success",
            "message": db.command("ping")
        }
########################################################################################################################################################################################################################    
@company_list.route('/recent/page/main')
class RecentCompanyList(Resource):
    @company_list.header('Bearer', 'JWT 토큰', required = False)
    @company_list.doc(description='JWT 토큰 있으면, 최근 채용 공고에서 요구하는 필수 자격증과 회원이 보유한 자격증 비교.\n 없으면 최근 채용 공고 리스트 반환.')
    
    @company_list.response(402, 'Invalid Token')
    @company_list.response(401, 'Expired Token')
    @company_list.response(500, 'DB Connection Error')

    def get(self) -> str:
        '''최근 채용 공고 기업 메인페이지용'''
        collection = db[COLLECTION_CMI_FLAT]
        token = request.headers.get('Bearer')

        company_list = collection.find({}, minimul).sort("hiringPeriodStartDate", -1).limit(6)
        company_list = list(company_list)

        company_list = simplify_certifications_to_list_names(company_list)

        # 토큰이 있는 경우
        if token:
            user_id = decode_token(JWT_SECRET_KEY, token)

            if user_id == 402:
                return {
                    "status": "fail",
                    "message": "Invalid Token"
                }, 402
            elif user_id == 401:
                return {
                    "status": "fail",
                    "message": "Expired Token"
                }, 401
            else:
                user_certification = get_user_certifications_mysql(user_id)

                if user_certification == 500:
                    return {
                        "status": "fail",
                        "message": "DB Connection Error"
                    }, 500

                # 회사에서 요구하는 자격증 중 회원이 가지고 있는 자격증과 비교하여 분류
                company_list = user_certifications_classification_by_essential(company_list, user_certification)

        return {
            "description": "최근 채용공고 리스트",
            "company_list": company_list
        }, 200

########################################################################################################################################################################################################################
@company_list.route('/certification/page/main')
class SearchCertification(Resource):
    @company_list.header('Bearer', 'JWT 토큰', required = False)
    @company_list.doc(description='JWT 토큰 있으면, 회원이 가지고 있는 자격증을 기반으로 회사 리스트를 반환.\n 없으면 랜덤 자격증을 기반으로 회사 리스트를 반환.')

    @company_list.response(402, 'Invalid Token')
    @company_list.response(401, 'Expired Token')
    @company_list.response(500, 'DB Connection Error')

    def get(self) -> str:
        """@@ 자격증으로 가능한 기업 메인페이지용"""

        token = request.headers.get('Bearer')
        
        # 로그인 상태가 아니거나 토큰이 없는 경우
        if not token:
            collection = db[COLLECTION_CERT]
            certification_list = collection.find({}, {'_id': 0, 'certifications': 1})
            certification_list = list(certification_list)

            random_certification = random.choice(certification_list)
            random_certification = random.choice(random_certification['certifications'])

            query = {
                'certificationsEssential.name': {'$in': [random_certification]}
            }

            description = "랜덤 필수 자격증 기반 회사 리스트"

        else:
            user_id = decode_token(JWT_SECRET_KEY, token)

            if user_id == 402:
                return {
                    "status": "fail",
                    "message": "Token has expired"
                }, 402
            elif user_id == 401:
                return {
                    "status": "fail",
                    "message": "Invalid token"
                }, 401
            
            else: # 유저 ID 가 정상적으로 반환된 경우 MySQL에서 유저의 자격증 리스트를 가져옴
                user_certification = get_user_certifications_mysql(user_id)

                if user_certification == 500:
                    return {
                        "status": "fail",
                        "message": "DB Connection Error"
                    }, 500
                
                user_random_certification = random.choice(user_certification)

                query = {
                'certificationsEssential.name': {'$in': [user_random_certification]}
                }

                description = "유저 보유 자격증 기반 회사 리스트"
        
        collection = db[COLLECTION_CMI_FLAT]
        company_list = collection.find(query, {'_id': 0, 'infoNo': 1, 'companyName': 1, 'hiringPeriodEndDate': 1}).limit(6)
        company_list = list(company_list)

        company_list = serialize_company_list(company_list)

        company_list = {
            "description": description,
            "search_certification": random_certification if not token else user_random_certification,
            'company_list': company_list
        }

        return company_list, 200

########################################################################################################################################################################################################################    
@company_list.route('/try/page/<string:page>')
class TryCompanyList(Resource):
    @company_list.header('Bearer', 'JWT 토큰', required = False)
    @company_list.param('page', '페이지 유형 main / try', 'query')
    @company_list.doc(description='JWT가 없으면 401 반환. 유저의 자격증 적합도에 따른 회사 리스트 반환.\n page 가 main 이면 2개, try 이면 4개 반환.')
    
    @company_list.response(400, 'Unauthorized')
    @company_list.response(402, 'Invalid Token')
    @company_list.response(401, 'Expired Token')
    @company_list.response(404, 'Invalid Page')
    @company_list.response(500, 'DB Connection Error')

    def get(self, page) -> str:
        """유저의 자격증 적합도에 따른 회사 리스트 1Try / 2Try / 3Try"""

        token = request.headers.get('Bearer')
        
        if not token:
            # 토큰 없을 경우 애초에 표시 안함
            return {
                "status": "fail",
                "message": "Unauthorized"
            }, 400
        
        collection = db[COLLECTION_CMI_FLAT]
        company_list = collection.find({}, require_end_date)
        company_list = list(company_list)

        company_list = simplify_certifications_to_list_names(company_list)
        company_list = add_d_day_to_companies(company_list)

        user_id = decode_token(JWT_SECRET_KEY, token)

        if user_id == 402:
            return {
                "status": "fail",
                "message": "Invalid Token"
            }, 402
        elif user_id == 401:
            return {
                "status": "fail",
                "message": "Expired Token"
            }, 401
        
        else:
            user_certification = get_user_certifications_mysql(user_id)

            if user_certification == 500:
                return {
                    "status": "fail",
                    "message": "DB Connection Error"
                }, 500

            # 회사에서 요구하는 자격증 중 회원이 가지고 있는 자격증과 비교하여 분류 - have, havent
            company_list = user_certifications_classification_by_essential(company_list, user_certification)

            one_try = []
            two_try = []
            three_try = []

            for company in company_list:
                cert_len = len(company["certificationsEssentialUserHavent"])
                if cert_len == 0:
                    one_try.append(company)
                elif cert_len == 1:
                    two_try.append(company)
                else:
                    three_try.append(company)

                del company["certificationsEssential"]
            
        
        if page == 'main':
            output_count = 2
        elif page == 'try':
            output_count = 4
        else:
            return {
                "status": "fail",
                "message": "Invalid Page"
            }, 404
        
        return {
            "description": "유저의 자격증 적합도에 따른 회사 리스트",
            'one_try': [random.sample(one_try, min(len(one_try), output_count))],
            'two_try': [random.sample(two_try, min(len(two_try), output_count))],
            'three_try': [random.sample(three_try, min(len(three_try), output_count))],
        }, 200

########################################################################################################################################################################################################################    
# App Run
if __name__ == '__main__':
    app.run(debug=True, port=FLASK_PORT)