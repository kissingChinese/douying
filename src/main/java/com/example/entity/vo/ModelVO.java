package com.example.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class ModelVO {

    private Long userId;
    // 兴趣视频分类
    private List<String> labels;
}
