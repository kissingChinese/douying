package com.example.video;
import java.util.Arrays;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.entity.BaseEntity;
import javax.validation.constraints.NotBlank;

/**
 * 分类,隐藏视频标签
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Type extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "分类名称不可为空")
    private String name;

    private String description;

    private String icon;

    private Boolean open; //逻辑开启分类

    private String labelNames;

    private Integer sort;

    @TableField(exist = false)
    private Boolean used; //表示用户是否订阅这个分类

    public List<String> buildLabel(){
        return Arrays.asList(labelNames.split(",")); //将标签以逗号进行分割
    } //TODO
}

