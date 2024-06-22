package com.example.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;


@Data
@NoArgsConstructor
@ToString
public class HotVideo implements  Serializable {

    private static final long serialVersionUID = 1L;

    String hotFormat;

    Double hot;

    Long videoId;

    String title;

    public HotVideo(Double hot,Long videoId,String title){
        this.hot = hot;
        this.videoId = videoId;
        this.title = title;
    }


    public void hotFormat(){
        BigDecimal bigDecimal = new BigDecimal(this.hot);
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));
        DecimalFormat formater = new DecimalFormat("0.0");
        formater.setRoundingMode(RoundingMode.HALF_UP);    // 5000008.89
        String formatNum = formater.format(decimal);
        this.setHotFormat( formatNum+"万");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotVideo hotVideo = (HotVideo) o;
        return Objects.equals(videoId, hotVideo.videoId) &&
                Objects.equals(title, hotVideo.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, title);
    }
}
