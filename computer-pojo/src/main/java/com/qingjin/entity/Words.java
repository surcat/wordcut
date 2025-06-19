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
public class Words implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String word;
    private String phonetic;
    private String definition;
    private String example;
}