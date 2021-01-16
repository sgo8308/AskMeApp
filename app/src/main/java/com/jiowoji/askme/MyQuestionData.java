package com.jiowoji.askme;

public class MyQuestionData {
    String question;
    String title;
    String id;
    String nickName;
    String job;

    public MyQuestionData(String question,String nickName, String title, String id, String job) {
        this.question = question;
        this.title = title;
        this.id = id;
        this.nickName = nickName;
        this.job = job;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
