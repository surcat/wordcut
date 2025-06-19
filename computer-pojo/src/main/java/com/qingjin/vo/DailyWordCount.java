package com.qingjin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyWordCount {
    private LocalDate date;         // 日期
    private int wordCount;          // 学习的单词数量（去重）
    private int operationCount;     // 操作次数（不去重）
    
    // 格式化日期为星期几
    public String getWeekDay() {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return weekDays[date.getDayOfWeek().getValue() % 7];
    }
}