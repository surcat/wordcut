package com.qingjin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @className: StudyLog
 * @author: qingjin
 * @description: TODO
 * @date: 2025/6/19 21:23
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long wordId;
    private Integer operationType;
    private LocalDate logDate;
    private String timePeriod;
    private LocalDateTime createdAt;
}
