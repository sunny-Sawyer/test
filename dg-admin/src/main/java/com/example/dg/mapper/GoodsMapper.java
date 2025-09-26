package com.example.dg.mapper;


import com.example.dg.common.annotation.AutoFill;

import com.example.dg.common.enumeration.OperationType;
import com.example.dg.pojo.dto.GoodsPageQueryDTO;
import com.example.dg.pojo.entity.Goods;
import com.example.dg.pojo.vo.GoodsVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {

    /**
     * 根据分类id查询商品数量
     * @param categoryId
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    @Select("select count(id) from goods where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增商品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Goods dish);

    /**
     * 商品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<GoodsVO> pageQuery(GoodsPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @Select("select * from goods where id = #{id}")
    Goods getById(Long id);

    /**
     * 根据id删除商品
     * @param id
     */
    @Delete("delete from goods where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据id批量删除商品
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改商品基本信息
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Goods dish);

    /**
     * 动态条件查询商品
     * @param dish
     * @return
     */
    List<Goods> list(Goods dish);

    /**
     * 修改商品起售停售
     * @param status
     * @param id
     */
    @Update("update goods set status = #{status} where id = #{id}")
    void startOrStop(Integer status, Long id);

    /**
     * 根据条件统计商品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 根据分类id查询起售中的商品
     * @param categoryId
     * @param status
     * @return
     */
    List<GoodsVO> listByCategoryIdAndStatus(Long categoryId, Integer status);
}
