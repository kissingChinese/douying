package com.example.service.impl;

import com.qiniu.storage.model.FileInfo;
import com.example.config.LocalCache;
import com.example.config.QiNiuConfig;
import com.example.entity.File;
import com.example.exception.BaseException;
import com.example.mapper.FileMapper;
import com.example.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.QiNiuFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    @Autowired
    private QiNiuFileService qiNiuFileService;

    @Override
    public Long save(String fileKey,Long userId) {  //filekey是文件的编号标识

        // 判断文件
        final FileInfo videoFileInfo = qiNiuFileService.getFileInfo(fileKey);

        if (videoFileInfo == null){
            throw new IllegalArgumentException("参数不正确");
        }

        final File videoFile = new File();
        String type = videoFileInfo.mimeType;//通过七牛云提供的FileInfo类可以获取上传文件的信息
        videoFile.setFileKey(fileKey);
        videoFile.setFormat(type);
        videoFile.setType(type.contains("video") ? "视频" : "图片");
        videoFile.setUserId(userId);
        videoFile.setSize(videoFileInfo.fsize);
        save(videoFile);

        return videoFile.getId();
    }

    @Override
    public Long generatePhoto(Long fileId,Long userId) { //获取视频封面图片
        final File file = getById(fileId); //通过文件表找到该文件
        final String fileKey = file.getFileKey() + "?vframe/jpg/offset/1";
        final File fileInfo = new File();
        fileInfo.setFileKey(fileKey);
        fileInfo.setFormat("image/*");
        fileInfo.setType("图片");
        fileInfo.setUserId(userId);
        save(fileInfo);
        return fileInfo.getId();
    }

    @Override
    public File getFileTrustUrl(Long fileId) {
        File file = getById(fileId);
        if (Objects.isNull(file)) {
            throw new BaseException("未找到该文件");
        }
        final String s = UUID.randomUUID().toString();
        LocalCache.put(s,true);
        String url = QiNiuConfig.CNAME + "/" + file.getFileKey();

        if (url.contains("?")){
            url = url+"&uuid="+s;
        }else {
            url = url+"?uuid="+s;
        }
        file.setFileKey(url);
        return file;
    }
}
