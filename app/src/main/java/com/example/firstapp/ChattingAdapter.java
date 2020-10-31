package com.example.firstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ChattingAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ChattingData> chattingDatas;

    public ChattingAdapter(Context mContext, ArrayList<ChattingData> chattingDatas) {
        this.mContext = mContext;
        this.chattingDatas = chattingDatas;
        mLayoutInflater = LayoutInflater.from(mContext);;
    }

    @Override
    public int getCount() {
        return chattingDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return chattingDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_chatting, null);


        TextView myText = (TextView)view.findViewById(R.id.myText);
        myText.setText(chattingDatas.get(position).getMyText());

        return view;
    }
}
