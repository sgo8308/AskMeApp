package com.example.firstapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class PeopleAdapter extends BaseAdapter {
        //어댑터가 동작할 액티비티의 컨텍스트를 받아와서 레이아웃 인플레이터를 만드는데 참조하고 그 외 안드로이드 시스템이 필요할 때 참조한다.
        Context mContext ;
        //아이템의 레이아웃을 뷰 객체로 반환하는데 사용한다.
        LayoutInflater mLayoutInflater ;
        //아이템의 데이터를 담고 있는 클래스를 담아두는 리스트
        ArrayList<MemberData> memberDatas;

        //생성자를 이용해서 컨텍스트와 데이터 리스트를 세팅해주고 전달받은 컨텍스트를 이용해 레이아웃인플레이터를 생성해준다.
        public PeopleAdapter(Context mContext, HashMap<String,MemberData> memberDatas) {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);

            this.mContext = mContext;
            this.memberDatas = new ArrayList<>();
            this.memberDatas.addAll(memberDatas.values());
            mLayoutInflater = LayoutInflater.from(mContext);

            //현재 로그인한 아이디 제일 위에 보여주기
            String targetA = sharedPreferences.getString("id","");
            int indexOfLogInId = -1;
            for (int i = 0; i < this.memberDatas.size(); i++) {
                if(targetA.equals(this.memberDatas.get(i).getId())) {
                    indexOfLogInId = i;
                }
            }
            Collections.swap(this.memberDatas, indexOfLogInId, 0);
        }

        //데이터 리스트의 사이즈를 계산한다. 이 사이즈만큼 getView가 호출된다.
        @Override
        public int getCount() {
            return memberDatas.size();
        }
        //데이터 리스트에 있는 아이템 데이터의 위치를 가져온다.
        @Override
        public long getItemId(int position) {
            return position;
        }
        //데이터 리스트에 있는 아이템 데이터를 가져온다.
        @Override
        public MemberData getItem(int position) {
            return memberDatas.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = mLayoutInflater.inflate(R.layout.item_people, null);
            TextView id = view.findViewById(R.id.text_userId);
            TextView job = view.findViewById(R.id.text_job);
//            TextView likeCount = view.findViewById(R.id.text_likeCount);
//            TextView viewCount = view.findViewById(R.id.text_viewCount);
            TextView commentCount = view.findViewById(R.id.text_commentCount);
            CircleImageView profileImage = view.findViewById(R.id.image_profile);
            id.setText(memberDatas.get(position).getNickName());
            job.setText(memberDatas.get(position).getJob());
//            likeCount.setText(Integer.toString(memberDatas.get(position).getLikeCount()));
//            viewCount.setText(Integer.toString(memberDatas.get(position).getViewCount()));
            commentCount.setText(Integer.toString(memberDatas.get(position).getCommentCount()));
            if(!memberDatas.get(position).getProfilePhoto().equals("")){
                Glide
                        .with(mContext)
                        .load(Uri.parse(memberDatas.get(position).getProfilePhoto()))
                        .into(profileImage);
            }
            return view;
        }
        public void addItem(MemberData memberData){
            memberDatas.add(0,memberData);
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
