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
import com.firstProject.usefulltools.form.BmiForm;
import com.firstProject.usefulltools.form.PfcForm;
import com.firstProject.usefulltools.form.RmForm;
import com.firstProject.usefulltools.service.RecodeService;

public class Analytics { // 様々なデータを計算しているクラス

    public String getCurrentUsername() { // ログインしているusernameを取ってくる

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return userDetails.getUsername();
        }
        return null; // 認証されていない場合はnullを返す
    }

    public static String yearAndMonthAndDay() { // 現在の日付を取得

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }

    public static String yearAndMonth() { // 現在の年月取得

        LocalDate currenDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formatterDate = currenDate.format(formatter);

        return formatterDate;
    }

    public static String getPreviousYearDay() { // 前日の日付を取得

        LocalDate currentDate = LocalDate.parse(yearAndMonthAndDay());
        LocalDate previousMonthDate = currentDate.minusMonths(1);

        return previousMonthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static double calculateTotalWeight(List<RecodeInfo> itemlist) { // 当日のトータル重量を計算

        double alllWeight = 0.0;

        for (RecodeInfo info : itemlist) {
            if (yearAndMonthAndDay().equals(info.getDate_column()))
                alllWeight += info.getWeight() * info.getRep();
        }
        return alllWeight;

    }

    public static double calculatePreviousTotalWeight(List<RecodeInfo> itemlist) { // 前日のトータル重量を計算

        double alllWeight = 0.0;

        for (RecodeInfo info : itemlist) {
            if (getPreviousYearDay().equals(info.getDate_column()))
                alllWeight += info.getWeight() * info.getRep();
        }
        return alllWeight;

    }

    public static double calculatePreviousDayCountPercentage(List<RecodeInfo> itemlist) { // 前月比を計算

        double currentMonthCount = caculateTotalCount(itemlist);
        double previousMonthCount = caculatePreviousTotalCount(itemlist);

        // パーセンテージを計算
        if (previousMonthCount == 0) {
            return 0.0; // 前月のデータがない場合、0% を返す
        } else {
            return ((double) (currentMonthCount - previousMonthCount) / previousMonthCount) * 100.0;
        }
    }

    public static double calculatePreviousMonthWeightPercentage(List<RecodeInfo> itemlist) {
        // 現在の月の合計重量を計算
        double currentMonthCount = calculateTotalWeight(itemlist);

        // 前月の合計重量を計算
        double previousMonthCount = calculatePreviousTotalWeight(itemlist);

        // パーセンテージを計算
        if (previousMonthCount == 0) {
            return 0.0; // 前月のデータがない場合、0.0% を返す
        } else {     
            return Math.floor(((currentMonthCount - previousMonthCount) / previousMonthCount) * 100.0) ;
            // 小数点以下二桁までの計算として切り捨て
        }
    }

    public static double calculateMonthTotalWeight(List<RecodeInfo> itemlist) { // 当月のトータル重量を計算

        double alllWeight = 0.0;

        for (RecodeInfo info : itemlist) {
            if (yearAndMonth().equals(info.getDate_column().substring(0, 7)))
                alllWeight += info.getWeight() * info.getRep();
        }
        return alllWeight;
    }

    @Autowired
    RecodeService recodeService;

    public static long caculatefromday(List<RecodeInfo> LastList) { // 最後のトレーニング日からの経過日数を取得

        if (LastList == null || LastList.isEmpty()) {
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

    public static int caculateTotalCount(List<RecodeInfo> itemlist) { // 当日のトータル重量を取得

        int allCount = 0;

        for (RecodeInfo info : itemlist) {
            if (yearAndMonthAndDay().equals(info.getDate_column()))
                allCount++;
        }
        return allCount;
    }

    public static int calculatepreviousMonthWeight(List<RecodeInfo> itemlist) { // 当月のトータル重量を計算

        int alllWeight = 0;

        for (RecodeInfo info : itemlist) {
            if (getPreviousYearMonth().equals(info.getDate_column().substring(0, 7)))
                alllWeight += info.getWeight() * info.getRep();
        }

        return alllWeight;

    }

    public static double calculateMonthTotalWeightPercentage(List<RecodeInfo> itemlist) {
        double currentMonthWeight = 100;
        double previousMonthWeight = 51;

        // パーセンテージを計算
        if (previousMonthWeight == 0) {
            return 0.0; // 前月のデータがない場合、0.0% を返す
        } else {
            double percentage = ((currentMonthWeight - previousMonthWeight) / previousMonthWeight) * 100.0;
            return Math.floor(percentage * 100.0) / 1.0;
            // 小数点以下二桁までの計算として切り捨て
        }
    }

    public static int caculatePreviousTotalCount(List<RecodeInfo> itemlist) { // 当日のトレーニング回数を計算

        int allCount = 0;

        for (RecodeInfo info : itemlist) {
            if (getPreviousYearDay().equals(info.getDate_column()))
                allCount++;
        }
        return allCount;
    }

    public static int calculateMonthTotalCount(List<RecodeInfo> itemlist) { // 当月のトレーニング回数を計算

        int allCount = 0;

        for (RecodeInfo info : itemlist) {
            if (yearAndMonth().equals(info.getDate_column().substring(0, 7)))
                allCount++;
        }
        return allCount;
    }

    public static int calculatepreviousMonthCount(List<RecodeInfo> itemlist) { // 前月のトレーニング回数を計算

        int allCount = 0;

        for (RecodeInfo info : itemlist) {
            if (getPreviousYearMonth().equals(info.getDate_column().substring(0, 7)))
                allCount++;
        }
        return allCount;
    }

    public static String getPreviousYearMonth() { // 前月を取得

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
            return (int) ((double) (currentMonthCount - previousMonthCount) / previousMonthCount * 100.0);
        }
    }

    public static int calculateTotalTrainingCount(List<RecodeInfo> itemlist) { // 当月のトレーニング回数

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

    public static double maxBenchPress(List<RecodeInfo> itemlist) { // ベンチプレスの最高記録

        double maxWeight = 0.0;
        String BenchPress = "ベンチプレス";

        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight)
                maxWeight = info.getWeight();
        }
        return maxWeight;

    }

    public static double maxSquat(List<RecodeInfo> itemlist) { // スクワットの最高記録

        double maxWeight = 0.0;
        String BenchPress = "スクワット";

        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight)
                maxWeight = info.getWeight();
        }
        return maxWeight;

    }

    public static double maxDeadLift(List<RecodeInfo> itemlist) { // デットリフトの最高記録

        double maxWeight = 0.0;
        String BenchPress = "デットリフト";

        for (RecodeInfo info : itemlist) {
            if (BenchPress.equals(info.getEvent()) && info.getWeight() > maxWeight)
                maxWeight = info.getWeight();
        }
        return maxWeight;

    }

    public static long lastMaxBenchPress(List<RecodeInfo> itemlist) { // 最後の記録更新から経過した日数

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

    public static long lastMaxSquat(List<RecodeInfo> itemlist) { // 最後の記録更新から経過した日数

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

    public static long lastaMaxDeadLift(List<RecodeInfo> itemlist) { // 最後の記録更新から経過した日数

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

    public static double rmExchange(RmForm rmForm) { // RM換算機

        double weight = rmForm.getRmWeight();
        double rep = rmForm.getRep();
        double maxWeight = 0.0;

        maxWeight = weight * (1 + (rep / 40));

        return maxWeight;

    }

    public static double bmiConverter(BmiForm bmiForm) {
        double weight = bmiForm.getBmiWeight();
        double height = bmiForm.getHight() / 100.0;
        double bmi = 0.0;

        if (weight == 0.0 || height == 0.0) {
            bmi = 0.0;
        } else {
            bmi = Math.floor(weight / (height * height)) / 1.0;
        }

        return bmi;

    }

    public static double basalMetabolismRateCalculator(PfcForm pfcform) {
        double bmr = 0.0;

        String sex = pfcform.getPfcSex();
        if (sex != null && sex.equals("男")) {
            bmr = Math.floor(
                    (66.5 + pfcform.getPfcWeight() * 13.8 + pfcform.getPfcHight() * 5.0 - pfcform.getPfcAge() * 6.8));
        } else if (sex != null) {
            bmr = Math.floor(
                    (665 + pfcform.getPfcWeight() * 9.6 + pfcform.getPfcHight() * 1.9 - pfcform.getPfcAge() * 4.7));
        }

        return bmr;
    }

    public static double totalCalCalculator(PfcForm pfcform) {
        double totalCal = 0.0;
        totalCal = Math.floor(basalMetabolismRateCalculator(pfcform) * pfcform.getActivelevel());

        return totalCal;
    }

    public static double idealBasalMetabolismCalculator(PfcForm pfcform) {
        double bmr = 0.0;

        String sex = pfcform.getPfcSex();
        if (sex != null && sex.equals("男")) {
            bmr = Math.floor((66.5 + (pfcform.getPfcWeight() * 13.8) + (pfcform.getPfcHight() * 5.0)
                    - (pfcform.getPfcAge() * 6.8))
                    * pfcform.getActivelevel());
        } else if (sex != null) {
            bmr = Math.floor(
                    (665 + pfcform.getPfcWeight() * 9.6 + pfcform.getPfcHight() * 1.9 - pfcform.getPfcAge() * 4.7)
                            * pfcform.getActivelevel());
        }

        return bmr;
    }

    public static double idealTotalCalCalculator(PfcForm pfcform) {
        double idealTotalCal = 0.0;
        idealTotalCal = Math.floor(basalMetabolismRateCalculator(pfcform) + totalCalCalculator(pfcform) / 2);

        return idealTotalCal;
    }

    public static double proteinCalculator(PfcForm pfcform) {
        double protein = 0.0;
        protein = Math.floor(pfcform.getPfcWeight() * 1.6);

        return protein;
    }

    public static double fatCalculator(PfcForm pfcform) {
        double fat = 0.0;

        fat = Math.floor((0.3 * idealTotalCalCalculator(pfcform)) / 9);

        return fat;
    }

    public static double carbohydrateCalculator(PfcForm pfcform) {
        double carbohydrate = 0.0;

        carbohydrate = Math.floor((0.7 * idealTotalCalCalculator(pfcform)) / 4);

        return carbohydrate;
    }

}
