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
	 * @param username ログインIDが渡される
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {// ユーザー情報をDBから取得しログイン認証する
		var userInfo = repository.findById(username)// 実際にはusernameに当たるlogin_Idを今回は取得している
				.orElseThrow(() -> new UsernameNotFoundException(username));// 該当するユーザー情報が見つからない場合はUsernameNotFoundExceptionをスロー

		return User.withUsername(userInfo.getLoginId())
				.password(userInfo.getPassword())
				.roles("USER")//権限のこと。Spring securityの既存処理で使用する項目ではなく、業務仕様で権限によって処理分岐させたい時に使用する。
				.build();//セットした情報でユーザーを作成する処理
		/**
		 * Userクラスは後にパスワード等、各フィールドのチェック処理に回され、
		 * チェックOKI担った場合は、セッションにBeanごと情報が保管される。
		 */
	}

}
