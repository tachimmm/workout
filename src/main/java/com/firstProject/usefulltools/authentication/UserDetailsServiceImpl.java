package com.firstProject.usefulltools.authentication;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.firstProject.usefulltools.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	/** ユーザー情報テーブルRepository */
	private final UserInfoRepository repository;

	/**
	 * ユーザー情報生成
	 * 
	 * @param username ログインID
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//ユーザー情報をDBから取得しログイン認証する
		var userInfo = repository.findById(username)//実際にはusernameに当たるlogin_Idを今回は取得している
				.orElseThrow(() -> new UsernameNotFoundException(username));//当するユーザー情報が見つからない場合はUsernameNotFoundExceptionをスロー

		return User.withUsername(userInfo.getLoginId())
				.password(userInfo.getPassword())
				.roles("USER")
				.build();
	}

}
