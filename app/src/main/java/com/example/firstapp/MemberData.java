package com.example.firstapp;

public class MemberData {

    String id;
    String passWord;
    String profilePhoto;
    String introduction;
    String job;
    String nickName;
    int likeCount;
    int commentCount;
    int viewCount;
    int coinCount;
    boolean profileSetting;

    public MemberData(String id,String nickName, String passWord, String profilePhoto, String introduction, String job, int likeCount, int commentCount, int viewCount, int coinCount, boolean profileSetting) {
        this.id = id;
        this.nickName = nickName;
        this.passWord = passWord;
        this.profilePhoto = profilePhoto;
        this.introduction = introduction;
        this.job = job;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.coinCount = coinCount;
        this.profileSetting = profileSetting;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public boolean isProfileSetting() {
        return profileSetting;
    }

    public void setProfileSetting(boolean profileSetting) {
        this.profileSetting = profileSetting;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getNickName() { return nickName; }

    public void setNickName(String nickName) { this.nickName = nickName; }
}
