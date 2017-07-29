package com.yyyclj.facescore.utils;

/**
 * Created by 姚尧 on 2017/7/28.
 */

public class Constants {
    //设置两个之前获取的两个常量
    public static final String Key = "DTlpe1CiLT9GrZJ749P7FR2MVdU8eH2d";
    public static final String Secret = "zSfr2_LnY0pJhyp0CY6iq4SyOXBXjpsv";
    //检测人脸url
    public static final String URL_DETECT = "https://api-cn.faceplusplus.com/facepp/v3/detect";
    public static final String IMG_FILE = "image_file";
    //选择的参数
    public static final String ATTRIBUTES = "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity";

    //并发数最大
    public static final String CONCURRENCY_LIMIT_EXCEEDED = "CONCURRENCY_LIMIT_EXCEEDED";
}
