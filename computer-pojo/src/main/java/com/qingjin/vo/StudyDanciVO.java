package com.qingjin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: StudyDanciVO
 * @author: qingjin
 * @description: TODO
 * @date: 2025/6/19 16:52
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description="单词背诵界面返回数据格式")
public class StudyDanciVO {
    @ApiModelProperty("累计学习天数")
    private long learnDay;

    @ApiModelProperty("今日新词数")
    private long dailyNew;

    @ApiModelProperty("代复习单词数")
    private long dailyOld;

    @ApiModelProperty("已完成单词数")
    private long todayWord;
}
