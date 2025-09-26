package com.example.dg.controller.admin;

import com.example.dg.pojo.dto.GoodsDTO;
import com.example.dg.pojo.dto.GoodsPageQueryDTO;
import com.example.dg.pojo.entity.Goods;
import com.example.dg.pojo.vo.GoodsVO;
import com.example.dg.result.PageResult;
import com.example.dg.result.Result;
import com.example.dg.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 商品管理
 */
@RestController
@RequestMapping("/admin/goods")
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增商品
     * @param goodsDTO
     * @return
     */
    @PostMapping()
    @PreAuthorize("@ss.hasRoleId('1,2')")
    public Result save(@RequestBody GoodsDTO goodsDTO) {
        log.info("新增商品：{}", goodsDTO);
        goodsService.save(goodsDTO);

        //清理缓存数据
        String key =  "dish_" + goodsDTO.getCategoryId();
        redisTemplate.delete(key);

        return Result.success();
    }

    /**
     * 商品分页查询
     * @param goodsPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    public Result<PageResult> page(GoodsPageQueryDTO goodsPageQueryDTO) {
        log.info("菜品分页查询：{}", goodsPageQueryDTO);
        PageResult pageResult = goodsService.pageQuery(goodsPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @DeleteMapping()
    @PreAuthorize("@ss.hasRoleId('1,2')")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除：{}", ids);
        goodsService.deleteBatch(ids);

        //清理所有缓存数据，一个个删缓存太复杂，直接全删
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    public Result<GoodsVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品信息：{}", id);
        GoodsVO goodsVO = goodsService.getByIdWithFlavor(id);
        return Result.success(goodsVO);
    }

    /**
     * 修改商品
     * @param goodsDTO
     * @return
     */
    @PutMapping()
    @PreAuthorize("@ss.hasRoleId('1,2')")
    public Result update(@RequestBody GoodsDTO goodsDTO){
        log.info("修改菜品信息：{}", goodsDTO);
        goodsService.updateWithFlavor(goodsDTO);

        //清理所有缓存数据，因为修改菜品时，如果修改分类见会影响到两份缓存数据
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 商品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("菜品起售停售：{},{}", status, id);

        goodsService.startOrStop(status, id);

        //清理所有缓存数据
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 根据分类id查询商品
     * @param categoryId
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    public Result<List<Goods>> list(Long categoryId){
        List<Goods> list = goodsService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
