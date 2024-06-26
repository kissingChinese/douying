package com.example.service.schedul;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.Setting;
import com.example.entity.vo.HotVideo;
import com.example.service.SettingService;
import com.example.service.video.VideoService;
import com.example.util.RedisCacheUtil;
import com.example.video.Video;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.constant.AuditStatus;
import com.example.constant.RedisConstant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class HotRank {

    @Autowired
    private VideoService videoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SettingService settingService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper om = new ObjectMapper();

    {
        jackson2JsonRedisSerializer.setObjectMapper(om);
    }

    /**
     * 热度排行榜
     */
    @Scheduled(cron = "0 0 */1 * * ?") //
    public void hotRank() {
        // 控制数量
        final TopK topK = new TopK(10, new PriorityQueue<HotVideo>(10, Comparator.comparing(HotVideo::getHot)));
        long limit = 1000;
        // 每次拿1000个
        long id = 0;
        List<Video> videos = videoService.list(new LambdaQueryWrapper<Video>()
                .select(Video::getId, Video::getShareCount, Video::getHistoryCount, Video::getStartCount, Video::getFavoritesCount,
                        Video::getGmtCreated, Video::getTitle).gt(Video::getId, id)
                .eq(Video::getAuditStatus, AuditStatus.SUCCESS).eq(Video::getOpen, 0).last("limit " + limit));

        //将获取到的满足条件的视频来计算期热度值
        while (!ObjectUtils.isEmpty(videos)) {
            for (Video video : videos) {
                Long shareCount = video.getShareCount();
                Double historyCount = video.getHistoryCount() * 0.8;
                Long startCount = video.getStartCount();
                Double favoritesCount = video.getFavoritesCount() * 1.5;
                final Date date = new Date();
                long t = date.getTime() - video.getGmtCreated().getTime();
                // 随机获取6位数,用于去重
                final double v = weightRandom();
                final double hot = hot(shareCount + historyCount + startCount + favoritesCount + v, TimeUnit.MILLISECONDS.toDays(t));
                final HotVideo hotVideo = new HotVideo(hot, video.getId(), video.getTitle());
                topK.add(hotVideo);//添加到优先队列中
            }
            id = videos.get(videos.size() - 1).getId();
            videos = videoService.list(new LambdaQueryWrapper<Video>().gt(Video::getId, id).last("limit " + limit));
            ;
        }
        final byte[] key = RedisConstant.HOT_RANK.getBytes();
        final List<HotVideo> hotVideos = topK.get();
        final Double minHot = hotVideos.get(0).getHot();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (HotVideo hotVideo : hotVideos) {
                final Double hot = hotVideo.getHot();
                try {
                    hotVideo.setHot(null);
                    // 不这样写铁报错！序列化问题
                    connection.zAdd(key, hot, jackson2JsonRedisSerializer.serialize(om.writeValueAsString(hotVideo)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        redisTemplate.opsForZSet().removeRangeByScore(RedisConstant.HOT_RANK, minHot,0);


    }

    // 热门视频,没有热度排行榜实时且重要
    @Scheduled(cron = "0 0 */3 * * ?")
    public void hotVideo() {
        // 分片查询3天内的视频
        int limit = 1000;
        long id = 1; //用户保存上一次查询到的视频的ID
        List<Video> videos = videoService.selectNDaysAgeVideo(id, 3, limit);
        final Double hotLimit = settingService.list(new LambdaQueryWrapper<Setting>()).get(0).getHotLimit(); //查询公开的视频
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DATE);

        while (!ObjectUtils.isEmpty(videos)) {
            final ArrayList<Long> hotVideos = new ArrayList<>();
            for (Video video : videos) {
                Long shareCount = video.getShareCount();
                Double historyCount = video.getHistoryCount() * 0.8;
                Long startCount = video.getStartCount();
                Double favoritesCount = video.getFavoritesCount() * 1.5;
                final Date date = new Date();
                long t = date.getTime() - video.getGmtCreated().getTime();
                //调整热度值以防止相同热度的视频排名总是相同，计算视频的热度值
                final double hot = hot(shareCount + historyCount + startCount + favoritesCount, TimeUnit.MILLISECONDS.toDays(t));

                // 大于X热度说明是热门视频
                if (hot > hotLimit) {
                    hotVideos.add(video.getId());
                }
            }
            id = videos.get(videos.size() - 1).getId();
            videos = videoService.selectNDaysAgeVideo(id, 3, limit);
            // RedisConstant.HOT_VIDEO + 今日日期 作为key  达到元素过期效果
            if (!ObjectUtils.isEmpty(hotVideos)){
                String key = RedisConstant.HOT_VIDEO + today;
                redisTemplate.opsForSet().add(key, hotVideos.toArray(new Object[hotVideos.size()]));
                redisTemplate.expire(key, 3, TimeUnit.DAYS);
            }

        }
    }

    static double a = 0.011;

    public static double hot(double weight, double t) {
        return weight * Math.exp(-a * t); //通过半衰期公式计算热度
    }


    public double weightRandom() {
        int i = (int) ((Math.random() * 9 + 1) * 100000);
        return i / 1000000.0;
    }

}
