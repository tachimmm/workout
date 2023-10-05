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
        var userInfoOpt = service.resistUserInfo(form);

        if (userInfoOpt.isEmpty()) {
            model.addAttribute("errorMsg", "このユーザーIDは既に登録されています");
            return "register"; // 登録失敗時は登録画面に戻る
        } else {
            service.resistUserInfo(form); // ユーザーIDが存在しない場合は登録を実行
            return "redirect:" + UrlConst.LOGIN; // 登録成功時はログイン画面にリダイレクト
        }
    }
}
