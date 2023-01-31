package com.atguigu.guli.service.edu.Feign;

import com.atguigu.guli.common.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-vod")
@Service
public interface VodFileService {

    @DeleteMapping("/admin/vod/media/remove/{videoId}")
    R removeByVideo(@PathVariable String videoId);

    @DeleteMapping("/admin/vod/media/remove")
    R remove(@RequestBody List<String> videoList);
}
