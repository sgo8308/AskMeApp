package com.jiowoji.askme;

public class ExPeriencePostData {
    String name;
    String job;
    String detail;

    public ExPeriencePostData(String name, String job, String detail) {
        this.name = name;
        this.job = job;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
