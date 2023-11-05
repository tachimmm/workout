package com.firstProject.usefulltools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.firstProject.usefulltools.content.UrlConst;
import com.firstProject.usefulltools.form.LoginForm;
import com.firstProject.usefulltools.form.RegisterForm;
import com.firstProject.usefulltools.service.EventService;
import com.firstProject.usefulltools.service.LoginService;
import com.firstProject.usefulltools.service.RecodeService;
import com.firstProject.usefulltools.service.RegisterService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final LoginService loginService;

    private final PasswordEncoder passwordEncoder;

    private final RegisterService service;

    @GetMapping(UrlConst.REGISTER)
    public String view(Model model, RegisterForm form) {
        return "register";
    }

    @PostMapping(UrlConst.REGISTERS)
    public String register(Model model, RegisterForm form) {
        // ユーザーIDが既に存在するかチェック

        if (form.getPassword() == null || form.getPassword().length() < 6 || !form.getPassword().matches(".*[a-zA-Z].*")
                || !form.getPassword().matches(".*\\d.*")) {
            model.addAttribute("errorMsg", "パスワードは6文字以上かつ、英字と数字を含む必要があります");
            return "register";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("errorMsg", "パスワードと確認用パスワードが一致しません。");
            return "register";
        }

        var userInfoOpt = service.resistUserInfo(form);
        if (userInfoOpt.isEmpty()) {
            model.addAttribute("errorMsg", "このユーザーIDは既に登録されています");
            return "register"; // 登録失敗時は登録画面に戻る
        }

        // バリデーションエラーがなく、かつユーザーIDの重複もない場合の処理
        return "redirect:" + UrlConst.LOGIN; // 登録成功時はログイン画面にリダイレクト

    }

    @GetMapping(UrlConst.AccountDelete)
    public String account(LoginForm form) {

        return "AccountDelete";
    }

    @Autowired
    private final RegisterService registerService;

    @Autowired
    private final RecodeService recodeService;

    @Autowired
    private final EventService eventService;

    @PostMapping(UrlConst.AccountDelete)
    public String accountDelete(Model model, LoginForm form) {

        var userInfo = loginService.searchUserByid(form.getLoginId());
        var isCorrectUserAuth = userInfo.isPresent()
                && passwordEncoder.matches(form.getPassword(), userInfo.get().getPassword());

        String loginId = userInfo.get().getLoginId();

        if (isCorrectUserAuth) {
            // アカウント削除処理
            registerService.deleteDataById(loginId);
            // データベースからアカウントに紐づいたデータをすべて消す
            recodeService.deleteByName(loginId);

            eventService.deleteByUsername(loginId);

            return "redirect:" + "/logout"; // toppageに遷移

        } else {
            // ユーザー情報が存在しない場合の処理
            model.addAttribute("errorMsg", "ユーザーが登録されていないか、パスワードが間違ってます。");

            return "AccountDelete";
        }

    }

}
