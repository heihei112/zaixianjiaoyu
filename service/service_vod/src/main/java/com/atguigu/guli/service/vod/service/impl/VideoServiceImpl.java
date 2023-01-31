package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.VideoService;
import com.atguigu.guli.service.vod.util.AliyunVodSDKUtils;
import com.atguigu.guli.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VodProperties vodProperties;


    @Override
    public String uploadVideo(InputStream inputStream, String originFilename) {
        String title = originFilename.substring(0, originFilename.lastIndexOf("."));
        UploadStreamRequest request = new UploadStreamRequest(
                vodProperties.getKeyid(),vodProperties.getKeysecret(),title,originFilename,inputStream);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        String videoId = response.getVideoId();
        if (StringUtils.isEmpty(videoId)){
            log.error("阿里云上传失败"+response.getCode()+"-"+response.getMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

        return videoId;
    }

    @Override
    public void removeVodId(String videoId) throws ClientException {

        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoId);
        client.getAcsResponse(request);
    }

    @Override
    public void removeByList(List<String> videoList) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
        DeleteVideoRequest request = new DeleteVideoRequest();
        int size = videoList.size();
        StringBuffer videoStrs = new StringBuffer();
        for (int i = 0; i < size; i++) {
            videoStrs.append(videoList.get(i));
            if (i == size-1 || i % 20 == 19) {
                request.setVideoIds(videoStrs.toString());
                client.getAcsResponse(request);
                videoStrs = new StringBuffer();
            } else if (i % 20 < 19) {
                videoStrs.append(",");
            }
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) throws ClientException {
        // 初始化方法
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(),
                vodProperties.getKeysecret());
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoSourceId);

        GetPlayInfoResponse response = client.getAcsResponse(request);
        try {
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
              return playInfo.getPlayURL();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
