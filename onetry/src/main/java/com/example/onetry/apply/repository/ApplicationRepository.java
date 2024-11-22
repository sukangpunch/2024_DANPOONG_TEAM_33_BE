package com.example.onetry.apply.repository;

import com.example.onetry.apply.entity.Application;
import com.example.onetry.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);

    List<Application> findByUserOrderByCreateDateDesc(User user);

    List<Application> findByUserOrderByCreateDateAsc(User user);


    List<Application> findByUserOrderByApplicationDeadLineDesc(User user);

    List<Application> findByUserOrderByApplicationDeadLineAsc(User user);

    List<Application> findByUserOrderByCompanyNameAsc(User user);
    List<Application> findByUserOrderByCompanyNameDesc(User user);

}
