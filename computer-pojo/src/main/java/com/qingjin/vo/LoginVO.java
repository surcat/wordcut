package com.qingjin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description="用户登录返回数据格式")
public class LoginVO implements Serializable {
    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("jwt令牌")
    private String token;
}
