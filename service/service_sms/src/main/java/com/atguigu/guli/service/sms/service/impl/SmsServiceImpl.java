package com.atguigu.guli.service.sms.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.atguigu.guli.service.sms.service.SmsService;
import com.atguigu.guli.service.sms.util.HttpUtils;
import com.atguigu.guli.common.base.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {


    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void send(String mobile) throws ClientException {

        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "post";
        String appcode = "4dec5c9ab67a4b079aadff5314e310c9";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        //随机生成四位验证码,需要工具,看自己需求可以不加
        String fourBitRandom = RandomUtils.getFourBitRandom();
        querys.put("mobile", mobile);
        querys.put("param", "code:"+fourBitRandom);
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();
        System.out.println(fourBitRandom);

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            redisTemplate.opsForValue().set(mobile,fourBitRandom,30, TimeUnit.DAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    }

