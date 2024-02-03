package com.firstProject.usefulltools.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.firstProject.usefulltools.content.Analytics;
import com.firstProject.usefulltools.content.UrlConst;
import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.form.RmForm;
import com.firstProject.usefulltools.form.BmiForm;
import com.firstProject.usefulltools.form.PfcForm;
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
    public String topPage(Model model, RmForm rmForm, BmiForm bmiForm, PfcForm pfcForm) {

        String username = getCurrentUsername();
        double maxWeight = Analytics.rmExchange(rmForm);
        double Bmi = Analytics.bmiConverter(bmiForm);
        double basalMetabolismRate = Analytics.basalMetabolismRateCalculator(pfcForm);
        double idealBasalMetabolism = Analytics.idealBasalMetabolismCalculator(pfcForm);
        double totalCal = Analytics.totalCalCalculator(pfcForm);
        double fat = Analytics.fatCalculator(pfcForm);
        double protein = Analytics.proteinCalculator(pfcForm);
        double carbohydrate = Analytics.carbohydrateCalculator(pfcForm);

        if (username != null) {

            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(username);
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);

            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double totalCount = Analytics.caculateTotalCount(itemlist);
            double calculatePreviousDayCountPercentage = Analytics.calculatePreviousDayCountPercentage(itemlist);
            String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
            long dayfromday = Analytics.caculatefromday(LastList);

            model.addAttribute("calculatePreviousDayCountPercentage", calculatePreviousDayCountPercentage);
            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
            model.addAttribute("dayfromday", dayfromday);
            model.addAttribute("RmForm", rmForm);
            model.addAttribute("BmiForm", bmiForm);
            model.addAttribute("PfcForm", pfcForm);
            model.addAttribute("maxWeight", maxWeight);
            model.addAttribute("basalMetabolismRate", basalMetabolismRate);
            model.addAttribute("idealBasalMetabolism", idealBasalMetabolism);
            model.addAttribute("totalCal", totalCal);
            model.addAttribute("fat", fat);
            model.addAttribute("protein", protein);
            model.addAttribute("carbohydrate", carbohydrate);
            model.addAttribute("bmi", Bmi);

        } else {
            model.addAttribute("calculatePreviousDayCountPercentage", "0");
            model.addAttribute("username", "ゲスト");
            model.addAttribute("totalWeight", "0");
            model.addAttribute("totalCount", "0");
            model.addAttribute("dayfromday", "0");
            model.addAttribute("username", username);
            model.addAttribute("RmForm", rmForm);
            model.addAttribute("BmiForm", bmiForm);
            model.addAttribute("PfcForm", pfcForm);
            model.addAttribute("maxWeight", maxWeight);
            model.addAttribute("bmi", Bmi);
            model.addAttribute("basalMetabolismRate", basalMetabolismRate);
            model.addAttribute("idealBasalMetabolism", idealBasalMetabolism);
            model.addAttribute("totalCal", totalCal);
            model.addAttribute("fat", fat);
            model.addAttribute("protein", protein);
            model.addAttribute("carbohydrate", carbohydrate);
        }

        return "content-work-out-top";
    }

    @GetMapping(UrlConst.help)
    public String helpview(Model model) {
        String username = getCurrentUsername();

        if (username != null) {

            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(getCurrentUsername());
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);

            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double totalCount = Analytics.caculateTotalCount(itemlist);
            double calculatePreviousDayCountPercentage = Analytics.calculatePreviousDayCountPercentage(itemlist);
            String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
            long dayfromday = Analytics.caculatefromday(LastList);

            model.addAttribute("calculatePreviousDayCountPercentage", calculatePreviousDayCountPercentage);
            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
            model.addAttribute("dayfromday", dayfromday);
            model.addAttribute("username", username);

        } else {
            model.addAttribute("calculatePreviousDayCountPercentage", "0");
            model.addAttribute("username", "ゲスト");
            model.addAttribute("totalWeight", "0");
            model.addAttribute("totalCount", "0");
            model.addAttribute("dayfromday", "0");
            model.addAttribute("username", username);
        }

        return "help";
    }
}
