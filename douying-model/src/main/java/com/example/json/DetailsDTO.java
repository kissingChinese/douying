package com.example.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class DetailsDTO implements Serializable {
    Double score;
    String suggestion;
    String label;
    String group;
}
