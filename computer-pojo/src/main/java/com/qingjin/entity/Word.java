package com.qingjin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Word implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;  // 添加ID字段
    private String word;
    private String phonetic;
    private String definition;
    private String example;
}