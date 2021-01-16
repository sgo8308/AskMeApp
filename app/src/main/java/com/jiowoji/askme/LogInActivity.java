package com.jiowoji.askme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class LogInActivity extends AppCompatActivity {
    HashMap<String, MemberData> memberDatas;
    HashMap<String,PostsData> postsDatasHashMap;
    HashMap<String,HashMap<String,CommentData>> commentDatasSetHashMap;
    HashMap<String,HashMap<String,PeopleCommentData>> peopleCommentDatasSetHashMap;
    HashMap<String,HashMap<String,PeopleCommentData>> myPeopleCommentDatasSetHashMap;
    HashMap<String,HashMap<String,CommentData>> myCommentDatasHashMap;
    HashMap<String,HashMap<String,PostsData>> myPostsDatasHashMap;
//    HashMap<String, ArrayList<PaymentHistoryData>> paymentHistoryDataHashMap;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;
    SharedPreferences sharedPreferences4;
    SharedPreferences sharedPreferences5;
    SharedPreferences sharedPreferences6;
    SharedPreferences sharedPreferences7;
    SharedPreferences sharedPreferences8;
    SharedPreferences sharedPreferences9;
    EditText id;
    EditText password;
    Button logIn;
    int test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        test = 3;
        Log.i("알림", "oncreate");
        //모든 셰어드 여기서 한번에 생성
        sharedPreferences = getSharedPreferences(SharedPreferencesFileNameData.MemberDatas, MODE_PRIVATE);
        sharedPreferences3 = getSharedPreferences(SharedPreferencesFileNameData.PostsDatasHashMap, MODE_PRIVATE);
        sharedPreferences4 = getSharedPreferences(SharedPreferencesFileNameData.CommentDatasSetHashMap, MODE_PRIVATE);
        sharedPreferences5 = getSharedPreferences(SharedPreferencesFileNameData.MyPostsDatasHashMap, MODE_PRIVATE);
        sharedPreferences6 = getSharedPreferences(SharedPreferencesFileNameData.MyCommentDatasHashMap, MODE_PRIVATE);
        sharedPreferences7 = getSharedPreferences(SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap, MODE_PRIVATE);
        sharedPreferences8 = getSharedPreferences(SharedPreferencesFileNameData.MyPeopleCommentDatasHashMap, MODE_PRIVATE);
        sharedPreferences9 = getSharedPreferences(SharedPreferencesFileNameData.PaymentHistoryData, MODE_PRIVATE);

        Gson gson = new GsonBuilder().create();

//        ResetShared resetShared = new ResetShared();
//        resetShared.resetShared(this);

//            SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesFileNameData.MemberDatas,MODE_PRIVATE);
//            memberDatas = SharedPreferencesHandler.getMemberDataHashMap(this);
//            memberDatas.remove("sgo8308@gmail.com");
//            SharedPreferencesHandler.saveData(this,SharedPreferencesFileNameData.MemberDatas,memberDatas);


        if (sharedPreferences.getString(SharedPreferencesFileNameData.MemberDatas, "null").equals("null")) {
            memberDatas = new HashMap<>();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SharedPreferencesFileNameData.MemberDatas, gson.toJson(memberDatas));
            editor.commit();
        }

        if (sharedPreferences3.getString(SharedPreferencesFileNameData.PostsDatasHashMap, "null").equals("null")) {
            postsDatasHashMap = new HashMap<>();
            SharedPreferences.Editor editor3 = sharedPreferences3.edit();
            editor3.putString(SharedPreferencesFileNameData.PostsDatasHashMap, gson.toJson(postsDatasHashMap));
            editor3.commit();
        }

        if (sharedPreferences4.getString(SharedPreferencesFileNameData.CommentDatasSetHashMap, "null").equals("null")) {
            commentDatasSetHashMap = new HashMap<>();
            SharedPreferences.Editor editor4 = sharedPreferences4.edit();
            editor4.putString(SharedPreferencesFileNameData.CommentDatasSetHashMap, gson.toJson(commentDatasSetHashMap));
            editor4.commit();
        }

        if (sharedPreferences5.getString(SharedPreferencesFileNameData.MyPostsDatasHashMap, "null").equals("null")) {
            myPostsDatasHashMap = new HashMap<>();
            SharedPreferences.Editor editor5 = sharedPreferences5.edit();
            editor5.putString(SharedPreferencesFileNameData.MyPostsDatasHashMap, gson.toJson(myPostsDatasHashMap));
            editor5.commit();
        }

        if (sharedPreferences6.getString(SharedPreferencesFileNameData.MyCommentDatasHashMap, "null").equals("null")) {
            myCommentDatasHashMap = new HashMap<>();
            SharedPreferences.Editor editor6 = sharedPreferences6.edit();
            editor6.putString(SharedPreferencesFileNameData.MyCommentDatasHashMap, gson.toJson(myCommentDatasHashMap));
            editor6.commit();
        }

        if (sharedPreferences7.getString(SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap, "null").equals("null")) {
            peopleCommentDatasSetHashMap = new HashMap<>();
            SharedPreferences.Editor editor7 = sharedPreferences7.edit();
            editor7.putString(SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap, gson.toJson(peopleCommentDatasSetHashMap));
            editor7.commit();
        }

        if (sharedPreferences8.getString(SharedPreferencesFileNameData.MyPeopleCommentDatasHashMap, "null").equals("null")) {
            myPeopleCommentDatasSetHashMap = new HashMap<>();
            SharedPreferences.Editor editor8 = sharedPreferences8.edit();
            editor8.putString(SharedPreferencesFileNameData.MyPeopleCommentDatasHashMap, gson.toJson(myPeopleCommentDatasSetHashMap));
            editor8.commit();
        }

//        if (sharedPreferences9.getString(SharedPreferencesFileNameData.PaymentHistoryData, "null").equals("null")) {
//            paymentHistoryDataHashMap = new HashMap<>();
//            SharedPreferences.Editor editor9 = sharedPreferences9.edit();
//            editor9.putString(SharedPreferencesFileNameData.PaymentHistoryData, gson.toJson(paymentHistoryDataHashMap));
//            editor9.commit();
//        }


        //회원 가입 버튼 리스너
        Button button = findViewById(R.id.button_signUp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivityForResult(intent,1000);
            }
        });

        //뷰 찾기
        id = findViewById(R.id.editText_id);
        password = findViewById(R.id.editText_passWord);
        logIn = findViewById(R.id.button_logIn);


        //로그인 관련
        Type type = new TypeToken<HashMap<String, MemberData>>() {}.getType();
        HashMap<String, MemberData> memberDatas = gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.MemberDatas, ""), type);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    if (memberDatas.containsKey(id.getText().toString())) {
                        //아이디와 비밀번호 에딧텍스트 클리어
                        if (memberDatas.get(id.getText().toString()).getPassWord().equals(password.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent;
                            if(!memberDatas.get(id.getText().toString()).isProfileSetting()){
                                intent = new Intent(getApplicationContext(),ProfileSetting1Activity.class);
                            }else {
                                intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                            //현재 로그인하는 id 저장
                            sharedPreferences2 = getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences2.edit();
                            editor.putString("id",id.getText().toString());
                            editor.commit();

                            //어댑터들에 현재 로그인아이디 알려주기
                            ExperiencePostAdapter.nowLogInId = id.getText().toString();
                            PeoplePostAdapter.nowLogInId = id.getText().toString();
                            intent.putExtra("id", id.getText().toString());
                            //id 패스워드 클리어
                            id.setText("");
                            password.setText("");

                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(this);
    }
}