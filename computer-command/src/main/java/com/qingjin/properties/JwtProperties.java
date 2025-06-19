package com.qingjin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user.jwt")
@Data
public class JwtProperties {

    /**
     * 用户请求生成jwt令牌相关配置
     * Key ： 加密标签
     * Ttl ： 过期时间
     * TokenName ： token令牌名称
     */
    private String Key;
    private long Ttl;
    private String TokenName;


    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
/*
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;
*/

}
