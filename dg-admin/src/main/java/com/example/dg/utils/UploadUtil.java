package com.example.dg.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author xinxin
 * @version 1.0
 */
public class UploadUtil {
    public static String getLocalPath(MultipartFile multipartFile, String webUtilsValue) throws IOException {
        String uuid = UUID.randomUUID().toString();
        long l = System.currentTimeMillis();
        String path = ResourceUtils.getURL("classpath:").getPath();
//        File file = new File(path + WebUtils2.getUploadAvatarDirectory());
        File file = new File(path + webUtilsValue);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = null;
        String realFileName = null;
        if (!multipartFile.isEmpty()) {
            String originalFilename = multipartFile.getOriginalFilename();
            realFileName = uuid + "_" + l + "_" + originalFilename;
            file1 = new File(file, realFileName);
            multipartFile.transferTo(file1);
        }
//        String s = WebUtils2.getUploadAvatarDirectory().replace("static/", "") + "/" + realFileName;
        String s = webUtilsValue.replace("static/", "") + "/" + realFileName;
        return s;
    }
}
