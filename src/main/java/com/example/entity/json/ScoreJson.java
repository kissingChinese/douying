package com.example.entity.json;

import lombok.Data;



@Data
public class ScoreJson{
    Double minPulp;
    Double maxPulp;

    Double minTerror;
    Double maxTerror;

    Double minPolitician;
    Double maxPolitician;

    Integer auditStatus;

}