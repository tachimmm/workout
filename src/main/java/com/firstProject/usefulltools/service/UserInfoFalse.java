package com.firstProject.usefulltools.service;

import com.firstProject.usefulltools.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import com.firstProject.usefulltools.entity.UserInfo;
import java.util.Optional;

@RequiredArgsConstructor
public class UserInfoFalse {
    
    private final UserInfoRepository repository;

    public Optional<UserInfo> searchUserByid(String loginId) {

        return repository.findById(loginId);

    }
}

/*
 * Optional は、Javaで導入されたクラスで、値が存在するかどうかを表現するためのコンテナ
 * 値の存在の有無,Nullを回避する
 * 
*/