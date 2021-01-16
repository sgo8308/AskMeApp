package com.jiowoji.askme;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PeoplePostAcitivity extends AppCompatActivity {

    PeoplePostAdapter adapter ;
    ActionBar actionBar;
    EditText comment;
    Button inputText;
    InputMethodManager imm;

    Intent getIntent;
    int postNumber;
    String nowLogInId;
    final int EDIT_COMMENT_CODE = 401;
    final int EDIT_POST_CODE = 101;
    final int ANSWER_CODE = 501;
    final int EDIT_ANSWER_CODE = 601;
    final int COINSTORE_CODE = 701;

    boolean payCoin = false;

    String idPosted;
    String jobPosted;
    String nickNamePosted;

    HashMap<String,PostsData> postsDatasHashMap;
    HashMap<String,HashMap<String,PeopleCommentData>> peopleCommentDatasSetHashMap;
    HashMap<String,MemberData> memberDatas;
    ArrayList<PeopleCommentData> peopleCommentDatas;

    MemberData memberData;

    SharedPreferences sharedPreferences3;
    SharedPreferences sharedPreferences4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_post_acitivity);
        Log.i("알림","피플포스트 온크리에이트");
        //현재 접속한 아이디
        sharedPreferences4 = getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences4.getString("id","");

        //회원데이터에서 현재 접속한 아이디에 데이터 가져오기
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());
        memberData = memberDatas.get(nowLogInId);

        //객체 세팅
        adapter = new PeoplePostAdapter();
        peopleCommentDatas = new ArrayList<>();

        getIntent = getIntent();
        idPosted = getIntent.getStringExtra("id");
        jobPosted = getIntent.getStringExtra("job");
        nickNamePosted = getIntent.getStringExtra("nickName");

        //어댑터 헤더 기본뷰홀더에 코멘트 데이터 세팅
        peopleCommentDatasSetHashMap = SharedPreferencesHandler.getPeopleCommentDatasHashMap(getApplicationContext());

        if(peopleCommentDatasSetHashMap.get(idPosted) != null){
            peopleCommentDatas.addAll(peopleCommentDatasSetHashMap.get(idPosted).values());//오류 해결 방법 - postnumber가 string이 아니고 int 인데 그냥 집어넣어서 계속 nullpoint가 났다
            Collections.sort(peopleCommentDatas,new PeopleCommentDataComparator());
            adapter.setPeopleCommentData(peopleCommentDatas);
        }

        //어댑터 헤더뷰홀더에 포스트 데이터 세팅 그러면서 댓글 갯수도 같이 세팅
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());
        memberDatas.get(idPosted).setCommentCount(peopleCommentDatas.size());
        adapter.setMemberData(memberDatas.get(idPosted));

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

        adapter.setmContext(this);
        recyclerView.setAdapter(adapter);
        //어댑터에 코멘트 메뉴 리스너 세팅
        adapter.setOnCommentItemClickListener(new OnCommentMenuClickListener() {
            @Override
            public void onItemClick(int id,int position) {
                int commentNumber = adapter.getItem(position).getCommentNumber();
                switch (id){
                    case R.id.edit:
                        Intent intent = new Intent(getApplicationContext(), EditPeopleCommentActivity.class);
                        intent.putExtra("comment",adapter.getCommentDetail(position));
                        intent.putExtra("position",position);
                        intent.putExtra("commentNumber",commentNumber);
                        intent.putExtra("id",idPosted);
                        startActivityForResult(intent,EDIT_COMMENT_CODE);
                        break;
                    case R.id.delete:
                        //전체 피플 댓글 삭제 후 저장
                        HashMap<String,PeopleCommentData> innerHashMap = new HashMap<>();
                        if(peopleCommentDatasSetHashMap.get(idPosted) != null){
                            innerHashMap = peopleCommentDatasSetHashMap.get(idPosted);
                        }

                        innerHashMap.remove(Integer.toString(commentNumber));
                        peopleCommentDatasSetHashMap.put(idPosted,innerHashMap);

                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap,peopleCommentDatasSetHashMap);

                        ArrayList<PeopleCommentData> commentDatas2 = new ArrayList<>(); // 오류 해결 - 딴데 나갔다가 다시 삭제 할 때는 안됐는데 여기서 이렇게 새로 아예 정의하니까 해결됨됨
                        commentDatas2.addAll(innerHashMap.values());//오류 해결 방법 - postnumber가 string이 아니고 int 인데 그냥 집어넣어서 계속 nullpoint가 났다
                        Collections.sort(commentDatas2,new PeopleCommentDataComparator());
                        adapter.setPeopleCommentData(commentDatas2);

                        //내 피플 댓글 삭제 후 저장
                        HashMap<String,HashMap<String,CommentData>> myCommentDatasSetHashMap = SharedPreferencesHandler.getMyCommentDataHashMap(getApplicationContext());
                        HashMap<String,CommentData> innerHashMap2 = new HashMap<>();
                        if(myCommentDatasSetHashMap.get(nowLogInId) != null){
                            innerHashMap2 = myCommentDatasSetHashMap.get(nowLogInId);
                        }

                        innerHashMap2.remove(Integer.toString(commentNumber));
                        myCommentDatasSetHashMap.put(nowLogInId,innerHashMap2);
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyCommentDatasHashMap,myCommentDatasSetHashMap);


                        //댓글 갯수 세팅
                        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());
                        memberDatas.get(idPosted).setCommentCount(commentDatas2.size());
                        adapter.setMemberData(memberDatas.get(idPosted));
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);

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
                Intent intent = new Intent(getApplicationContext(),PeopleAnswerActivity.class);
                intent.putExtra("comment",adapter.getCommentDetail(position));
                intent.putExtra("commentNumber",commentNumber);
                intent.putExtra("id",idPosted);
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
                        Intent intent = new Intent(getApplicationContext(),PeopleAnswerActivity.class);
                        intent.putExtra("comment",adapter.getCommentDetail(position));
                        intent.putExtra("position",position);
                        intent.putExtra("id",idPosted);
                        intent.putExtra("commentNumber",commentNumber);
                        intent.putExtra("whatButton","edit");
                        startActivityForResult(intent,EDIT_ANSWER_CODE);
                        break;
                    case R.id.delete:
                        //답변데이터에 디테일만 삭제해서 답변 안보이게 하기
                        HashMap<String,PeopleCommentData> innerHashMap = new HashMap<>();
                        innerHashMap = peopleCommentDatasSetHashMap.get(idPosted);
                        PeopleCommentData peopleCommentData = innerHashMap.get(Integer.toString(commentNumber));
                        peopleCommentData.setDetailPosted("");
                        innerHashMap.put(Integer.toString(commentNumber),peopleCommentData);
                        peopleCommentDatasSetHashMap.put(idPosted,innerHashMap);
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap,peopleCommentDatasSetHashMap);
                        //코인 다시 차감하기
                        if(memberData.getCoinCount() > 0){
                            Toast.makeText(getApplicationContext(),"코인 1개가 차감되었습니다.",Toast.LENGTH_SHORT).show();
                            memberData.setCoinCount(memberData.getCoinCount() - 1);
                            memberDatas.put(nowLogInId,memberData);
                            SharedPreferencesHandler.saveData(PeoplePostAcitivity.this,SharedPreferencesFileNameData.MemberDatas,memberDatas);
                        }
                        //삭제 후 어댑터에 세팅해서 화면에 뿌리기
                        ArrayList<PeopleCommentData> commentDatas2 = new ArrayList<>(); // 오류 해결 - 딴데 나갔다가 다시 삭제 할 때는 안됐는데 여기서 이렇게 새로 아예 정의하니까 해결됨됨
                        commentDatas2.addAll(innerHashMap.values());//오류 해결 방법 - postnumber가 string이 아니고 int 인데 그냥 집어넣어서 계속 nullpoint가 났다
                        Collections.sort(commentDatas2,new PeopleCommentDataComparator());
                        adapter.setPeopleCommentData(commentDatas2);
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

                if(idPosted.equals(nowLogInId)){
                   Toast.makeText(getApplicationContext(),"본인은 질문할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    comment();
                }
            }
        });
    }

    public void comment(){
        if(memberData.getCoinCount() > 0){
            AlertDialog.Builder ad = new AlertDialog.Builder(PeoplePostAcitivity.this);
            ad.setTitle("코인 1개가 필요합니다. 질문하시겠습니까?");

            ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final MemberData memberData = memberDatas.get(nowLogInId);
                    if(memberDatas.get(nowLogInId).getCoinCount() > 0){
                        Toast.makeText(getApplicationContext(),"코인 1개가 차감되었습니다.",Toast.LENGTH_SHORT).show();
                        payCoin = true;
                        //1개 차감 후 저장
                        memberData.setCoinCount(memberData.getCoinCount() - 1);
                        memberDatas.put(nowLogInId,memberData);
                        SharedPreferencesHandler.saveData(PeoplePostAcitivity.this,SharedPreferencesFileNameData.MemberDatas,memberDatas);

                        if(comment.getText().toString().length()>0){
                            //댓글 번호 생성
                            sharedPreferences3 = getSharedPreferences(SharedPreferencesFileNameData.CommentNumber,MODE_PRIVATE);
                            int commentNumber = sharedPreferences3.getInt(SharedPreferencesFileNameData.CommentNumber,0);

                            //코멘트 데이터셋 다시 가져와서 데이터 집어넣기
                            peopleCommentDatasSetHashMap = SharedPreferencesHandler.getPeopleCommentDatasHashMap(getApplicationContext());
                            PeopleCommentData peopleCommentData = new PeopleCommentData(nowLogInId,memberData.getNickName(),memberData.getJob(),comment.getText().toString(),
                                    System.currentTimeMillis(),0,idPosted,commentNumber,memberData.getProfilePhoto(),"","","",0,0,"");

                            HashMap<String,PeopleCommentData> innerHashMap = new HashMap<>();
                            if(peopleCommentDatasSetHashMap.get(idPosted) != null){
                                innerHashMap = peopleCommentDatasSetHashMap.get(idPosted);
                            }
                            innerHashMap.put(Integer.toString(commentNumber),peopleCommentData);

                            peopleCommentDatasSetHashMap.put(idPosted,innerHashMap);

                            ArrayList<PeopleCommentData> commentDatas2 = new ArrayList<>();
                            commentDatas2.addAll(peopleCommentDatasSetHashMap.get(idPosted).values());
                            Collections.sort(commentDatas2,new PeopleCommentDataComparator());
                            adapter.setPeopleCommentData(commentDatas2);
                            SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.PeopleCommentDatasSetHashMap,peopleCommentDatasSetHashMap);

                            // 마이코멘트에 이 피플코멘트도 같이 저장
                            HashMap<String,HashMap<String,CommentData>> myCommentDatasSetHashMap = SharedPreferencesHandler.getMyCommentDataHashMap(getApplicationContext());
                            HashMap<String,CommentData> innerHashMap2 = new HashMap<>();
                            if(myCommentDatasSetHashMap.get(nowLogInId) != null){
                                innerHashMap2 = myCommentDatasSetHashMap.get(nowLogInId);
                            }

                            CommentData myCommentData = new CommentData(nowLogInId,memberData.getNickName(),memberData.getJob(),comment.getText().toString(),
                                    System.currentTimeMillis(),0,-1,commentNumber,memberData.getProfilePhoto(),idPosted,nickNamePosted,jobPosted,
                                    "",0,0,"","프로필 포스트");

                            innerHashMap2.put(Integer.toString(commentNumber),myCommentData);
                            myCommentDatasSetHashMap.put(nowLogInId,innerHashMap2);
                            SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MyCommentDatasHashMap,myCommentDatasSetHashMap);

                            //댓글 갯수 저장
                            memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());
                            memberDatas.get(idPosted).setCommentCount(commentDatas2.size());
                            adapter.setMemberData(memberDatas.get(idPosted));
                            SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);

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
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(PeoplePostAcitivity.this);
            builder.setTitle("코인이 부족합니다. 충전소로 이동하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(),CoinStoreActivity.class);
                            intent.putExtra("fromWhere","PeoplePostActivity");
                            intent.putExtra("id",idPosted);
                            intent.putExtra("job",jobPosted);
                            intent.putExtra("nickName",nickNamePosted);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("아니오",null);
            builder.show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
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
            adapter.setMemberData(memberDatas.get(idPosted));
        }else if((requestCode == ANSWER_CODE ||requestCode == EDIT_ANSWER_CODE) && resultCode == RESULT_OK){
            peopleCommentDatasSetHashMap = SharedPreferencesHandler.getPeopleCommentDatasHashMap(getApplicationContext());

            if(peopleCommentDatasSetHashMap.get(idPosted) != null){
                ArrayList<PeopleCommentData> commentDatas2 = new ArrayList<>();
                commentDatas2.addAll(peopleCommentDatasSetHashMap.get(idPosted).values());
                Collections.sort(commentDatas2,new PeopleCommentDataComparator());
                adapter.setPeopleCommentData(commentDatas2);
            }
        }

//        if(data != null){
//            if(data.getStringExtra("ifFromEditAnswer") != null){
//                if(!data.getStringExtra("isFromEditAnswer").equals("false")){
//                    Toast.makeText(getApplicationContext(),"코인 1개를 획득하셨습니다.",Toast.LENGTH_SHORT).show();
//                    MemberData memberData = memberDatas.get(nowLogInId);
//                    memberData.setCoinCount(memberData.getCoinCount() + 1);
//                    memberDatas.put(nowLogInId,memberData);
//                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);
//                }
//            }
//        }

        adapter.notifyDataSetChanged();

    }

}