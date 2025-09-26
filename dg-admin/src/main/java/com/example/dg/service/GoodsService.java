package com.example.dg.service;

import com.example.dg.pojo.dto.GoodsDTO;
import com.example.dg.pojo.dto.GoodsPageQueryDTO;
import com.example.dg.pojo.entity.Goods;
import com.example.dg.pojo.vo.GoodsVO;
import com.example.dg.result.PageResult;

import java.util.List;

public interface GoodsService {

    /**
     * 新增商品
     * @param goodsDTO
     */
    void save(GoodsDTO goodsDTO);

    /**
     * 分页查询菜品
     * @param goodsPageQueryDTO
     * @return
     */
    PageResult pageQuery(GoodsPageQueryDTO goodsPageQueryDTO);

    /**
     * 批量删除商品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    GoodsVO getByIdWithFlavor(Long id);

    /**
     * 修改商品
     * @param goodsDTO
     */
    void updateWithFlavor(GoodsDTO goodsDTO);

    /**
     * 商品起售停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类id查询商品
     * @param categoryId
     * @return
     */
    List<Goods> list(Long categoryId);

    /**
     * 根据分类id查询起售商品
     * @param goods
     * @return
     */
    List<GoodsVO> listByCategoryIdAndStatus(Goods goods);
}
