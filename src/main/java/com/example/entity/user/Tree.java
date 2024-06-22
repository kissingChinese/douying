package com.example.entity.user;

import lombok.Data;

import java.util.List;

@Data
public class Tree {

    private Long id;

    private Long pId;

    private String title;

    private List<Tree> children;

    private Boolean spread;
}
