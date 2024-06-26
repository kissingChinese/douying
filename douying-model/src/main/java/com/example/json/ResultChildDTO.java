package com.example.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class ResultChildDTO implements Serializable {
    String suggestion;
    ScenesDTO scenes;
}
