package com.onetry.spring.transcript.repository;


import com.onetry.spring.transcript.entity.Transcript;
import com.onetry.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
    List<Transcript> findByUser(User user);
}
