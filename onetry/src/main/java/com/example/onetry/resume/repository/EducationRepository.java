package com.example.onetry.resume.repository;

import com.example.onetry.resume.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByResumeIdOrderByIdAsc(Long resumeId);
}
