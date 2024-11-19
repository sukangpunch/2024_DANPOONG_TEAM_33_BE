package com.onetry.spring.userpreference.repository;


import com.onetry.spring.user.entity.User;
import com.onetry.spring.userpreference.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    Optional<UserPreference> findByUser(User user);
}
