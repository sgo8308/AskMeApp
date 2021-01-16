package com.jiowoji.askme;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {
    //아이템의 데이터를 담고 있는 클래스를 담아두는 리스트를 생성한다.
    ArrayList<PostsData> postsDatas = new ArrayList<PostsData>();
    //따로 정의한 OnTestItemClickListener라는 인터페이스의 변수를 만든다.
    OnMyPostsItemClickListener listener;

    @NonNull
    @Override
    //뷰홀더가 새로 만들어지는 시점에 호출된다. 레이아웃인플레이터를 통해 아이템의 레이아웃을 객체화 시켜준 후 새 뷰홀더에 담아 반환해준다.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_mypost,viewGroup,false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    //뷰홀더가 재사용되는 시점에 호출된다. 화면에 새롭게 보이게 되는 아이템의 포지션을 참조한 후 뷰홀더에서 아이템의 데이터를 세팅해준다.
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PostsData postsData = postsDatas.get(position);
        viewHolder.setItem(postsData);
        Log.i("알림",String.valueOf(position));
    }
    @Override
    public int getItemCount() {
        return postsDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        TextView viewCount;
        TextView likeCount;
        TextView commentCount;

        public ViewHolder(View itemView, final OnMyPostsItemClickListener listner){
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            time = itemView.findViewById(R.id.text_time);
//            viewCount = itemView.findViewById(R.id.text_viewCount);
//            likeCount = itemView.findViewById(R.id.text_likeCount);
            commentCount = itemView.findViewById(R.id.text_commentCount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listner !=null){
                        listner.onItemClick(ViewHolder.this,v,position);
                    }
                }
            });
        }
        public void setItem(PostsData item){
            title.setText(item.getTitle());
//            viewCount.setText(Integer.toString(item.getViewCount()));
//            likeCount.setText(Integer.toString(item.getLikeCount()));
            commentCount.setText(Integer.toString(item.getCommentCount()));
            TimeString timeString = new TimeString();
            time.setText(timeString.formatTimeString(item.getTime()));

        }
    }

    public void addItem(PostsData item){
        postsDatas.add(item);
    }

    public PostsData getItem(int position){
        return postsDatas.get(position);
    }

    public void setOnItemClickListener(OnMyPostsItemClickListener listener){
        this.listener = listener;
    }


    public void setPostsDatas(ArrayList<PostsData> postsDatas){
        this.postsDatas = postsDatas;
    }

    public void setItem(int position, PostsData item){ postsDatas.set(position,item); }

    public void deleteItem(int position){
        postsDatas.remove(position);
    }

    public void editItem(int position,PostsData postsData){

        postsDatas.get(position).setTitle(postsData.getTitle());

    }
    
    
}
