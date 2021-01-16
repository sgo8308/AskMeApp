package com.jiowoji.askme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;

public class WritingActivity extends AppCompatActivity {
    EditText title ;
    EditText detail ;
    Intent getIntent ;
    HashMap<String, MemberData> memberDatas;
    HashMap<String,PostsData> postsDatasHashMap = new HashMap<>();
    HashMap<String,HashMap<String,PostsData>> myPostsDatasHashMap = new HashMap<>();
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences4;
    String nowLogInId;
    int postNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        postsDatasHashMap = SharedPreferencesHandler.getPostsDataHashMap(getApplicationContext());
        myPostsDatasHashMap = SharedPreferencesHandler.getMyPostsDataHashMap(getApplicationContext());
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());

        //뷰 찾기
        title = findViewById(R.id.editText_job);
        detail = findViewById(R.id.editText_detail);

        //뒤로가기 버튼 클릭시
        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //인텐트 받기
        getIntent = getIntent();
        postNumber = getIntent.getIntExtra("postNumber",0);

        //포스팅 액티비티에서 수정으로 넘어왔다면 값세팅
        if (getIntent.getStringExtra("activityFrom").equals("ExperienceComment")){
            title.setText( postsDatasHashMap.get(Integer.toString(postNumber)).getTitle() );
            detail.setText( postsDatasHashMap.get(Integer.toString(postNumber)).getDetail() );
        }

        //포스팅 버튼 클릭시
        Button postButton = findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().length() > 0 && detail.getText().toString().length()>0){
                    sharedPreferences2 = getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
                    nowLogInId = sharedPreferences2.getString("id","");
                    //포스팅액티비티로부터 수정으로 넘어왔다면 값 수정하기
                    if (getIntent.getStringExtra("activityFrom").equals("ExperienceComment")){

                        postsDatasHashMap.get(Integer.toString(postNumber)).setTitle(title.getText().toString());
                        postsDatasHashMap.get(Integer.toString(postNumber)).setDetail(detail.getText().toString());
                        setResult(RESULT_OK);

                        //내 게시글에 값 수정
                        HashMap<String,PostsData> innerHashMap = new HashMap<>();
                        if(myPostsDatasHashMap.get(nowLogInId) != null){
                            innerHashMap = myPostsDatasHashMap.get(nowLogInId);
                        }
                        if(innerHashMap.get(Integer.toString(postNumber)) != null){
                            innerHashMap.get(Integer.toString(postNumber)).setTitle(title.getText().toString());
                            innerHashMap.get(Integer.toString(postNumber)).setDetail(detail.getText().toString());
                        }
                        myPostsDatasHashMap.put(nowLogInId,innerHashMap);

                    }else {
                        //게시글 번호 생성
                        sharedPreferences4 = getSharedPreferences(SharedPreferencesFileNameData.PostNumber,MODE_PRIVATE);
                        int postNumber = sharedPreferences4.getInt(SharedPreferencesFileNameData.PostNumber,0);
                        //게시글 데이터 추가 전체 게시글 그리고 저장
                        MemberData memberData = memberDatas.get(nowLogInId);
                        postsDatasHashMap.put(Integer.toString(postNumber),new PostsData(nowLogInId,memberData.getNickName(),title.getText().toString(),detail.getText().toString(),memberData.getJob(),System.currentTimeMillis(),
                                memberData.getProfilePhoto(),memberData.getLikeCount(),memberData.getViewCount(),0,postNumber));
                        //게시글 데이터 추가 내 게시글 그리고 저장
                        HashMap<String,PostsData> innerHashMap = new HashMap<>();
                        if(myPostsDatasHashMap.get(nowLogInId) != null){
                            innerHashMap = myPostsDatasHashMap.get(nowLogInId);
                        }
                        innerHashMap.put(Integer.toString(postNumber),new PostsData(nowLogInId,memberData.getNickName(),title.getText().toString(),detail.getText().toString(),memberData.getJob(),System.currentTimeMillis(),
                                memberData.getProfilePhoto(),memberData.getLikeCount(),memberData.getViewCount(),0,postNumber));
                        myPostsDatasHashMap.put(nowLogInId,innerHashMap);

                        //게시글 번호 1 추가해서 저장
                        SharedPreferences.Editor editor4 = sharedPreferences4.edit();
                        editor4.putInt(SharedPreferencesFileNameData.PostNumber,postNumber + 1);
                        editor4.commit();
                    }
                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.PostsDatasHashMap,postsDatasHashMap);
                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyPostsDatasHashMap,myPostsDatasHashMap);


                    finish();
                }

            }
        });

    }

    

}