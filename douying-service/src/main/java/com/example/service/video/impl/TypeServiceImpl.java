package com.example.service.video.impl;

import com.example.mapper.video.TypeMapper;
import com.example.service.video.TypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.video.Type;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
    @Override
    public List<String> getLabels(Long typeId) {
        final List<String> labels = this.getById(typeId).buildLabel(); //创建视频标签列表
        return labels;
    }
    @Override
    public List<String> random10Labels() {
        final List<Type> types = list(null);
        Collections.shuffle(types); //对type集合中的元素进行随机排序
        final ArrayList<String> labels = new ArrayList<>();
        for (Type type : types) {
            for (String label : type.buildLabel()) {
                if (labels.size() == 10){
                    return labels;
                }
                labels.add(label); //给每个分类取出十个标签
            }
        }
        return labels;
    }
}
