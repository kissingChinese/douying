package com.example.entity.video;

import java.util.Arrays;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.entity.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
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

    private Boolean open;

    private String labelNames;

    private Integer sort;

    @TableField(exist = false)
    private Boolean used;

    public List<String> buildLabel(){
        return Arrays.asList(labelNames.split(","));
    }
}

