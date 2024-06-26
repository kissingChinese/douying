package com.example.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Data
@ToString
public class TypeDTO implements Serializable {
    String suggestion;
    List<CutsDTO> cuts;
    List<DetailsDTO> details;
}
