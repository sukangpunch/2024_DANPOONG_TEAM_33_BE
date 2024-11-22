package com.example.onetry.userpreference.repository;

import com.example.onetry.user.entity.User;
import com.example.onetry.userpreference.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    Optional<UserPreference> findByUser(User user);
}
