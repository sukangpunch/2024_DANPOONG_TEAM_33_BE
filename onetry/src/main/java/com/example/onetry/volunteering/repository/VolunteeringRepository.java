package com.example.onetry.volunteering.repository;

import com.example.onetry.user.entity.User;
import com.example.onetry.volunteering.entity.Volunteering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteeringRepository extends JpaRepository<Volunteering, Long> {
    List<Volunteering> findByUser(User user);
}
