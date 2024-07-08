package com.firstProject.usefulltools.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.firstProject.usefulltools.content.UrlConst;
import lombok.RequiredArgsConstructor;

/**
 * Spring Securityカスタマイズクラス
 */
@EnableWebSecurity//（@Configuration が付いたクラス）Spring Boot アプリケーションで Spring Security を有効にする
@Configuration//Spring Security の設定を簡単に始めることができる
@RequiredArgsConstructor
public class WebSecurityConfig {

	/** パスワードエンコーダー */
	private final PasswordEncoder passwordEncoder;

	/** ユーザー情報取得Service */
	private final UserDetailsService userDetailsService;

	/** メッセージ取得 */
	private final MessageSource messageSource;

	/** ユーザー名のname属性 */
	private final String USERNAME_PARAMETER = "loginId";

	/**
	 * Spring Securityの各種カスタマイズを行います。
	 * 
	 * <p>
	 * カスタマイズ設定するのは、以下の項目になります。
	 * <ul>
	 * <li>認証不要URL</li>
	 * <li>ログイン画面のURL</li>
	 * <li>usernameとして利用するリクエストパラメーター名</li>
	 * <li>ログイン成功時のリダイレクト先URL</li>
	 * </ul>
	 * 
	 * @param http セキュリティ設定
	 * @return カスタマイズ結果
	 * @throws Exception 予期せぬ例外が発生した場合
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				.csrf(csrf -> csrf
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				.authorizeHttpRequests(
						authorize -> authorize.requestMatchers(UrlConst.NO_AUTHENTICATION).permitAll()// ←常にアクセス可能
								.anyRequest().authenticated())//←ユーザー済みの場合アクセス可能。.requestMatchersで指定したURL以外のURLが対象
				.formLogin(
						login -> login.loginPage(UrlConst.LOGIN) // 自作ログイン画面(Controller)を使うための設定
								.usernameParameter(USERNAME_PARAMETER) // ユーザ名パラメータのname属性,DBでのusernameに当たる名前今回はlogin_id
								.defaultSuccessUrl(UrlConst.WORKOUTTOP)); // ログイン成功後のリダイレクトURL
		http.logout(logout -> {
			logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));// ログアウトする処理、セッションを終了する
		});

		return http.build();
	}

	/**
	 * Providerのカスタマイズを行い、独自Providerを返却します。
	 * <li>UserDetailsService</li>
	 * <li>PasswordEncoder</li>
	 * ↑↑も記載しないと空の状態になってしまう。
	 * 
	 * <p>
	 * カスタマイズ設定するのは、以下のフィールドになります。
	 * <ul>
	 * <li>UserDetailsService</li>
	 * <li>PasswordEncoder</li>
	 * <li>MessageSource</li>
	 * </ul>
	 * 
	 * @return カスタマイズしたProvider情報
	 */
	@Bean
	AuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		provider.setMessageSource(messageSource);

		return provider;
	}
}
