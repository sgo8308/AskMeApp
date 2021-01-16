package com.jiowoji.askme;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;

        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.HashMap;

        import static android.content.Context.MODE_PRIVATE;

public class MyQuestionFragment extends Fragment {

    MyQuestionAdapter adapter = new MyQuestionAdapter();
    final int WRITING_CODE =101;
    final int POST_CODE =201;
    PostsData postsData;
    HashMap<String,HashMap<String,CommentData>> myCommentDatasSetHashMap;
    ArrayList<CommentData> myCommentDatas;
    String nowLogInId;
    final int PEOPLEPOST_CODE = 301;
    final int EXPERIENCEPOST_CODE = 401;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_question, container, false);

        //현재 로그인 아이디 찾기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences.getString("id","");

        //리싸이클러뷰 세팅
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        myCommentDatasSetHashMap = SharedPreferencesHandler.getMyCommentDataHashMap(getContext());

        if(myCommentDatasSetHashMap.get(nowLogInId) != null){
            myCommentDatas = new ArrayList<>();
            myCommentDatas.addAll(myCommentDatasSetHashMap.get(nowLogInId).values());//오류 해결 방법 - postnumber가 string이 아니고 int 인데 그냥 집어넣어서 계속 nullpoint가 났다
            Collections.sort(myCommentDatas,new MyCommentDataComparater());
            adapter.setCommentDatas(myCommentDatas);
        }

        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new OnMyQuestionItemClickListener() {
            @Override
            public void onItemClick(MyQuestionAdapter.ViewHolder holder, View view, int position) {

                CommentData myCommentData = adapter.getItem(position);
                if(myCommentData.getPostTitle().equals("프로필 포스트")){
                    Intent intent = new Intent(getContext(),PeoplePostAcitivity.class);
                    intent.putExtra("id",myCommentData.getIdPosted());
                    intent.putExtra("job",myCommentData.getJobPosted());
                    intent.putExtra("nickName",myCommentData.getNickNamePosted());
                    startActivityForResult(intent,PEOPLEPOST_CODE);

                }else {
                    Intent intent = new Intent(getContext(),ExperiencePostActivity.class);
                    intent.putExtra("postNumber",myCommentData.getPostNumber());
                    startActivityForResult(intent,EXPERIENCEPOST_CODE);
                }
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        myCommentDatasSetHashMap = SharedPreferencesHandler.getMyCommentDataHashMap(getContext());

        if(myCommentDatasSetHashMap.get(nowLogInId) != null){
            myCommentDatas = new ArrayList<>();
            myCommentDatas.addAll(myCommentDatasSetHashMap.get(nowLogInId).values());//오류 해결 방법 - postnumber가 string이 아니고 int 인데 그냥 집어넣어서 계속 nullpoint가 났다
            Collections.sort(myCommentDatas,new MyCommentDataComparater());
            adapter.setCommentDatas(myCommentDatas);
        }

        adapter.notifyDataSetChanged();
    }

}