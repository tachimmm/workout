package com.firstProject.usefulltools.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.firstProject.usefulltools.entity.EventInfo;
import com.firstProject.usefulltools.form.ScheduleForm;
import com.firstProject.usefulltools.repository.EventInfoRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class CalendarEventService {
    @Autowired
    EventInfoRepository eventInfoRepository;

    public CalendarEventService(EventInfoRepository eventInfoRepository) {
        this.eventInfoRepository = eventInfoRepository;
    }

    public List<EventInfo> getAllCalendarEvents() {
        return eventInfoRepository.findAll();
    }

        public void create(ScheduleForm scheduleForm) {
        EventInfo eventInfo = createSchedule(scheduleForm);
        eventInfoRepository.save(eventInfo);
    }

    public List<EventInfo> findByUsername(String username){
        return eventInfoRepository.findByUsername(username);
    }

    public void deleteDataById(Long id) {
        Long save_id = id;
        eventInfoRepository.deleteById(save_id);
    }

    private EventInfo createSchedule(ScheduleForm scheduleForm) {
        EventInfo eventInfo = new EventInfo();
        
        // EventInfoクラスのセッターメソッドを使用して値をセット
        eventInfo.setUsername(scheduleForm.getUsername());
        eventInfo.setTitle(scheduleForm.getTitle());
        eventInfo.setStart(scheduleForm.getStartDate());
        eventInfo.setEnd(scheduleForm.getEndDate());
    
        return eventInfo;
    }
    

}
