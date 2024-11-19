package com.onetry.spring.certification.repository;


import com.onetry.spring.certification.entity.Certification;
import com.onetry.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByUser(User user);
}
