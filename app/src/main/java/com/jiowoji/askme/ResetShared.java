package com.jiowoji.askme;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ResetShared {
    HashMap<String, MemberData> memberDatas;
    HashMap<String,PostsData> postsDatasHashMap;
    HashMap<String,HashMap<String,CommentData>> commentDatasSetHashMap;
    HashMap<String,HashMap<String,PeopleCommentData>> peopleCommentDatasSetHashMap;
    HashMap<String,HashMap<String,PeopleCommentData>> myPeopleCommentDatasSetHashMap;
    HashMap<String,HashMap<String,CommentData>> myCommentDatasHashMap;
    HashMap<String,HashMap<String,PostsData>> myPostsDatasHashMap;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;
    SharedPreferences sharedPreferences4;
    SharedPreferences sharedPreferences5;
    SharedPreferences sharedPreferences6;
    SharedPreferences sharedPreferences7;
    SharedPreferences sharedPreferences8;
    EditText id;
    EditText password;
    Button logIn;
    int test;


    public void resetShared(Activity activity){
        sharedPreferences = activity.getSharedPreferences(SharedPreferencesFileNameData.MemberDatas, MODE_PRIVATE);
        sharedPreferences3 = activity.getSharedPreferences(SharedPreferencesFileNameData.PostsDatasHashMap, MODE_PRIVATE);
        sharedPreferences4 = activity.getSharedPreferences(SharedPreferencesFileNameData.CommentDatasSetHashMap, MODE_PRIVATE);
        sharedPreferences5 = activity.getSharedPreferences(SharedPreferencesFileNameData.MyPostsDatasHashMap, MODE_PRIVATE);
        sharedPreferences6 = activity.getSharedPreferences(SharedPreferencesFileNameData.MyCommentDatasHashMap, MODE_PRIVATE);
        sharedPreferences7 = activity.getSharedPreferences(SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap, MODE_PRIVATE);
        sharedPreferences8 = activity.getSharedPreferences(SharedPreferencesFileNameData.MyPeopleCommentDatasHashMap, MODE_PRIVATE);

        Gson gson = new GsonBuilder().create();

        memberDatas = new HashMap<>();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesFileNameData.MemberDatas, gson.toJson(memberDatas));
        editor.commit();



        postsDatasHashMap = new HashMap<>();
        SharedPreferences.Editor editor3 = sharedPreferences3.edit();
        editor3.putString(SharedPreferencesFileNameData.PostsDatasHashMap, gson.toJson(postsDatasHashMap));
        editor3.commit();


        commentDatasSetHashMap = new HashMap<>();
        SharedPreferences.Editor editor4 = sharedPreferences4.edit();
        editor4.putString(SharedPreferencesFileNameData.CommentDatasSetHashMap, gson.toJson(commentDatasSetHashMap));
        editor4.commit();


        myPostsDatasHashMap = new HashMap<>();
        SharedPreferences.Editor editor5 = sharedPreferences5.edit();
        editor5.putString(SharedPreferencesFileNameData.MyPostsDatasHashMap, gson.toJson(myPostsDatasHashMap));
        editor5.commit();


        myCommentDatasHashMap = new HashMap<>();
        SharedPreferences.Editor editor6 = sharedPreferences6.edit();
        editor6.putString(SharedPreferencesFileNameData.MyCommentDatasHashMap, gson.toJson(myCommentDatasHashMap));
        editor6.commit();


        peopleCommentDatasSetHashMap = new HashMap<>();
        SharedPreferences.Editor editor7 = sharedPreferences7.edit();
        editor7.putString(SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap, gson.toJson(peopleCommentDatasSetHashMap));
        editor7.commit();

        myPeopleCommentDatasSetHashMap = new HashMap<>();
        SharedPreferences.Editor editor8 = sharedPreferences8.edit();
        editor8.putString(SharedPreferencesFileNameData.MyPeopleCommentDatasHashMap, gson.toJson(myPeopleCommentDatasSetHashMap));
        editor8.commit();

    }


}
