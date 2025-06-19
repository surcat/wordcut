package com.qingjin.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer userId;

    private LocalDate lastTime;

    private Integer todayWord = 0;
}