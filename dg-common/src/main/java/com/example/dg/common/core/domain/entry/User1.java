//package com.example.dg.common.core.domain.entry;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Data;
//
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Pattern;
//import java.io.Serializable;
//import java.util.Date;
//
///**
// * @author xinxin
// * @version 1.0
// */
//@Data
//@TableName("user")
//@Schema(description = "用户实体类")
//public class User implements Serializable {
//    private static final long serialVersionUID = -40356785423868312L;
//
//    @TableId(type = IdType.AUTO)
//    @Schema(description = "用户ID")
//    private Integer userId;
//
//    @NotEmpty
//    @Schema(description = "用户名")
//    @Pattern(regexp = "^[a-zA-Z\\d_]{5,}$", message = "用户名必须包含至少5个字符，只能包含字母、数字和下划线")
//    private String username;
//
//    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Za-z]).{8,10}$")
//    @Schema(description = "密码，至少一位数字，一位大小写字母，字数8~10")
//    private String password;
//
//    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
//    @Schema(description = "邮箱")
//    private String email;
//
//    @Pattern(regexp = "^1[3-9]\\d{9}$")
//    @Schema(description = "手机号")
//    @NotEmpty(message = "手机号不能为空")
//    private String phone;
//
//    @Schema(description = "性别")
//    private Integer gender;
//
//    @Schema(description = "创建时间")
//    private Date createdAt;
//
//    @Schema(description = "更新时间")
//    private Date updatedAt;
//
//    @Schema(description = "用户角色")
//    private Integer role;
//
//    @Schema(description = "用户简介")
//    private String profile;
//
//    @Schema(description = "用户头像")
//    private String avatar;
//}
