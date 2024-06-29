package com.firstProject.usefulltools.service;

import java.util.Optional;
import org.dozer.DozerBeanMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.firstProject.usefulltools.entity.UserInfo;
import com.firstProject.usefulltools.form.RegisterForm;
import com.firstProject.usefulltools.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;


/**
 * ユーザー登録画面Service
 */

@Service
@RequiredArgsConstructor
public class RegisterService {

    /** ユーザー情報テーブルDAO */
    private final UserInfoRepository repository;

    /**PasswordEncoder */
    private final PasswordEncoder passwordEncoder;

    /**
     * 
     * @param form入力情報
     * @return　登録情報(ユーザー情報Entity)
     */
    public Optional<UserInfo> resistUserInfo(RegisterForm form){
        var userInfoExstedOpt = repository.findById(form.getLoginId());
        if(userInfoExstedOpt.isPresent()){
            return Optional.empty();//ユーザーが既に存在する場合（isPresent() が true の場合）、空の Optional を返します。
        }

        //存在しなかったらDBへ登録
        var mapper = new DozerBeanMapper();//registerFormをuserInfoへマッピング
        var userInfo = mapper.map(form, UserInfo.class);

        var encodedPassword = passwordEncoder.encode(form.getPassword());//registerFormのパスワードをエンコードし格納
        userInfo.setPassword(encodedPassword);//エンコードしたパスワードをuserInfoにセット

        return Optional.of(repository.save(userInfo));
    }


    public void deleteDataById(String loginId) {
        
        String Login_id = loginId;

        repository.deleteById(Login_id);

    }
}
