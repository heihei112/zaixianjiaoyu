package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Helen
 * @since 2022-07-08
 */
public interface TeacherService extends IService<Teacher> {

    IPage<Teacher> selectPage(int page, int limit, TeacherQueryVo teacherQueryVo);

    List<Map<String, Object>> selectMaps(String name);

    boolean removeFile(String id);

    Map<String,Object> selectByTeacherAndCourse(String teacherId);

    List<Teacher> selectHotTeacher();
}
