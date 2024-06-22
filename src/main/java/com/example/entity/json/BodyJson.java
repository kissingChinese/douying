package com.example.entity.json;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


@Data
//@ToString
public class BodyJson implements Serializable {
    String id;
    String status;
    ResultJson result;


    public boolean compare(Double min, Double max, Double value) {
        return value >= min && value <= max;
    }

    public boolean checkViolation(List<CutsJson> types,Double min, Double max){
        for (CutsJson cutsJson : types) {
            if (!ObjectUtils.isEmpty(cutsJson.details)){
                for (DetailsJson detail : cutsJson.details) {
                   if (compare(min,max,detail.getScore())){
                       return true;
                   }
                }
            }
        }
        return false;
    }


    // 视频和图片分开处理
    public List<CutsJson> getTerror(){
        final TypeJson terror = result.getResult().getScenes().getTerror();
        if (!ObjectUtils.isEmpty(terror.getCuts())){
            return terror.getCuts();
        }


        final CutsJson cutsJson = new CutsJson();
        cutsJson.setDetails(terror.getDetails());
        cutsJson.setSuggestion(terror.getSuggestion());
        return Collections.singletonList(cutsJson);
    }

    public List<CutsJson> getPolitician(){
        final TypeJson politician = result.getResult().getScenes().getPolitician();
        if (!ObjectUtils.isEmpty(politician.getCuts())){
            return politician.getCuts();
        }

        final CutsJson cutsJson = new CutsJson();
        cutsJson.setDetails(politician.getDetails());
        cutsJson.setSuggestion(politician.getSuggestion());

        return Collections.singletonList(cutsJson);
    }

    public List<CutsJson> getPulp(){
        final TypeJson pulp = result.getResult().getScenes().getPulp();
        if (!ObjectUtils.isEmpty(pulp.getCuts())){
            return pulp.cuts;
        }

        final CutsJson cutsJson = new CutsJson();
        cutsJson.setDetails(pulp.getDetails());
        cutsJson.setSuggestion(pulp.getSuggestion());

        return Collections.singletonList(cutsJson);
    }





}
