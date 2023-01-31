package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.base.DTO.CourseDto;
import com.atguigu.guli.service.edu.Feign.OssFileService;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2022-07-08
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    @Autowired
    private OssFileService ossFileService;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CourseCollectMapper courseCollectMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    /**
     * 添加课程数据
     */
    public String saveCourse(CourseInfoForm courseInfoForm) {
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.insert(course);

        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoForm.getDescription());

        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    /**
     * 查询一级分类和二级分类
     * @param parentId
     * @return
     */
    @Override
    public CourseInfoForm getCourseById(String parentId) {

        Course course = baseMapper.selectById(parentId);

        CourseDescription courseDescription = courseDescriptionMapper.selectById(parentId);

        CourseInfoForm courseInfoForm = new CourseInfoForm();

        BeanUtils.copyProperties(course, courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    /**
     * 修改方法
     * @param courseInfoForm
     * @return
     */
    @Override
    public void updateCourse(CourseInfoForm courseInfoForm) {
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.updateById(course);

        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoForm.getDescription());

        courseDescriptionMapper.updateById(courseDescription);

    }

    /**
     * 使用条件和分页查询所有课程
     * @param page
     * @param limit
     * @param courseQueryVo
     * @return
     */
    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVo courseQueryVo) {

        QueryWrapper<CourseVo> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("c.gmt_create");


        // 二级分类Id
        String subjectId = courseQueryVo.getSubjectId();
        // 一级分类标题
        String title = courseQueryVo.getTitle();
        // 讲师id
        String teacherId = courseQueryVo.getTeacherId();
        // 一级分类Id
        String subjectParentId = courseQueryVo.getSubjectParentId();
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("c.title",title);
        }
        if (!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("c.teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("c.subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("c.subject_id",subjectId);
        }


        Page<CourseVo> pageParam = new Page<>(page, limit);
        List<CourseVo> reduces =  baseMapper.selectPageByCourseAndQuery(pageParam,queryWrapper);
        pageParam.setRecords(reduces);
        return pageParam;
    }

    @Override
    public boolean removeCoverById(String id) {
        Course course = baseMapper.selectById(id);
        if (course!=null) {
            String cover = course.getCover();
            if (!StringUtils.isEmpty(cover)){
                R r = ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public boolean removeCourseById(String id) {
        // 删除收藏信息
        QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",id);

        courseCollectMapper.delete(queryWrapper);

        // 评论信息: comment
        QueryWrapper<Comment> comment = new QueryWrapper<>();
        queryWrapper.eq("course_id",id);
        commentMapper.delete(comment);

        // 章节信息: video

        QueryWrapper<Video> video = new QueryWrapper<>();
        queryWrapper.eq("course_id",id);
        videoMapper.delete(video);

        //章节信息：chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);

        //课程详情：course_description
        courseDescriptionMapper.deleteById(id);

        return this.removeById(id);
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.selectByPushAndById(id);
    }

    /**
     * 通过id发布状态
     * @param id
     * @return
     */
    @Override
    public boolean publishCourseById(String id) {
        UpdateWrapper<Course> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id).set("status",Course.COURSE_NORMAL);
        return this.update(wrapper);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo queryVo) {
        // 一级分类id
        String subjectParentId = queryVo.getSubjectParentId();
        // 二级分类id
        String subjectId = queryVo.getSubjectId();
        // 根据销量排序
        String buyCountSort = queryVo.getBuyCountSort();
        // 根据价格排序
        String priceSort = queryVo.getPriceSort();
        // 根据时间排序
        String gmtCreateSort = queryVo.getGmtCreateSort();

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", Course.COURSE_NORMAL);
        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(buyCountSort)){
            queryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(gmtCreateSort)){
            queryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(priceSort)){
            if (queryVo.getType() == null || queryVo.getType() == 1) {
                queryWrapper.orderByDesc("price");
            } else {
                queryWrapper.orderByAsc("price");
            }

        }

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public WebCourseVo selectWebCourseVoById(String id) {
        Course course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);

        return baseMapper.selectWebCourseVoById(id);
    }

    @Cacheable(value = "index", key = "'selectHotCourse'")
    @Override
    public List<Course> selectHotCourse() {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {

        return baseMapper.selectCounstById(courseId);
    }
}
