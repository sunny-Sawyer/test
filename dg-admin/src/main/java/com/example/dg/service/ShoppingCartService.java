package com.example.dg.service;

import com.example.dg.pojo.dto.ShoppingCartDTO;
import com.example.dg.pojo.entity.ShoppingCart;

import java.util.List;


public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> list(Long userId);

    /**
     * 清空购物车
     */
    void clear(Long userId);

    /**
     * 减少购物车
     * @param shoppingCartDTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);

}
