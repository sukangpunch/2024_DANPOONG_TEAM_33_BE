package com.example.onetry.portfolio.repository;

import com.example.onetry.portfolio.entity.Portfolio;
import com.example.onetry.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long>{
    List<Portfolio> findByUser(User user);
    Optional<Portfolio> findByGenerateFileName(String generateFileName);

}
