package com.example.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.user.UserSubscribeMapper;
import com.example.service.user.UserSubscribeService;
import com.example.user.UserSubscribe;
import org.springframework.stereotype.Service;
@Service
public class UserSubscribeServiceImpl  extends ServiceImpl<UserSubscribeMapper, UserSubscribe> implements UserSubscribeService {
}
