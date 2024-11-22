package com.example.onetry.resume.repository;

import com.example.onetry.resume.entity.Resume;
import com.example.onetry.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeCustomRepository {
    Optional<Resume> findByUser(User user);
}
