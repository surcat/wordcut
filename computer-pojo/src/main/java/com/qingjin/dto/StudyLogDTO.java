package com.qingjin.dto;

/**
 * @className: StudyLogDTO
 * @author: qingjin
 * @description: TODO
 * @date: 2025/6/19 21:10
 * @version: 1.0
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * 单词操作记录日志
 */
public class StudyLogDTO {
    private Long wordId;
    private Integer operation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String timePeriod;
}
