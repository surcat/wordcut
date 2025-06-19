package com.qingjin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @className: UserInformation
 * @author: qingjin
 * @description: TODO
 * @date: 2025/6/18 23:23
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation implements Serializable {
    private static final long serialVersionUID = 1L;

    // id 主键 唯一标识
    private Long id;

    // 用户ID
    private Long userId;

    // 学习天数
    private Long learnDay;

    // 学习单词数
    private Long learnWord;
}
