package com.example.entity.vo;

import com.example.config.QiNiuConfig;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Data
public class VideoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // YV ID 以YV+UUID
    private String yv;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    @NotBlank(message = "url不能为空")
    private String url;

    private String cover;

    private Boolean open;

    private Long userId;

    // 审核快慢状态 0 慢速  1快速
    private Boolean auditQueueStatus;

    // 视频分类
    private String videoType;

    @NotBlank(message = "给个标签吧,不然没人看到你的视频")
    private String labelNames;

    @NotNull(message = "分类不能为空")
    private Long typeId;

    private Date gmtCreated;

    public List<String> buildLabel(){
        if (ObjectUtils.isEmpty(this.labelNames)) return Collections.EMPTY_LIST;
        return Arrays.asList(this.labelNames.split(","));
    }


    // 和get方法分开，避免发生歧义
    public String getVideoUrl(){
        return QiNiuConfig.CNAME + "/" + this.url;
    }

    public String getCoverUrl(){
        return QiNiuConfig.CNAME + "/" + this.cover;
    }

}
