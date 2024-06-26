package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.service.video.VideoTypeService;
import com.example.user.User;
import com.example.video.Video;
import com.example.video.VideoType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.constant.RedisConstant;
import com.example.entity.vo.HotVideo;
import com.example.entity.vo.Model;
import com.example.entity.vo.UserModel;
import com.example.service.InterestPushService;
import com.example.service.video.TypeService;
import com.example.util.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Service
public class InterestPushServiceImpl implements InterestPushService {

    private static final Logger log = LoggerFactory.getLogger(InterestPushServiceImpl.class);
    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private TypeService typeService;

    @Autowired
    VideoTypeService videoTypeService;

    @Autowired
    private RedisTemplate redisTemplate;


    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Async
    public void pushSystemStockIn(Video video) {
        // 往系统库中添加
        final List<String> labels = video.buildLabel();
        final Long videoId = video.getId();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String label : labels) {
                connection.sAdd((RedisConstant.SYSTEM_STOCK + label).getBytes(),String.valueOf(videoId).getBytes());
            }
            return null;
        });
    }

    @Override
    @Async
    public void pushSystemTypeStockIn(Video video) {
        final Long typeId = video.getTypeId();
        redisCacheUtil.sSet(RedisConstant.SYSTEM_TYPE_STOCK + typeId,video.getId());
    }


    //TODO
    @Override
    public Collection<Long> listVideoIdByTypeId(Long typeId) {
        // 随机推送10个
        final List<Object> list = redisTemplate.opsForSet().randomMembers(RedisConstant.SYSTEM_TYPE_STOCK + typeId, 12);
        if(list.size()==0){
            //将视频从数据库中全部查出来
            List<VideoType> videos = videoTypeService.list(new LambdaQueryWrapper<VideoType>().eq(VideoType::getTypeId, typeId));
            for(VideoType video : videos){
                list.add(video.getVideoId());
                redisCacheUtil.sSet(RedisConstant.SYSTEM_STOCK + video.getTypeId(),video.getVideoId());
            }
        }
        // 可能会有null
        final HashSet<Long> result = new HashSet<>();
        for (Object aLong : list) {
            if (aLong!=null){
                result.add(Long.parseLong(aLong.toString()));
            }
        }
        return result;
    }

    @Override
    @Async
    public void deleteSystemStockIn(Video video) {
        final List<String> labels = video.buildLabel();
        final Long videoId = video.getId();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String label : labels) {
                connection.sRem((RedisConstant.SYSTEM_STOCK + label).getBytes(),String.valueOf(videoId).getBytes());
            }
            return null;
        });
    }

    @Override
    @Async
    public void initUserModel(Long userId, List<String> labels) {//TODO

        final String key = RedisConstant.USER_MODEL + userId;
        Map<Object, Object> modelMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(labels)) {
            final int size = labels.size();
            // 将标签分为等分概率,不可能超过100个分类
            double probabilityValue = 100 / size;
            for (String labelName : labels) {
                modelMap.put(labelName, probabilityValue);
            }
        }
        redisCacheUtil.del(key);
        redisCacheUtil.hmset(key, modelMap);
        // 为用户模型设置ttl

    }

    @Override
    @Async
    public void updateUserModel(UserModel userModel) {
        final Long userId = userModel.getUserId();
        // 游客不用管
        if (userId != null) {
            final List<Model> models = userModel.getModels();//获取用户模型列表
            // 重缓存中获取用户模型
            String key = RedisConstant.USER_MODEL + userId;//生成redis键值
            Map<Object, Object> modelMap = redisCacheUtil.hmget(key); //从缓存中获取这个用户模型
            if (modelMap == null) { //缓存中无模型数据
                modelMap = new HashMap<>();
            }
            for (Model model : models) {
                // 修改用户模型
                //模型存在
                if (modelMap.containsKey(model.getLabel())) {
                    //将模型中存在的标签的分数进行累加，淘汰分值为0或者等于0的标签（更新模型的必要手段）
                    modelMap.put(model.getLabel(), Double.parseDouble(modelMap.get(model.getLabel()).toString()) + model.getScore());
                    final Object o = modelMap.get(model.getLabel());
                    if (o == null || Double.parseDouble(o.toString()) < 0.0){
                        modelMap.remove(o);
                    }
                } else {
                    modelMap.put(model.getLabel(), model.getScore());
                }
            }

            // 每个标签概率同等加上标签数，再同等除以标签数  防止数据膨胀
            final int labelSize = modelMap.keySet().size();

            //记得集合去空处理
            for (Object o : modelMap.keySet()) {
                if(o!=null){
                    modelMap.put(o,(Double.parseDouble(modelMap.get(o).toString()) + labelSize )/ labelSize);//概率缩放
                }
                else {
                    modelMap.remove(o);
                }

            }



            redisCacheUtil.hmset(key, modelMap);
        }
    }

    @Override
    public Collection<Long> listVideoIdByUserModel(User user) {
        // 创建结果集
        Set<Long> videoIds = new HashSet<>(10);

        if (user != null) {
            final Long userId = user.getId();
            // 从模型中拿概率
            final Map<Object, Object> modelMap = redisCacheUtil.hmget(RedisConstant.USER_MODEL + userId);
            if (!ObjectUtils.isEmpty(modelMap)) {
                // 组成数组
                final String[] probabilityArray = initProbabilityArray(modelMap);
                final Boolean sex = user.getSex();
                // 获取视频
                final Random randomObject = new Random();
                final ArrayList<String> labelNames = new ArrayList<>();
                // 随机获取X个视频
                for (int i = 0; i < 8; i++) {
                    String labelName = probabilityArray[randomObject.nextInt(probabilityArray.length)];
                    labelNames.add(labelName);
                }
                // 提升性能
                String t = RedisConstant.SYSTEM_STOCK;
                // 随机获取
                List<Object> list = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                    for (String labelName : labelNames) {
                        String key = t + labelName;
                        connection.sRandMember(key.getBytes());
                    }
                    return null;
                });
                // 获取到的videoIds
                Set<Long> ids = list.stream().filter(id->id!=null).map(id->Long.parseLong(id.toString())).collect(Collectors.toSet());
                String key2 = RedisConstant.HISTORY_VIDEO;

                // 去重
                List simpIds = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                    for (Long id : ids) {
                        connection.get((key2 + id + ":" + userId).getBytes());
                    }
                    return null;
                });
                simpIds = (List) simpIds.stream().filter(o->!ObjectUtils.isEmpty(o)).collect(Collectors.toList());;
                if (!ObjectUtils.isEmpty(simpIds)){
                    for (Object simpId : simpIds) {
                        final Long l = Long.valueOf(simpId.toString());
                        if (ids.contains(l)){
                            ids.remove(l);
                        }
                    }
                }
                videoIds.addAll(ids);

                // 随机挑选一个视频
                final Long aLong = randomVideoId(sex);
                if (aLong!=null){
                    videoIds.add(aLong);
                }
                return videoIds;
            }
        }
        // 游客
        // 随机获取10个标签
        final List<String> labels = typeService.random10Labels();
        final ArrayList<String> labelNames = new ArrayList<>();
        int size = labels.size();
        final Random random = new Random();
        // 获取随机的标签
        for (int i = 0; i < 10; i++) {
            final int randomIndex = random.nextInt(size);
            labelNames.add(RedisConstant.SYSTEM_STOCK + labels.get(randomIndex));
        }
        // 获取videoId
        final List<Object> list = redisCacheUtil.sRandom(labelNames);
        if (!ObjectUtils.isEmpty(list)){
            videoIds = list.stream().filter(id ->!ObjectUtils.isEmpty(id)).map(id -> Long.valueOf(id.toString())).collect(Collectors.toSet());
        }

        return videoIds;
    }

    @Override
    public Collection<Long> listVideoIdByLabels(List<String> labelNames) {
        final ArrayList<String> labelKeys = new ArrayList<>();
        for (String labelName : labelNames) {
            labelKeys.add(RedisConstant.SYSTEM_STOCK + labelName);
        }
        Set<Long> videoIds = new HashSet<>();
        final List<Object> list = redisCacheUtil.sRandom(labelKeys);
        if (!ObjectUtils.isEmpty(list)){
            videoIds = list.stream().filter(id ->!ObjectUtils.isEmpty(id)).map(id -> Long.valueOf(id.toString())).collect(Collectors.toSet());
        }
        return videoIds;
    }

    @Override
    @Async
    public void deleteSystemTypeStockIn(Video video) {
        final Long typeId = video.getTypeId();
        redisCacheUtil.setRemove(RedisConstant.SYSTEM_TYPE_STOCK + typeId,video.getId());
    }


    public Long randomHotVideoId() throws IOException {
        final Object o = redisTemplate.opsForZSet().randomMember(RedisConstant.HOT_RANK);
        try {
            return objectMapper.readValue(o.toString(),HotVideo.class).getVideoId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long randomVideoId(Boolean sex) {
        String key = RedisConstant.SYSTEM_STOCK + (sex ? "美女" : "宠物");
        final Object o = redisCacheUtil.sRandom(key);
        if (o!=null){
            return Long.parseLong(o.toString());
        }
        return null;
    }

    // 随机获取视频id
    public Long getVideoId(Random random, String[] probabilityArray) {
        String labelName = probabilityArray[random.nextInt(probabilityArray.length)];
        // 获取对应所有视频
        String key = RedisConstant.SYSTEM_STOCK + labelName;
        final Object o = redisCacheUtil.sRandom(key);
        if (o!=null){
            return Long.parseLong(o.toString()) ;
        }
        return null;
    }

    // 初始化概率数组 -> 保存的元素是标签
    public String[] initProbabilityArray(Map<Object, Object> modelMap) {
        // key: 标签  value：概率
        Map<String, Integer> probabilityMap = new HashMap<>();
        int size = modelMap.size();
        final AtomicInteger n = new AtomicInteger(0);
        modelMap.forEach((k, v) -> {
            // 防止结果为0,每个同等加上标签数
            int probability = (((Double) v).intValue() + size) / size;
            n.getAndAdd(probability);
            probabilityMap.put(k.toString(), probability);
        });
        final String[] probabilityArray = new String[n.get()];

        final AtomicInteger index = new AtomicInteger(0);
        // 初始化数组
        probabilityMap.forEach((labelsId, p) -> {
            int i = index.get();
            int limit = i + p;
            while (i < limit) {
                probabilityArray[i++] = labelsId;
            }
            index.set(limit);
        });
        return probabilityArray;
    }




}
