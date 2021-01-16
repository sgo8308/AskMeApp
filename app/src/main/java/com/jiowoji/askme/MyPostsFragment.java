package com.jiowoji.askme;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;

        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import com.google.gson.reflect.TypeToken;

        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.HashMap;

        import static android.content.Context.MODE_PRIVATE;

public class MyPostsFragment extends Fragment {

    MyPostsAdapter adapter ;
    final int POST_CODE =201;
    PostsData postsData;
    HashMap<String, HashMap<String,PostsData>> myPostsDatasHashMap;
    ArrayList<PostsData> postsDatas;
    HashMap<String, MemberData> memberDatas;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;
    SharedPreferences sharedPreferences4;
    Gson gson;
    Type type;
    String nowLogInId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_post, container, false);

        adapter = new MyPostsAdapter();
        //현재 로그인 아이디 찾기
        sharedPreferences2 = getActivity().getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences2.getString("id","");

        // 내포스트 데이터 어뎁터에 세팅
        sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesFileNameData.MyPostsDatasHashMap,MODE_PRIVATE);
        gson = new GsonBuilder().create();
        type = new TypeToken<HashMap<String, HashMap<String,PostsData>>>(){}.getType();
        myPostsDatasHashMap = gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.MyPostsDatasHashMap,""),type);

        HashMap<String,PostsData> innerHashMap = new HashMap<>();
        if(myPostsDatasHashMap.get(nowLogInId) != null){
            innerHashMap = myPostsDatasHashMap.get(nowLogInId);
        }

        postsDatas = new ArrayList<>();
        postsDatas.addAll(innerHashMap.values());
        Collections.sort(postsDatas,new PostsDataComparator());

        adapter.setPostsDatas(postsDatas);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnMyPostsItemClickListener() {
            @Override
            public void onItemClick(MyPostsAdapter.ViewHolder holder, View view, int position) {
                Log.i("알림","클릭은 됐음 클릭된 포지션은" + Integer.toString(position));
                postsData = adapter.getItem(position);
                Intent intent = new Intent(getContext(), ExperiencePostActivity.class);
                intent.putExtra("postNumber",postsData.getPostNumber());
                startActivityForResult(intent,POST_CODE);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        myPostsDatasHashMap = gson.fromJson(sharedPreferences.getString(SharedPreferencesFileNameData.MyPostsDatasHashMap,""),type);

        HashMap<String,PostsData> innerHashMap = new HashMap<>();
        if(myPostsDatasHashMap.get(nowLogInId) != null){
            innerHashMap = myPostsDatasHashMap.get(nowLogInId);
        }

        postsDatas = new ArrayList<>();
        postsDatas.addAll(innerHashMap.values());
        Collections.sort(postsDatas,new PostsDataComparator());

        adapter.setPostsDatas(postsDatas);

        adapter.notifyDataSetChanged();
    }

}