package com.example.entity.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class ResultChildJson implements Serializable {
    String suggestion;
    ScenesJson scenes;
}
