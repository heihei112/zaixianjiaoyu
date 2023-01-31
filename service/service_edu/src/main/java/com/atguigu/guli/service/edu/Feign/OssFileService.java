package com.atguigu.guli.service.edu.Feign;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.fallbock.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
@Service
public interface OssFileService {
    @GetMapping("/admin/oss/file/test")
    R Test();

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);
}