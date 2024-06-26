package com.example.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.vo.AssignRoleVO;
import com.example.entity.vo.AuthorityVO;
import com.example.user.Role;
import com.example.user.Tree;
import com.example.util.R;
import java.util.List;
public interface RoleService extends IService<Role> {
    List<Tree> tree();

    R removeRole(String id);

    R gavePermission(AuthorityVO authorityVO);

    R gaveRole(AssignRoleVO assignRoleVO);

}
