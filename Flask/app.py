import os

from flask import Flask, jsonify, request, Blueprint, g
from flask_restx import Api, Resource

import pymysql

from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi

from dotenv import load_dotenv
load_dotenv()

JWT_SECRET_KEY = os.getenv('JWT_SECRET_KEY')
########################################################################################################################################################################################################################    
# Utis 함수

from utils.jwt import decode_token
########################################################################################################################################################################################################################    
# MongoDB 연결

MONGODB_URI = os.environ.get('MONGODB_URI')
MONGODB_DB = os.environ.get('MONGODB_DB')

# MongoDB Collections
COLLECTION_CMI = os.environ.get('COLLECTION_CMI')

client = MongoClient(MONGODB_URI, server_api=ServerApi('1'))
db = client[MONGODB_DB]
########################################################################################################################################################################################################################    
# MySQL 연결

def get_db_connection():
    if 'db' not in g:
        g.db = pymysql.connect(
            host=os.getenv('DB_HOST'),
            port=int(os.getenv('DB_PORT')),
            user=os.getenv('DB_USER'),
            password=os.getenv('DB_PASSWORD'),
            db=os.getenv('DB_NAME'),
            charset='utf8'
        )
    return g.db

def get_cursor():
    db = get_db_connection()
    if 'cursor' not in g:
        g.cursor = db.cursor()
    return g.cursor
########################################################################################################################################################################################################################    
# FLASK 설정

FLASK_PORT = os.environ.get('FLASK_PORT')

app = Flask(__name__)

blueprint = Blueprint('api', __name__, url_prefix='/flask')

# API 인스턴스 생성
api = Api(blueprint, version='1.00', title='Onet Flask API',
          description='Flask API for Onet', doc='/docs', )

app.register_blueprint(blueprint)
########################################################################################################################################################################################################################    
# 네임스페이스 설정  

ping = api.namespace('ping', description = '상태 확인')
company_list = api.namespace('company/list', description = '회사 리스트')
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
    @company_list.header('Authorization', 'JWT 토큰', required = False)
    
    def get(self):
        collection = db[COLLECTION_CMI]
        auth_header = request.headers.get('Authorization')

        feild = {
            '_id': 0,
            'info_no': 1,
            'company.name': 1, 
            'certifications.essential': 1,
        }

        # 로그인 상태가 아니거나 토큰이 없는 경우
        if not auth_header:
            company_list = collection.find({}, feild).sort("hiring_period.start_date.$date", -1).limit(6)
            company_list = list(company_list)

            # 'essential' 필드 리스트화
            for company in company_list:
                certifications = company.get("certifications", {})
                
                if "essential" in certifications and isinstance(certifications["essential"], list):
                    certifications["essential"] = [cert.get("name", "") for cert in certifications["essential"]]
                
                # 업데이트된 certifications 저장
                company["certifications"] = certifications
        
        user_id = decode_token(JWT_SECRET_KEY, auth_header)

        return {
            "status": "success",
            "data": result
        }


########################################################################################################################################################################################################################    
# App Run
if __name__ == '__main__':
    app.run(debug=True, port=FLASK_PORT)