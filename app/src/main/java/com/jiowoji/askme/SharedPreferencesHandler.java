package com.jiowoji.askme;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesHandler {

    static public HashMap<String,PostsData> getPostsDataHashMap(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesFileNameData.PostsDatasHashMap,MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String,PostsData>>(){}.getType();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.PostsDatasHashMap,""),type);
    }

    static public HashMap<String,MemberData> getMemberDataHashMap(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesFileNameData.MemberDatas,MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String,MemberData>>(){}.getType();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.MemberDatas,""),type);
    }

    static public HashMap<String,HashMap<String,PostsData>> getMyPostsDataHashMap(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesFileNameData.MyPostsDatasHashMap,MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String,HashMap<String,PostsData>>>(){}.getType();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.MyPostsDatasHashMap,""),type);
    }

    static public HashMap<String,HashMap<String,CommentData>> getMyCommentDataHashMap(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesFileNameData.MyCommentDatasHashMap,MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String,HashMap<String,CommentData>>>(){}.getType();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.MyCommentDatasHashMap,""),type);
    }

    static public HashMap<String,HashMap<String,CommentData>> getCommentDataHashMap(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesFileNameData.CommentDatasSetHashMap,MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String,HashMap<String,CommentData>>>(){}.getType();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.CommentDatasSetHashMap,""),type);
    }

    static public HashMap<String,HashMap<String,PeopleCommentData>> getPeopleCommentDatasHashMap(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap,MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String,HashMap<String,PeopleCommentData>>>(){}.getType();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap,""),type);
    }

    static public HashMap<String,HashMap<String,PeopleCommentData>> getMyPeopleCommentDatasHashMap(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesFileNameData.MyPeopleCommentDatasHashMap,MODE_PRIVATE);
        Type type = new TypeToken<HashMap<String,HashMap<String,PeopleCommentData>>>(){}.getType();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.MyPeopleCommentDatasHashMap,""),type);
    }

//    static public HashMap<String,ArrayList<PaymentHistoryData>> getPaymentHistoryDatasHashMap (Context context){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesFileNameData.PaymentHistoryData,MODE_PRIVATE);
//        Type type = new TypeToken<HashMap<String,ArrayList<PaymentHistoryData>>>(){}.getType();
//        Gson gson = new GsonBuilder().create();
//        return gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.PaymentHistoryData,""),type);
//    }

    static public ArrayList<PostsData> getPostsDatasArraySorted(Context context){
        Gson gson = new GsonBuilder().create();
        HashMap<String,PostsData> postsDatasHashMap = getPostsDataHashMap(context);
        ArrayList<PostsData> postsDatas = new ArrayList<>();
        postsDatas.addAll(postsDatasHashMap.values());
        Collections.sort(postsDatas,new PostsDataComparator());
        return postsDatas;
    }



    static public<T> void saveData(Context context,String fileName,T dataHashMap){
        Gson gson = new GsonBuilder().create();
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fileName,gson.toJson(dataHashMap));
        editor.commit();
    }

}
