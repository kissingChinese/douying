package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.video.Type;
import com.example.entity.video.Video;
import com.example.entity.video.VideoShare;
import com.example.service.user.UserService;
import com.example.service.video.TypeService;
import com.example.service.video.VideoService;
import com.example.util.JwtUtils;
import com.example.util.R;
import com.example.entity.vo.BasePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/luckyjourney/index")
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private TypeService typeService;

    /**
     * 兴趣推送视频
     * @return
     */
    @GetMapping("/pushVideos")
    public R pushVideos(HttpServletRequest request){
        final Long userId = JwtUtils.getUserId(request);
        return R.ok().data(videoService.pushVideos(userId));
    }

    /**
     * 搜索视频
     * @return
     */
    @GetMapping("/search")
    public R searchVideo(@RequestParam(required = false) String searchName, BasePage basePage,HttpServletRequest request){

        return R.ok().data(videoService.searchVideo(searchName,basePage,JwtUtils.getUserId(request)));
    }

    /**
     * 根据视频分类获取
     * @param typeId
     * @return
     */
    @GetMapping("/video/type/{typeId}")
    public R getVideoByTypeId(@PathVariable Long typeId){

        return R.ok().data(videoService.getVideoByTypeId(typeId));
    }

    /**
     * 获取所有分类
     * @return
     */
    @GetMapping("/types")
    public R getTypes(HttpServletRequest request){
        final List<Type> types = typeService.list(new LambdaQueryWrapper<Type>().select(Type::getIcon, Type::getId, Type::getName).orderByDesc(Type::getSort));

        final Set<Long> set = userService.listSubscribeType(JwtUtils.getUserId(request)).stream().map(Type::getId).collect(Collectors.toSet());

        for (Type type : types) {
            if (set.contains(type.getId())) {
                type.setUsed(true);
            }else {
                type.setUsed(false);
            }
        }
        return R.ok().data(types);
    }

    /**
     * 分享视频
     * @param videoId
     * @param request
     * @return
     */
    @PostMapping("/share/{videoId}")
    public R share(@PathVariable Long videoId, HttpServletRequest request){

        String ip = null;
        if (request.getHeader("x-forwarded-for") == null)
            ip = request.getRemoteAddr();
        else
            ip = request.getHeader("x-forwarded-for");
        final VideoShare videoShare = new VideoShare();

        videoShare.setVideoId(videoId);
        videoShare.setIp(ip);
        if (JwtUtils.checkToken(request)) {
            videoShare.setUserId(JwtUtils.getUserId(request));
        }
        videoService.shareVideo(videoShare);
        return R.ok();
    }

    /**
     * 根据id获取视频详情
     * @param id
     * @return
     */
    @GetMapping("/video/{id}")
    public R getVideoById(@PathVariable Long id,HttpServletRequest request){
        final Long userId = JwtUtils.getUserId(request);
        return R.ok().data(videoService.getVideoById(id,userId));
    }

    /**
     * 获取热度排行榜
     * @return
     */
    @GetMapping("/video/hot/rank")
    public R listHotRank(){
        return R.ok().data(videoService.hotRank());
    }

    /**
     * 根据视频标签推送相似视频
     * @param video
     * @return
     */
    @GetMapping("/video/similar")
    public R pushVideoSimilar(Video video){
        return R.ok().data(videoService.listSimilarVideo(video));
    }

    /**
     * 推送热门视频
     * @return
     */
    @GetMapping("/video/hot")
    public R listHotVideo(){
        return R.ok().data(videoService.listHotVideo());
    }

    /**
     * 根据用户id获取视频
     * @param userId
     * @param basePage
     * @return
     */
    @GetMapping("/video/user")
    public R listVideoByUserId(@RequestParam(required = false) Long userId,
                               BasePage basePage,HttpServletRequest request){

        userId = userId == null ? JwtUtils.getUserId(request) : userId;
        return R.ok().data(videoService.listByUserIdOpenVideo(userId,basePage));
    }

    /**
     * 获取用户搜索记录
     * @return
     */
    @GetMapping("/search/history")
    public R searchHistory(HttpServletRequest request){
        return R.ok().data(userService.searchHistory(JwtUtils.getUserId(request)));
    }

    // 删除搜索记录
    @DeleteMapping("/search/history")
    public R deleteSearchHistory(HttpServletRequest request){
        userService.deleteSearchHistory(JwtUtils.getUserId(request));
        return R.ok();
    }
}
