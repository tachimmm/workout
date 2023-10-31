package com.firstProject.usefulltools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.firstProject.usefulltools.entity.EventInfo;
import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.form.ScheduleForm;
import com.firstProject.usefulltools.repository.EventInfoRepository;
import com.firstProject.usefulltools.service.CalendarEventService;
import com.firstProject.usefulltools.service.RecodeService;
import com.firstProject.usefulltools.content.Analytics;

@Controller
public class WorkOutSchedule {

    @Autowired
    private EventInfoRepository eventInfoRepository; // EventInfoRepositoryを使用する

    private String getCurrentUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return userDetails.getUsername();
        }

        return null; // 認証されていない場合はnullを返す
    }

    @PostMapping("/usefulltools/content-work-out-ScheduleCalender")
    public ResponseEntity<String> createEvent(@RequestBody EventInfo eventInfo) {

        // リクエストボディからデータを受け取り、データベースに保存
        EventInfo savedEvent = eventInfoRepository.save(eventInfo);

        return ResponseEntity.ok("Event created successfully with ID: " + savedEvent.getSave_id());
    }


    @GetMapping("/usefulltools/content-work-out-ScheduleList")
    public String ListView(Model model) {

        String username = getCurrentUsername();

        if (username != null) {

            model.addAttribute("username", username);

            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(getCurrentUsername());
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);

            double calculatePreviousDayCountPercentage = Analytics.calculatePreviousDayCountPercentage(itemlist);
            double calculatePreviousDayWeightPercentage = Analytics.calculatePreviousDayWeightPercentage(itemlist);
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
            model.addAttribute("calculatePreviousDayWeightPercentage", calculatePreviousDayWeightPercentage);

            List<EventInfo> itemlists = calendarEventService.findByUsername(username);

            model.addAttribute("itemlist", itemlists);
        }

        return "content-work-out-ScheduleList";
    }

    @Autowired
    RecodeService recodeService;

    @GetMapping("/usefulltools/content-work-out-ScheduleCalender")
    public String WorkOutScheduleCalenderView(Model model) {

        String username = getCurrentUsername();

        if (username != null) {

            List<RecodeInfo> LastList = recodeService.findLatesRecodeInfo(getCurrentUsername());
            List<RecodeInfo> itemlist = recodeService.findByUsername(username);

            double calculatePreviousDayCountPercentage = Analytics.calculatePreviousDayCountPercentage(itemlist);
            double calculatePreviousDayWeightPercentage = Analytics.calculatePreviousDayWeightPercentage(itemlist);
            double totalWeight = Analytics.calculateTotalWeight(itemlist);
            double totalCount = Analytics.caculateTotalCount(itemlist);
            String yearAndMonthAndDay = Analytics.yearAndMonthAndDay();
            long dayfromday = Analytics.caculatefromday(LastList);

            model.addAttribute("calculatePreviousDayCountPercentage", calculatePreviousDayCountPercentage);
            model.addAttribute("calculatePreviousDayWeightPercentage", calculatePreviousDayWeightPercentage);
            model.addAttribute("username", username);
            model.addAttribute("totalWeight", totalWeight);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("yearAndMonthAndDay", yearAndMonthAndDay);
            model.addAttribute("dayfromday", dayfromday);
        }

        return "content-work-out-ScheduleCalender";
    }

    @GetMapping("/usefulltools/ScheduleAdd")
    public String ScheduleAddView(Model model, ScheduleForm form) {

        model.addAttribute("ScheduleForm", form);

        return "scheduleAdd";
    }

    @PostMapping("/usefulltools/ScheduleDelete")
    String delete(@RequestParam Integer id) {

        Long longId = Long.valueOf(id);
        calendarEventService.deleteDataById(longId);

        return "redirect:/usefulltools/content-work-out-ScheduleList";
    }

    // コントローラーにスケジュール削除エンドポイントを追加
    @Autowired
    private CalendarEventService calendarEventService;

    @PostMapping("/usefulltools/ScheduleAdd")
    public String ScheduleAdd(Model model, ScheduleForm form) {

        String username = getCurrentUsername();
        form.setUsername(username); // RecodeFormにusernameをセット

        calendarEventService.create(form);

        return "redirect:/usefulltools/content-work-out-ScheduleList";
    }


    @RestController
    @RequestMapping("/api/data")
    public class DataController {

        @GetMapping("/fetch-events")
        public List<EventInfo> getAllCalendarEvents() {

            String username = getCurrentUsername();

            return eventInfoRepository.findByUsername(username);
            
        }

    }
}
