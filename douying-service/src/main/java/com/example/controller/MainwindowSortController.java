package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.service.user.UserService;
import com.example.service.video.TypeService;
import com.example.service.video.VideoService;
import com.example.util.JwtUtils;
import com.example.util.R;
import com.example.entity.vo.BasePage;
import com.example.video.Type;
import com.example.video.Video;
import com.example.video.VideoShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/douying/index")
public class MainwindowSortController {

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private TypeService typeService;

    /**
     * 兴趣推送视频(通过当前用户的ID来进行指定推送)
     * @return
     */
    @GetMapping("/pushVideos")
    public R pushVideos(HttpServletRequest request){
        final Long userId = JwtUtils.getUserId(request);
        return R.ok().data(videoService.pushVideos(userId));
    }

    /**
     *
     * @param searchName 视频名称
     * @param basePage 分页参数对象
     * @param request
     * @return
     */
    @GetMapping("/search")
    public R searchVideo(@RequestParam(required = false) String searchName, BasePage basePage,HttpServletRequest request){

        return R.ok().data(videoService.searchVideo(searchName,basePage,JwtUtils.getUserId(request)));
    }

    /**
     * 根据视频分类获取
     * @param typeId 分类ID
     * @return
     */
    @GetMapping("/video/type/{typeId}")
    public R getVideoByTypeId(@PathVariable Long typeId){
        return R.ok().data(videoService.getVideoByTypeId(typeId));
    }

    /**
     * 获取所有的分类，根据分类ID进行排序
     * @param request
     * @return
     */
    @GetMapping("/types")
    public R getTypes(HttpServletRequest request){
        //所有分类
        final List<Type> types = typeService.list(new LambdaQueryWrapper<Type>().select(Type::getIcon, Type::getId, Type::getName).orderByDesc(Type::getSort));
        //用户订阅的分类
        final Set<Long> set = userService.listSubscribeType(JwtUtils.getUserId(request)).stream().map(Type::getId).collect(Collectors.toSet());

        //用户订阅的分类是否是包含在所有的分类中
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
        if (request.getHeader("x-forwarded-for") == null) //用来表示 HTTP 请求端真实 IP
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
     * 获取热度排行榜(重要功能)
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
    @GetMapping("/video/hot")  //todo
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
