package com.example.dg.controller;

import com.example.dg.pojo.entity.SnapShotMsg;
import com.example.dg.result.Result;
import com.example.dg.service.SnapShotMsgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 随手拍信息控制器
 * @author xinxin
 * @version 1.0
 */
@Tag(name = "随手拍管理")
@RestController
@RequestMapping("/snapshot")
@Slf4j
public class SnapShotMsgController {
    
    @Resource
    private SnapShotMsgService snapShotMsgService;

    @Operation(summary = "发布随手拍")
    @PostMapping("/publish")
    public Result publishSnapShot(@RequestHeader("Authorization") String token,
                                 @RequestParam("description") String description,
                                 @RequestParam(value = "images", required = false) MultipartFile[] images) {
        SnapShotMsg snapShotMsg = new SnapShotMsg();
        snapShotMsg.setDescription(description);
        return snapShotMsgService.publishSnapShot(snapShotMsg, images);
    }

    @Operation(summary = "分页查询随手拍列表")
    @GetMapping("/list")
    public Result getSnapShotList(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) Integer status,
                                 @RequestParam(required = false) Integer userId) {
        return snapShotMsgService.getSnapShotList(pageNum, pageSize, status, userId);
    }

    @Operation(summary = "获取随手拍详情")
    @GetMapping("/detail/{id}")
    public Result getSnapShotDetail(@PathVariable Integer id) {
        return snapShotMsgService.getSnapShotDetail(id);
    }

    @Operation(summary = "点赞随手拍")
    @PostMapping("/like/{id}")
    public Result likeSnapShot(@RequestHeader("Authorization") String token,
                              @PathVariable Integer id) {
        return snapShotMsgService.likeSnapShot(id);
    }

    @Operation(summary = "删除随手拍")
    @DeleteMapping("/delete/{id}")
    public Result deleteSnapShot(@RequestHeader("Authorization") String token,
                                @PathVariable Integer id) {
        return snapShotMsgService.deleteSnapShot(id);
    }

    @Operation(summary = "审核随手拍")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @PostMapping("/audit")
    public Result auditSnapShot(@RequestHeader("Authorization") String token,
                               @RequestParam Integer id,
                               @RequestParam Integer status) {
        return snapShotMsgService.auditSnapShot(id, status);
    }

    @Operation(summary = "管理员分页查询随手拍列表")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @GetMapping("/admin/list")
    public Result getAdminSnapShotList(@RequestHeader("Authorization") String token,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam(required = false) Integer userId) {
        return snapShotMsgService.getSnapShotList(pageNum, pageSize, status, userId);
    }
}
