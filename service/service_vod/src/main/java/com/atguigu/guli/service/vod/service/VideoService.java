package com.atguigu.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

public interface VideoService {

    String uploadVideo(InputStream inputStream , String originFilename);

    void removeVodId(String videoId) throws ClientException;

    void removeByList(List<String> videoList) throws ClientException;

    String getPlayAuth(String videoSourceId) throws ClientException;
}
