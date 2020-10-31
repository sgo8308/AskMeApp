package com.example.firstapp;

import android.view.View;

public interface OnPostsItemClickListener {

    public void onItemClick(PostsAdapter.ViewHolder holder , View view, int position);
}
