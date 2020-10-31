package com.example.firstapp;

import java.util.Comparator;

public class PeopleCommentDataComparator implements Comparator<PeopleCommentData> {
    @Override
    public int compare(PeopleCommentData a,PeopleCommentData b){
        if(a.getCommentNumber() > b.getCommentNumber()) return 1;
        if(a.getCommentNumber() < b.getCommentNumber()) return -1;
        return 0;
    }
}
