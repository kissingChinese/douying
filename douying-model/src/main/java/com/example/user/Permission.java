package com.example.user;

import com.baomidou.mybatisplus.annotation.TableField;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = false)
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long pId;

    private String path;

    private String href;

    private String icon;

    private String name;

    private Integer isMenu;

    private String target;

    private Integer sort;

    private Integer state;

    @TableField(exist = false)
    private List<Permission> children;


}
