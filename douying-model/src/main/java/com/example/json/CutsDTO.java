package com.example.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class CutsDTO implements Serializable {
    List<DetailsDTO> details;
    String suggestion;
    Long offset;
}
