package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.Feign.VodFileService;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2022-07-08
 */
@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodFileService vodFileService;


    /**
     * 删除课时 同时删除视频
     * @param id
     */
    @Override
    public void removeMediaVideoById(String id) {
        log.warn("videoId:{}",id);
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        log.warn("videoSourceId:{}",videoSourceId);
        vodFileService.removeByVideo(videoSourceId);
    }

    @Override
    public void removeMediaVideoByMap(String chapterId) {

        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("chapter_id",chapterId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        List<String> list = removeByComment(maps);
        // 调用Vod删除视频
        vodFileService.remove(list);

    }

    @Override
    public void removeMediaVideoCourse(String courseId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("course_Id",courseId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        List<String> list = removeByComment(maps);

        vodFileService.remove(list);
    }


    // 抽取的公共方法
    public List<String> removeByComment(List<Map<String,Object>> maps){
        List<String> list = new ArrayList<>();
        maps.forEach((map)->{
            String videoId =  (String)map.get("video_source_id");
            list.add(videoId);
        });
        return list;
    }


}
