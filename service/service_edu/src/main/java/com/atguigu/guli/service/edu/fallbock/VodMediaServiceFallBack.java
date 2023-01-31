package com.atguigu.guli.service.edu.fallbock;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.VodFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VodMediaServiceFallBack implements VodFileService {

    @Override
    public R removeByVideo(String videoId) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R remove(List<String> videoList) {
        log.info("熔断保护");
        return R.error();
    }
}