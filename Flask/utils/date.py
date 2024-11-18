def add_d_day_to_companies(company_list):
    """
    회사 리스트에 D-day 값을 추가.

    Args:
        company_list (list): 회사 데이터가 포함된 리스트.

    Returns:
        list: D-day 값이 추가된 회사 데이터 리스트.
    """
    for company in company_list:
        end_date = company["hiring_period"].get("end_date")
        if end_date:
            company["hiring_period"]["D-day"] = calculate_d_day(end_date)
    return company_list