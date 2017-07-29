package com.yyyclj.facescore.bean;

/**
 * Created by 姚尧 on 2017/7/28.
 */

public class FaceInfo {
    private String face_token;
    private FacePositionInfo face_rectangle;
    private Object landmark;
    private AttributesInfo attributes;

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public FacePositionInfo getFace_rectangle() {
        return face_rectangle;
    }

    public void setFace_rectangle(FacePositionInfo face_rectangle) {
        this.face_rectangle = face_rectangle;
    }

    public Object getLandmark() {
        return landmark;
    }

    public void setLandmark(Object landmark) {
        this.landmark = landmark;
    }

    public AttributesInfo getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesInfo attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "FaceInfo{" +
                "face_token='" + face_token + '\'' +
                ", face_rectangle=" + face_rectangle +
                ", landmark=" + landmark +
                ", attributes=" + attributes +
                '}';
    }
}
