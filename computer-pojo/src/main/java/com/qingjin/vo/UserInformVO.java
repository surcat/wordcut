package com.qingjin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: UserInformVO
 * @author: qingjin
 * @description: TODO
 * @date: 2025/6/18 23:15
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description="账号信息查询返回数据格式")
public class UserInformVO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("注册邮箱")
    private String email;

    @ApiModelProperty("学习天数")
    private long learnDay;

    @ApiModelProperty("学习单词数")
    private long learnWord;
}
