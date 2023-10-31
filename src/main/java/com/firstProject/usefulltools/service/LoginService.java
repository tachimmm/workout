package com.firstProject.usefulltools.service;
//レポジトリクラスを呼び出すクラス
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.firstProject.usefulltools.entity.UserInfo;
import com.firstProject.usefulltools.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;

@Service
//
@RequiredArgsConstructor
public class LoginService {
    //newされたものがrepositoryに入る
    private final UserInfoRepository repository;
    
    public Optional<UserInfo> searchUserByid(String loginId){

        return repository.findById(loginId);
    }
}
