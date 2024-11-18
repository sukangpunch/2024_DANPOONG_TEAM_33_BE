from ..app import get_db_connection
from ..app import get_cursor

def get_user_certifications_mysql(user_id):
    """
    유저가 보유한 자격증 리스트를 반환하는 함수.
    """
    cursor = get_cursor()

    sql = "SELECT certification_name FROM certification WHERE user_id = %s"

    try:
        cursor.execute(sql, (user_id,))
        user_certification = cursor.fetchall()
    except Exception as e:
        print(e)
        return 500

    # 유저 자격증 리스트화
    user_certification = [cert[0] for cert in user_certification]

    return user_certification
