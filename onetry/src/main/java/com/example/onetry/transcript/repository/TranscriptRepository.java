package com.example.onetry.transcript.repository;

import com.example.onetry.transcript.entity.Transcript;
import com.example.onetry.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
    List<Transcript> findByUser(User user);
}
