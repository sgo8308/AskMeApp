package com.jiowoji.askme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyQuestionAdapter extends RecyclerView.Adapter<MyQuestionAdapter.ViewHolder> {


    ArrayList<CommentData> commentDatas = new ArrayList<CommentData>();
    OnMyQuestionItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_my_question,viewGroup,false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CommentData commentData = commentDatas.get(position);
        viewHolder.setItem(commentData);
    }

    @Override
    public int getItemCount() {
        return commentDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView idPosted;
        TextView postTitle;
        TextView jobPosted;
        TextView question;
        public ViewHolder(View itemView, final OnMyQuestionItemClickListener listner){
            super(itemView);

            idPosted = itemView.findViewById(R.id.text_id);
            postTitle = itemView.findViewById(R.id.text_title);
            jobPosted = itemView.findViewById(R.id.text_job);
            question = itemView.findViewById(R.id.text_question);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();// 몇번쟤 뷰인지

                    if(listner !=null){
                        listner.onItemClick(ViewHolder.this,v,position);
                        //왜 this를 붙히지? -> 그냥 this는 OnCickListener라는 클래스를 가리키고
                        // ViewHolder.this라고하면 이 클래스의 상위 객체인 ViewHolder 클래스를 가리침
                    }
                }
            });
        }

        public void setItem(CommentData commentData){
            idPosted.setText(commentData.getNickNamePosted());
            jobPosted.setText(commentData.getJobPosted());
            postTitle.setText(commentData.getPostTitle());
            question.setText(commentData.getDetail());
        }
    }

    public void addItem(CommentData commentData){
        commentDatas.add(0,commentData);
    }

    public void setCommentDatas(ArrayList<CommentData> commentDatas){
        this.commentDatas = commentDatas;
    }

    public CommentData getItem(int position){
        return commentDatas.get(position);
    }

    public void setItem(int position, CommentData commentData){ commentDatas.set(position,commentData); }

    public void deleteItem(int position){
        commentDatas.remove(position);
    }

    public void setOnItemClickListener(OnMyQuestionItemClickListener listener){
        this.listener = listener;
    }




}
