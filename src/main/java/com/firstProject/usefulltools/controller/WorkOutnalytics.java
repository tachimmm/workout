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

            String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
            double calculateMonthTotalWeight = Analytics.calculateMonthTotalWeight(itemlist);
            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double maxBenchPress = Analytics.maxBenchPress(itemlist);
            double maxSquat = Analytics.maxSquat(itemlist);
            double maxDeadLift = Analytics.maxDeadLift(itemlist);
            double calculatePreviousMonthPercentage = Analytics.calculatePreviousMonthPercentage(itemlist);
            double calculateMonthTotalWeightPercentage = Analytics.calculateMonthTotalWeightPercentage(itemlist);
            double calculatePreviousDayCountPercentage = Analytics.calculatePreviousDayCountPercentage(itemlist);
            double calculatePreviousDayWeightPercentage = Analytics.calculatePreviousDayWeightPercentage(itemlist);
            int totalCount = Analytics.caculateTotalCount(itemlist);
            int calculateMonthTotalCount = Analytics.calculateMonthTotalCount(itemlist);
            int calculateTotalTrainingCount = Analytics.calculateTotalTrainingCount(itemlist);
            long dayfromday = Analytics.caculatefromday(LastList);
            long lastMaxBenchPress = Analytics.lastMaxBenchPress(itemlist);
            long lastMaxSquat = Analytics.lastMaxSquat(itemlist);
            long lastaMaxDeadLift = Analytics.lastaMaxDeadLift(itemlist);


            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("calculateMonthTotalWeight", calculateMonthTotalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("calculateMonthTotalCount", calculateMonthTotalCount);
            model.addAttribute("calculatePreviousMonthPercentage", calculatePreviousMonthPercentage);
            model.addAttribute("calculateMonthTotalWeightPercentage", calculateMonthTotalWeightPercentage);
            model.addAttribute("calculatePreviousDayCountPercentage", calculatePreviousDayCountPercentage);
            model.addAttribute("calculatePreviousDayWeightPercentage", calculatePreviousDayWeightPercentage);
            model.addAttribute("calculateTotalTrainingCount", calculateTotalTrainingCount);
            model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
            model.addAttribute("maxBenchPress", maxBenchPress);
            model.addAttribute("maxSquat", maxSquat);
            model.addAttribute("maxDeadLift", maxDeadLift);
            model.addAttribute("dayfromday", dayfromday);
            model.addAttribute("lastMaxBenchPress", lastMaxBenchPress);
            model.addAttribute("lastMaxSquat", lastMaxSquat);
            model.addAttribute("lastaMaxDeadLift", lastaMaxDeadLift);


        }
        return "content-work-out-analytics";
    }
}