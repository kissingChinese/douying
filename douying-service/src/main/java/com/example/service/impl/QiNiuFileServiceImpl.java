package com.example.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.example.config.QiNiuConfig;
import com.example.service.QiNiuFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.File;
@Service
public class QiNiuFileServiceImpl implements QiNiuFileService {

    @Autowired
    private QiNiuConfig qiNiuConfig;

    @Override
    public String getToken() {
        return qiNiuConfig.videoUploadToken();
    }

    @Override
    public String uploadFile(File file) {
        Configuration cfg = new Configuration(Region.region2());
        UploadManager uploadManager = new UploadManager(cfg);
        try {

            Response response = uploadManager.put(file,null,qiNiuConfig.videoUploadToken());
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
           return putRet.key;
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);

                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    @Override
    @Async
    public void deleteFile(String url) {
        Configuration cfg = new Configuration(Region.region0());
        String bucket = qiNiuConfig.getBucketName();
        final Auth auth = qiNiuConfig.buildAuth();
        String key = url;
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }

    }

    @Override
    public FileInfo getFileInfo(String url) {
        Configuration cfg = new Configuration(Region.region0());
        final Auth auth = qiNiuConfig.buildAuth();
        final String bucket = qiNiuConfig.getBucketName();

        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(bucket, url);
            return fileInfo;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
        return null;
    }

}
