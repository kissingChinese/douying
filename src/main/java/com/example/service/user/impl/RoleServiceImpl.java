package com.example.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.user.Role;
import com.example.entity.user.RolePermission;
import com.example.entity.user.Tree;
import com.example.entity.user.UserRole;
import com.example.mapper.user.RoleMapper;
import com.example.service.user.PermissionService;
import com.example.service.user.RolePermissionService;
import com.example.service.user.RoleService;
import com.example.service.user.UserRoleService;
import com.example.entity.vo.AssignRoleVO;
import com.example.entity.vo.AuthorityVO;
import com.example.service.user.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**

 *

 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Resource
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserRoleService userRoleService;


    @Override
    public List<Tree> tree() {
        List<Tree> trees = permissionService.list(null).stream().map(permission -> {

            Tree tree = new Tree();
            BeanUtils.copyProperties(permission, tree);
            tree.setTitle(permission.getName());
            tree.setSpread(true);
            return tree;
        }).collect(Collectors.toList());


        // 找到根节点
        List<Tree> parent = trees.stream().filter(tree -> tree.getPId().compareTo(0L) == 0).collect(Collectors.toList());
        for (Tree item : parent) {
            item.setChildren(new ArrayList<Tree>());
            item.getChildren().add(findChildren(item,trees));
        }

        return parent;
    }



    @Override
    @Transactional
    public R removeRole(String id) {
        try{
            // 删除角色权限中间表
            rolePermissionService.remove(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId,id));
            // 删除角色表
            baseMapper.deleteById(id);
            // 删除用户角色表
            userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId,id));
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.error().message("删除失败");
        }
        return R.ok().message("删除成功");
    }

    @Override
    @Transactional
    public R gavePermission(AuthorityVO authorityVO) {
        try{
            rolePermissionService.remove(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId,authorityVO.getRid()));
            List<RolePermission> list = new ArrayList<>();
            Integer rid = authorityVO.getRid();
            for (Integer pId : authorityVO.getPid()) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(rid);
                rolePermission.setPermissionId(pId);
                list.add(rolePermission);
            }
            rolePermissionService.saveBatch(list);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.error().message("分配权限失败");
        }
        return R.ok().message("分配权限成功");
    }

    @Override
    @Transactional
    public R gaveRole(AssignRoleVO assignRoleVO) {
        // 获取被分配角色的用户信息
        Long uId = assignRoleVO.getUId();
        try{
            userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,uId));
            List<UserRole> userRoles = new ArrayList<>();
            for (Long id : assignRoleVO.getRId()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(uId);
                userRole.setRoleId(id);
                userRoles.add(userRole);
            }
            userRoleService.saveBatch(userRoles);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.error().message("分配角色失败");
        }
        return R.ok().message("分配角色成功");
    }

    private Tree findChildren(Tree datum, List<Tree> trees) {
        datum.setChildren(new ArrayList<Tree>());
        for (Tree tree : trees) {
            if (tree.getPId().compareTo(datum.getId()) == 0) {
                datum.getChildren().add(findChildren(tree,trees));
            }
        }
        return datum;
    }
}
