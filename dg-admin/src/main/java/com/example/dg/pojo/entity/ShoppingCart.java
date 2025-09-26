package com.example.dg.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //名称
    private String name;

    //图片
    private String image;

    //用户id
    private Long userId;

    //商品id
    private Long goodsId;

    //数量
    private Integer number;

    //所需积分
    private Integer points;

    //创建时间
    private LocalDateTime createAt;
}
