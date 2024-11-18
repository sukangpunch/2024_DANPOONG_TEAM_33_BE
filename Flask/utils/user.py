def user_certifications_classification(company_list, user_certifications):
    """
    company_list: 기업이 요구하는 자격증 리스트 (문자열의 리스트 형식)
    user_certifications: 유저가 보유한 자격증 리스트 (문자열의 리스트 형식)
    """

    for company in company_list:

        have = []
        havent = []

        for user in user_certifications:
            if user in company["certifications"]["essential"]:
                have.append(user)
            else:
                havent.append(user)

            company['certifications'] = {}
            company["certifications"]["have"] = have
            company["certifications"]["havent"] = havent
        
        del company['certifications']['essential']

    return company_list