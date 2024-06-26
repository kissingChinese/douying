package com.example.service.impl;
import com.example.entity.Setting;
import com.example.mapper.SettingMapper;
import com.example.service.SettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements SettingService {

}
