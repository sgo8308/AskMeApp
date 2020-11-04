package com.example.firstapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
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

public class PeoplePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    ArrayList<PeopleCommentData> mPeopleCommentDatas = new ArrayList<>() ;
    MemberData memberData;
    OnCommentMenuClickListener listenerComment;
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
            itemView = inflater.inflate(R.layout.header_people_comment, viewGroup, false);
            holder = new PeoplePostAdapter.HeaderViewHolder(itemView);
        } else {
            itemView = inflater.inflate(R.layout.item_comment, viewGroup, false);
            holder = new PeoplePostAdapter.ViewHolder(itemView,listenerComment,listenerCommentAnswer,listenerAnswerMenu);
        }
        return holder;
    }

    @Override
    //뷰홀더가 재사용되는 시점에 호출된다. 화면에 새롭게 보이게 되는 아이템의 포지션을 참조한 후 뷰홀더에서 아이템의 데이터를 세팅해준다.
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PeoplePostAdapter.HeaderViewHolder) {
            PeoplePostAdapter.HeaderViewHolder headerViewHolder = (PeoplePostAdapter.HeaderViewHolder) holder;
            headerViewHolder.setHeader(memberData);
        }else {
            Log.i("알림","바인딩됨");
            PeoplePostAdapter.ViewHolder viewHolder = (PeoplePostAdapter.ViewHolder) holder;
            PeopleCommentData peopleCommentData = mPeopleCommentDatas.get(position - 1);
            viewHolder.setItem(peopleCommentData);
        }
    }
    //데이터 리스트의 갯수를 반환해준다. 리싸이클러뷰에서 어댑터에서 관리하는 아이템의 개수를 알아야 할 때 사용된다.
    @Override
    public int getItemCount() {
        return mPeopleCommentDatas.size() + 1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView detail;
        TextView job;
        TextView viewCount;
        TextView commentCount;
        TextView likeCount;
        CircleImageView profileImage;

        public HeaderViewHolder(final View headerView){
            super(headerView);
            name = headerView.findViewById(R.id.text_id);
            detail = headerView.findViewById(R.id.text_introduction);
            job = headerView.findViewById(R.id.text_job);
//            viewCount = headerView.findViewById(R.id.text_viewCount);
//            likeCount = headerView.findViewById(R.id.text_likeCount);
            commentCount = headerView.findViewById(R.id.text_commentCount);
            profileImage = headerView.findViewById(R.id.image_profile);
        }

        public void setHeader(MemberData memberData){
            name.setText(memberData.getNickName());
            if(memberData.getIntroduction() != null){
                detail.setText(memberData.getIntroduction());
            }
            job.setText(memberData.getJob());
            commentCount.setText(Integer.toString(memberData.getCommentCount()));
            if(!memberData.getProfilePhoto().equals("")){
                Glide
                        .with(mContext)
                        .load(Uri.parse(memberData.getProfilePhoto()))
                        .into(profileImage);
            }
//            viewCount.setText(Integer.toString(memberData.getViewCount()));
//            likeCount.setText(Integer.toString(memberData.getLikeCount()));
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
        //댓글 작성자
        TextView name;
        TextView detail;
        TextView job;
        ImageButton menu;
        TextView time;
        CircleImageView profileImage;
        Button button_answer;

        //답변자
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
        public void setItem(PeopleCommentData peopleCommentData){
            //댓글 작성자
            name.setText(peopleCommentData.getNickName());
            detail.setText(peopleCommentData.getDetail());
            job.setText(peopleCommentData.getJob());

            HashMap<String,MemberData> memberDataHashMap = SharedPreferencesHandler.getMemberDataHashMap(mContext);
            MemberData memberData = memberDataHashMap.get(peopleCommentData.getId());

            if(!memberData.getProfilePhoto().equals("")){
                Glide
                        .with(mContext)
                        .load(Uri.parse(memberData.getProfilePhoto()))
                        .into(profileImage);
            }
            TimeString timeString = new TimeString();
            time.setText(timeString.formatTimeString(peopleCommentData.getTime()));

            if(nowLogInId.equals(peopleCommentData.getId())){
                menu.setVisibility(View.VISIBLE);
            }else {
                menu.setVisibility(View.GONE);
            }

            //답변자
            if(peopleCommentData.getDetailPosted().equals("")){
                answerLayOut.setVisibility(View.GONE);
                button_answer.setVisibility(View.VISIBLE);
            }else {
                answerLayOut.setVisibility(View.VISIBLE);
                button_answer.setVisibility(View.GONE);
            }

            if(peopleCommentData.getId().equals(peopleCommentData.getOwnerId())){
                button_answer.setVisibility(View.GONE);
            }

            if(nowLogInId.equals(peopleCommentData.getOwnerId())){
                menuPosted.setVisibility(View.VISIBLE);
            }else {
                menuPosted.setVisibility(View.GONE);
                button_answer.setVisibility(View.GONE);
            }

            namePosted.setText(peopleCommentData.getNickNamePosted());
            detailPosted.setText(peopleCommentData.getDetailPosted());
            jobPosted.setText(peopleCommentData.getJobPosted());

            MemberData memberDataPosted = memberDataHashMap.get(peopleCommentData.getOwnerId());

            if(!memberDataPosted.getProfilePhoto().equals("")){
                Glide
                        .with(mContext)
                        .load(Uri.parse(memberDataPosted.getProfilePhoto()))
                        .into(profileImagePosted);
            }
            timePosted.setText(timeString.formatTimeString(peopleCommentData.getTimePosted()));
        }
    }

    public static void setmContext(Context mContext) {
        PeoplePostAdapter.mContext = mContext;
    }

    public void deleteItem(int position){
        mPeopleCommentDatas.remove(position );
    }

    public void editItem(int position,String editedDetail){
        mPeopleCommentDatas.get(position).setDetail(editedDetail);
    }

    public void addItem(PeopleCommentData peopleCommentData){
        mPeopleCommentDatas.add(peopleCommentData);
    }

    public PeopleCommentData getItem(int position){
        return mPeopleCommentDatas.get(position);
    }

    public String getCommentDetail(int position){return mPeopleCommentDatas.get(position).detail;};

    public void setOnCommentItemClickListener(OnCommentMenuClickListener listenerComment){
        this.listenerComment = listenerComment;
    }
    public void setOnCommentAnswerClickListener(OnCommentAnswerClickListener listenerCommentAnswer){
        this.listenerCommentAnswer = listenerCommentAnswer;
    }
    public void setOnAnswerMenuClickListener(OnAnswerMenuClickListener listenerAnswerMenu){
        this.listenerAnswerMenu = listenerAnswerMenu;
    }

    public void setPeopleCommentData(ArrayList<PeopleCommentData> mPeopleCommentDatas) {
        this.mPeopleCommentDatas = mPeopleCommentDatas;
    }

    public void setMemberData(MemberData memberData) {
        this.memberData = memberData;
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
