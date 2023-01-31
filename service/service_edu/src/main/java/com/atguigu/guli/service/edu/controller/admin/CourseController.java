package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.OssFileService;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2022-07-08
 */
@RestController
@RequestMapping("/admin/edu/course")
@Api(tags = "课程管理")
//@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private VideoService videoService;


    @ApiOperation( "保存课程接口")
    @PostMapping("save-course-info")
    public R saveCourseInfo(@RequestBody CourseInfoForm courseInfoForm){

        String courseId = courseService.saveCourse(courseInfoForm);

        return R.ok().data("courseId",courseId).message("添加课程成功");
    }

    @ApiOperation("查询课程回显")
    @GetMapping("/course-info/{parentId}")
    public R selectCourseById(@PathVariable String parentId) {
        CourseInfoForm courseInfoForm = courseService.getCourseById(parentId);
        if (courseInfoForm != null) {
            return R.ok().data("item", courseInfoForm);
        } else {
            return R.ok().message("数据回显出错");
        }
    }
    @ApiOperation( "保存课程接口")
    @PutMapping("update-course-info")
    public R updateCourseInfo(@RequestBody CourseInfoForm courseInfoForm){

        courseService.updateCourse(courseInfoForm);

        return R.ok().message("修改课程成功");
    }

    @ApiOperation("展示课程")
    @GetMapping("list/{page}/{limit}")
    public R index(@PathVariable Long page, @PathVariable Long limit,CourseQueryVo courseQueryVo){
        IPage<CourseVo> courseVoIPage =  courseService.selectPage(page,limit,courseQueryVo);

        List<CourseVo> records = courseVoIPage.getRecords();
        long total = courseVoIPage.getTotal();
        return R.ok().data("total",total).data("records",records).message("查询课程成功");
    }
    @ApiOperation("删除课程模块")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "课程id", required = true) @PathVariable String id){
        // 删除阿里云oss课程封面
        courseService.removeCoverById(id);
        // 删除阿里云vod中的视频
        videoService.removeMediaVideoCourse(id);

        boolean result  = courseService.removeCourseById(id);
        if (result){
            return R.ok().message("删除成功");
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("根据ID获取课程发布信息")
    @GetMapping("course-publish/{id}")
    public R getCoursePublishVoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id){

        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);
        if (coursePublishVo != null) {
            return R.ok().data("item", coursePublishVo);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("通过id发布课程")
    @PutMapping("publish-course/{id}")
    public R updateCourseStatus(@ApiParam(value = "课程id",required = true)
                                    @PathVariable String id){
        boolean result =  courseService.publishCourseById(id);
        if (result) {
            return R.ok().message("发布成功");
        } else {
            return R.error().message("发布失败");
        }
    }

}

