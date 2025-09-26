package com.example.dg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xinxin
 * @version 1.0
 * @Date 2024/11/29 0:16
 */ 
public class WebUtils2 {
    //定义一个用户头像上传的路径
    public static String UPLOAD_AVATAR_DIRECTORY = "static/avatar/";
    //定义一个反馈图片上传的路径
    public static String UPLOAD_Feedback_DIRECTORY = "static/feedback/";
    //定义一个项目图片上传的路径
    public static String UPLOAD_ITEM_DIRECTORY = "static/item/";
    //定义一个活动图片上传的路径
    public static String UPLOAD_EVENT_DIRECTORY = "static/event/";


    public static String getUploadAvatarDirectory() {
        return UPLOAD_AVATAR_DIRECTORY + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    public static String getUploadFeedbackDirectory() {
        return UPLOAD_Feedback_DIRECTORY + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    public static String getUploadItemDirectory() {
        return UPLOAD_ITEM_DIRECTORY + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    public static String getUploadEventDirectory() {
        return UPLOAD_EVENT_DIRECTORY + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
}
