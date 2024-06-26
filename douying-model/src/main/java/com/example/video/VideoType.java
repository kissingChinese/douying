package com.example.video;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.entity.BaseEntity;


@Data
@EqualsAndHashCode(callSuper = false)
public class VideoType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long videoId;

    private Long typeId;

}
