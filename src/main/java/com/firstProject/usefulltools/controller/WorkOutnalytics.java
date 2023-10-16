package com.firstProject.usefulltools.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.firstProject.usefulltools.content.Analytics;
import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.service.RecodeService;

@Controller
public class WorkOutnalytics {

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null; // 認証されていない場合はnullを返す
    }

    public String dayAndTime() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
    }

    @Autowired
    RecodeService recodeService;

    @GetMapping("/usefulltools/content-work-out-analytics")
    public String WorkOutAnalytics(Model model) {
        String username = getCurrentUsername();
        if (username != null) {
    List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(getCurrentUsername());
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);
        
            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double totalCount = Analytics.caculateTotalCount(itemlist);
            String dayAndTime = Analytics.dayAndTime();
            long dayfromday = Analytics.caculatefromday(LastList);


            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("dayAndTime", dayAndTime);
            model.addAttribute("dayfromday", dayfromday);
        }
        return "content-work-out-analytics";
    }
}