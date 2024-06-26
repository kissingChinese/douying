package com.example.controller.admin;

import com.example.authority.Authority;
import com.example.config.LocalCache;
import com.example.entity.Setting;
import com.example.json.SettingScoreDTO;
import com.example.util.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.service.SettingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
@RestController
@RequestMapping("/admin/setting")
public class AdminSettingController {

    @Autowired
    private SettingService settingService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    @Authority("admin:setting:get")
    public R get() throws IOException {
        final Setting setting = settingService.list(null).get(0);
        final SettingScoreDTO settingScoreDTO = objectMapper.readValue(setting.getAuditPolicy(), SettingScoreDTO.class);
        setting.setSettingScoreDTO(settingScoreDTO);
        return R.ok().data(setting);
    }

    @PutMapping
    @Authority("admin:setting:update")
    public R update(@RequestBody @Validated Setting setting){
        settingService.updateById(setting);
        for (String s : setting.getAllowIp().split(",")) {
            LocalCache.put(s,true);
        }
        return R.ok().message("修改成功");
    }
}
