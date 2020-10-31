package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class AnswerActivity extends AppCompatActivity {
    Intent getIntent;
    int commentNumber;
    int postNumber;
    TextView text_comment ;
    EditText editText_anwswer;
    String comment;
    HashMap<String,HashMap<String, CommentData>> commentDataHashMap;
    HashMap<String, MemberData> memberDatas;
    String nowLogInId;
    MemberData memberData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        //뒤로가기 버튼 클릭시
        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        //코멘트 데이터셋 찾기
        commentDataHashMap = SharedPreferencesHandler.getCommentDataHashMap(this);

        //인텐트 가져와서 값 세팅
        getIntent = getIntent();
        commentNumber = getIntent.getIntExtra("commentNumber",0);
        postNumber = getIntent.getIntExtra("postNumber",0);
        comment = getIntent.getStringExtra("comment");

        //뷰 찾고 댓글 텍스트 입력
        text_comment = findViewById(R.id.text_comment);
        editText_anwswer = findViewById(R.id.editText_answer);
        text_comment.setText(comment);

        //포스팅 액티비티에서 수정으로 넘어왔다면 값세팅
        if (getIntent.getStringExtra("whatButton").equals("edit")){
            editText_anwswer.setText( commentDataHashMap.get(Integer.toString(postNumber)).get(Integer.toString(commentNumber)).getDetailPosted());
        }

        //멤버 데이터셋 찾기
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(this);
        //현재 로그인 아이디 찾기
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences.getString("id","");
        //답변다는 사람의 정보
        memberData = memberDatas.get(nowLogInId);

        Button button_finish = findViewById(R.id.button_finish);
        button_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //코멘트데이터에 답변 데이터 세팅하기
                if(editText_anwswer.getText().toString().length() > 5){
                    //수정에서 넘어왔을 때

                    HashMap<String,CommentData> innerHashMap = commentDataHashMap.get(Integer.toString(postNumber));
                    CommentData commentData = innerHashMap.get(Integer.toString(commentNumber));
                    if (!getIntent.getStringExtra("whatButton").equals("edit")){
                        commentData.setLikeCountPosted(0);
                        commentData.setTimePosted(System.currentTimeMillis());
                    }
                    commentData.setIdPosted(memberData.getId());
                    commentData.setNickNamePosted(memberData.getNickName());
                    commentData.setJobPosted(memberData.getJob());
                    commentData.setDetailPosted(editText_anwswer.getText().toString());
                    commentData.setLikeCountPosted(0);
                    commentData.setProfileImagePosted(memberData.getProfilePhoto());
                    commentData.setTimePosted(System.currentTimeMillis());
                    //코멘트 데이터 셋에 집어 넣기
                    innerHashMap.put(Integer.toString(commentNumber),commentData);
                    commentDataHashMap.put(Integer.toString(postNumber),innerHashMap);
                    //코멘트 데이터셋에 셰어드에 저장하기
                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.CommentDatasSetHashMap,commentDataHashMap);
                    //인텐트 세팅하고 종료
                    Intent intent = new Intent();
                    intent.putExtra("answer",editText_anwswer.getText().toString());
                    setResult(RESULT_OK,intent);

                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"답변이 너무 짧습니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}