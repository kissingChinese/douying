package com.example.service;
import io.jsonwebtoken.lang.Collections;
import java.util.Collection;
import java.util.List;
public interface FeedService {

    /**
     * 推入发件箱
     * @param userId 发件箱用户id
     * @param videoId 视频id
     */
    void pusOutBoxFeed(Long userId,Long videoId,Long time);

    /**
     * 推入收件箱
     * @param userId
     * @param videoId
     */
    void pushInBoxFeed(Long userId,Long videoId,Long time);

    /**
     * 删除发件箱
     * 当前用户删除视频时 调用
     * ->删除当前用户的发件箱中视频以及粉丝下的收件箱
     * @param userId 当前用户
     * @param fans 粉丝id
     * @param videoId 视频id 需要删除的
     */
    void deleteOutBoxFeed(Long userId,Collection<Long> fans,Long videoId);

    /**
     * 删除收件箱
     * 当前用户取关用户时调用
     * 删除自己收件箱中的videoIds
     * @param userId
     * @param videoIds 关注人发的视频id
     */
    void deleteInBoxFeed(Long userId, List<Long> videoIds);

    /**
     * 初始化关注流-拉模式 with TTL
     * @param userId
     */
    void initFollowFeed(Long userId,Collection<Long> followIds);
}
