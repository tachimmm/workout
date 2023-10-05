package com.firstProject.usefulltools.service;

import com.firstProject.usefulltools.repository.UserInfoRepository;
import com.firstProject.usefulltools.entity.UserInfo;

import java.util.Optional;

public class UserInfoFalse {
    private final UserInfoRepository repository;
    
    public UserInfoFalse(UserInfoRepository repository) {
        this.repository = repository;
    }
    
    public Optional<UserInfo> searchUserByid(String loginId) {
        return repository.findById(loginId);
    }
}
