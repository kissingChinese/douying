package com.example.service.video;

import com.example.entity.video.VideoStar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *

 */
public interface VideoStarService extends IService<VideoStar> {

    /**
     * 视频点赞
     * @param videoStar
     */
    boolean starVideo(VideoStar videoStar);


    /**
     * 视频点赞用户
     * @param videoId
     * @return
     */
    List<Long> getStarUserIds(Long videoId);

    /**
     * 点赞状态
     * @param videoId
     * @param userId
     * @return
     */
    Boolean starState(Long videoId, Long userId);
}
