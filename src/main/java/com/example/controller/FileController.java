package com.example.controller;

import com.example.config.LocalCache;
import com.example.config.QiNiuConfig;
import com.example.entity.File;
import com.example.entity.Setting;
import com.example.holder.UserHolder;
import com.example.service.FileService;
import com.example.service.SettingService;
import com.example.util.R;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/luckyjourney/file")
public class FileController implements InitializingBean {


    @Autowired
    FileService fileService;

    @Autowired
    QiNiuConfig qiNiuConfig;

    @Autowired
    SettingService settingService;

    /**
     * 保存到文件表
     * @return
     */
    @PostMapping
    public R save(String fileKey){

        return R.ok().data(fileService.save(fileKey, UserHolder.get()));
    }

    @GetMapping("/getToken")
    public R token(String type){

        return R.ok().data(qiNiuConfig.uploadToken(type));
    }

    @GetMapping("/{fileId}")
    public void getUUid(HttpServletRequest request, HttpServletResponse response, @PathVariable  Long fileId) throws IOException {

     /*   String ip = request.getHeader("referer");
        if (!LocalCache.containsKey(ip)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }*/
        // 如果不是指定ip调用的该接口，则不返回
        File url = fileService.getFileTrustUrl(fileId);
        response.setContentType(url.getType());
        response.sendRedirect(url.getFileKey());
    }

    @PostMapping("/auth")
    public void auth(@RequestParam(required = false) String uuid, HttpServletResponse response) throws IOException {
        if (uuid == null || LocalCache.containsKey(uuid) == null){
            response.sendError(401);
        }else {
            LocalCache.rem(uuid);
            response.sendError(200);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Setting setting = settingService.list(null).get(0);
        for (String s : setting.getAllowIp().split(",")) {
            LocalCache.put(s,true);
        }
    }
}
