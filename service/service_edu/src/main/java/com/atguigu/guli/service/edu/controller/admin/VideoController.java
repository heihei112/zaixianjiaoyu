package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.VodFileService;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2022-07-08
 */
@RestController
@RequestMapping("/admin/edu/video")
@Api(tags = "课时管理")
//@CrossOrigin
public class VideoController {
    @Autowired
    private VideoService videoService;

    @ApiOperation("添加课时")
    @PostMapping("save")
    public R saveVideo(@ApiParam("添加课时") @RequestBody Video video ){
        boolean save = videoService.save(video);
        if (save) {
            return R.ok().message("添加成功");
        } else {
            return R.error().message("添加失败");
        }
    }

    @ApiOperation("根据Id查询课时")
    @GetMapping("get/{id}")
    public R getById(@ApiParam("根据id查询课时") @PathVariable String id) {
        Video byId = videoService.getById(id);
        if (byId!=null) {
            return R.ok().data("item",byId).message("查询成功");
        } else {
            return R.error().message("没有数据");
        }
    }

    @ApiOperation("根据id修改课时")
    @PutMapping("update")
    public R updateById(@ApiParam(value="课时对象", required = true) @RequestBody Video video){

        boolean result = videoService.updateById(video);
        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID删除课时")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "课时ID", required = true)
            @PathVariable String id){

        //TODO 删除视频：VOD
        //在此处调用vod中的删除视频文件的接口
        videoService.removeMediaVideoById(id);

        // 删除课时方法
        boolean result = videoService.removeById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }
}

