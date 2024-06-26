package com.example.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ResultDTO implements Serializable {
    Integer code;
    String message;
    ResultChildDTO result;
}


