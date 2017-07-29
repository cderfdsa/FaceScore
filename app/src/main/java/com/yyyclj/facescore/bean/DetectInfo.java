package com.yyyclj.facescore.bean;

import java.util.List;

/**
 * Created by 姚尧 on 2017/7/28.
 */

public class DetectInfo {
    private String request_id;
    private List<Object> faces;
    private String image_id;
    private Integer time_used;
    private String error_message;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public List<Object> getFaces() {
        return faces;
    }

    public void setFaces(List<Object> faces) {
        this.faces = faces;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public Integer getTime_used() {
        return time_used;
    }

    public void setTime_used(Integer time_used) {
        this.time_used = time_used;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    @Override
    public String toString() {
        return "DetectInfo{" +
                "request_id='" + request_id + '\'' +
                ", faces=" + faces +
                ", image_id='" + image_id + '\'' +
                ", time_used=" + time_used +
                ", error_message='" + error_message + '\'' +
                '}';
    }
}
