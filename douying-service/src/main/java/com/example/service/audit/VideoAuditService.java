package com.example.service.audit;

import com.example.json.BodyDTO;
import com.example.json.ScoreDTO;
import com.example.json.SettingScoreDTO;
import com.example.response.AuditResponse;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.StringMap;
import com.example.constant.AuditStatus;
import com.example.entity.Setting;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class VideoAuditService extends AbstractAuditService<String, AuditResponse>{


    static String videoUrl = "http://ai.qiniuapi.com/v3/video/censor";
    static String videoBody = "{\n" +
            "    \"data\": {\n" +
            "        \"uri\": \"${url}\",\n" +
            "        \"id\": \"video_censor_test\"\n" +
            "    },\n" +
            "    \"params\": {\n" +
            "        \"scenes\": [\n" +
            "            \"pulp\",\n" +
            "            \"terror\",\n" +
            "            \"politician\"\n" +
            "        ],\n" +
            "        \"cut_param\": {\n" +
            "            \"interval_msecs\": 5000\n" +
            "        }\n" +
            "    }\n" +
            "}";


    @Override
    public AuditResponse audit(String url) {
        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setAuditStatus(AuditStatus.SUCCESS);

        if (!isNeedAudit()) {
            return auditResponse;
        }
        url = appendUUID(url);

        String body = videoBody.replace("${url}", url);
        String method = "POST";
        // 获取token
        final String token = qiNiuConfig.getToken(videoUrl, method, body, contentType);
        StringMap header = new StringMap();
        header.put("Host", "ai.qiniuapi.com");
        header.put("Authorization", token);
        header.put("Content-Type", contentType);
        Configuration cfg = new Configuration(Region.region2());
        final Client client = new Client(cfg);
        try {
            Response response = client.post(videoUrl, body.getBytes(), header, contentType);
            final Map map = objectMapper.readValue(response.getInfo().split(" \n")[2], Map.class);
            final Object job = map.get("job");
            url = "http://ai.qiniuapi.com/v3/jobs/video/" + job.toString();
            method = "GET";
            header = new StringMap();
            header.put("Host", "ai.qiniuapi.com");
            header.put("Authorization", qiNiuConfig.getToken(url, method, null, null));
            while (true) {
                Response response1 = client.get(url, header);
                final BodyDTO bodyDTO = objectMapper.readValue(response1.getInfo().split(" \n")[2], BodyDTO.class);
                if (bodyDTO.getStatus().equals("FINISHED")) {
                    // 1.从系统配置表获取 pulp politician terror比例
                    final Setting setting = settingService.getById(1);
                    final SettingScoreDTO settingScoreRule = objectMapper.readValue(setting.getAuditPolicy(), SettingScoreDTO.class);
                    final List<ScoreDTO> auditRule = Arrays.asList(settingScoreRule.getManualScore(), settingScoreRule.getPassScore(), settingScoreRule.getSuccessScore());
                    auditResponse = audit(auditRule, bodyDTO);
                    return auditResponse;
                }
                Thread.sleep(2000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auditResponse;
    }
}
