package com.example.json;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


@Data
//@ToString
public class BodyDTO implements Serializable {
    String id;
    String status;
    ResultDTO result;


    public boolean compare(Double min, Double max, Double value) {
        return value >= min && value <= max;
    }

    public boolean checkViolation(List<CutsDTO> types, Double min, Double max){
        for (CutsDTO cutsDTO : types) {
            if (!ObjectUtils.isEmpty(cutsDTO.details)){
                for (DetailsDTO detail : cutsDTO.details) {
                   if (compare(min,max,detail.getScore())){
                       return true;
                   }
                }
            }
        }
        return false;
    }


    // 视频和图片分开处理
    public List<CutsDTO> getTerror(){
        final TypeDTO terror = result.getResult().getScenes().getTerror();
        if (!ObjectUtils.isEmpty(terror.getCuts())){
            return terror.getCuts();
        }


        final CutsDTO cutsDTO = new CutsDTO();
        cutsDTO.setDetails(terror.getDetails());
        cutsDTO.setSuggestion(terror.getSuggestion());
        return Collections.singletonList(cutsDTO);
    }

    public List<CutsDTO> getPolitician(){
        final TypeDTO politician = result.getResult().getScenes().getPolitician();
        if (!ObjectUtils.isEmpty(politician.getCuts())){
            return politician.getCuts();
        }

        final CutsDTO cutsDTO = new CutsDTO();
        cutsDTO.setDetails(politician.getDetails());
        cutsDTO.setSuggestion(politician.getSuggestion());

        return Collections.singletonList(cutsDTO);
    }

    public List<CutsDTO> getPulp(){
        final TypeDTO pulp = result.getResult().getScenes().getPulp();
        if (!ObjectUtils.isEmpty(pulp.getCuts())){
            return pulp.cuts;
        }

        final CutsDTO cutsDTO = new CutsDTO();
        cutsDTO.setDetails(pulp.getDetails());
        cutsDTO.setSuggestion(pulp.getSuggestion());

        return Collections.singletonList(cutsDTO);
    }





}
