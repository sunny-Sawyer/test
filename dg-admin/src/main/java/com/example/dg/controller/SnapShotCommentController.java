package com.example.dg.controller;

import com.example.dg.pojo.entity.SnapShotComment;
import com.example.dg.result.Result;
import com.example.dg.service.SnapShotCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 随手拍评论控制器
 * @author xinxin
 * @version 1.0
 */
@Tag(name = "随手拍评论管理")
@RestController
@RequestMapping("/snapshot/comment")
@Slf4j
public class SnapShotCommentController {
    
    @Resource
    private SnapShotCommentService snapShotCommentService;

    @Operation(summary = "发布评论")
    @PostMapping("/publish")
    public Result publishComment(@RequestHeader("Authorization") String token,
                                @RequestBody SnapShotComment comment) {
        return snapShotCommentService.publishComment(comment);
    }

    @Operation(summary = "获取随手拍的评论列表")
    @GetMapping("/list/{ssmid}")
    public Result getCommentList(@PathVariable Integer ssmid,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        return snapShotCommentService.getCommentList(ssmid, pageNum, pageSize);
    }

    @Operation(summary = "点赞评论")
    @PostMapping("/like/{id}")
    public Result likeComment(@RequestHeader("Authorization") String token,
                             @PathVariable Integer id) {
        return snapShotCommentService.likeComment(id);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/delete/{id}")
    public Result deleteComment(@RequestHeader("Authorization") String token,
                               @PathVariable Integer id) {
        return snapShotCommentService.deleteComment(id);
    }

    @Operation(summary = "审核评论")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @PostMapping("/audit")
    public Result auditComment(@RequestHeader("Authorization") String token,
                              @RequestParam Integer id,
                              @RequestParam Integer status) {
        return snapShotCommentService.auditComment(id, status);
    }

    @Operation(summary = "管理员查询所有评论（包括待审核）")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @GetMapping("/admin/list")
    public Result getAdminCommentList(@RequestHeader("Authorization") String token,
                                     @RequestParam(required = false) Integer ssmid,
                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) Integer status) {
        // 管理员可以查看所有状态的评论
        return snapShotCommentService.getCommentList(ssmid, pageNum, pageSize);
    }
}
