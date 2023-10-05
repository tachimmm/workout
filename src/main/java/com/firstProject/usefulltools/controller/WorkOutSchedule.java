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
import com.firstProject.usefulltools.form.ScheduleForm;
import com.firstProject.usefulltools.repository.EventInfoRepository;
import com.firstProject.usefulltools.service.CalendarEventService;

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
        String username =getCurrentUsername();
        if(username != null){
            model.addAttribute("username",username);

            List<EventInfo> itemlist = calendarEventService.findByUsername(username);

            model.addAttribute("itemlist",itemlist);
        }
        return "content-work-out-ScheduleList";
    }

    @GetMapping("/usefulltools/content-work-out-ScheduleCalender")
    public String WorkOutScheduleCalenderView(Model model) {
        String username = getCurrentUsername();
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "content-work-out-ScheduleCalender";
    }

    @GetMapping("/usefulltools/ScheduleAdd")
    public String ScheduleAddView(Model model, ScheduleForm form) {
        model.addAttribute("ScheduleForm", form);
        return "scheduleAdd";
    }

    @PostMapping("/usefulltools/ScheduleDelete")
    String delete(@RequestParam Integer id){
        Long longId = Long.valueOf(id);
        calendarEventService.deleteDataById(longId);

        return "redirect:/usefulltools/content-work-out-ScheduleList";
    }

    // コントローラーにスケジュール削除エンドポイントを追加
    @Autowired
    private CalendarEventService calendarEventService;

    @PostMapping("/usefulltools/ScheduleAdd")
    public String ScheduleAdd(Model model ,ScheduleForm form) {
            String username = getCurrentUsername();
        form.setUsername(username); // RecodeFormにusernameをセット

        calendarEventService.create(form);
        return "redirect:/usefulltools/content-work-out-ScheduleList";
    }


    @RestController
    @RequestMapping("/api/data")
    public class DataController {
        
        private final CalendarEventService calendarEventService;

        public DataController(CalendarEventService calendarEventService) {
            this.calendarEventService = calendarEventService;
        }

        @GetMapping("/fetch-events")
        public List<EventInfo> getAllCalendarEvents() {
            String username = getCurrentUsername();
            return eventInfoRepository.findByUsername(username);
        }

    }
}
