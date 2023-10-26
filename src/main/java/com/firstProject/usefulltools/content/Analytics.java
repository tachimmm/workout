package com.firstProject.usefulltools.content;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.form.RmForm;
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

    public static String yearAndMonthAndDay() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
    }

    public static String yearAndMonth() {
        LocalDate currenDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formatterDate = currenDate.format(formatter);
        return formatterDate;
    }

      public static String getPreviousYearDay() {
        LocalDate currentDate = LocalDate.parse(yearAndMonthAndDay() + "-01");
        LocalDate previousMonthDate = currentDate.minusMonths(1);
        return previousMonthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static double calculateTotalWeight(List<RecodeInfo> itemlist) {

        double alllWeight = 0.0;
        for (RecodeInfo info : itemlist) {
            if (yearAndMonthAndDay().equals(info.getDate_column()))
                alllWeight += info.getWeight();
        }
        return alllWeight;

    }

    public static double calculatePreviousTotalWeight(List<RecodeInfo> itemlist) {

        double alllWeight = 0.0;
        for (RecodeInfo info : itemlist) {
            if (getPreviousYearDay().equals(info.getDate_column()))
                alllWeight += info.getWeight();
        }
        return alllWeight;

    }

    public static double calculatePreviousDayCountPercentage(List<RecodeInfo> itemlist) {

        double currentMonthCount = caculateTotalCount(itemlist);
        double previousMonthCount = caculatePreviousTotalCount(itemlist);

        // パーセンテージを計算
        if (previousMonthCount == 0) {
            return 0.0; // 前月のデータがない場合、0% を返す
        } else {
            return ((double) (currentMonthCount - previousMonthCount) / previousMonthCount) * 100.0;
        }
    }

    public static double calculatePreviousDayWeightPercentage(List<RecodeInfo> itemlist) {

        double currentMonthCount = calculateTotalWeight(itemlist);
        double previousMonthCount = calculatePreviousTotalWeight(itemlist);

        // パーセンテージを計算
        if (previousMonthCount == 0) {
            return 0.0; // 前月のデータがない場合、0% を返す
        } else {
            return ((double) (currentMonthCount - previousMonthCount) / previousMonthCount) * 100.0;
        }
    }

    public static double calculateMonthTotalWeight(List<RecodeInfo> itemlist) {

        double alllWeight = 0.0;
        for (RecodeInfo info : itemlist) {
            if (yearAndMonth().equals(info.getDate_column().substring(0, 7)))
                alllWeight += info.getWeight();
        }
        return alllWeight;
    }

    public static int calculatepreviousMonthWeight(List<RecodeInfo> itemlist) {

        int allCount = 0;
        for (RecodeInfo info : itemlist) {
            if (getPreviousYearMonth().equals(info.getDate_column().substring(0, 7)))
                allCount++;
        }
        return allCount;
    }

    public static double calculateMonthTotalWeightPercentage(List<RecodeInfo> itemlist) {

        double currentMonthWeight = calculateMonthTotalWeight(itemlist);
        double previousMontWeight = calculatepreviousMonthWeight(itemlist);

        // パーセンテージを計算
        if (previousMontWeight == 0) {
            return 0.0; // 前月のデータがない場合、0% を返す
        } else {
            return ((double) (currentMonthWeight - previousMontWeight) / previousMontWeight) * 100.0;
        }
    }

    @Autowired
    RecodeService recodeService;

    public static long caculatefromday(List<RecodeInfo> LastList) {
        if (LastList.isEmpty()) {
            return 0; // もしくはエラーハンドリングの方法に応じて適切な値を返す
        }

        RecodeInfo info = LastList.get(0); // リストの最初の要素が最新のレコード

        String lastday = info.getDate_column();
        String day = yearAndMonthAndDay();

        LocalDate localDate1 = LocalDate.parse(lastday);
        LocalDate localDate2 = LocalDate.parse(day);

        long daysBetween = ChronoUnit.DAYS.between(localDate1, localDate2);

        return daysBetween;
    }

    public static int caculateTotalCount(List<RecodeInfo> itemlist) {

        int allCount = 0;
        for (RecodeInfo info : itemlist) {
            if (yearAndMonthAndDay().equals(info.getDate_column()))
                allCount++;
        }
        return allCount;
    }

      public static int caculatePreviousTotalCount(List<RecodeInfo> itemlist) {

        int allCount = 0;
        for (RecodeInfo info : itemlist) {
            if (getPreviousYearDay().equals(info.getDate_column()))
                allCount++;
        }
        return allCount;
    }

    public static int calculateMonthTotalCount(List<RecodeInfo> itemlist) {

        int allCount = 0;
        for (RecodeInfo info : itemlist) {
            if (yearAndMonth().equals(info.getDate_column().substring(0, 7)))
                allCount++;
        }
        return allCount;
    }

    public static int calculatepreviousMonthCount(List<RecodeInfo> itemlist) {

        int allCount = 0;
        for (RecodeInfo info : itemlist) {
            if (getPreviousYearMonth().equals(info.getDate_column().substring(0, 7)))
                allCount++;
        }
        return allCount;
    }

    public static String getPreviousYearMonth() {
        LocalDate currentDate = LocalDate.parse(yearAndMonth() + "-01");
        LocalDate previousMonthDate = currentDate.minusMonths(1);
        return previousMonthDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public static double calculatePreviousMonthPercentage(List<RecodeInfo> itemlist) {

        int currentMonthCount = calculateMonthTotalCount(itemlist);
        int previousMonthCount = calculatepreviousMonthCount(itemlist);

        // パーセンテージを計算
        if (previousMonthCount == 0) {
            return 0.0; // 前月のデータがない場合、0% を返す
        } else {
            return ((double) (currentMonthCount - previousMonthCount) / previousMonthCount) * 100.0;
        }
    }

    public static int calculateTotalTrainingCount(List<RecodeInfo> itemlist) {
        int count = 0;
        String currentYearAndMonth = yearAndMonth();
        Set<String> countedDates = new HashSet<>(); // 既にカウントした日付を追跡するためのセット

        for (RecodeInfo info : itemlist) {
            String dateColumn = info.getDate_column();
            if (currentYearAndMonth.equals(dateColumn.substring(0, 7))) {
                String day = dateColumn.substring(8, 10); // 日を取得

                if (!countedDates.contains(day)) {
                    countedDates.add(day); // 既にカウントした日付をセットに追加
                    count++;
                }
            }
        }

        return count;
    }

    public static double maxBenchPress(List<RecodeInfo> itemlist) {

        double maxWeight = 0.0;
        String BenchPress = "ベンチプレス";
        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight)
                maxWeight = info.getWeight();
        }
        return maxWeight;

    }

    public static double maxSquat(List<RecodeInfo> itemlist) {

        double maxWeight = 0.0;
        String BenchPress = "スクワット";
        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight)
                maxWeight = info.getWeight();
        }
        return maxWeight;

    }

    public static double maxDeadLift(List<RecodeInfo> itemlist) {

        double maxWeight = 0.0;
        String BenchPress = "デットリフト";
        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight)
                maxWeight = info.getWeight();
        }
        return maxWeight;

    }

    public static long lastMaxBenchPress(List<RecodeInfo> itemlist) {
        double maxWeight = 0.0;
        String BenchPress = "ベンチプレス";
        long daysBetween = 0;

        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight) {
                maxWeight = info.getWeight();

                String lastday = info.getDate_column();
                String day = yearAndMonthAndDay();

                LocalDate localDate1 = LocalDate.parse(lastday);
                LocalDate localDate2 = LocalDate.parse(day);

                daysBetween = ChronoUnit.DAYS.between(localDate1, localDate2);
            }
        }

        return daysBetween;
    }

    public static long lastMaxSquat(List<RecodeInfo> itemlist) {

        double maxWeight = 0.0;
        long daysBetween = 0;
        String BenchPress = "スクワット";
        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight) {
                maxWeight = info.getWeight();

                String lastday = info.getDate_column();
                String day = yearAndMonthAndDay();

                LocalDate localDate1 = LocalDate.parse(lastday);
                LocalDate localDate2 = LocalDate.parse(day);

                daysBetween = ChronoUnit.DAYS.between(localDate1, localDate2);
            }
        }

        return daysBetween;
    }

    public static long lastaMaxDeadLift(List<RecodeInfo> itemlist) {

        double maxWeight = 0.0;
        long daysBetween = 0;
        String BenchPress = "デットリフト";
        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight) {
                maxWeight = info.getWeight();

                String lastday = info.getDate_column();
                String day = yearAndMonthAndDay();

                LocalDate localDate1 = LocalDate.parse(lastday);
                LocalDate localDate2 = LocalDate.parse(day);

                daysBetween = ChronoUnit.DAYS.between(localDate1, localDate2);
            }
        }

        return daysBetween;
    }

   public static double rmExchange(RmForm rmForm){

    double weight = rmForm.getWeight();
    double rep = rmForm.getRep();
    double maxWeight = 0.0;

    maxWeight = weight * ( 1 + ( rep / 40 ));

    return maxWeight;

   }
    
}
