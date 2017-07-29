package com.yyyclj.facescore.bean;

/**
 * Created by 姚尧 on 2017/7/28.
 * 特征点
 */

public class AttributesInfo {
    private Object gender;
    private Object age;
    private Object smiling;
    private Object eyestatus;
    private Object headpose;
    private Object blur;
    private Object emotion;
    private Object facequality;
    private Object ethnicity; //人种

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getAge() {
        return age;
    }

    public void setAge(Object age) {
        this.age = age;
    }

    public Object getSmiling() {
        return smiling;
    }

    public void setSmiling(Object smiling) {
        this.smiling = smiling;
    }

    public Object getEyestatus() {
        return eyestatus;
    }

    public void setEyestatus(Object eyestatus) {
        this.eyestatus = eyestatus;
    }

    public Object getHeadpose() {
        return headpose;
    }

    public void setHeadpose(Object headpose) {
        this.headpose = headpose;
    }

    public Object getBlur() {
        return blur;
    }

    public void setBlur(Object blur) {
        this.blur = blur;
    }

    public Object getEmotion() {
        return emotion;
    }

    public void setEmotion(Object emotion) {
        this.emotion = emotion;
    }

    public Object getFacequality() {
        return facequality;
    }

    public void setFacequality(Object facequality) {
        this.facequality = facequality;
    }

    public Object getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Object ethnicity) {
        this.ethnicity = ethnicity;
    }

    @Override
    public String toString() {
        return "AttributesInfo{" +
                "gender='" + gender + '\'' +
                ", age=" + age +
                ", smiling=" + smiling +
                ", eyestatus=" + eyestatus +
                ", headpose=" + headpose +
                ", blur=" + blur +
                ", emotion=" + emotion +
                ", facequality=" + facequality +
                ", ethnicity='" + ethnicity + '\'' +
                '}';
    }
}
