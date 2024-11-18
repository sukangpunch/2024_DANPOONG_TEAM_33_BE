def simplify_certifications_to_list(company_list):
    """
    주어진 데이터에서 'essential' 및 'additional' 필드를 리스트로 단순화.

    Args:
        company_list (list): 각 데이터 항목이 포함된 리스트.

    Returns:
        list: 필드가 단순화된 데이터 리스트.
    """
    for company in company_list:
        certifications = company.get("certifications", {})
        
        # 'essential' 필드 변환
        if "essential" in certifications and isinstance(certifications["essential"], list):
            certifications["essential"] = [cert.get("name", "") for cert in certifications["essential"]]
        
        # 'additional' 필드 변환
        if "additional" in certifications and isinstance(certifications["additional"], list):
            certifications["additional"] = [cert.get("name", "") for cert in certifications["additional"]]
        
        # 업데이트된 certifications 저장
        company["certifications"] = certifications

    return company_list