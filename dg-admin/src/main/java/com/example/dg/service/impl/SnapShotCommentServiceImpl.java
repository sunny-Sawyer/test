package com.example.dg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dg.common.utils.SecurityUtils;
import com.example.dg.common.utils.StringUtils;
import com.example.dg.mapper.SnapShotCommentMapper;
import com.example.dg.pojo.entity.SnapShotComment;
import com.example.dg.result.Result;
import com.example.dg.service.SnapShotCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
* @author 26030
* @description 针对表【snap_shot_comment(随手拍评论)】的数据库操作Service实现
* @createDate 2025-09-17 12:18:54
*/
@Service
public class SnapShotCommentServiceImpl extends ServiceImpl<SnapShotCommentMapper, SnapShotComment>
    implements SnapShotCommentService {

    @Resource
    private SnapShotCommentMapper snapShotCommentMapper;

    @Override
    public Result publishComment(SnapShotComment comment) {
        try {
            // 验证必要字段
            if (comment.getSsmid() == null || comment.getSsmid() <= 0) {
                return Result.error("随手拍ID不能为空");
            }
            if (StringUtils.isEmpty(comment.getDescription())) {
                return Result.error("评论内容不能为空");
            }

            // 获取当前用户ID
            Long currentUserId = SecurityUtils.getUserId();
            comment.setUserId(currentUserId.intValue());
            
            // 设置默认值
            comment.setStatus(0); // 待审核
            comment.setLikeCount(0);
            comment.setCreatedAt(new Date());
            
            int result = snapShotCommentMapper.insert(comment);
            
            if (result > 0) {
                return Result.success("评论成功，等待审核");
            } else {
                return Result.error("评论失败");
            }
        } catch (Exception e) {
            return Result.error("评论失败：" + e.getMessage());
        }
    }

    @Override
    public Result getCommentList(Integer ssmid, Integer pageNum, Integer pageSize) {
        try {
            if (ssmid == null || ssmid <= 0) {
                return Result.error("随手拍ID不能为空");
            }
            
            Page<SnapShotComment> page = new Page<>(pageNum, pageSize);
            QueryWrapper<SnapShotComment> queryWrapper = new QueryWrapper<>();
            
            // 按随手拍ID查询
            queryWrapper.eq("ssmid", ssmid);
            // 只查询已通过审核的评论
            queryWrapper.eq("status", 1);
            // 按创建时间升序
            queryWrapper.orderByAsc("created_at");
            
            Page<SnapShotComment> resultPage = snapShotCommentMapper.selectPage(page, queryWrapper);
            
            Result result = Result.success("查询成功");
            result.put("list", resultPage.getRecords());
            result.put("total", resultPage.getTotal());
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
            result.put("pages", resultPage.getPages());
            return result;
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Result auditComment(Integer id, Integer status) {
        try {
            if (id == null || id <= 0) {
                return Result.error("评论ID不能为空");
            }
            if (status == null || (status != 1 && status != 2)) {
                return Result.error("审核状态只能为1（通过）或2（驳回）");
            }
            
            SnapShotComment comment = snapShotCommentMapper.selectById(id);
            if (comment == null) {
                return Result.error("评论不存在");
            }
            
            comment.setStatus(status);
            comment.setUpdatedAt(new Date());
            
            int result = snapShotCommentMapper.updateById(comment);
            
            if (result > 0) {
                String statusText = status == 1 ? "通过" : "驳回";
                return Result.success("审核" + statusText + "成功");
            } else {
                return Result.error("审核失败");
            }
        } catch (Exception e) {
            return Result.error("审核失败：" + e.getMessage());
        }
    }

    @Override
    public Result deleteComment(Integer id) {
        try {
            if (id == null || id <= 0) {
                return Result.error("评论ID不能为空");
            }
            
            SnapShotComment comment = snapShotCommentMapper.selectById(id);
            if (comment == null) {
                return Result.error("评论不存在");
            }
            
            // 检查权限：只有作者或管理员可以删除
            Long currentUserId = SecurityUtils.getUserId();
            String currentUserRole = SecurityUtils.getLoginUser().getUser().getRoleId();
            
            if (!currentUserId.equals(comment.getUserId().longValue()) && 
                !"1".equals(currentUserRole) && !"2".equals(currentUserRole)) {
                return Result.error("没有权限删除该评论");
            }
            
            int result = snapShotCommentMapper.deleteById(id);
            
            if (result > 0) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @Override
    public Result likeComment(Integer id) {
        try {
            if (id == null || id <= 0) {
                return Result.error("评论ID不能为空");
            }
            
            SnapShotComment comment = snapShotCommentMapper.selectById(id);
            if (comment == null) {
                return Result.error("评论不存在");
            }
            
            // 点赞数+1
            comment.setLikeCount(comment.getLikeCount() + 1);
            comment.setUpdatedAt(new Date());
            
            int result = snapShotCommentMapper.updateById(comment);
            
            if (result > 0) {
                return Result.success("点赞成功");
            } else {
                return Result.error("点赞失败");
            }
        } catch (Exception e) {
            return Result.error("点赞失败：" + e.getMessage());
        }
    }
}




