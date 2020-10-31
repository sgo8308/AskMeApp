package com.example.firstapp;

import java.util.Comparator;

public class PostsDataComparator implements Comparator<PostsData> {
    @Override
    public int compare(PostsData a,PostsData b){
        if(a.getPostNumber() > b.getPostNumber()) return -1;
        if(a.getPostNumber() < b.getPostNumber()) return 1;
        return 0;
    }
}
