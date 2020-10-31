package com.example.firstapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class EditPeopleCommentActivity extends AppCompatActivity {

    EditText comment ;
    HashMap<String, MemberData> memberDatas;
    MemberData memberData;
    ArrayList<PeopleCommentData> commentDatas;
    HashMap< String,HashMap<String,PeopleCommentData>> commentDatasSetHashMap;
    SharedPreferences sharedPreferences4;
    Intent getIntent;
    int commentNumber;
    String memberId;
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
        memberId = getIntent.getStringExtra("id");

        //어댑터 헤더 기본뷰홀더에 코멘트 데이터 세팅
        commentDatasSetHashMap = SharedPreferencesHandler.getPeopleCommentDatasHashMap(getApplicationContext());

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
                    PeopleCommentData peopleCommentData = commentDatasSetHashMap.get(memberId).get(Integer.toString(commentNumber));
                    peopleCommentData.setDetail(comment.getText().toString());

                    HashMap<String,PeopleCommentData> innerHashMap = new HashMap<>();
                    if(commentDatasSetHashMap.get(memberId) != null){
                        innerHashMap = commentDatasSetHashMap.get(memberId);
                    }
                    innerHashMap.put(Integer.toString(commentNumber),peopleCommentData);

                    commentDatasSetHashMap.put(memberId,innerHashMap);

                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap,commentDatasSetHashMap);

                    //마이 코멘트 데이터세트에도 세팅
                    HashMap<String,HashMap<String,CommentData>> myCommentDatasSetHashMap = SharedPreferencesHandler.getMyCommentDataHashMap(getApplicationContext());
                    HashMap<String,CommentData> innerHashMap2 = new HashMap<>();
                    if(myCommentDatasSetHashMap.get(nowLogInId) != null){
                        innerHashMap2 = myCommentDatasSetHashMap.get(nowLogInId);
                    }

                    CommentData myCommentData = myCommentDatasSetHashMap.get(nowLogInId).get(Integer.toString(commentNumber));
                    myCommentData.setDetail(comment.getText().toString());

                    innerHashMap2.put(Integer.toString(commentNumber),myCommentData);
                    myCommentDatasSetHashMap.put(nowLogInId,innerHashMap2);
                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyCommentDatasHashMap,myCommentDatasSetHashMap);

                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });
    }

}