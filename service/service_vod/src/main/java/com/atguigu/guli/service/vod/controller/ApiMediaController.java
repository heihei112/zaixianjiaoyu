package com.atguigu.guli.service.vod.controller;

import com.atguigu.guli.common.base.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
@Api(tags = "获取阿里云播放凭证")
@RequestMapping("/api/edu/media")
@Slf4j
public class ApiMediaController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("获取播放凭证")
    @GetMapping("playAuth/{videoSourceId}")
    public R getPlayAuth(@ApiParam("通过id获取播放凭证") @PathVariable String videoSourceId){

        try {
            String getPlayURL = videoService.getPlayAuth(videoSourceId);
            return R.ok().data("playUrl",getPlayURL);
        } catch (Exception e) {
            log.warn(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.FETCH_VIDEO_UPLOADAUTH_ERROR);
        }
    }
}