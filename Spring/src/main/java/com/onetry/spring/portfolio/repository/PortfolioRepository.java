package com.onetry.spring.portfolio.repository;


import com.onetry.spring.portfolio.entity.Portfolio;
import com.onetry.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    List<Portfolio> findByUser(User user);

    Optional<Portfolio> findByGenerateFileName(String generateFileName);

}
