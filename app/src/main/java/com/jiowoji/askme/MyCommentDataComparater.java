package com.jiowoji.askme;

import java.util.Comparator;

public class MyCommentDataComparater implements Comparator<CommentData> {
    @Override
    public int compare(CommentData a,CommentData b){
        if(a.getCommentNumber() > b.getCommentNumber()) return -1;
        if(a.getCommentNumber() < b.getCommentNumber()) return 1;
        return 0;
    }
}
