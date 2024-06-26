package com.example.video;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.config.QiNiuConfig;
import com.example.entity.BaseEntity;
import com.example.entity.vo.UserVO;
import org.springframework.util.ObjectUtils;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Data
@EqualsAndHashCode(callSuper = false)
public class Video extends BaseEntity {
    private static final long serialVersionUID = 1L;

    // YV ID 以YV+UUID
    private String yv;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    private Long url; //文件的ID
    private Long userId; //视频的归属人

    private Long cover;
    /**
     * 公开/私密，0：公开，1：私密，默认为0
     */
    private Boolean open;

    // 审核状态:通过,未通过,审核中
    private Integer auditStatus;

    // 审核状态的消息，当前嵌套在这里，应该有一个审核表?
    private String msg;

    // 审核快慢状态 0 慢速  1快速
    private Boolean auditQueueStatus; //审核的优先级

    // 点赞数
    private Long startCount;

    // 分享数
    private Long shareCount;

    // 浏览次数
    private Long historyCount;

    // 收藏次数
    private Long favoritesCount;

    // 视频时长
    private String duration;

    // 视频分类
    @TableField(exist = false)
    private String videoType;

    private String labelNames; //视频的标签

    private Long typeId; //类型ID

    // 关联的用户
    @TableField(exist = false)
    private UserVO user;

    // 关联分类名称
    @TableField(exist = false)
    private String typeName;

    // 是否点赞
    @TableField(exist = false)
    private Boolean start;

    // 是否收藏
    @TableField(exist = false)
    private Boolean favorites;

    // 是否关注
    @TableField(exist = false)
    private Boolean follow;

    // 用户昵称
    @TableField(exist = false)
    private String userName;

    // 审核状态名称
    @TableField(exist = false)
    private String auditStateName;

    // 是否公开
    @TableField(exist = false)
    private String openName;

    public List<String> buildLabel(){
        if (ObjectUtils.isEmpty(this.labelNames)) return Collections.EMPTY_LIST;
        else if(this.labelNames.contains(",")) return Arrays.asList(this.labelNames.split(","));
        else if(this.labelNames.contains("，"))  return Arrays.asList(this.labelNames.split("，"));
        else
            return Arrays.asList(this.labelNames.split(" "));
    }

    // 和get方法分开，避免发生歧义
    public String buildVideoUrl(){
        return QiNiuConfig.CNAME + "/" + this.url;
    }

    public String buildCoverUrl(){
        return QiNiuConfig.CNAME + "/" + this.cover;
    }


}

