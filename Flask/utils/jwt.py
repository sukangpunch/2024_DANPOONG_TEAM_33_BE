import jwt
from jwt.exceptions import InvalidTokenError, ExpiredSignatureError

def decode_token(JWT_SECRET_KEY, auth_header):

    # "Bearer " 이후의 실제 토큰 값 추출
    token = auth_header[len("Bearer "):].strip()
    print(f"Decoded token: {token}")  # 디버깅용 로그

    try:
        # JWT 토큰 디코딩
        decoded_payload = jwt.decode(token, JWT_SECRET_KEY, algorithms=["HS256"])
        print(f"Decoded payload: {decoded_payload}")  # 디버깅용 로그

        # 디코딩된 페이로드에서 user_id(PK) 추출
        user_id = decoded_payload.get("id")
        if not user_id:
            return 400

        # 성공적으로 user_id 반환
        return user_id
    except ExpiredSignatureError:
        print("Expired token")  # 디버깅용 로그
        return 401
    except InvalidTokenError:
        print("Invalid token")  # 디버깅용 로그
        return 401
