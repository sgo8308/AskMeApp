package com.example.firstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public String adapterName;
    ArrayList<PostsData> postsDatas ;
    OnPostsItemClickListener listener;
    HashMap<String, MemberData> memberDatas;
    String nowLogInId ;
    static Context mContext;
    MemberData memberData;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.i("알림","온크리에이트 뷰홀더");
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_post,viewGroup,false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.i("알림","바인드 뷰홀더 합니다 포스트 어댑터에서");
        PostsData item = postsDatas.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return postsDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userId;
        TextView postTitle;
        TextView job;
        TextView likeCount;
        TextView viewCount;
        TextView commentCount;
        TextView time;
        CircleImageView imageProfile;

        public ViewHolder(View itemView, final OnPostsItemClickListener listner){
            super(itemView);

            userId = itemView.findViewById(R.id.text_userId);
            postTitle = itemView.findViewById(R.id.text_title);
            job = itemView.findViewById(R.id.text_job);
//            likeCount = itemView.findViewById(R.id.text_likeCount);
//            viewCount = itemView.findViewById(R.id.text_viewCount);
            commentCount = itemView.findViewById(R.id.text_commentCount);
            time = itemView.findViewById(R.id.text_time);
            imageProfile = itemView.findViewById(R.id.image_profile);

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

        public void setItem(PostsData item){
            userId.setText(item.getNickName());
            postTitle.setText(item.getTitle());
            job.setText(item.getJob());
//            likeCount.setText(Integer.toString(item.getLikeCount()));
//            viewCount.setText(Integer.toString(item.getViewCount()));
            commentCount.setText(Integer.toString(item.getCommentCount()));

            HashMap<String,MemberData> memberDataHashMap = SharedPreferencesHandler.getMemberDataHashMap(mContext);
            MemberData memberData = memberDataHashMap.get(item.getId());

            if(!memberData.getProfilePhoto().equals("")){
                Glide
                        .with(mContext)
                        .load(Uri.parse(memberData.getProfilePhoto()))
                        .into(imageProfile);
            }
            TimeString timeString = new TimeString();
            time.setText(timeString.formatTimeString(item.getTime()));
        }
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void addItem(PostsData item){
        postsDatas.add(0,item);
    }

    public void setPostsDatas(ArrayList<PostsData> postsDatas){
        this.postsDatas = postsDatas;
    }

    public PostsData getItem(int position){
        return postsDatas.get(position);
    }

    public void setItem(int position, PostsData item){ postsDatas.set(position,item); }

    public void deleteItem(int position){
        postsDatas.remove(position);
    }

    public void editItem(int position,PostsData postsData){

        postsDatas.get(position).setTitle(postsData.getTitle());
        postsDatas.get(position).setDetail(postsData.getDetail());

    }
    public void setOnItemClickListener(OnPostsItemClickListener listener){
        this.listener = listener;
    }

    public ArrayList<PostsData> getItems(){
        return postsDatas;
    }

    public String getAdapterName() {
        return adapterName;
    }

    public void setAdapterName(String adapterName) {
        this.adapterName = adapterName;
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
