package com.jiowoji.askme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    HashMap<String, MemberData> memberDatas;
    ArrayList<PostsData> postsDatas;
    ArrayList<CommentData> commentDatas;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;
    SharedPreferences sharedPreferences4;
    SharedPreferences sharedPreferences5;

    HashMap<String,HashMap<String,CommentData>> commentDatasSetHashMap;
    HashMap<String,HashMap<String,PostsData>> myPostsDatasHashMap;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        gson  = new GsonBuilder().create();

        sharedPreferences2 = getSharedPreferences(SharedPreferencesFileNameData.CommentDatasSetHashMap,MODE_PRIVATE);
        Type type2 = new TypeToken<HashMap< String,HashMap<String,CommentData> > >(){}.getType();
        commentDatasSetHashMap = gson.fromJson(sharedPreferences2.getString(SharedPreferencesFileNameData.CommentDatasSetHashMap,""),type2);
        Log.i("알림",sharedPreferences2.getString(SharedPreferencesFileNameData.CommentDatasSetHashMap,""));
        Log.i("알림","두번째 넘어감");

        sharedPreferences5 = getSharedPreferences(SharedPreferencesFileNameData.MyPostsDatasHashMap,MODE_PRIVATE);
        Type type3 = new TypeToken<HashMap< String, HashMap<String,PostsData>> >(){}.getType();
        //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
        Log.i("알림",sharedPreferences5.getString(SharedPreferencesFileNameData.MyPostsDatasHashMap,""));
        myPostsDatasHashMap = gson.fromJson(sharedPreferences5.getString(SharedPreferencesFileNameData.MyPostsDatasHashMap,""),type3);

        Log.i("알림","첫번째 넘어감");


    }
}