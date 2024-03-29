package com.firstProject.usefulltools.controller;

import java.util.Collections;
import java.util.List;
import com.firstProject.usefulltools.service.RecodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.firstProject.usefulltools.content.Analytics;
import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.form.RecodeForm;
import com.firstProject.usefulltools.form.RecodeSearchForm;

@Controller
public class WorkOutRecodeController {

    private String getCurrentUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }

        return null; // 認証されていない場合はnullを返す
    }

    @Autowired
    RecodeService recodeService;

    @GetMapping("/usefulltools/content-work-out-recorde")
    public String WorkOutRecodeView(Model model) {

        String username = getCurrentUsername();

        if (username != null) {

            model.addAttribute("username", username);

            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(getCurrentUsername());
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);

            Collections.reverse(itemlist);

            double calculatePreviousDayCountPercentage = Analytics.calculatePreviousDayCountPercentage(itemlist);
            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double totalCount = Analytics.caculateTotalCount(itemlist);
            String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
            long dayfromday = Analytics.caculatefromday(LastList);

            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
            model.addAttribute("dayfromday", dayfromday);
            model.addAttribute("calculatePreviousDayCountPercentage", calculatePreviousDayCountPercentage);

            model.addAttribute("itemlist", itemlist);

        }

        return "content-work-out-recorde";
    }

    @GetMapping("/usefulltools/RecodeAdd")
    public String view(Model model, RecodeForm form) {

        model.addAttribute("RecodeForm", form); // フォームオブジェクトをモデルに追加

        return "RecodeAdd";
    }

    @PostMapping("/usefulltools/RecodeAdd")
    public String RecodeAdd(Model model, RecodeForm form) {

        String username = getCurrentUsername();

        form.setUsername(username); // RecodeFormにusernameをセット
        recodeService.create(form);

        return "redirect:/usefulltools/content-work-out-recorde";
    }

    @GetMapping("/usefulltools/RecodeSearch")
    public String SearchView(Model model) {

        String username = getCurrentUsername();

        if (username != null) {

            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(getCurrentUsername());
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);

            double calculatePreviousDayCountPercentage = Analytics.calculatePreviousDayCountPercentage(itemlist);
            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double totalCount = Analytics.caculateTotalCount(itemlist);
            String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
            long dayfromday = Analytics.caculatefromday(LastList);

            model.addAttribute("calculatePreviousDayCountPercentage", calculatePreviousDayCountPercentage);
            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
            model.addAttribute("dayfromday", dayfromday);
        }

        return "RecodeSearch";
    }

    @PostMapping("usefulltools/RecodeSearch")
    public String Search(Model model, RecodeSearchForm form) {

        String username = getCurrentUsername();

        form.setUsername(username);
        // 検索にヒットするワークアウト情報を取得
        List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(getCurrentUsername());
        List<RecodeInfo> itemlist = recodeService.findByUsername(username);
        List<RecodeInfo> itemlists = recodeService.findByUsernameSearch(form);

        double totalWeight = Analytics.calculateTotalWeight(itemlist);
        double totalCount = Analytics.caculateTotalCount(itemlist);
        String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
        long dayfromday = Analytics.caculatefromday(LastList);

        model.addAttribute("username", username);
        model.addAttribute("totalWeight", totalWeight);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
        model.addAttribute("dayfromday", dayfromday);
        model.addAttribute("itemlist", itemlists);
        model.addAttribute("username", username);

        return "RecodeSearch";
    }


    @PostMapping("/usefulltools/RecodeDelete")

    String delete(@RequestParam Integer id) {
        
        Long longId = Long.valueOf(id);
        recodeService.deleteDataById(longId);

        return "redirect:/usefulltools/content-work-out-recorde";

    }

    

}
