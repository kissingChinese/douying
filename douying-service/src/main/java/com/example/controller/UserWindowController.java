package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.config.QiNiuConfig;

import com.example.entity.vo.BasePage;
import com.example.entity.vo.Model;
import com.example.entity.vo.UpdateUserVO;
import com.example.entity.vo.UserModel;
import com.example.holder.UserHolder;
import com.example.service.user.FavoritesService;
import com.example.service.user.UserService;
import com.example.user.Favorites;
import com.example.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/douying/customer")
public class UserWindowController {



    @Autowired
    QiNiuConfig qiNiuConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private FavoritesService favoritesService;


    /**
     * 获取个人信息
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/getInfo/{userId}")
    public R getInfo(@PathVariable Long userId){
        return R.ok().data(userService.getInfo(userId));
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/getInfo")
    public R getDefaultInfo(){
        return R.ok().data(userService.getInfo(UserHolder.get()));
    }

    /**
     * 获取关注人员
     * @param basePage
     * @param userId
     * @return
     */
    @GetMapping("/follows")
    public R getFollows(BasePage basePage, Long userId){
        return R.ok().data(userService.getFollows(userId,basePage));
    }

    /**
     * 获取粉丝
     * @param basePage
     * @param userId
     * @return
     */
    @GetMapping("/fans")
    public R getFans(BasePage basePage,Long userId){
        return R.ok().data(userService.getFans(userId,basePage));
    }


    /**
     * 获取所有的收藏夹
     * @return
     */
    @GetMapping("/favorites")
    public R listFavorites(){
        final Long userId = UserHolder.get();
        List<Favorites> favorites = favoritesService.listByUserId(userId);
        return R.ok().data(favorites);
    }


    /**
     * 获取指定收藏夹
     * @param id
     * @return
     */
    @GetMapping("/favorites/{id}")
    public R getFavorites(@PathVariable Long id){
        return R.ok().data(favoritesService.getById(id));
    }

    /**
     * 添加/修改收藏夹
     * @param favorites
     * @return
     */
    @PostMapping("/favorites")
    public R saveOrUpdateFavorites(@RequestBody @Validated Favorites favorites){
        final Long userId = UserHolder.get();
        final Long id = favorites.getId();
        favorites.setUserId(userId);
        final int count = favoritesService.count(new LambdaQueryWrapper<Favorites>()
                .eq(Favorites::getName, favorites.getName()).eq(Favorites::getUserId, userId).ne(Favorites::getId,favorites.getId()));
        if (count == 1){
            return R.error().message("已存在相同名称的收藏夹");
        }
        favoritesService.saveOrUpdate(favorites);
        return R.ok().message(id !=null ? "修改成功" : "添加成功");
    }

    /**
     * 删除收藏夹
     * @param id
     * @return
     */
    @DeleteMapping("/favorites/{id}")
    public R deleteFavorites(@PathVariable Long id){
        favoritesService.remove(id,UserHolder.get());
        return R.ok().message("删除成功");
    }


    /**
     * 订阅分类
     */
    @PostMapping("/subscribe")
    public R subscribe(@RequestParam(required = false) String types){
        final HashSet<Long> typeSet = new HashSet<>();
        String msg = "取消订阅";
        if (!ObjectUtils.isEmpty(types)){
            for (String s : types.split(",")) {
                typeSet.add(Long.parseLong(s));
            }
            msg = "订阅成功";
        }
        userService.subscribe(typeSet);
        return R.ok().message(msg);
    }

    /**
     * 获取用户订阅的分类
     * @return
     */
    @GetMapping("/subscribe")
    public R listSubscribeType(){
        return R.ok().data(userService.listSubscribeType(UserHolder.get()));
    }

    // 获取用户没订阅的分类
    @GetMapping("/noSubscribe")
    public R listNoSubscribeType(){
        return R.ok().data(userService.listNoSubscribeType(UserHolder.get()));
    }

    /**
     * 关注/取关
     * @param followsUserId
     * @return
     */
    @PostMapping("/follows")
    public R follows(@RequestParam Long followsUserId){

        return R.ok().message(userService.follows(followsUserId) ? "已关注" : "已取关");
    }

    /**
     * 用户停留时长修改模型
     * @param model
     * @return
     */
    @PostMapping("/updateUserModel")
    public R updateUserModel(@RequestBody Model model){
        final Double score = model.getScore();
        if (score == -0.5 || score == 1.0){
            final UserModel userModel = new UserModel();
            userModel.setUserId(UserHolder.get());
            userModel.setModels(Collections.singletonList(model));
            userService.updateUserModel(userModel);
        }
        return R.ok();
    }
    /**
     * 获取用户上传头像的token
     * @return
     */
    @GetMapping("/avatar/token")
    public R avatarToken(){
        return R.ok().data(qiNiuConfig.imageUploadToken());
    }


    /**
     *  修改用户信息
     * @param user
     * @return
     */
    @PutMapping
    public R updateUser(@RequestBody @Validated UpdateUserVO user){
        userService.updateUser(user);
        return R.ok().message("修改成功");
    }


}
