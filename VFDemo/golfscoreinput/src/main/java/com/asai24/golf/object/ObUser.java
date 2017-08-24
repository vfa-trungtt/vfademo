package com.asai24.golf.object;

import java.util.ArrayList;

/**
 * Created by huynq on 9/27/16.
 */
public class ObUser {
    String first_name;
    String last_name;
    String birthday;
    String gender;
    String country;
    String prefecture;
    String member_status;
    String payment_coupon_segment;
    String nickname;
    String publish_profile;
    String header_image_path;
    String avatar_image_path;
    ArrayList<String> purchase_status;

    public ObUser(String first_name) {
        this.first_name = first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public void setMember_status(String member_status) {
        this.member_status = member_status;
    }

    public void setPayment_coupon_segment(String payment_coupon_segment) {
        this.payment_coupon_segment = payment_coupon_segment;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPublish_profile(String publish_profile) {
        this.publish_profile = publish_profile;
    }

    public void setHeader_image_path(String header_image_path) {
        this.header_image_path = header_image_path;
    }

    public void setAvatar_image_path(String avatar_image_path) {
        this.avatar_image_path = avatar_image_path;
    }

    public void setPurchase_status(ArrayList<String> purchase_status) {
        this.purchase_status = purchase_status;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public String getMember_status() {
        return member_status;
    }

    public String getPayment_coupon_segment() {
        return payment_coupon_segment;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPublish_profile() {
        return publish_profile;
    }

    public String getHeader_image_path() {
        return header_image_path;
    }

    public String getAvatar_image_path() {
        return avatar_image_path;
    }

    public ArrayList<String> getPurchase_status() {
        return purchase_status;
    }
}
