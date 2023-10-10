package com.firstProject.usefulltools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.firstProject.usefulltools.content.UrlConst;
import com.firstProject.usefulltools.form.RegisterForm;
import com.firstProject.usefulltools.service.RegisterService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService service;

    @GetMapping(UrlConst.REGISTER)
    public String view(Model model, RegisterForm form) {
        return "register";
    }

    @PostMapping(UrlConst.REGISTERS)
    public String register(Model model, RegisterForm form) {
        // ユーザーIDが既に存在するかチェック
        

        if (form.getPassword() == null || form.getPassword().length() < 6 || !form.getPassword().matches(".*[a-zA-Z].*") || !form.getPassword().matches(".*\\d.*")) {
            model.addAttribute("errorMsg", "パスワードは6文字以上かつ、英字と数字を含む必要があります");
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
}
