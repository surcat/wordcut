package com.qingjin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.qingjin.constant.DataTimeFrontConstant;
import com.qingjin.entity.LearnPlan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @className: LearnPlanVO
 * @author: qingjin
 * @description: 用户学习计划展示对象
 * @date: 2025/6/19
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用户学习计划信息")
public class LearnPlanVO {

    @ApiModelProperty(value = "每日新词数量", example = "20")
    private Integer dailyNew;

    @ApiModelProperty(value = "每日复习数量", example = "20")
    private Integer dailyOld;

    @ApiModelProperty(
            value = "目标考试类型：0-英语四级, 1-英语六级, 2-考研英语, 3-托福, 4-雅思",
            example = "0"
    )
    private Integer targetTest;

    @ApiModelProperty(
            value = "目标考试类型名称",
            example = "英语四级"
    )
    private String targetTestName;



    @JsonFormat(pattern = DataTimeFrontConstant.DEFAULT_DATE_FORMAT)
    @ApiModelProperty(
            value = "考试日期（格式：" + DataTimeFrontConstant.DEFAULT_DATE_FORMAT + "）",
            example = "2025-12-20"
    )
    private LocalDate targetDate;

    @ApiModelProperty(
            value = "学习模式：0-智能算法, 1-艾宾浩斯记忆法, 2-自定义",
            example = "0"
    )
    private Integer learnMode;

    @ApiModelProperty(
            value = "学习模式名称",
            example = "智能算法"
    )
    private String learnModeName;

    /**
     * 从实体对象转换为VO对象
     * @param entity 数据库实体
     * @return 展示对象
     */
    public static LearnPlanVO fromEntity(LearnPlan entity) {
        if (entity == null) {
            return null;
        }

        return LearnPlanVO.builder()
                .dailyNew(entity.getDailyNew())
                .dailyOld(entity.getDailyOld())
                .targetTest(entity.getTargetTest())
                .targetTestName(getTargetTestName(entity.getTargetTest()))
                .targetDate(entity.getTargetData()) // 直接使用LocalDate类型
                .learnMode(entity.getLearnMode())
                .learnModeName(getLearnModeName(entity.getLearnMode()))
                .build();
    }

    /**
     * 获取考试类型名称
     * @param code 考试类型代码
     * @return 考试类型名称
     */
    private static String getTargetTestName(Integer code) {
        if (code == null) return "未知";

        switch (code) {
            case 0: return "英语四级";
            case 1: return "英语六级";
            case 2: return "考研英语";
            case 3: return "托福";
            case 4: return "雅思";
            default: return "未知考试";
        }
    }

    /**
     * 获取学习模式名称
     * @param code 学习模式代码
     * @return 学习模式名称
     */
    private static String getLearnModeName(Integer code) {
        if (code == null) return "未知";

        switch (code) {
            case 0: return "智能算法";
            case 1: return "艾宾浩斯记忆法";
            case 2: return "自定义";
            default: return "未知模式";
        }
    }
}