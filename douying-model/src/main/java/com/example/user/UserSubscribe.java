package com.example.user;

import lombok.Data;

/**
 *  用户订阅表
 *
 */
@Data
public class UserSubscribe {
    private Long id;
    private Long typeId;
    private Long userId;
}
