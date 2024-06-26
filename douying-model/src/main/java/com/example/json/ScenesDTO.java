package com.example.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ScenesDTO implements Serializable {
    private TypeDTO terror;
    private TypeDTO politician;
    private TypeDTO pulp;
    private TypeDTO antispam;

}
