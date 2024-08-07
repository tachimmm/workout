package com.firstProject.usefulltools.controller;


import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.firstProject.usefulltools.content.UrlConst;
import com.firstProject.usefulltools.form.LoginForm;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * ログイン画面Controllerクラス
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

	/** セッション情報 */
	private final HttpSession session;

	/**
	 * 画面の初期表示を行います。
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return ログイン画面
	 */
	@GetMapping(UrlConst.LOGIN)
	public String view(Model model, LoginForm form) {
		return "login";
	}

	/**
	 * ログインエラー時にセッションからエラーメッセージを取得して、画面の表示を行います。
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return ログイン画面
	 */
	@GetMapping(value = UrlConst.LOGIN, params = "error")
	public String viewWithError(Model model, LoginForm form) {

		var errorInfo = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);//Spring securityから渡ってきたエラーメッセージを格納
		
		model.addAttribute("errorMsg", errorInfo.getMessage());
		return "login";
	}

}