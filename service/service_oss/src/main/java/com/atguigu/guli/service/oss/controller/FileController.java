package com.atguigu.guli.service.oss.controller;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/admin/oss/file")
//@CrossOrigin
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("头像上传")
    @PostMapping("/upload")
    public R uploadOss(@RequestParam("file") MultipartFile file, @RequestParam("module") String module) throws IOException {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String upUrl = fileService.saveUpload(inputStream, module, originalFilename);

            return R.ok().message("上传成功").data("url", upUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }


    @ApiOperation(value = "删除图片")
    @DeleteMapping("/remove")
    public R removeFile(@RequestBody String url) {
        fileService.removeFile(url);
        return R.ok().message("文件删除成功");
    }
}
