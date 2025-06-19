package com.qingjin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 用户学习计划表实体类
 * 对应数据库表：learn_plan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LearnPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 主键 */
    private Integer id;

    /** 用户id */
    private Integer userId;

    /** 每日新词数量（默认20） */
    private Integer dailyNew = 20;

    /** 每日复习数量（默认20） */
    private Integer dailyOld = 20;

    /** 目标考试类型（默认0） */
    private Integer targetTest = 0;

    /** 考试日期 */
    private LocalDate targetData;

    /** 学习模式（默认0） */
    private Integer learnMode = 0;
}