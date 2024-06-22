package com.example.entity.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Data
@ToString
public class TypeJson implements Serializable {
    String suggestion;
    List<CutsJson> cuts;
    List<DetailsJson> details;
}
