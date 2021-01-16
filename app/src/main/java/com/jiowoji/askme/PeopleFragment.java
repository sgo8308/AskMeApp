package com.jiowoji.askme;
import androidx.annotation.Nullable;

import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PeopleFragment extends Fragment {
    final int POST_CODE =211;
    HashMap<String,MemberData> memberDatas = new HashMap<>();
    MemberData memberData;
    ListView listView;
    PeopleAdapter peopleAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_people, container, false);

        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getContext());

        //어댑터를 리스트뷰에 세팅해준다.
        listView = rootView.findViewById(R.id.listView);
        peopleAdapter = new PeopleAdapter(getContext(),memberDatas);
        listView.setAdapter(peopleAdapter);

        //리스트뷰에 아이템 클릭 리스너를 세팅해줘서 각각의 아이템이 선택 되었을 때 토스트메시지를 띄워준다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                memberData = peopleAdapter.getItem(position);
                Intent intent = new Intent(getContext(),PeoplePostAcitivity.class);
                intent.putExtra("id",memberData.getId());
                intent.putExtra("job",memberData.getJob());
                intent.putExtra("nickName",memberData.getNickName());
                startActivityForResult(intent,POST_CODE);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getContext());
        final PeopleAdapter peopleAdapter = new PeopleAdapter(getContext(),memberDatas);
        listView.setAdapter(peopleAdapter);
        peopleAdapter.notifyDataSetChanged();
    }
}

