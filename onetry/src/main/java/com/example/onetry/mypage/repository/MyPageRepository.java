package com.example.onetry.mypage.repository;


import com.example.onetry.mypage.dto.MyPageStatisticsDto;
import com.example.onetry.mypage.entity.MyPage;
import com.example.onetry.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MyPageRepository extends JpaRepository<MyPage, Long> {
    Optional<MyPage> findByUser(User user);
    @Query("SELECT new com.example.onetry.mypage.dto.MyPageStatisticsDto(AVG(m.portfolioCount), AVG(m.volunteeringTime), AVG(m.certificationCount)) " +
            "FROM MyPage m WHERE m.user.id != :userId")
    MyPageStatisticsDto findAverageExcludingUser(@Param("userId") Long userId);



}
