package com.example.entity.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class CutsJson implements Serializable {
    List<DetailsJson> details;
    String suggestion;
    Long offset;
}
