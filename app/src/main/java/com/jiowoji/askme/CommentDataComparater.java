package com.jiowoji.askme;

import java.util.Comparator;

public class CommentDataComparater implements Comparator<CommentData> {
    @Override
    public int compare(CommentData a,CommentData b){
        if(a.getCommentNumber() > b.getCommentNumber()) return 1;
        if(a.getCommentNumber() < b.getCommentNumber()) return -1;
        return 0;
    }
}
