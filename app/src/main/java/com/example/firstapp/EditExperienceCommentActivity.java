package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class EditExperienceCommentActivity extends AppCompatActivity {
    EditText comment ;
    HashMap<String, MemberData> memberDatas;
    MemberData memberData;
    ArrayList<CommentData> commentDatas;
    HashMap< String,HashMap<String,CommentData>> commentDatasSetHashMap;
    HashMap< String,HashMap<String,CommentData>> myCommentDatasSetHashMap;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences4;
    Gson gson;
    Intent getIntent;
    int commentNumber;
    int postNumber;
    String nowLogInId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);

        sharedPreferences4 = getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences4.getString("id","");

        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());
        memberData = memberDatas.get(nowLogInId);

        //객체 세팅
        commentDatas = new ArrayList<>();

        getIntent = getIntent();
        commentNumber = getIntent.getIntExtra("commentNumber",0);
        postNumber = getIntent.getIntExtra("postNumber",0);

        //어댑터 헤더 기본뷰홀더에 코멘트 데이터 세팅
        commentDatasSetHashMap = SharedPreferencesHandler.getCommentDataHashMap(getApplicationContext());

        final Intent getIntent = getIntent();
        comment = findViewById(R.id.text_comment);
        comment.setText(getIntent.getStringExtra("comment"));

        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


        Button postButton = findViewById(R.id.button_finish);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().length() > 0){
                    Intent intent = new Intent();
                    intent.putExtra("comment",comment.getText().toString());
                    intent.putExtra("position",getIntent.getIntExtra("position",0));
                    //바뀐 값 코멘트데이터세트에 다시세팅
                    CommentData commentData = commentDatasSetHashMap.get(Integer.toString(postNumber)).get(Integer.toString(commentNumber));
                    commentData.setDetail(comment.getText().toString());

                    HashMap<String,CommentData> innerHashMap = new HashMap<>();
                    if(commentDatasSetHashMap.get(Integer.toString(postNumber)) != null){
                        innerHashMap = commentDatasSetHashMap.get(Integer.toString(postNumber));
                    }
                    innerHashMap.put(Integer.toString(commentNumber),commentData);

                    commentDatasSetHashMap.put(Integer.toString(postNumber),innerHashMap);
                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.CommentDatasSetHashMap,commentDatasSetHashMap);

                    //마이 코멘트 데이터세트에도 세팅
                    myCommentDatasSetHashMap = SharedPreferencesHandler.getMyCommentDataHashMap(getApplicationContext());
                    HashMap<String,CommentData> innerHashMap2 = new HashMap<>();
                    if(myCommentDatasSetHashMap.get(nowLogInId) != null){
                        innerHashMap2 = myCommentDatasSetHashMap.get(nowLogInId);
                    }
                    innerHashMap2.put(Integer.toString(commentNumber),commentData);
                    myCommentDatasSetHashMap.put(nowLogInId,innerHashMap2);
                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyCommentDatasHashMap,myCommentDatasSetHashMap);

                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });
    }


}