package com.example.entity.video;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.entity.BaseEntity;

/**
 * <p>
 * 
 * </p>
 *

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VideoType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long videoId;

    private Long typeId;

}
