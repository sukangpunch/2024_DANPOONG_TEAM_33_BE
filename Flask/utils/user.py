def user_certifications_classification_by_essential(company_list, user_certifications):
    """
    company_list: 기업이 요구하는 자격증 리스트 (문자열의 리스트 형식)
    user_certifications: 유저가 보유한 자격증 리스트 (문자열의 리스트 형식)
    """

    for company in company_list:

        have = []
        havent = []

        for company_cert in company["certificationsEssential"]:
            if company_cert in user_certifications:
                have.append(company_cert)
            else:
                havent.append(company_cert)

        company["certificationsEssentialUserHave"] = have
        company["certificationsEssentialUserHavent"] = havent

    return company_list