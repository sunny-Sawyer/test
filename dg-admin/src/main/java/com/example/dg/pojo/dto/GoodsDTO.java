package com.example.dg.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsDTO {

    private Long id;

    //商品名称
    private String name;

    //商品分类id
    private Long categoryId;

    //商品价格
    private BigDecimal price;

    //图片
    private String image;

    //描述信息
    private String description;

    //0 停售 1 起售
    private Integer status;

}
