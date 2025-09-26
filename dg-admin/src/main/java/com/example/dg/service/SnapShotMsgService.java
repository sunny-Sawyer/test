package com.example.dg.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dg.pojo.entity.SnapShotMsg;
import com.example.dg.result.Result;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 26030
* @description 针对表【snap_shot_msg(随手拍信息)】的数据库操作Service
* @createDate 2025-09-17 12:18:29
*/
public interface SnapShotMsgService extends IService<SnapShotMsg> {

    /**
     * 发布随手拍
     *
     * @param snapShotMsg 随手拍信息
     * @param images 图片文件
     * @return 结果
     */
    Result publishSnapShot(SnapShotMsg snapShotMsg, MultipartFile[] images);

    /**
     * 分页查询随手拍列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param status 状态筛选
     * @param userId 用户ID筛选
     * @return 随手拍列表
     */
    Result getSnapShotList(Integer pageNum, Integer pageSize, Integer status, Integer userId);

    /**
     * 审核随手拍
     *
     * @param id 随手拍ID
     * @param status 审核状态（1-通过，2-驳回）
     * @return 结果
     */
    Result auditSnapShot(Integer id, Integer status);

    /**
     * 删除随手拍
     *
     * @param id 随手拍ID
     * @return 结果
     */
    Result deleteSnapShot(Integer id);

    /**
     * 点赞随手拍
     *
     * @param id 随手拍ID
     * @return 结果
     */
    Result likeSnapShot(Integer id);

    /**
     * 获取随手拍详情
     *
     * @param id 随手拍ID
     * @return 随手拍详情
     */
    Result getSnapShotDetail(Integer id);
}
