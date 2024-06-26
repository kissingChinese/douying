package com.example.service.user.impl;
import com.example.mapper.user.UserRoleMapper;
import com.example.service.user.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.UserRole;
import org.springframework.stereotype.Service;
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
