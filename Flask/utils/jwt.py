import jwt
from jwt.exceptions import InvalidTokenError, ExpiredSignatureError

def decode_token(JWT_SECRET_KEY, auth_header):

    # 헤더에 Authorization 토큰이 없거나 잘못된 경우 처리
    if not auth_header or not auth_header.startswith("Bearer "):
        return 400

    # "Bearer " 이후의 실제 토큰 값 추출
    token = auth_header[len("Bearer "):].strip()

    try:
        # JWT 토큰 디코딩
        decoded_payload = jwt.decode(token, JWT_SECRET_KEY, algorithms=["HS256"])

        # 디코딩된 페이로드에서 user_id(PK) 추출
        user_id = decoded_payload.get("id")
        if not user_id:
            return 400

        # 성공적으로 user_id 반환
        return user_id
    except ExpiredSignatureError:
        return 401
    except InvalidTokenError:
        return 401