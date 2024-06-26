package com.example.service.video.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.mapper.video.VideoStarMapper;
import com.example.service.video.VideoStarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.video.VideoStar;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class VideoStarServiceImpl extends ServiceImpl<VideoStarMapper, VideoStar> implements VideoStarService {
    @Override
    public boolean starVideo(VideoStar videoStar) {
        try {
            // 添加概率
            this.save(videoStar);
        }catch (Exception e){
            // 存在则取消点赞
            this.remove(new LambdaQueryWrapper<VideoStar>().eq(VideoStar::getVideoId,videoStar.getVideoId()).eq(VideoStar::getUserId,videoStar.getUserId()));
            return false;
        }
        return true;
    }



    @Override
    public List<Long> getStarUserIds(Long videoId) { //获取给当前用户点赞的用户ID
        return this.list(new LambdaQueryWrapper<VideoStar>().eq(VideoStar::getVideoId,videoId)).stream().map(VideoStar::getUserId).collect(Collectors.toList());
    }

    @Override
    public Boolean starState(Long videoId, Long userId) { //判断用户是否给视频点赞

        if (userId == null) return  false;

        return this.count(new LambdaQueryWrapper<VideoStar>().eq(VideoStar::getVideoId,videoId).eq(VideoStar::getUserId,userId)) == 1;
    }
}
