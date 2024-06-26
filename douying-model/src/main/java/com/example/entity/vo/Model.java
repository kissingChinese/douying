package com.example.entity.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class Model implements Serializable {
    private String label;
    private Long videoId;
    private Double score;
}
