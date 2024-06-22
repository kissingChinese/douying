package com.example.entity.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class DetailsJson implements Serializable {
    Double score;
    String suggestion;
    String label;
    String group;
}
