package com.jiowoji.askme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class ProfileSetting2Activity extends AppCompatActivity {
    EditText editText_job;
    EditText editText_introduction;
    EditText editText_nickName;
    HashMap<String,MemberData> memberDatas;
    SharedPreferences sharedPreferences;
    String nowLogInId;
    MemberData memberData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting2);

        sharedPreferences =getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences.getString("id","");
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(this);
        memberData = memberDatas.get(nowLogInId);

        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberData.setNickName(editText_nickName.getText().toString());
                memberData.setIntroduction(editText_introduction.getText().toString());
                memberData.setJob(editText_job.getText().toString());
                memberDatas.put(nowLogInId,memberData);

                SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("job",editText_job.getText().toString());
                intent.putExtra("nickName",editText_nickName.getText().toString());
                setResult(RESULT_OK,intent);

                finish();
            }
        });

        editText_job = findViewById(R.id.editText_job);
        editText_introduction = findViewById(R.id.editText_introduction);
        editText_nickName = findViewById(R.id.editText_nickName);

        editText_nickName.setText(memberData.getNickName());
        editText_job.setText(memberData.getJob());
        editText_introduction.setText(memberData.getIntroduction());
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        saveState();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        restoreState();
//    }
//
//    protected void restoreState(){
//        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
//        if(pref !=null && (pref.contains("titleInput"))){
//            String titleInputValue = pref.getString("titleInput","");
//            String knowledgeInputValue = pref.getString("knowledgeInput","");
//            titleInput.setText(titleInputValue);
//            knowledgeInput.setText(knowledgeInputValue);
//        }
//    }
//
//    protected void saveState(){
//        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("titleInput",titleInput.getText().toString());
//        editor.putString("knowledgeInput",knowledgeInput.getText().toString());
//        editor.commit();
//    }
}
