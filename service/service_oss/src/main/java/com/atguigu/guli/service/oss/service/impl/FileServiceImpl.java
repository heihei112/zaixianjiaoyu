package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private OssProperties ossProperties;

    @Override
    public String saveUpload(InputStream inputStream, String module, String originalFileName) {

        String bucketname = ossProperties.getBucketname();
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();

        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        // Oss实例是否存在
        if (!ossClient.doesBucketExist(bucketname)){
             ossClient.createBucket(bucketname);
             ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }
        // 构建日期路径:avatar/02/02/
        String folder = new DateTime().toString("yyyy/HH/dd");

        // 获取到文件名称变为随机数
        String fileName = UUID.randomUUID().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String key = module +"/" + folder+"/"+ fileName + fileExtension;

        ossClient.putObject(ossProperties.getBucketname(),key,inputStream);
        return "https://" + bucketname+"."+endpoint+"/"+key;
    }

    @Override
    public void removeFile(String url) {
        String bucketname = ossProperties.getBucketname();
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();

        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);


        String host =  "https://"+bucketname+"."+endpoint+"/";
        String objectName = url.substring(host.length());

        ossClient.deleteObject(bucketname,objectName);

        ossClient.shutdown();
    }
}
