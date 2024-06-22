package com.example.entity.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;


@Data
public class BasePage {

    private Long page = 1L;
    private Long limit = 15L;

    public IPage page(){
        return new Page(page == null ? 1L : this.page,limit == null ? 15L : this.limit);
    }
}
