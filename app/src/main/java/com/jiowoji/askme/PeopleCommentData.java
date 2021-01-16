package com.jiowoji.askme;

public class PeopleCommentData {
    String id;
    String nickName;
    String job;
    String detail;
    long time;
    int likeCount;
    String ownerId;
    int commentNumber;
    String profileImage;

    String nickNamePosted;
    String jobPosted;
    String detailPosted;
    long timePosted;
    int likeCountPosted;
    String profileImagePosted;


    public PeopleCommentData(String id, String nickName, String job, String detail, long time, int likeCount, String ownerId, int commentNumber, String profileImage, String nickNamePosted, String jobPosted, String detailPosted, long timePosted, int likeCountPosted, String profileImagePosted) {
        this.id = id;
        this.nickName = nickName;
        this.job = job;
        this.detail = detail;
        this.time = time;
        this.likeCount = likeCount;
        this.ownerId = ownerId;
        this.commentNumber = commentNumber;
        this.profileImage = profileImage;
        this.nickNamePosted = nickNamePosted;
        this.jobPosted = jobPosted;
        this.detailPosted = detailPosted;
        this.timePosted = timePosted;
        this.likeCountPosted = likeCountPosted;
        this.profileImagePosted = profileImagePosted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getNickNamePosted() {
        return nickNamePosted;
    }

    public void setNickNamePosted(String nickNamePosted) {
        this.nickNamePosted = nickNamePosted;
    }

    public String getJobPosted() {
        return jobPosted;
    }

    public void setJobPosted(String jobPosted) {
        this.jobPosted = jobPosted;
    }

    public String getDetailPosted() {
        return detailPosted;
    }

    public void setDetailPosted(String detailPosted) {
        this.detailPosted = detailPosted;
    }

    public long getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(long timePosted) {
        this.timePosted = timePosted;
    }

    public int getLikeCountPosted() {
        return likeCountPosted;
    }

    public void setLikeCountPosted(int likeCountPosted) {
        this.likeCountPosted = likeCountPosted;
    }

    public String getProfileImagePosted() {
        return profileImagePosted;
    }

    public void setProfileImagePosted(String profileImagePosted) {
        this.profileImagePosted = profileImagePosted;
    }
}
