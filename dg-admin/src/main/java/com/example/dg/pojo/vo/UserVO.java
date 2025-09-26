package com.example.dg.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xinxin
 * @version 1.0
 */
@Schema(description = "用户视图类")
@Data
public class UserVO {
    @Schema(description = "用户ID")
    private Integer userId;
    private Integer id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户头像")
    private String avatar;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "个人简介")
    private String profile;
    @Schema(description = "用户角色")
    private String role;
    @Schema(description = "性别")
    private String gender;
}
