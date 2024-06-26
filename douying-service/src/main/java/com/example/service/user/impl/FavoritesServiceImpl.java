package com.example.service.user.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.mapper.user.FavoritesMapper;
import com.example.service.user.FavoritesService;
import com.example.service.user.FavoritesVideoService;
import com.example.exception.BaseException;
import com.example.holder.UserHolder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.Favorites;
import com.example.user.FavoritesVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites> implements FavoritesService {
    @Autowired
    private FavoritesVideoService favoritesVideoService;


    @Override
    @Transactional
    public void remove(Long id, Long userId) {

        // 不能删除默认收藏夹
        final Favorites favorites = getOne(new LambdaQueryWrapper<Favorites>().eq(Favorites::getId, id).eq(Favorites::getUserId, userId));
        if (favorites.getName().equals("默认收藏夹")){
            throw new BaseException("默认收藏夹不允许被删除");
        }

        final boolean result = remove(new LambdaQueryWrapper<Favorites>().eq(Favorites::getId, id).eq(Favorites::getUserId, userId));
        // 如果能删除成功说明是自己的收藏夹
        if (result){
            favoritesVideoService.remove(new LambdaQueryWrapper<FavoritesVideo>().eq(FavoritesVideo::getFavoritesId,id));
        }else {
            throw new BaseException("不能删除别人的文件夹");
        }
    }

    @Override
    public List<Favorites> listByUserId(Long userId) {
        // 查出收藏夹id
        final List<Favorites> favorites = list(new LambdaQueryWrapper<Favorites>().eq(Favorites::getUserId, userId));
        if (ObjectUtils.isEmpty(favorites)) return Collections.EMPTY_LIST;
        // 根据收藏夹id获取对应数
        final List<Long> fIds = favorites.stream().map(Favorites::getId).collect(Collectors.toList());
        final Map<Long, Long> fMap = favoritesVideoService.list(new LambdaQueryWrapper<FavoritesVideo>().in(FavoritesVideo::getFavoritesId, fIds))
                .stream().collect(Collectors.groupingBy(FavoritesVideo::getFavoritesId,
                        Collectors.counting()));
        // 计算对应视频总数
        for (Favorites favorite : favorites) {
            final Long videoCount = fMap.get(favorite.getId());
            favorite.setVideoCount(videoCount == null ? 0 :videoCount);
        }

        return favorites;
    }

    @Override
    public List<Long> listVideoIds(Long favoritesId, Long userId) {

        // 不直接返回中间表是为了隐私性 (当前没实现收藏夹公开功能)

        // 校验
        final Favorites favorites = getOne(new LambdaQueryWrapper<Favorites>().eq(Favorites::getId, favoritesId).eq(Favorites::getUserId, userId));
        if (favorites == null){
            throw new BaseException("收藏夹为空");
        }

        final List<Long> videoIds = favoritesVideoService.list(new LambdaQueryWrapper<FavoritesVideo>().eq(FavoritesVideo::getFavoritesId, favoritesId))
                .stream().map(FavoritesVideo::getVideoId).collect(Collectors.toList());

        return videoIds;
    }

    @Override
    public boolean favorites(Long fId, Long vId) {

        final Long userId = UserHolder.get();

        try {
            final FavoritesVideo favoritesVideo = new FavoritesVideo();
            favoritesVideo.setFavoritesId(fId);
            favoritesVideo.setVideoId(vId);
            favoritesVideo.setUserId(userId);
            favoritesVideoService.save(favoritesVideo);

        }catch (Exception e){
            favoritesVideoService.remove(new LambdaQueryWrapper<FavoritesVideo>().eq(FavoritesVideo::getFavoritesId, fId)
                    .eq(FavoritesVideo::getVideoId, vId).eq(FavoritesVideo::getUserId, userId));
            return false;
        }
        return true;
    }

    @Override
    public Boolean favoritesState(Long videoId, Long userId) {
        if (userId == null) return false;
        return favoritesVideoService.count(new LambdaQueryWrapper<FavoritesVideo>().eq(FavoritesVideo::getVideoId,videoId).eq(FavoritesVideo::getUserId,userId)) == 1;
    }

    @Override
    public void exist(Long userId, Long fId) {
        final int count = count(new LambdaQueryWrapper<Favorites>().eq(Favorites::getUserId, userId).eq(Favorites::getId, fId));
        if (count == 0){
            throw new BaseException("收藏夹选择错误");
        }
    }
}
