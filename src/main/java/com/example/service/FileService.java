package com.example.service;

import com.example.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *

 */
public interface FileService extends IService<File> {


    Long save(String fileKey,Long userId);

    /**
     * 根据视频id生成图片
     * @param fileId
     * @return
     */
    Long generatePhoto(Long fileId,Long userId);

    /**
     * 获取文件真实URL
     * @param fileId 文件id
     * @return
     */
    File getFileTrustUrl(Long fileId);
}
