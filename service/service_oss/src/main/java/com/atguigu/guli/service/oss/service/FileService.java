package com.atguigu.guli.service.oss.service;

import java.io.InputStream;

public interface FileService {

    public String saveUpload(InputStream inputStream , String module ,String originalFileName);

    void removeFile(String url);

}
