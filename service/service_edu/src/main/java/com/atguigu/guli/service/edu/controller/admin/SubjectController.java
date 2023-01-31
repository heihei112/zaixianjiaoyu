package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2022-07-08
 */
@Api(tags = "课程导入模块")
@RestController
//@CrossOrigin
@Slf4j
@RequestMapping("/admin/edu/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation("导入Excel文件")
    @PostMapping("import")
    public R batchImport(@RequestParam("file") MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("上传成功");
        } catch (Exception e) {
            log.info(ExceptionUtils.getMessage(e));
            throw  new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation("查询分类数据")
    @GetMapping("nested-list")
    public R selectByIdAndParentId(){
        List<SubjectVo> subjectVos = subjectService.nestedList();
        return R.ok().data("items",subjectVos);
    }

}

