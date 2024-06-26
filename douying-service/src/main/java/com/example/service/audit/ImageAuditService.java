package com.example.service.audit;

import com.example.json.*;
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

/**
 *  图片审核
 */
@Service
public class ImageAuditService extends AbstractAuditService<String, AuditResponse>{

    static String imageUlr = "http://ai.qiniuapi.com/v3/image/censor";
    static String imageBody = "{\n" +
            "    \"data\": {\n" +
            "        \"uri\": \"${url}\"\n" +
            "    },\n" +
            "    \"params\": {\n" +
            "        \"scenes\": [\n" +
            "            \"pulp\",\n" +
            "            \"terror\",\n" +
            "            \"politician\"\n" +
            "        ]\n" +
            "    }\n" +
            "}";;


    @Override
    public AuditResponse audit(String url) {
        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setAuditStatus(AuditStatus.SUCCESS);

        if (!isNeedAudit()) {
            return auditResponse;
        }

        url = appendUUID(url);

        String body = imageBody.replace("${url}", url);
        String method = "POST";
        // 获取token
        final String token = qiNiuConfig.getToken(imageUlr, method, body, contentType);
        StringMap header = new StringMap();
        header.put("Host", "ai.qiniuapi.com");
        header.put("Authorization", token);
        header.put("Content-Type", contentType);
        Configuration cfg = new Configuration(Region.region2());
        final Client client = new Client(cfg);
        try {
            Response response = client.post(imageUlr, body.getBytes(), header, contentType);

            final Map map = objectMapper.readValue(response.getInfo().split(" \n")[2], Map.class);
            final ResultChildDTO result = objectMapper.convertValue(map.get("result"), ResultChildDTO.class);
            final BodyDTO bodyDTO = new BodyDTO();
            final ResultDTO resultDTO = new ResultDTO();
            resultDTO.setResult(result);
            bodyDTO.setResult(resultDTO);

            final Setting setting = settingService.getById(1);
            final SettingScoreDTO settingScoreRule = objectMapper.readValue(setting.getAuditPolicy(), SettingScoreDTO.class);

            final List<ScoreDTO> auditRule = Arrays.asList(settingScoreRule.getManualScore(), settingScoreRule.getPassScore(), settingScoreRule.getSuccessScore());
            // 审核
            auditResponse = audit(auditRule, bodyDTO);
            return auditResponse;
        } catch (Exception e) {
            auditResponse.setAuditStatus(AuditStatus.SUCCESS);
            e.printStackTrace();
        }
        return auditResponse;
    }
}
