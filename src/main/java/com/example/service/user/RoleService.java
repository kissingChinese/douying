package com.example.service.user;

import com.example.entity.user.Role;
import com.example.entity.user.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.vo.AssignRoleVO;
import com.example.entity.vo.AuthorityVO;
import com.example.util.R;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *

 */
public interface RoleService extends IService<Role> {

    List<Tree> tree();

    R removeRole(String id);

    R gavePermission(AuthorityVO authorityVO);

    R gaveRole(AssignRoleVO assignRoleVO);

}
