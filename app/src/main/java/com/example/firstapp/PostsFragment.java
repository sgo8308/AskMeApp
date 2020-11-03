package com.example.firstapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.FontRequest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class PostsFragment extends Fragment {
    PostsAdapter adapter ;
    final int WRITING_CODE =101;
    final int POST_CODE =201;
    FloatingActionButton floatingActionButton;
    PostsData postsData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_experience, container, false);


        adapter = new PostsAdapter();
        adapter.setPostsDatas(SharedPreferencesHandler.getPostsDatasArraySorted(getContext()));

        //리싸이클러뷰 세팅
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setmContext(getContext());
        recyclerView.setAdapter(adapter);
        //어댑터 아이템 클릭 세팅
        adapter.setOnItemClickListener(new OnPostsItemClickListener() {
            @Override
            public void onItemClick(PostsAdapter.ViewHolder holder, View view, int position) {
                postsData = adapter.getItem(position);
                Intent intent = new Intent(getContext(), ExperiencePostActivity.class);
                intent.putExtra("postNumber",postsData.getPostNumber());
                startActivityForResult(intent,POST_CODE);
            }
        });

        //플로팅버튼 세팅
        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WritingActivity.class);
                intent.putExtra("activityFrom","ExperienceFragment");
                startActivityForResult(intent,WRITING_CODE);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        adapter.setPostsDatas(SharedPreferencesHandler.getPostsDatasArraySorted(getContext()));
        adapter.notifyDataSetChanged();
    }

}