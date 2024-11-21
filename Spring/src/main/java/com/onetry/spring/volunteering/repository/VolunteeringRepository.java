package com.onetry.spring.volunteering.repository;


import com.onetry.spring.user.entity.User;
import com.onetry.spring.volunteering.entity.Volunteering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteeringRepository extends JpaRepository<Volunteering, Long> {
    List<Volunteering> findByUser(User user);
}
