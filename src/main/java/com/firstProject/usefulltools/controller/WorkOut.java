package com.firstProject.usefulltools.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.firstProject.usefulltools.content.UrlConst;
import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.service.RecodeService;
import org.springframework.ui.Model;

@Controller
public class WorkOut {
    
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
            model.addAttribute("username", username);
        }
        return "content-work-out-top";
    }

    @Autowired
    RecodeService recodeService;

    @GetMapping("/usefulltools/content-work-out-analytics")
    public String WorkOutAnalytics(Model model) {
        String username = getCurrentUsername();
        if (username != null) {
            model.addAttribute("username", username);
    
            // ログインユーザーのusernameに関連するワークアウト情報を取得
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);
            
            // 総重量を計算
            double totalWeight = calculateTotalWeight(itemlist);
            
            //totalcount
            double totalCount = caculateTotalCount(itemlist);
            // モデルに総重量を追加
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
        }
        return "content-work-out-analytics";
    }
    
    private double calculateTotalWeight(List<RecodeInfo> itemlist) {
        
        double alllWeight = 0.0;
        for (RecodeInfo info : itemlist) {
            // 重量を合計
            alllWeight += info.getWeight();
        }
        return alllWeight;
        
    }
    
    @SuppressWarnings("unused")
    private int caculateTotalCount(List<RecodeInfo>itemlist){
        
        int allCount = 0;
        for(RecodeInfo info : itemlist){
            allCount ++;
        }
        return allCount;
    }

   
}



