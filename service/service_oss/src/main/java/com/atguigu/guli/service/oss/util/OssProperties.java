package com.atguigu.guli.service.oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Data
@Component
public class OssProperties {
    private String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private String keyid = "LTAI5tQeBzCtUkP9PABr3UYx";
    private String keysecret = "iKx9aQ4gP3jwF3r5cSUvyRt0BKuq3l";
    private String bucketname = "guli-file-7-9";
}
