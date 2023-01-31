package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.OssFileService;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author limou
 * @since 2022-07-08
 */
@Api(tags = "讲师管理")
@RestController
//@CrossOrigin
@RequestMapping("/admin/edu/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;


    /**
     * 查询所有数据
     * @return
     */
    @ApiOperation( "所有讲师列表")
    @GetMapping("/list")
    public R findAll(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list).message("查询成功");
    }

    /**
     * 通过id删除数据
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(value = "讲师ID") @PathVariable("id") String id ){
        // 删除图片
        teacherService.removeFile(id);

        // 删除用户
        boolean result = teacherService.removeById(id);
        if (result){
            return R.ok().message("删除成功");
        }else
            return R.error().message("数据不存在");
    }

    @ApiOperation(value = "根据ID删除讲师列表")
    @DeleteMapping("/removeList")
    public R removeByIdList(@ApiParam(value = "讲师ID列表") @RequestBody List<String> idList ){



        boolean result = teacherService.removeByIds(idList);
        if (result){
            return R.ok().message("删除成功");
        }else
            return R.error().message("数据不存在");
    }

    @ApiOperation(value = "查询分页信息")
    @GetMapping("/selectPage/{page}/{limit}")
    public R selectPage(@ApiParam(value = "分页起始值") @PathVariable("page") int page,
                        @ApiParam(value = "每页分页条数") @PathVariable("limit") int limit,
                        @ApiParam(value = "查询条件") TeacherQueryVo teacherQueryVo){
      IPage<Teacher> pageParam =  teacherService.selectPage(page,limit,teacherQueryVo);
        List<Teacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation(value = "添加讲师数据")
    @PostMapping("/save")
    public R save(@ApiParam(value = "添加的参数") @RequestBody Teacher teacher){
        teacherService.save(teacher);
        return R.ok().message("添加成功");
    }

    @ApiOperation(value = "修改数据")
    @PutMapping("/update")
    public R update(@RequestBody Teacher teacher){
        boolean result = teacherService.updateById(teacher);
        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }
    @ApiOperation(value = "回显用户信息")
    @GetMapping("/getById/{id}")
    public R getById(@PathVariable("id") String id){
        Teacher item = teacherService.getById(id);
        if (item!=null){
            return R.ok().data("item",item);
        }else {
            return R.error().message("查询失败");
        }
    }

    @ApiOperation("模糊查询名称展示")
    @GetMapping("/list/name/{name}")
    public R selectByNameMap(@PathVariable("name") String name){
        List<Map<String,Object>> nameList = teacherService.selectMaps(name);
        return R.ok().data("name",nameList).message("查询成功");
    }


}

