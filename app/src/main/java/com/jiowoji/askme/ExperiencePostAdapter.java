package com.jiowoji.askme;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExperiencePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    ArrayList<CommentData> mCommentDatas = new ArrayList<>() ;
    PostsData postsData;
    OnCommentMenuClickListener listenerCommentMenu;
    OnCommentAnswerClickListener listenerCommentAnswer;
    OnAnswerMenuClickListener listenerAnswerMenu;
    static String nowLogInId;
    static Context mContext;

    @NonNull
    @Override
    //뷰홀더가 새로 만들어지는 시점에 호출된다. 레이아웃인플레이터를 통해 아이템의 레이아웃을 객체화 시켜준 후 새 뷰홀더에 담아 반환해준다.
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView;
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_HEADER) {
            itemView = inflater.inflate(R.layout.header_experience_comment, viewGroup, false);
            holder = new HeaderViewHolder(itemView);
        } else {
            itemView = inflater.inflate(R.layout.item_comment, viewGroup, false);
            holder = new ViewHolder(itemView,listenerCommentMenu,listenerCommentAnswer,listenerAnswerMenu);
        }
        return holder;
    }

    @Override
    //뷰홀더가 재사용되는 시점에 호출된다. 화면에 새롭게 보이게 되는 아이템의 포지션을 참조한 후 뷰홀더에서 아이템의 데이터를 세팅해준다.
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.setHeader(postsData);
        }else {
            Log.i("알림","바인딩됨");
            ViewHolder viewHolder = (ViewHolder) holder;
            CommentData commentData = mCommentDatas.get(position - 1);
            viewHolder.setItem(commentData);
        }
    }
    //데이터 리스트의 갯수를 반환해준다. 리싸이클러뷰에서 어댑터에서 관리하는 아이템의 개수를 알아야 할 때 사용된다.
    @Override
    public int getItemCount() {
        Log.i("알림","겟아이템카운트 들어간다.");
        return mCommentDatas.size() + 1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView detail;
        TextView job;
        TextView title;
        TextView viewCount;
        TextView commentCount;
        TextView time;
        TextView likeCount;
        CircleImageView profileImage;

        public HeaderViewHolder(final View headerView){
            super(headerView);
            name = headerView.findViewById(R.id.text_id);
            detail = headerView.findViewById(R.id.text_introduction);
            job = headerView.findViewById(R.id.text_job);
            title = headerView.findViewById(R.id.text_title);
//            viewCount = headerView.findViewById(R.id.text_viewCount);
//            likeCount = headerView.findViewById(R.id.text_likeCount);
            time = headerView.findViewById(R.id.text_time);
            commentCount = headerView.findViewById(R.id.text_commentCount);
            profileImage = headerView.findViewById(R.id.image_profile);
        }
        public void setHeader(PostsData postsData){
            HashMap<String,MemberData> memberDatas = SharedPreferencesHandler.getMemberDataHashMap(mContext);
            String nickName = memberDatas.get(postsData.getId()).getNickName();
            String job1 = memberDatas.get(postsData.getId()).getJob();
            name.setText(nickName);
            detail.setText(postsData.getDetail());
            title.setText(postsData.getTitle());
            job.setText(job1);
//            viewCount.setText(Integer.toString(postsData.getViewCount()));
            commentCount.setText(Integer.toString(postsData.getCommentCount()));

            HashMap<String,MemberData> memberDataHashMap = SharedPreferencesHandler.getMemberDataHashMap(mContext);
            MemberData memberData = memberDataHashMap.get(postsData.getId());

            if(!memberData.getProfilePhoto().equals("")){
                Glide
                        .with(mContext)
                        .load(Uri.parse(memberData.getProfilePhoto()))
                        .into(profileImage);
            }
            TimeString timeString = new TimeString();
            time.setText(timeString.formatTimeString(postsData.getTime()));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    //뷰홀더 클래스를 만든다.
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView detail;
        TextView job;
        ImageButton menu;
        TextView time;
        CircleImageView profileImage;
        Button button_answer;

        TextView namePosted;
        TextView detailPosted;
        TextView jobPosted;
        ImageButton menuPosted;
        TextView timePosted;
        CircleImageView profileImagePosted;
        LinearLayout answerLayOut;

        public ViewHolder(final View itemView, final OnCommentMenuClickListener listnerComment, final OnCommentAnswerClickListener listenerCommentAnswer,final OnAnswerMenuClickListener listenerAnswerMenu){
            super(itemView);
            //댓글 작성자
            name = itemView.findViewById(R.id.text_name);
            detail = itemView.findViewById(R.id.text_introduction);
            job = itemView.findViewById(R.id.text_job);
            menu = itemView.findViewById(R.id.button_menu);
            time = itemView.findViewById(R.id.text_time);
            profileImage = itemView.findViewById(R.id.image_profile);
            button_answer = itemView.findViewById(R.id.button_answer);

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(),v);
                    MenuInflater menuInflater = new MenuInflater(itemView.getContext());
                    menuInflater.inflate(R.menu.post_menu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int position = getAdapterPosition();
                            if(listnerComment!=null){
                                listnerComment.onItemClick(item.getItemId(),position - 1);
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            button_answer = itemView.findViewById(R.id.button_answer);
            button_answer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listenerCommentAnswer!=null){
                        listenerCommentAnswer.onItemClick(position - 1);
                    }
                }
            });

            //답변자
            answerLayOut = itemView.findViewById(R.id.layOut_answer);
            namePosted = itemView.findViewById(R.id.text_name_posted);
            detailPosted = itemView.findViewById(R.id.text_answer);
            jobPosted = itemView.findViewById(R.id.text_job_posted);
            menuPosted = itemView.findViewById(R.id.button_menu_posted);
            timePosted = itemView.findViewById(R.id.text_time_posted);
            profileImagePosted = itemView.findViewById(R.id.image_profile_posted);

            menuPosted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(),v);
                    MenuInflater menuInflater = new MenuInflater(itemView.getContext());
                    menuInflater.inflate(R.menu.post_menu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int position = getAdapterPosition();
                            if(listenerAnswerMenu!=null){
                                listenerAnswerMenu.onItemClick(item.getItemId(),position - 1);
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        public void setItem(CommentData commentData){
            //댓글 작성자
            HashMap<String,MemberData> memberDatas = SharedPreferencesHandler.getMemberDataHashMap(mContext);
            String nickName = memberDatas.get(commentData.getId()).getNickName();
            String job1 = memberDatas.get(commentData.getId()).getJob();

            name.setText(nickName);
            detail.setText(commentData.getDetail());
            job.setText(job1);

            HashMap<String,MemberData> memberDataHashMap = SharedPreferencesHandler.getMemberDataHashMap(mContext);
            MemberData memberData = memberDataHashMap.get(commentData.getId());

            if(!memberData.getProfilePhoto().equals("")){
                Glide
                        .with(mContext)
                        .load(Uri.parse(memberData.getProfilePhoto()))
                        .into(profileImage);
            }
            TimeString timeString = new TimeString();
            time.setText(timeString.formatTimeString(commentData.getTime()));

            if(nowLogInId.equals(commentData.getId())){
                menu.setVisibility(View.VISIBLE);
            }else {
                menu.setVisibility(View.GONE);
            }

            //답변자
            //수정삭제 가려받기
            if(commentData.getDetailPosted().equals("")){
                answerLayOut.setVisibility(View.GONE);
                button_answer.setVisibility(View.VISIBLE);
            }else {
                answerLayOut.setVisibility(View.VISIBLE);
                button_answer.setVisibility(View.GONE);
            }

            if(commentData.getId().equals(commentData.getIdPosted())){
                button_answer.setVisibility(View.GONE);
            }

            if(nowLogInId.equals(commentData.getIdPosted())){
                menuPosted.setVisibility(View.VISIBLE);
            }else {
                menuPosted.setVisibility(View.GONE);
                button_answer.setVisibility(View.GONE);
            }

            String nickNamePosted = memberDatas.get(commentData.getIdPosted()).getJob();
            String job1Posted = memberDatas.get(commentData.getIdPosted()).getJob();
            namePosted.setText(nickNamePosted);
            detailPosted.setText(commentData.getDetailPosted());
            jobPosted.setText(job1Posted);

            MemberData memberDataPosted = memberDataHashMap.get(commentData.getIdPosted());
            if(!memberDataPosted.getProfilePhoto().equals("")){
                Glide
                        .with(mContext)
                        .load(Uri.parse(memberDataPosted.getProfilePhoto()))
                        .into(profileImagePosted);
            }
            timePosted.setText(timeString.formatTimeString(commentData.getTimePosted()));
        }
    }

    public void deleteItem(int position){
        mCommentDatas.remove(position );
    }

    public void editItem(int position,String editedDetail){
        mCommentDatas.get(position).setDetail(editedDetail);
    }

    public void addItem(CommentData commentData){
        mCommentDatas.add(commentData);
    }

    public CommentData getItem(int position){
        return mCommentDatas.get(position);
    }

    public String getCommentDetail(int position){return mCommentDatas.get(position).detail;};

    public void setOnCommentItemClickListener(OnCommentMenuClickListener listenerCommentMenu){
        this.listenerCommentMenu = listenerCommentMenu;
    }
    public void setOnCommentAnswerClickListener(OnCommentAnswerClickListener listenerCommentAnswer){
        this.listenerCommentAnswer = listenerCommentAnswer;
    }
    public void setOnAnswerMenuClickListener(OnAnswerMenuClickListener listenerAnswerMenu){
        this.listenerAnswerMenu = listenerAnswerMenu;
    }
    public void setCommentData(ArrayList<CommentData> mCommentDatas) {
        this.mCommentDatas = mCommentDatas;
    }

    public void setPostsdata(PostsData postsdata) {
        this.postsData = postsdata;
    }

    public void setContext(Context mContext){
        this.mContext = mContext;
    }

}
