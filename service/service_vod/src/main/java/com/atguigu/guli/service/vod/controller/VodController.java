package com.atguigu.guli.service.vod.controller;

import com.aliyuncs.exceptions.ClientException;
import com.atguigu.guli.common.base.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Api(tags="阿里云视频点播")
//@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/vod/media")
@Slf4j
public class VodController {

    @Autowired
    private VideoService videoService;

    @PostMapping("upload")
    public R uploadVideo(@RequestParam("file") MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String videoId =  videoService.uploadVideo(inputStream,originalFilename);
            return R.ok().message("文件上传成功").data("videoId",videoId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

    }

    @DeleteMapping("remove/{videoId}")
    public R removeVideoId(@PathVariable("videoId") String videoId){
        try {
            videoService.removeVodId(videoId);
            return R.ok().message("删除成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @DeleteMapping("remove")
    public R removeList(@RequestBody List<String> videoList) {
        try {
            videoService.removeByList(videoList);
            return R.ok().message("文件删除成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }


}
