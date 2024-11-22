package com.example.onetry.certification.repository;

import com.example.onetry.certification.entity.Certification;
import com.example.onetry.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByUser(User user);
    Optional<Certification> findByGenerateFileName(String generateFileName);
}
