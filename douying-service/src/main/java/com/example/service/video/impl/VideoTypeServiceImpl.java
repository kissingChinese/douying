package com.example.service.video.impl;
import com.example.mapper.video.VideoTypeMapper;
import com.example.service.video.VideoTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.video.VideoType;
import org.springframework.stereotype.Service;
@Service
public class VideoTypeServiceImpl extends ServiceImpl<VideoTypeMapper, VideoType> implements VideoTypeService {
    /**
     * 将视频的类型插入到视频类型表中
     * @param videoType
     */
    void insertVideoType(VideoType videoType) {
        this.save(videoType);
    }

}
