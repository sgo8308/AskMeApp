package com.example.firstapp;

import android.view.View;

public interface OnMyPostsItemClickListener {

    public void onItemClick(MyPostsAdapter.ViewHolder holder , View view, int position);
}
