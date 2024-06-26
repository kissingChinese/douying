package com.example.json;

import lombok.Data;


@Data
public class SettingScoreDTO {
    // 通过
    ScoreDTO successScore;
    // 人工审核
    ScoreDTO manualScore;
    // PASS
    ScoreDTO passScore;
}
