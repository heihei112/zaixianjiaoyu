package com.atguigu.guli.service.edu.fallbock;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;


@Slf4j
@Service
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R Test() {
        return R.error();
    }

    @Override
    public R removeFile(String url) {
        log.error("开启熔断保护机制");
        return R.error();
    }
}
