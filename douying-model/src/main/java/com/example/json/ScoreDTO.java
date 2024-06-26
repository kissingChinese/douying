package com.example.json;

import lombok.Data;



@Data
public class ScoreDTO {
    Double minPulp;
    Double maxPulp;

    Double minTerror;
    Double maxTerror;

    Double minPolitician;
    Double maxPolitician;

    Integer auditStatus;

}