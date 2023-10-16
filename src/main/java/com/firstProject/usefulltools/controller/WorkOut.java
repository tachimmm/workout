package com.firstProject.usefulltools.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.firstProject.usefulltools.content.Analytics;
import com.firstProject.usefulltools.content.UrlConst;
import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.service.RecodeService;
import org.springframework.ui.Model;

@Controller
public class WorkOut {
    @Autowired
    private RecodeService recodeService;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null; // 認証されていない場合はnullを返す
    }

    @RequestMapping(UrlConst.WORKOUTTOP)
    public String topPage(Model model) {

        String username = getCurrentUsername();
        if (username != null) {
            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(getCurrentUsername());
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);

            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double totalCount = Analytics.caculateTotalCount(itemlist);
            double calculatePreviousDayCountPercentage = Analytics.calculatePreviousDayCountPercentage(itemlist);
            double calculatePreviousDayWeightPercentage = Analytics.calculatePreviousDayWeightPercentage(itemlist);
            String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
            long dayfromday = Analytics.caculatefromday(LastList);

            model.addAttribute("calculatePreviousDayCountPercentage", calculatePreviousDayCountPercentage);
            model.addAttribute("calculatePreviousDayWeightPercentage", calculatePreviousDayWeightPercentage);
            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
            model.addAttribute("dayfromday", dayfromday);
            model.addAttribute("username", username);
        }
        return "content-work-out-top";
    }

}
