package com.example.constant;

import java.util.HashMap;
import java.util.Map;
/**
 * 审核消息映射关系
 */
public class AuditMsgMap {

    static Map<String,String> msgMap = new HashMap<>();
    static {
        msgMap.put("sexy","性感图片");
        msgMap.put("pulp","色情图片");
        msgMap.put("bloodiness","血腥");
        msgMap.put("self_burning","自焚");
        msgMap.put("beheaded","斩首类");
        msgMap.put("march_crowed","人群聚集类");
        msgMap.put("fight_police","警民冲突");
        msgMap.put("fight_person","民众肢体接触");
        msgMap.put("illegal_flag","违规旗帜类");
        msgMap.put("guns","枪");
        msgMap.put("knives","刀");
        msgMap.put("anime_bloodiness","动漫血腥类");
        msgMap.put("anime_knives","二次元刀");
        msgMap.put("anime_guns","二次元枪");
        msgMap.put("politics","涉政");
        msgMap.put("violence","暴恐");
        msgMap.put("domestic_statesman","国内政要");
        msgMap.put("foreign_statesman","国外政要");
        msgMap.put("chinese_martyr","革命英烈");
        msgMap.put("affairs_official_gov","落马官员（政府）");
        msgMap.put("affairs_official_ent","落马官员（企事业）");
        msgMap.put("terrorist","恐怖分子");
        msgMap.put("affairs_celebrity","劣迹艺人");
        msgMap.put("spam","含垃圾信息");
        msgMap.put("ad","涉政人物");
        msgMap.put("abuse","辱骂");
        msgMap.put("porn","色情");
        msgMap.put("flood","灌水");
        msgMap.put("contraband","违禁");
        msgMap.put("meaningless","无意义");
    }

    public static String getInfo(String key){
        return msgMap.get(key);
    }
}
