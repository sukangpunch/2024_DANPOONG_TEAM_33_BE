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
from utils.user import user_certifications_classification
from utils.date import add_d_day_to_companies
from utils.simplify import simplify_certifications_to_list
from utils.mysql.user import get_user_certifications_mysql
from utils.date import serialize_company_list
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
            'info_no': 1,
            'company.name': 1, 
            'certifications.essential': 1,
        }

require_end_date = {
            '_id': 0,
            'info_no': 1,
            'company.name': 1, 
            'certifications.essential': 1,
            'hiring_period.end_date': 1,
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
    @company_list.doc(description='최근 채용공고 리스트를 반환합니다.')
    
    def get(self):
        collection = db[COLLECTION_CMI]
        auth_header = request.headers.get('Bearer')

        company_list = collection.find({}, minimul).sort("hiring_period.start_date.$date", -1).limit(6)
        company_list = list(company_list)
        company_list = simplify_certifications_to_list(company_list)

        # 토큰이 있는 경우
        if auth_header:
            user_id = decode_token(JWT_SECRET_KEY, auth_header)

            if user_id == 400:
                return {
                    "status": "fail",
                    "message": "Invalid Token"
                }, 400
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
                company_list = user_certifications_classification(company_list, user_certification)

        return {
            "description": "최근 채용공고 리스트",
            "company_list": company_list
        }, 200

########################################################################################################################################################################################################################
@company_list.route('/certification/page/main')
class SearchCertification(Resource):
    @company_list.header('Bearer', 'JWT 토큰', required = False)
    @company_list.doc(description='자격증 기반 회사 리스트를 반환합니다.')

    def get(self):
        auth_header = request.headers.get('Bearer')
        
        # 로그인 상태가 아니거나 토큰이 없는 경우
        if not auth_header:
            collection = db[COLLECTION_CERT]
            certification_list = collection.find({}, {'_id': 0,'info_no':1, 'certifications': 1})
            certification_list = list(certification_list)

            random_certification = random.choice(certification_list)
            random_certification = random.choice(random_certification['certifications'])

            query = {
                'certifications.essential.name': {'$in': [random_certification]}
            }

            description = "랜덤 필수 자격증 기반 회사 리스트"
        else:
            user_id = decode_token(JWT_SECRET_KEY, auth_header)

            if user_id == 400:
                return {
                    "status": "fail",
                    "message": "Invalid Token"
                }, 400
            elif user_id == 401:
                return {
                    "status": "fail",
                    "message": "Expired Token"
                }, 401
            else:
                print(user_id)
                user_certification = get_user_certifications_mysql(user_id)

                if user_certification == 500:
                    return {
                        "status": "fail",
                        "message": "DB Connection Error"
                    }, 500
                
                user_random_certification = random.choice(user_certification)

                query = {
                'certifications.essential.name': {'$in': [user_random_certification]}
                }

                description = "유저 보유 자격증 기반 회사 리스트"
        
        collection = db[COLLECTION_CMI]
        company_list = collection.find(query, {'_id': 0, 'company.name': 1, 'hiring_period.end_date': 1}).limit(4)
        company_list = list(company_list)

        company_list = serialize_company_list(company_list)

        company_list = {
            "description": description,
            "search_certification": random_certification if not auth_header else user_random_certification,
            'company_list': company_list
        }

        return company_list, 200

########################################################################################################################################################################################################################    
@company_list.route('/try/page/<string:page>')
class TryCompanyList(Resource):
    @company_list.header('Bearer', 'JWT 토큰', required = False)
    @company_list.param('page', '페이지 유형 main / try', 'query')
    @company_list.doc(description='유저의 자격증 적합도에 따른 회사 리스트를 반환합니다.')
    
    def get(self, page):
        auth_header = request.headers.get('Bearer')
        
        collection = db[COLLECTION_CMI]
        company_list = collection.find({}, require_end_date)
        company_list = list(company_list)

        company_list = serialize_company_list(company_list)
        company_list = add_d_day_to_companies(company_list)

        if not auth_header:
            # 토큰 없을 경우 애초에 표시 안함
            return {
                "status": "fail",
                "message": "Unauthorized"
            }, 401

        user_id = decode_token(JWT_SECRET_KEY, auth_header)

        if user_id == 400:
            return {
                "status": "fail",
                "message": "Invalid Token"
            }, 400
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
            company_list = user_certifications_classification(company_list, user_certification)

            one_try = []
            two_try = []
            three_try = []

            for company in company_list:
                cert_len = len(company["certifications"]["havent"])
                if cert_len == 0:
                    company['userTryRate'] = 1
                    one_try.append(company)
                elif cert_len == 1:
                    company['userTryRate'] = 2
                    two_try.append(company)
                else:
                    company['userTryRate'] = 3
                    three_try.append(company)
            
        
        if page == 'main':
            output_count = 2
        elif page == 'try':
            output_count = 4
        else:
            return {
                "status": "fail",
                "message": "Invalid Page"
            }, 400
        
        return {
            "description": "유저의 자격증 적합도에 따른 회사 리스트",
            "company_list": {'one_try': [random.sample(one_try, min(len(one_try), output_count))],
                             'two_try': [random.sample(two_try, min(len(two_try), output_count))],
                             'three_try': [random.sample(three_try, min(len(three_try), output_count))],
                            }
        }, 200

########################################################################################################################################################################################################################    
# App Run
if __name__ == '__main__':
    app.run(debug=True, port=FLASK_PORT)