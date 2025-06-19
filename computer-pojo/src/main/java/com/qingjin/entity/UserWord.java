package com.qingjin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWord implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 账号表ID
     */
    private Long userId;
    
    /**
     * 单词表ID
     */
    private Long wordId;
    
    /**
     * 掌握程度：
     * 0：不认识
     * 1：模糊
     * 2：熟悉
     */
    private Integer isMastered;
    
    /**
     * 最近一次复习时间
     */
    private LocalDate lastReview;

    // 掌握程度枚举常量
    public static final class MasteryLevel {
        public static final int UNKNOWN = 0;  // 不认识
        public static final int VAGUE = 1;    // 模糊
        public static final int FAMILIAR = 2; // 熟悉
    }
}