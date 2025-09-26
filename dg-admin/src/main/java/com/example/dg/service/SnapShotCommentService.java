package com.example.dg.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dg.pojo.entity.SnapShotComment;
import com.example.dg.result.Result;

/**
* @author 26030
* @description 针对表【snap_shot_comment(随手拍评论)】的数据库操作Service
* @createDate 2025-09-17 12:18:54
*/
public interface SnapShotCommentService extends IService<SnapShotComment> {

    /**
     * 发布评论
     *
     * @param comment 评论信息
     * @return 结果
     */
    Result publishComment(SnapShotComment comment);

    /**
     * 获取随手拍的评论列表
     *
     * @param ssmid 随手拍ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 评论列表
     */
    Result getCommentList(Integer ssmid, Integer pageNum, Integer pageSize);

    /**
     * 审核评论
     *
     * @param id 评论ID
     * @param status 审核状态（1-通过，2-驳回）
     * @return 结果
     */
    Result auditComment(Integer id, Integer status);

    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 结果
     */
    Result deleteComment(Integer id);

    /**
     * 点赞评论
     *
     * @param id 评论ID
     * @return 结果
     */
    Result likeComment(Integer id);
}
