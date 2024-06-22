package com.example.entity.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ScenesJson implements Serializable {
    private TypeJson terror;
    private TypeJson politician;
    private TypeJson pulp;
    private TypeJson antispam;

}
