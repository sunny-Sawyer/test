package com.example.dg.mapper;

import com.example.dg.utils.WebUtils2;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * @author xinxin
 * @version 1.0
 */
public class TestFile {
    public static void main(String[] args) throws FileNotFoundException {
        String path = ResourceUtils.getURL("classpath:").getPath();
        String uploadFileDirectory = WebUtils2.getUploadAvatarDirectory();
        String replace = uploadFileDirectory.replace("static/", "");
        System.out.println(replace);
    }
}
