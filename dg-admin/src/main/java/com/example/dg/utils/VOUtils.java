//package com.example.museum.utils;
//
//import com.example.museum.entity.Feedback;
//import com.example.museum.entity.User;
//import com.example.museum.vo.FeedbackVO;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author xinxin
// * @version 1.0
// */
//public class VOUtils<T, K> {
//    private void setFeedbackVOS(ArrayList<T> listVO, List<K> list) {
//        for (K k : K) {
//            FeedbackVO feedbackVO = new FeedbackVO();
//            feedbackVO.setContent(feedback.getContent());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//            String createTime = sdf.format(feedback.getCreatedAt());
//            feedbackVO.setCreatedTime(createTime);
//            feedbackVO.setFeedbackType(feedback.getFeedbackType());
//            feedbackVO.setImagePath(feedback.getImagePath());
//            Integer userId = feedback.getUserId();
//            User user = userMapper.selectUserById(userId);
//            feedbackVO.setUsername(user.getUsername());
//            feedbackVOS.add(feedbackVO);
//        }
//    }
//}
