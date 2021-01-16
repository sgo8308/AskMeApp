package com.jiowoji.askme;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PostsFragment extends Fragment {
    PostsAdapter adapter ;
    final int WRITING_CODE =101;
    final int POST_CODE =201;
    FloatingActionButton floatingActionButton;
    PostsData postsData;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_experience, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);

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

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(),"온리쥼",Toast.LENGTH_SHORT).show();
        adapter = new PostsAdapter();
        adapter.setPostsDatas(SharedPreferencesHandler.getPostsDatasArraySorted(getContext()));

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
        adapter.notifyDataSetChanged();
    }
}