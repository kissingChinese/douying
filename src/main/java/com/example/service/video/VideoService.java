package com.example.service.video;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.video.Video;
import com.example.entity.video.VideoShare;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.vo.BasePage;
import com.example.entity.vo.HotVideo;

import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *

 */
public interface VideoService extends IService<Video> {

    /**
     * 获取视频信息
     * @param id
     * @param userId 当前用户id
     * @return
     */
    Video getVideoById(Long id,Long userId);

    /**
     * 发布/修改视频
     * 修改无法更换视频源
     * @param video
     */
    void publishVideo(Video video);

    /**
     * 删除视频
     * @param id
     */
    void deleteVideo(Long id);

    /**
     * 主页推送视频
     * @return
     */
    Collection<Video> pushVideos(Long userId);

    /**
     * 根据视频分类获取视频,乱序
     * @param typeId
     * @return
     */
    Collection<Video> getVideoByTypeId(Long typeId);

    /**
     * 搜索视频
     * @param search
     * @return
     */
    IPage<Video> searchVideo(String search,BasePage basePage,Long userId);

    /**
     * 审核放行处理
     * @param video
     */
    void auditProcess(Video video);

    /**
     * 视频点赞
     * @param videoId
     * @return
     */
    boolean startVideo(Long videoId);

    /**
     * 分享视频
     * @param videoShare
     * @return
     */
    boolean shareVideo(VideoShare videoShare);

    /**
     * 添加浏览记录
     * @param videoId
     */
    void historyVideo(Long videoId,Long userId) throws Exception;

    /**
     * 收藏视频
     * @param fId
     * @param vId
     * @return
     */
    boolean favoritesVideo(Long fId, Long vId);

    /**
     * 获取当前用户浏览记录,带分页
     * @return
     */
    LinkedHashMap<String, List<Video>> getHistory(BasePage basePage);

    /**
     * 根据收藏夹获取视频
     * @param favoritesId
     * @return
     */
    Collection<Video> listVideoByFavorites(Long favoritesId);

    /**
     * 获取热度排行榜
     * @return
     */
    Collection<HotVideo> hotRank();

    /**
     * 根据标签推送相似视频
     * @param video
     * @return
     */
    Collection<Video> listSimilarVideo(Video video);

    /**
     * 根据userId获取对应视频,只包含公开的
     * @param userId
     * @return
     */
    IPage<Video> listByUserIdOpenVideo(Long userId, BasePage basePage);

    /**
     * 获取当前审核队列
     * @return
     */
    String getAuditQueueState();

    /**
     * 获取N天前的视频
     * @param id id
     * @param days 天数
     * @param limit 限制
     * @return
     */
    List<Video> selectNDaysAgeVideo(long id,int days,int limit);

    /**
     * 获取热门视频
     * @return
     */
    Collection<Video> listHotVideo();

    /**
     * 关注流
     * @param userId 用户id
     * @param lastTime 滚动分页参数，首次为null，后续为上次的末尾视频时间
     * @return
     */
    Collection<Video> followFeed(Long userId,Long lastTime);

    /**
     * 拉模式
     * @param userId
     */
    void initFollowFeed(Long userId);

    /**
     * 查询用户所管理的视频
     * @param basePage
     * @param userId
     * @return
     */
    IPage<Video> listByUserIdVideo(BasePage basePage, Long userId);

    /**
     * 获取该用户所发布的视频
     * @param userId
     * @return
     */
    Collection<Long> listVideoIdByUserId(Long userId);

    /**
     * 下架视频
     * @param id
     */
    void violations(Long id);
}
