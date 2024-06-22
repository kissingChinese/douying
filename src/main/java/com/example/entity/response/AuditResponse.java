package com.example.entity.response;

import lombok.Data;
import lombok.ToString;

/**
 * 封装视频审核返回结果
 */
@Data
@ToString
public class AuditResponse {

    private Integer auditStatus;
    // true:违规 false:正常
    private Boolean flag;
    // 信息
    private String msg;

    private Long offset;

    public AuditResponse(Integer auditStatus,String msg){
        this.auditStatus = auditStatus;
        this.msg = msg;
    }
    public AuditResponse(){}
}
