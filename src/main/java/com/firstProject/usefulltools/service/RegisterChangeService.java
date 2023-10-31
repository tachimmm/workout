package com.firstProject.usefulltools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.firstProject.usefulltools.entity.UserInfo;
import com.firstProject.usefulltools.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterChangeService {

    /**PasswordEncoder */
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoRepository userInfoRepository;


    public void updatePassword(String loginId, String newPassword) {
        
        UserInfo userInfo = userInfoRepository.findById(loginId).orElse(null);
        
        if (userInfo != null) {

            var encodedChangePassword = passwordEncoder.encode(newPassword);

            userInfo.setPassword(encodedChangePassword);
            userInfoRepository.save(userInfo);
        }
    }
}



