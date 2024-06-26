package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 逻辑删除，0：未删除，1：删除，默认为0
     */
    @TableLogic
    private Boolean isDeleted;
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtUpdated;

}
