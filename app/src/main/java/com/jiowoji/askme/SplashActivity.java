package com.jiowoji.askme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    TextView title;
    TextView line;
    final int MSG_A  = 0;
    final int MSG_B  = 1;
    final int MSG_C  = 2;
    ArrayList<String> lineArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        title = findViewById(R.id.text_title);
        line = findViewById(R.id.text_line);
        lineArray = new ArrayList<>();

        StringBuffer lineText = new StringBuffer();
        lineText.append("-");
        for(int i=0; i<10; i++) {
            lineArray.add(lineText.toString());
            lineText.append("-");
        }

        //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
        final Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {

                        switch (msg.what) {
                            case MSG_A :
                                Log.i("알림","받은 i = " + msg.arg1);
                                line.setText(lineArray.get(msg.arg1));
                                break ;
                            case MSG_B :
                                title.setText("\" 질문 받습니다 \"");
                                break ;
                            case MSG_C :
                                Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                break ;

                            // TODO : add case.
                        }
                    }
                };

        Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message2 = handler.obtainMessage(MSG_B) ;
                        Message message3 = handler.obtainMessage(MSG_C) ;

                        for(int i=0; i<10; i++) {
                            Message message = handler.obtainMessage(MSG_A) ;
                            message.arg1 = i ;
                            Log.i("알림","보내는  i = " + i);
                            handler.sendMessage(message) ;
                            try{Thread.sleep(200);
                            }catch(Exception e){}
                        }

                        handler.sendMessage(message2);

                        try{Thread.sleep(300);
                        }catch(Exception e){}

                        handler.sendMessage(message3);
                    }
                });
                thread.start();


    }
}