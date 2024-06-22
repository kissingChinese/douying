package com.example.service.user;

import com.example.entity.user.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *

 */
public interface PermissionService extends IService<Permission> {

    Map<String, Object> initMenu(Long userId);


    List<Permission> treeSelect();


    void removeMenu(Long id);
}
