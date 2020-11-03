package com.example.firstapp;

import java.io.Serializable;

public class PostsData implements Serializable {

    String id ;
    String nickName ;
    String title;
    String detail;
    String job;
    long time;
    String profileImage;
    int likeCount;
    int viewCount;
    int commentCount;
    int postNumber;


    public PostsData(String id, String nickName, String title, String detail, String job, long time, String profileImage, int likeCount, int viewCount, int commentCount, int postNumber) {
        this.id = id;
        this.nickName = nickName;
        this.title = title;
        this.detail = detail;
        this.job = job;
        this.time = time;
        this.profileImage = profileImage;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.postNumber = postNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPostNumber() {
        return postNumber;
    }
    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
