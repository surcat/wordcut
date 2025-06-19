package com.qingjin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InformCardVO {
    private Long wordLearned;         // 已学习单词
    private Long wordMastered;        // 已掌握单词
    private Long lastWeekLearned;     // 上周已学习单词（用于计算变化率）
    private Long lastWeekMastered;    // 上周已掌握单词（用于计算变化率）
    private Integer dailyNew;            // 每日新学单词量
    private Integer todayLearned;        // 今日已学单词
    private Integer totalToday;          // 今日计划总单词
    private Long learnedDays;         // 累计学习天数
    private Long predictTime;         // 预计用时（分钟）
    private Long daysUntilExam;       // 距离考试天数
    private List<Integer> efficiencyData; // 学习效率数据（7天）
}
