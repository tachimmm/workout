package com.firstProject.usefulltools.content;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.service.RecodeService;

public class Analytics {
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null; // 認証されていない場合はnullを返す
    }

    public static String dayAndTime() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
    }

    public static double calculateTotalWeight(List<RecodeInfo> itemlist) {

        double alllWeight = 0.0;
        for (RecodeInfo info : itemlist) {
            if (dayAndTime().equals(info.getDate_column()))
                alllWeight += info.getWeight();
        }
        return alllWeight;

    }

    @Autowired
    RecodeService recodeService;

    public static long caculatefromday(List<RecodeInfo> LastList) {
       
        RecodeInfo info = LastList.get(0); // リストの最初の要素が最新のレコード

        String lastday = info.getDate_column();
        String day = dayAndTime();

        LocalDate localDate1 = LocalDate.parse(lastday);
        LocalDate localDate2 = LocalDate.parse(day);

        long daysBetween = ChronoUnit.DAYS.between(localDate1, localDate2);

        return daysBetween;
    }

    public static int caculateTotalCount(List<RecodeInfo> itemlist) {

        int allCount = 0;
        for (RecodeInfo info : itemlist) {
            if (dayAndTime().equals(info.getDate_column()))
                allCount++;
        }
        return allCount;
    }

}
