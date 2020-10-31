package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {
    EditText messageInput;
    ArrayList<ChattingData> chattingDatas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        this.InitializeChattingData();

        ListView listView = (ListView)findViewById(R.id.listView);
        final ChattingAdapter chattingAdapter = new ChattingAdapter(this,chattingDatas);

        listView.setAdapter(chattingAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){

            }
        });

        messageInput = findViewById(R.id.messageInput);

        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                finish();
            }
        });

        ImageButton cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });

        ImageButton galleryButton = findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(intent);
            }
        });

        ImageButton imageButton = findViewById(R.id.button_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                finish();
            }
        });

    }

    public void InitializeChattingData()
    {
        chattingDatas = new ArrayList<ChattingData>();
        chattingDatas.add(new ChattingData( "잘 있어요 여러분 ~"));
        chattingDatas.add(new ChattingData( "잘 있어요 여러분 ~"));
        chattingDatas.add(new ChattingData( "잘 있어요 여러분 ~"));
        chattingDatas.add(new ChattingData( "잘 있어요 여러분 ~"));


    }



    @Override
    protected void onPause() {
        super.onPause();

        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreState();
    }

    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref !=null && (pref.contains("message"))){
            String message = pref.getString("message","");
            messageInput.setText(message);
        }
    }

    protected void saveState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("message",messageInput.getText().toString());
        editor.commit();
    }

}
