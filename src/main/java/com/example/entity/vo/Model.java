package com.example.entity.vo;

import com.example.controller.CustomerController;
import lombok.Data;


@Data
public class Model {
    private String label;
    private Long videoId;
    /**
     * 暴漏的接口只有根据停留时长 {@link CustomerController#updateUserModel}
     */

    private Double score;
}
