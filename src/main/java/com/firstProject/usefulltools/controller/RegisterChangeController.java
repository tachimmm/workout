package com.firstProject.usefulltools.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.firstProject.usefulltools.content.UrlConst;
import com.firstProject.usefulltools.form.RegisterChangeForm;
import com.firstProject.usefulltools.service.LoginService;
import com.firstProject.usefulltools.service.RegisterChangeService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterChangeController {

    private final LoginService loginService;
    private final RegisterChangeService registerChangeService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(UrlConst.REGISTERCHANGE)
    public String RegisterChange(Model model, RegisterChangeForm form) {

        model.addAttribute("registerChangeForm", form); // フォームオブジェクトをモデルに追加

        return "register-change";
    }


    @PostMapping(UrlConst.REGISTERCHANGE)
    public String updatePassword(Model model, RegisterChangeForm form) {

        var userInfo = loginService.searchUserByid(form.getLoginId()); // ログインサービスを利用

        // パスワードハッシュ化したものを使用
        if (form.getPassword() == null || form.getNewPassword().length() < 6 || !form.getNewPassword().matches(".*[a-zA-Z].*")
                || !form.getNewPassword().matches(".*\\d.*")) {
            model.addAttribute("pwerrorMsg", "パスワードは6文字以上かつ、英字と数字を含む必要があります");
            return "register-change";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("pwerrorMsg", "新しいパスワードと確認用パスワードが一致しません。");
            return "register-change";
        }

        //下記条件を満たせばisCorrectUserAuthに"true"
        var isCorrectUserAuth = userInfo.isPresent()//userInfoが空でないかどうかをチェック
                && passwordEncoder.matches(form.getPassword(), userInfo.get().getPassword());//入力されたパスワードがDB内のパスワードと一致するかどうかを確認

        if (isCorrectUserAuth) {
            // データベースに新しいパスワードを上書きする処理
            registerChangeService.updatePassword(form.getLoginId(), form.getNewPassword());

            return "redirect:" + UrlConst.LOGIN; // ログインページに遷移

        } else {
            // ユーザー情報が存在しない場合の処理
            model.addAttribute("errorMsg", "ユーザーが登録されていないか、パスワードが間違ってます。");
            
            return "register-change";
        }

    }
}
