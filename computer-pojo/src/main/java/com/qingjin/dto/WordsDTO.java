package com.qingjin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: WordsDTO
 * @author: qingjin
 * @description: TODO
 * @date: 2025/6/19 6:49
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "单词数据传输类")
public class WordsDTO {
    @ApiModelProperty("单词")
    private String word;
    @ApiModelProperty("音标")
    private String phonetic;
    @ApiModelProperty("中文翻译")
    private String definition;
    @ApiModelProperty("例句")
    private String example;
}
