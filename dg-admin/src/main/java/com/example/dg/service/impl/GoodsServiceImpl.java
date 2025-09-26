package com.example.dg.service.impl;

import com.example.dg.common.constant.MessageConstant;
import com.example.dg.common.constant.StatusConstant;
import com.example.dg.common.exception.DeletionNotAllowedException;
import com.example.dg.mapper.GoodsMapper;
import com.example.dg.pojo.dto.GoodsDTO;
import com.example.dg.pojo.dto.GoodsPageQueryDTO;
import com.example.dg.pojo.entity.Goods;
import com.example.dg.pojo.vo.GoodsVO;
import com.example.dg.result.PageResult;
import com.example.dg.service.GoodsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 新增商品
     * @param goodsDTO
     */
    @Transactional //对多个表进行插入，保证事务一致性
    @Override
    public void save(GoodsDTO goodsDTO) {

        Goods goods = new Goods();

        BeanUtils.copyProperties(goodsDTO, goods);

        //向菜品表插入1条数据
        goodsMapper.insert(goods);

    }

    /**
     * 商品分页查询
     * @param goodsPageQueryDTO
     * @return PageResult
     */
    @Override
    public PageResult pageQuery(GoodsPageQueryDTO goodsPageQueryDTO) {
        PageHelper.startPage(goodsPageQueryDTO.getPage(), goodsPageQueryDTO.getPageSize());
        Page<GoodsVO> page = goodsMapper.pageQuery(goodsPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 商品批量删除
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断菜品是否起售
        for (Long id : ids) {
            Goods goods = goodsMapper.getById(id);
            if(goods.getStatus() == StatusConstant.ENABLE){
                //起售中的菜品不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //根据id批量删除菜品数据
        goodsMapper.deleteByIds(ids);

    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @Override
    public GoodsVO getByIdWithFlavor(Long id) {
        //根据id查询菜品数据
        Goods goods = goodsMapper.getById(id);

        //将查询到的数据封装到dishVO中
        GoodsVO goodsVO = new GoodsVO();
        BeanUtils.copyProperties(goods, goodsVO);

        return goodsVO;
    }

    /**
     * 修改商品
     * @param goodsDTO
     */
    @Override
    public void updateWithFlavor(GoodsDTO goodsDTO) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO, goods);

        //修改菜品基本信息
        goodsMapper.update(goods);

    }

    /**
     * 商品起售停售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        goodsMapper.startOrStop(status, id);
    }

    /**
     * 根据分类id查询商品
     * @param categoryId
     * @return
     */
    public List<Goods> list(Long categoryId) {
        Goods goods = Goods.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return goodsMapper.list(goods);
    }

    /**
     * 用户根据分类id查询起售商品
     * @param goods
     * @return
     */
    @Override
    public List<GoodsVO> listByCategoryIdAndStatus(Goods goods) {
        Long categoryId = goods.getCategoryId();
        Integer status = goods.getStatus();
        List<GoodsVO> list = goodsMapper.listByCategoryIdAndStatus(categoryId, status);
        return list;
    }

}
