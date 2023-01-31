package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.base.DTO.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin
@Api(tags = "课程列表展示")
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired

    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;



    @ApiOperation("查询所有课程列表")
    @GetMapping("list")
    public R list(@ApiParam(value = "封装的查询条件",required = false) WebCourseQueryVo webCourseQueryVo){

       List<Course> courseList = courseService.webSelectList(webCourseQueryVo);
       return R.ok().data("courseList",courseList);
    }

    @ApiOperation("根据id查询课程")
    @GetMapping("get/{courseId}")
    public R selectWebCourseById(@ApiParam("课程id") @PathVariable("courseId") String courseId){
        // 查询课程信息集合
        System.out.println("==============="+courseId);
        WebCourseVo courseList = courseService.selectWebCourseVoById(courseId);
        // 查询分类信息List
        List<ChapterVo> chapterList = chapterService.nestedList(courseId);
        return R.ok().data("courseList",courseList).data("chapterList",chapterList);
    }

    @ApiOperation("查询课程信息")
    @GetMapping("inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(@PathVariable("courseId") String courseId){
       CourseDto courseDto = courseService.getCourseDtoById(courseId);
       return courseDto;
    }
}
