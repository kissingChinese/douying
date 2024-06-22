package com.example.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改用户信息
 */
@Data
public class UpdateUserVO {

    @NotBlank(message = "昵称不可为空")
    private String nickName;

    private Long avatar;

    private Boolean sex;

    private String description;

    private Long defaultFavoritesId;



}
