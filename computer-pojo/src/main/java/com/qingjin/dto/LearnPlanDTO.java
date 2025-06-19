package com.qingjin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LearnPlanDTO {
    // 用户ID
    private Long userId;
    
    // 每日新词数量
    private Long dailyNew;
    
    // 每日复习数量
    private Long dailyOld;
    
    /**
     * 目标考试类型
     * 0：英语四级
     * 1：英语六级
     * 2：考研英语
     * 3：托福
     * 4：雅思
     */
    private Integer targetTest;
    
    // 考试日期（使用Jackson进行日期格式化）
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate targetData;
    
    /**
     * 学习模式
     * 0：智能算法
     * 1：艾宾浩斯记忆法
     * 2：自定义
     */
    private Integer learnMode;
}