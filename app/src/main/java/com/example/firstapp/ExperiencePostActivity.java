package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class ExperiencePostActivity extends AppCompatActivity {
    ExperiencePostAdapter adapter ;
    ActionBar actionBar;
    EditText comment;
    Button inputText;
    InputMethodManager imm;

    Intent getIntent;
    int postNumber;
    String nowLogInId;
    String idPosted;
    final int EDIT_COMMENT_CODE = 401;
    final int EDIT_POST_CODE = 101;
    final int ANSWER_CODE = 501;
    final int EDIT_ANSWER_CODE = 601;

    boolean payCoin = false;

    HashMap<String,PostsData> postsDatasHashMap;
    HashMap<String,HashMap<String,PostsData>> myPostsDatasHashMap;
    HashMap<String, HashMap<String,CommentData> > commentDatasSetHashMap;
    HashMap<String, HashMap<String,CommentData> > myCommentDatasSetHashMap;
    HashMap<String,MemberData> memberDatas;

    ArrayList<CommentData> commentDatas;
    MemberData memberData;

    SharedPreferences sharedPreferences3;
    SharedPreferences sharedPreferences4;
    String postTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_post);
        //현재 접속한 아이디
        sharedPreferences4 = getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences4.getString("id","");

        //회원데이터에서 현재 접속한 아이디에 데이터 가져오기
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());
        memberData = memberDatas.get(nowLogInId);

        //객체 세팅
        adapter = new ExperiencePostAdapter();
        commentDatas = new ArrayList<>();

        getIntent = getIntent();
        postNumber = getIntent.getIntExtra("postNumber",0);

        //어댑터 헤더 기본뷰홀더에 코멘트 데이터 세팅
        commentDatasSetHashMap = SharedPreferencesHandler.getCommentDataHashMap(getApplicationContext());

        if(commentDatasSetHashMap.get(Integer.toString(postNumber)) != null){
            commentDatas.addAll(commentDatasSetHashMap.get(Integer.toString(postNumber)).values());//오류 해결 방법 - postnumber가 string이 아니고 int 인데 그냥 집어넣어서 계속 nullpoint가 났다
            Collections.sort(commentDatas,new CommentDataComparater());
            adapter.setCommentData(commentDatas);
        }

        //어댑터 헤더뷰홀더에 포스트 데이터 세팅
        postsDatasHashMap = SharedPreferencesHandler.getPostsDataHashMap(getApplicationContext());
        postsDatasHashMap.get(Integer.toString(postNumber)).setCommentCount(commentDatas.size());
        adapter.setPostsdata(postsDatasHashMap.get(Integer.toString(postNumber)));

        adapter.setContext(getApplicationContext());

        //액션바 세팅
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //리싸이클러뷰 세팅
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        //어댑터에 코멘트 메뉴 리스너 세팅
        adapter.setOnCommentItemClickListener(new OnCommentMenuClickListener() {
            @Override
            public void onItemClick(int id,int position) {
                int commentNumber = adapter.getItem(position).getCommentNumber();
                switch (id){
                    case R.id.edit:
                        Intent intent = new Intent(getApplicationContext(), EditExperienceCommentActivity.class);
                        intent.putExtra("comment",adapter.getCommentDetail(position));
                        intent.putExtra("position",position);
                        intent.putExtra("postNumber",postNumber);
                        intent.putExtra("commentNumber",commentNumber);
                        startActivityForResult(intent,EDIT_COMMENT_CODE);
                        break;
                    case R.id.delete:
                        //전체 댓글 데이터 삭제후 저장
                        HashMap<String,CommentData> innerHashMap = new HashMap<>();
                        if(commentDatasSetHashMap.get(Integer.toString(postNumber)) != null){
                            innerHashMap = commentDatasSetHashMap.get(Integer.toString(postNumber));
                        }

                        innerHashMap.remove(Integer.toString(commentNumber));
                        commentDatasSetHashMap.put(Integer.toString(postNumber),innerHashMap);
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.CommentDatasSetHashMap,commentDatasSetHashMap);

                        ArrayList<CommentData> commentDatas2 = new ArrayList<>(); // 오류 해결 - 딴데 나갔다가 다시 삭제 할 때는 안됐는데 여기서 이렇게 새로 아예 정의하니까 해결됨됨
                        commentDatas2.addAll(innerHashMap.values());//오류 해결 방법 - postnumber가 string이 아니고 int 인데 그냥 집어넣어서 계속 nullpoint가 났다
                        Collections.sort(commentDatas2,new CommentDataComparater());
                        adapter.setCommentData(commentDatas2);

                        // 내 댓글 데이터 삭제 후 저장
                        myCommentDatasSetHashMap = SharedPreferencesHandler.getMyCommentDataHashMap(getApplicationContext());
                        HashMap<String,CommentData> innerHashMap2 = new HashMap<>();
                        if(myCommentDatasSetHashMap.get(nowLogInId) != null){
                            innerHashMap2 = myCommentDatasSetHashMap.get(nowLogInId);
                        }
                        innerHashMap2.remove(Integer.toString(commentNumber));
                        myCommentDatasSetHashMap.put(nowLogInId,innerHashMap2);
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyCommentDatasHashMap,myCommentDatasSetHashMap);


                        //전체 포스트 데이터 댓글 갯수 세팅
                        postsDatasHashMap = SharedPreferencesHandler.getPostsDataHashMap(getApplicationContext());
                        postsDatasHashMap.get(Integer.toString(postNumber)).setCommentCount(commentDatas2.size());
                        adapter.setPostsdata(postsDatasHashMap.get(Integer.toString(postNumber)));
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.PostsDatasHashMap,postsDatasHashMap);

                        //내 포스트 댓글 갯수 세팅
                        String idPosted = postsDatasHashMap.get(Integer.toString(postNumber)).getId();
                        myPostsDatasHashMap = SharedPreferencesHandler.getMyPostsDataHashMap(getApplicationContext());
                        myPostsDatasHashMap.get(idPosted).get(Integer.toString(postNumber)).setCommentCount(commentDatas2.size());
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyPostsDatasHashMap,myPostsDatasHashMap);

                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        //어댑터에 코멘트 답변버튼 리스너 세팅
        adapter.setOnCommentAnswerClickListener(new OnCommentAnswerClickListener() {
            @Override
            public void onItemClick(int position) {
                int commentNumber = adapter.getItem(position).getCommentNumber();
                Intent intent = new Intent(getApplicationContext(),AnswerActivity.class);
                intent.putExtra("comment",adapter.getCommentDetail(position));
                intent.putExtra("commentNumber",commentNumber);
                intent.putExtra("postNumber",postNumber);
                intent.putExtra("position",position);
                intent.putExtra("whatButton","answer");
                startActivityForResult(intent,ANSWER_CODE);
            }
        });
        //어댑터에 답변 메뉴 리스너 세팅
        adapter.setOnAnswerMenuClickListener(new OnAnswerMenuClickListener() {
            @Override
            public void onItemClick(int id,int position) {
                int commentNumber = adapter.getItem(position).getCommentNumber();
                switch (id){
                    case R.id.edit:
                        Intent intent = new Intent(getApplicationContext(),AnswerActivity.class);
                        intent.putExtra("comment",adapter.getCommentDetail(position));
                        intent.putExtra("position",position);
                        intent.putExtra("postNumber",postNumber);
                        intent.putExtra("commentNumber",commentNumber);
                        intent.putExtra("whatButton","edit");
                        startActivityForResult(intent,EDIT_ANSWER_CODE);
                        break;
                    case R.id.delete:
                        //답변데이터에 디테일만 삭제해서 답변 안보이게 하기
                        HashMap<String,CommentData> innerHashMap = new HashMap<>();
                        innerHashMap = commentDatasSetHashMap.get(Integer.toString(postNumber));
                        CommentData commentData = innerHashMap.get(Integer.toString(commentNumber));
                        commentData.setDetailPosted("");
                        innerHashMap.put(Integer.toString(commentNumber),commentData);
                        commentDatasSetHashMap.put(Integer.toString(postNumber),innerHashMap);
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.CommentDatasSetHashMap,commentDatasSetHashMap);
                        //삭제 후 어댑터에 세팅해서 화면에 뿌리기
                        ArrayList<CommentData> commentDatas2 = new ArrayList<>(); // 오류 해결 - 딴데 나갔다가 다시 삭제 할 때는 안됐는데 여기서 이렇게 새로 아예 정의하니까 해결됨됨
                        commentDatas2.addAll(innerHashMap.values());//오류 해결 방법 - postnumber가 string이 아니고 int 인데 그냥 집어넣어서 계속 nullpoint가 났다
                        Collections.sort(commentDatas2,new CommentDataComparater());
                        adapter.setCommentData(commentDatas2);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        //댓글 작성할 때
        comment = findViewById(R.id.text_comment);
        inputText = findViewById(R.id.button_50coin);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 다 작성하면 키보드 숨기려고

        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPosted = postsDatasHashMap.get(Integer.toString(postNumber)).getId();
                if(idPosted.equals(nowLogInId)){
                    Toast.makeText(getApplicationContext(),"본인은 질문할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    comment();
                    }
            }
        });

    }

    public void comment(){
        AlertDialog.Builder ad = new AlertDialog.Builder(ExperiencePostActivity.this);
        ad.setTitle("코인 1개가 필요합니다. 질문하시겠습니까?");

        ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final MemberData memberData = memberDatas.get(nowLogInId);
                if(memberDatas.get(nowLogInId).getCoinCount() > -100){
                    Toast.makeText(getApplicationContext(),"코인 1개가 차감되었습니다.",Toast.LENGTH_SHORT).show();
                    payCoin = true;
                    //1개 차감 후 저장
                    memberData.setCoinCount(memberData.getCoinCount() - 1);
                    memberDatas.put(nowLogInId,memberData);
                    SharedPreferencesHandler.saveData(ExperiencePostActivity.this,SharedPreferencesFileNameData.MemberDatas,memberDatas);

                    if(comment.getText().toString().length()>0){
                        //댓글 번호 생성
                        sharedPreferences3 = getSharedPreferences(SharedPreferencesFileNameData.CommentNumber,MODE_PRIVATE);
                        int commentNumber = sharedPreferences3.getInt(SharedPreferencesFileNameData.CommentNumber,0);

                        //코멘트 데이터셋 다시 가져와서 데이터 집어넣기
                        String jobPosted = postsDatasHashMap.get(Integer.toString(postNumber)).getJob();
                        String nickNamePosted = postsDatasHashMap.get(Integer.toString(postNumber)).getNickName();
                        postTitle = postsDatasHashMap.get(Integer.toString(postNumber)).getTitle();
                        commentDatasSetHashMap = SharedPreferencesHandler.getCommentDataHashMap(getApplicationContext());
                        CommentData commentData = new CommentData(nowLogInId,memberData.getNickName(),memberData.getJob(),comment.getText().toString(),
                                System.currentTimeMillis(),0,postNumber,commentNumber,memberData.getProfilePhoto(),idPosted,nickNamePosted,jobPosted
                                ,"",0,0,"",postTitle);

                        HashMap<String,CommentData> innerHashMap = new HashMap<>();
                        if(commentDatasSetHashMap.get(Integer.toString(postNumber)) != null){
                            innerHashMap = commentDatasSetHashMap.get(Integer.toString(postNumber));
                        }
                        innerHashMap.put(Integer.toString(commentNumber),commentData);

                        commentDatasSetHashMap.put(Integer.toString(postNumber),innerHashMap);

                        ArrayList<CommentData> commentDatas2 = new ArrayList<>();
                        commentDatas2.addAll(commentDatasSetHashMap.get(Integer.toString(postNumber)).values());
                        Collections.sort(commentDatas2,new CommentDataComparater());
                        adapter.setCommentData(commentDatas2);
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.CommentDatasSetHashMap,commentDatasSetHashMap);

                        // 마이코멘트 가져와서 데이터 집어넣고 저장
                        myCommentDatasSetHashMap = SharedPreferencesHandler.getMyCommentDataHashMap(getApplicationContext());
                        HashMap<String,CommentData> innerHashMap2 = new HashMap<>();
                        if(myCommentDatasSetHashMap.get(nowLogInId) != null){
                            innerHashMap2 = myCommentDatasSetHashMap.get(nowLogInId);
                        }
                        innerHashMap2.put(Integer.toString(commentNumber),commentData);
                        myCommentDatasSetHashMap.put(nowLogInId,innerHashMap2);
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyCommentDatasHashMap,myCommentDatasSetHashMap);

                        //전체 포스트 데이터 댓글 갯수 세팅
                        postsDatasHashMap = SharedPreferencesHandler.getPostsDataHashMap(getApplicationContext());
                        postsDatasHashMap.get(Integer.toString(postNumber)).setCommentCount(commentDatas2.size());
                        adapter.setPostsdata(postsDatasHashMap.get(Integer.toString(postNumber)));
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.PostsDatasHashMap,postsDatasHashMap);

                        //내 포스트 댓글 갯수 세팅
                        myPostsDatasHashMap = SharedPreferencesHandler.getMyPostsDataHashMap(getApplicationContext());
                        myPostsDatasHashMap.get(idPosted).get(Integer.toString(postNumber)).setCommentCount(commentDatas2.size());
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyPostsDatasHashMap,myPostsDatasHashMap);


                        comment.getText().clear();
                        imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
                        adapter.notifyDataSetChanged();

                        //댓글넘버 값 1추가해서 저장
                        SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                        editor3.putInt(SharedPreferencesFileNameData.CommentNumber,commentNumber + 1);
                        editor3.commit();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"코인이 부족합니다.",Toast.LENGTH_SHORT).show();
                    payCoin = false;
                }
            }
        });
        ad.setNegativeButton("아니요", null);
        ad.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(nowLogInId.equals(postsDatasHashMap.get(Integer.toString(postNumber)).getId())){
            inflater.inflate(R.menu.post_menu,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                Intent intent1 = new Intent(this, WritingActivity.class);
                intent1.putExtra("activityFrom","ExperienceComment");
                intent1.putExtra("postNumber",postNumber);
                startActivityForResult(intent1,EDIT_POST_CODE);

                break;
            case R.id.delete:
                postsDatasHashMap.remove(Integer.toString(postNumber));
                SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.PostsDatasHashMap,postsDatasHashMap);

                myPostsDatasHashMap = SharedPreferencesHandler.getMyPostsDataHashMap(getApplicationContext());

                HashMap<String,PostsData> innerHashMap = new HashMap<>();
                if(myPostsDatasHashMap.get(nowLogInId) != null){
                    innerHashMap = myPostsDatasHashMap.get(nowLogInId);
                }
                innerHashMap.remove(Integer.toString(postNumber));
                myPostsDatasHashMap.put(nowLogInId,innerHashMap);

                SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyPostsDatasHashMap,myPostsDatasHashMap);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_COMMENT_CODE &&resultCode == RESULT_OK && data != null){ //이 부분 주의할 것

            adapter.editItem(data.getIntExtra("position",0),data.getStringExtra("comment"));

        }else if(requestCode == EDIT_POST_CODE &&resultCode == RESULT_OK ) {
            postsDatasHashMap = SharedPreferencesHandler.getPostsDataHashMap(getApplicationContext());
            adapter.setPostsdata(postsDatasHashMap.get(Integer.toString(postNumber)));
        }else if((requestCode == ANSWER_CODE ||requestCode == EDIT_ANSWER_CODE) && resultCode == RESULT_OK){
            commentDatasSetHashMap = SharedPreferencesHandler.getCommentDataHashMap(getApplicationContext());

            if(commentDatasSetHashMap.get(Integer.toString(postNumber)) != null){
                ArrayList<CommentData> commentDatas2 = new ArrayList<>();
                commentDatas2.addAll(commentDatasSetHashMap.get(Integer.toString(postNumber)).values());
                Collections.sort(commentDatas2,new CommentDataComparater());
                adapter.setCommentData(commentDatas2);
            }
        }
        if(data != null){
            if(data.getStringExtra("isFromEditAnswer") != null){
                if(data.getStringExtra("isFromEditAnswer").equals("false")){
                    Toast.makeText(getApplicationContext(),"코인 1개를 획득하셨습니다.",Toast.LENGTH_SHORT).show();
                    MemberData memberData = memberDatas.get(nowLogInId);
                    memberData.setCoinCount(memberData.getCoinCount() + 1);
                    memberDatas.put(nowLogInId,memberData);
                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);
                }
            }
        }
        adapter.notifyDataSetChanged();

    }
}