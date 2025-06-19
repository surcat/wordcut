package com.qingjin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录数据传输类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "登录数据传输类")
public class LoginDTO implements Serializable {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;
}
