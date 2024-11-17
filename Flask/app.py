import os

from flask import Flask, jsonify, request, Blueprint, g
from flask_restx import Api, Resource

import pymysql

from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi

from dotenv import load_dotenv

load_dotenv()

FLASK_PORT = os.environ.get('FLASK_PORT')
########################################################################################################################################################################################################################    
# MONGODB 연결

MONGODB_URI = os.environ.get('MONGODB_URI')
MONGODB_DB = os.environ.get('MONGODB_DB')

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

app = Flask(__name__)

blueprint = Blueprint('api', __name__, url_prefix='/flask')

# API 인스턴스 생성
api = Api(blueprint, version='1.00', title='Onet Flask API',
          description='Flask API for Onet', doc='/docs', )

app.register_blueprint(blueprint)
########################################################################################################################################################################################################################    
# 네임스페이스 설정  

ping = api.namespace('ping', description='상태 확인')
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
# App Run
if __name__ == '__main__':
    app.run(debug=True, port=FLASK_PORT)