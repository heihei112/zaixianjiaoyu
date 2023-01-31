package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.VideoVo;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2022-07-08
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public boolean removeChapter(String id) {


        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",id);
        videoMapper.delete(queryWrapper);

        return this.removeById(id);
    }

    @Override
    public List<ChapterVo> nestedList(String courseId) {
        // 存放章节的集合
        List<ChapterVo> chapterVos = new ArrayList<>();

        // 获取章节信息
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId).orderByAsc("sort","id");
        List<Chapter> chaptersList = baseMapper.selectList(queryWrapper);

        // 获取课时信息
        QueryWrapper<Video> queryWrapperVideo = new QueryWrapper<>();
        queryWrapperVideo.eq("course_id", courseId).orderByAsc("sort", "id");

        // 查询出所有视频信息
        List<Video> videoList = videoMapper.selectList(queryWrapperVideo);

        chaptersList.forEach(chapter -> {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            // 添加章节信息
            chapterVos.add(chapterVo);

            // 查询所有的视频存储到该集合
            List<VideoVo> videoVoList = new ArrayList<>();
            videoList.forEach(video -> {
                if (video.getChapterId().equals(chapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);

                    videoVoList.add(videoVo);
                }
            });
            // 将视频封装到实体类中
            chapterVo.setChildren(videoVoList);
        });
        return chapterVos;
    }
}
