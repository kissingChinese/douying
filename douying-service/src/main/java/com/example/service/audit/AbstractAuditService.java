package com.example.service.audit;
import com.example.json.*;
import com.example.response.AuditResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.config.LocalCache;
import com.example.config.QiNiuConfig;
import com.example.constant.AuditMsgMap;
import com.example.constant.AuditStatus;
import com.example.entity.Setting;
import com.example.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

/**
 * 统一封装审核逻辑，并留给子类进行编排或者调用普通逻辑
 */
@Service
public abstract class AbstractAuditService<T,R> implements AuditService<T,R> {

    @Autowired
    protected QiNiuConfig qiNiuConfig;

    @Autowired
    protected SettingService settingService;


    protected ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    static final String contentType = "application/json";

    /**
     * 审核
     * @param scoreDTOList
     * @param bodyDTO
     * @return
     */
    protected AuditResponse audit(List<ScoreDTO> scoreDTOList, BodyDTO bodyDTO) {
        AuditResponse audit = new AuditResponse();
        // 遍历的是通过,人工,失败的审核规则,我当前没有办法知道是什么状态
        for (ScoreDTO scoreDTO : scoreDTOList) {
            audit = audit(scoreDTO, bodyDTO);
            // 如果为true,说明命中得分，提前返回
            if (audit.getFlag()){
                audit.setAuditStatus(scoreDTO.getAuditStatus());
                return audit;
            }
        }
        // 如果出来了说明审核的内容没分数 / 审核比例没调好(人员问题)
        // 比较suggest
        final ScenesDTO scenes = bodyDTO.getResult().getResult().getScenes();
        if (endCheck(scenes)){
            audit.setAuditStatus(AuditStatus.SUCCESS);
        }else {
            audit.setAuditStatus(AuditStatus.PASS);
            audit.setMsg("内容不合法");
        }
        return audit;
    }

    /**
     * 返回对应规则的信息
     * @param types
     * @param minPolitician
     * @return
     */
    private AuditResponse getInfo(List<CutsDTO> types, Double minPolitician, String key) {
        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setFlag(false);
        String info = null;
        // 获取信息
        for (CutsDTO type : types) {
            for (DetailsDTO detail : type.getDetails()) {
                // 人工/PASS ? 交给七牛云状态，我只获取信息和offset
                if (detail.getScore() > minPolitician) {
                    // 如果违规,则填充额外信息
                    if (!detail.getLabel().equals(key)) {
                        info = AuditMsgMap.getInfo(detail.getLabel());
                        auditResponse.setMsg(info);
                        auditResponse.setOffset(type.getOffset());
                    }
                    auditResponse.setFlag(true);
                }

            }
        }
        if (auditResponse.getFlag() && ObjectUtils.isEmpty(auditResponse.getMsg())){
            auditResponse.setMsg("该视频违法日平台规则");
        }

        return auditResponse;
    }


    /**
     * 当前审核规则如果能匹配上也就是进入了if判断中,则需要获取违规信息
     * 如果走到末尾则说明没有匹配上
     * @param scoreDTO
     * @param bodyDTO
     * @return
     */
    private AuditResponse audit(ScoreDTO scoreDTO, BodyDTO bodyDTO) {

        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setFlag(true);
        auditResponse.setAuditStatus(scoreDTO.getAuditStatus());

        final Double minPolitician = scoreDTO.getMinPolitician();
        final Double maxPolitician = scoreDTO.getMaxPolitician();
        final Double minPulp = scoreDTO.getMinPulp();
        final Double maxPulp = scoreDTO.getMaxPulp();
        final Double minTerror = scoreDTO.getMinTerror();
        final Double maxTerror = scoreDTO.getMaxTerror();

        // 所有都要比较,如果返回的有问题则直接返回
        if (!ObjectUtils.isEmpty(bodyDTO.getPolitician())) {
            if (bodyDTO.checkViolation(bodyDTO.getPolitician(),minPolitician,maxPolitician)) {
                final AuditResponse response = getInfo(bodyDTO.getPolitician(), minPolitician, "group");
                auditResponse.setMsg(response.getMsg());
                if (response.getFlag()) {
                    auditResponse.setOffset(response.getOffset());
                    return auditResponse;
                }
            }
        }
        if (!ObjectUtils.isEmpty(bodyDTO.getPulp())) {
            if (bodyDTO.checkViolation(bodyDTO.getPulp(),minPulp,maxPulp)) {
                final AuditResponse response = getInfo(bodyDTO.getPulp(), minPulp, "normal");
                auditResponse.setMsg(response.getMsg());
                // 如果违规则提前返回
                if (response.getFlag()) {
                    auditResponse.setOffset(response.getOffset());
                    return auditResponse;
                }
            }
        }
        if (!ObjectUtils.isEmpty(bodyDTO.getTerror())) {
            if (bodyDTO.checkViolation(bodyDTO.getTerror(),minTerror,maxTerror)) {
                final AuditResponse response = getInfo(bodyDTO.getTerror(), minTerror, "normal");
                auditResponse.setMsg(response.getMsg());
                if (response.getFlag()) {
                    auditResponse.setOffset(response.getOffset());
                    return auditResponse;
                }
            }
        }
        auditResponse.setMsg("正常");
        auditResponse.setFlag(false);
        return auditResponse;
    }

    /**
     * 最后检查,可能没得分,检查suggestion
     * @param scenes
     * @return
     */
    private boolean endCheck(ScenesDTO scenes){
        final TypeDTO terror = scenes.getTerror();
        final TypeDTO politician = scenes.getPolitician();
        final TypeDTO pulp = scenes.getPulp();
        if (terror.getSuggestion().equals("block") || politician.getSuggestion().equals("block") || pulp.getSuggestion().equals("block")) {
            return false;
        }
        return true;
    }

    /**
     * 根据系统配置表查询是否需要审核
     * @return
     */
    protected Boolean isNeedAudit(){
        final Setting setting = settingService.list(null).get(0);
        return setting.getAuditOpen();
    }



    protected String appendUUID(String url){

        final Setting setting = settingService.list(null).get(0);

        if (setting.getAuth()) {
            final String uuid = UUID.randomUUID().toString();
            LocalCache.put(uuid,true);
            if (url.contains("?")){
                url = url+"&uuid="+uuid;
            }else {
                url = url+"?uuid="+uuid;
            }
            return url;
        }
        return url;
    }
}
