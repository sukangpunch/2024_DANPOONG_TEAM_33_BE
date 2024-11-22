package com.example.onetry.resume.repository;

import com.example.onetry.resume.dto.res.ResumeInfo;
import com.example.onetry.user.entity.User;

public interface ResumeCustomRepository {
    ResumeInfo getResume(User reqUser);
}
