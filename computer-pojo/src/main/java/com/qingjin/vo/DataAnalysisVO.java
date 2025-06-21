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
// DashboardSummaryDTO.java - 聚合数据DTO
public class DataAnalysisVO {
    private Long totalLearningDays;
    private Long continuousLearningDays;
    private Long masteredWords;
    private Double averageEfficiency;
    private Double memoryPersistence;
    private Double masteredWordsChange;
    private Double efficiencyChange;
    private Double persistenceChange;
    
    private List<Integer> timeDistribution;
    private List<Double> theoreticalForgettingCurve;
    private List<Double> actualForgettingCurve;
    private List<Integer> vocabularyTrendTotal;
    private List<Integer> vocabularyTrendNew;
    private List<Double> efficiencyTrend;
    
    // 构造函数、getter和setter
}