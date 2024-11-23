package com.example.onetry.resume.repository;

import com.example.onetry.resume.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByResumeIdOrderByIdAsc(Long resumeId);
}
