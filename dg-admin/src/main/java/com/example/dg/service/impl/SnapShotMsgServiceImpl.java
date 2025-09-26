package com.example.dg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dg.common.utils.SecurityUtils;
import com.example.dg.common.utils.StringUtils;
import com.example.dg.mapper.SnapShotMsgMapper;
import com.example.dg.pojo.entity.SnapShotMsg;
import com.example.dg.result.Result;
import com.example.dg.service.SnapShotMsgService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
* @author 26030
* @description 针对表【snap_shot_msg(随手拍信息)】的数据库操作Service实现
* @createDate 2025-09-17 12:18:29
*/
@Service
public class SnapShotMsgServiceImpl extends ServiceImpl<SnapShotMsgMapper, SnapShotMsg>
    implements SnapShotMsgService {

    @Resource
    private SnapShotMsgMapper snapShotMsgMapper;

    @Override
    public Result publishSnapShot(SnapShotMsg snapShotMsg, MultipartFile[] images) {
        try {
            // 验证必要字段
            if (StringUtils.isEmpty(snapShotMsg.getDescription())) {
                return Result.error("描述内容不能为空");
            }

            // 获取当前用户ID
            Long currentUserId = SecurityUtils.getUserId();
            snapShotMsg.setUserId(currentUserId.intValue());
            
            // 处理图片上传
            if (images != null && images.length > 0) {
                String imagePaths = uploadImagesToOSS(images);
                snapShotMsg.setImagePaths(imagePaths);
            }
            
            // 设置默认值
            snapShotMsg.setStatus(0); // 待审核
            snapShotMsg.setLikeCount(0);
            snapShotMsg.setCreatedAt(new Date());
            
            int result = snapShotMsgMapper.insert(snapShotMsg);
            
            if (result > 0) {
                return Result.success("发布成功，等待审核");
            } else {
                return Result.error("发布失败");
            }
        } catch (Exception e) {
            return Result.error("发布失败：" + e.getMessage());
        }
    }

    @Override
    public Result getSnapShotList(Integer pageNum, Integer pageSize, Integer status, Integer userId) {
        try {
            Page<SnapShotMsg> page = new Page<>(pageNum, pageSize);
            QueryWrapper<SnapShotMsg> queryWrapper = new QueryWrapper<>();
            
            // 按状态筛选
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            // 按用户ID筛选
            if (userId != null) {
                queryWrapper.eq("user_id", userId);
            }
            
            // 按创建时间降序
            queryWrapper.orderByDesc("created_at");
            
            Page<SnapShotMsg> resultPage = snapShotMsgMapper.selectPage(page, queryWrapper);
            
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
    public Result auditSnapShot(Integer id, Integer status) {
        try {
            if (id == null || id <= 0) {
                return Result.error("随手拍ID不能为空");
            }
            if (status == null || (status != 1 && status != 2)) {
                return Result.error("审核状态只能为1（通过）或2（驳回）");
            }
            
            SnapShotMsg snapShotMsg = snapShotMsgMapper.selectById(id);
            if (snapShotMsg == null) {
                return Result.error("随手拍不存在");
            }
            
            snapShotMsg.setStatus(status);
            snapShotMsg.setUpdatedAt(new Date());
            
            int result = snapShotMsgMapper.updateById(snapShotMsg);
            
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
    public Result deleteSnapShot(Integer id) {
        try {
            if (id == null || id <= 0) {
                return Result.error("随手拍ID不能为空");
            }
            
            SnapShotMsg snapShotMsg = snapShotMsgMapper.selectById(id);
            if (snapShotMsg == null) {
                return Result.error("随手拍不存在");
            }
            
            // 检查权限：只有作者或管理员可以删除
            Long currentUserId = SecurityUtils.getUserId();
            String currentUserRole = SecurityUtils.getLoginUser().getUser().getRoleId();
            
            if (!currentUserId.equals(snapShotMsg.getUserId().longValue()) && 
                !"1".equals(currentUserRole) && !"2".equals(currentUserRole)) {
                return Result.error("没有权限删除该随手拍");
            }
            
            int result = snapShotMsgMapper.deleteById(id);
            
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
    public Result likeSnapShot(Integer id) {
        try {
            if (id == null || id <= 0) {
                return Result.error("随手拍ID不能为空");
            }
            
            SnapShotMsg snapShotMsg = snapShotMsgMapper.selectById(id);
            if (snapShotMsg == null) {
                return Result.error("随手拍不存在");
            }
            
            // 点赞数+1
            snapShotMsg.setLikeCount(snapShotMsg.getLikeCount() + 1);
            snapShotMsg.setUpdatedAt(new Date());
            
            int result = snapShotMsgMapper.updateById(snapShotMsg);
            
            if (result > 0) {
                return Result.success("点赞成功");
            } else {
                return Result.error("点赞失败");
            }
        } catch (Exception e) {
            return Result.error("点赞失败：" + e.getMessage());
        }
    }

    @Override
    public Result getSnapShotDetail(Integer id) {
        try {
            if (id == null || id <= 0) {
                return Result.error("随手拍ID不能为空");
            }
            
            SnapShotMsg snapShotMsg = snapShotMsgMapper.selectById(id);
            if (snapShotMsg == null) {
                return Result.error("随手拍不存在");
            }
            
            Result result = Result.success("查询成功");
            result.put("snapShot", snapShotMsg);
            return result;
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 上传图片到OSS
     * 
     * @param images 图片文件数组
     * @return 图片路径字符串（逗号分隔）
     */
    private String uploadImagesToOSS(MultipartFile[] images) {
        // TODO: 实现OSS上传逻辑
        // 这里暂时返回模拟的OSS路径
        // 实际开发中需要集成阿里云OSS SDK
        
        List<String> paths = Arrays.stream(images)
            .map(image -> {
                // 模拟生成OSS路径
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                return "https://your-bucket.oss-region.aliyuncs.com/snapshots/" + fileName;
            })
            .collect(Collectors.toList());
            
        return String.join(",", paths);
    }
}




