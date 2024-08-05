package com.firstProject.usefulltools.controller;

import java.util.Collections;
import java.util.List;
import com.firstProject.usefulltools.service.RecodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.firstProject.usefulltools.content.Analytics;
import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.form.RecodeForm;
import com.firstProject.usefulltools.form.RecodeSearchForm;

@Controller
public class WorkOutRecodeController {

    public class AnalyticsData {

        /**
         * データの保持: 総重量、総回数、経過日数のデータを保持します。
         * データのカプセル化: 分析結果に関連する複数のデータを一つのオブジェクトとしてカプセル化します。
         * データの転送: コントローラからビューへのデータ転送を容易にします。
         * データの操作: ゲッターとセッターを通じてデータの安全な操作を提供します。
         */
        private double totalWeight;
        private double totalCount;
        private String dayfromday;

        // Getters and Setters
        public double getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(double totalWeight) {
            this.totalWeight = totalWeight;
        }

        public double getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(double totalCount) {
            this.totalCount = totalCount;
        }

        public String getDayfromday() {
            return dayfromday;
        }

        public void setDayfromday(String dayfromday) {
            this.dayfromday = dayfromday;
        }
    }

    @Autowired
    private SecuritySession securitySession;

    @Autowired
    RecodeService recodeService;

    @GetMapping("/usefulltools/RecodeDataAjax")
    @ResponseBody
    public List<RecodeInfo> itemlist() {
        String username = securitySession.getUsername();
        List<RecodeInfo> itemlist = recodeService.findByUsername(username);
        Collections.reverse(itemlist);// リストを逆順にしてからクライアントに送信
        return itemlist;
    }

    @GetMapping("/usefulltools/AnalyticsDataAjax")
    @ResponseBody // @ResponseBody を使用することで、このオブジェクトは自動的に JSON 形式に変換されてクライアントに返される
    public AnalyticsData getAnalyticsDataAjax() {
        String username = securitySession.getUsername();
        double totalWeight = 0;
        double totalCount = 0;
        String dayFromDayStr = "";

        if (username != null) {
            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(username);
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);
            Collections.reverse(itemlist);

            totalWeight = Analytics.calculateTotalWeight(itemlist);
            totalCount = Analytics.caculateTotalCount(itemlist);
            long dayFromDay = Analytics.caculatefromday(LastList);
            dayFromDayStr = String.valueOf(dayFromDay);
        }

        AnalyticsData analyticsData = new AnalyticsData();
        analyticsData.setTotalWeight(totalWeight);
        analyticsData.setTotalCount(totalCount);
        analyticsData.setDayfromday(dayFromDayStr);

        return analyticsData;
    }

    @GetMapping("/usefulltools/content-work-out-recorde")
    public String WorkOutRecodeView(Model model) {

        String username = securitySession.getUsername();

        if (username != null) {

            model.addAttribute("username", username);

            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(username);
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);

            Collections.reverse(itemlist);

            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double totalCount = Analytics.caculateTotalCount(itemlist);
            String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
            long dayfromday = Analytics.caculatefromday(LastList);

            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
            model.addAttribute("dayfromday", dayfromday);

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

        String username = securitySession.getUsername();

        form.setUsername(username); // RecodeFormにusernameをセット
        recodeService.create(form);

        return "redirect:/usefulltools/content-work-out-recorde";
    }

    @PostMapping("/usefulltools/RecodeAddAjax")
    public ResponseEntity<List<RecodeInfo>> recodeAddAjax(@RequestBody RecodeForm form) {
        String username = securitySession.getUsername();
        form.setUsername(username); // RecodeFormにusernameをセット
        recodeService.create(form);
        List<RecodeInfo> itemlist = recodeService.findByUsername(username);//最新のRecordリストを取得
        Collections.reverse(itemlist);// リストを逆順にしてからクライアントに送信
        return ResponseEntity.ok(itemlist);
    }

    @GetMapping("/usefulltools/RecodeSearch")
    public String SearchView(Model model) {

        String username = securitySession.getUsername();

        if (username != null) {

            model.addAttribute("username", username);
        }

        return "RecodeSearch";
    }

    @PostMapping("/usefulltools/RecodeSearchAjax")
    public ResponseEntity<List<RecodeInfo>> search(@RequestBody RecodeSearchForm form) {
        String username = securitySession.getUsername();

        form.setUsername(username);

        List<RecodeInfo> searchResult = recodeService.findByUsernameSearch(form);
        return ResponseEntity.ok(searchResult);
    }

    @PostMapping("/usefulltools/RecodeDelete")
    String delete(@RequestParam Integer id) {

        Long longId = Long.valueOf(id);
        recodeService.deleteDataById(longId);

        return "redirect:/usefulltools/content-work-out-recorde";

    }

}
