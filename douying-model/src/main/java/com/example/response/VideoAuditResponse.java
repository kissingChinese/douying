package com.example.response;

import com.example.task.VideoTask;
import lombok.Data;
import lombok.ToString;

/**
 *封装视频审核返回结果
 *
 */
@Data
@ToString
public class VideoAuditResponse {

    private AuditResponse videoAuditResponse;

    private AuditResponse imageAuditResponse;

    private AuditResponse textAuditResponse;

    private VideoTask videoTask;
}
