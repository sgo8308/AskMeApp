package com.jiowoji.askme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import static android.view.View.GONE;

public class SignUpActivity extends AppCompatActivity {
    EditText id;
    EditText passWord;
    EditText passWordConfirm;
    EditText editText_certificatonNumber;
    TextView text_time;

    ImageView image_idCheck;
    ImageView image_passWordCheck;
    ImageView image_passWordConfirmCheck;

    Button button_signUp;

    CheckBox checkBox_imAbove14 ;
    CheckBox checkBox_agreePersonalInfo ;
    CheckBox checkBox_agreeServiceUseMnual ;
    CheckBox checkBox_agreeAll ;

    Boolean passWordCheck = false;
    boolean clickCertification = false;
    boolean emailCertification = false;
    HashMap<String,MemberData> memberDatas ;
    GenerateCertNumber generateCertNumber = new GenerateCertNumber();
    String certificationNumber = "000000";
    Handler handlerTime;
    String time;
    Thread threadTime ;
    String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //인터넷 권한 허용
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        id = findViewById(R.id.editText_id);
        editText_certificatonNumber = findViewById(R.id.editText_certificationNumber);
        passWord = findViewById(R.id.editText_passWord);
        passWordConfirm = findViewById(R.id.editText_passWord2);
        text_time = findViewById(R.id.text_time);
        image_idCheck = findViewById(R.id.image_idCheck);
        image_passWordCheck = findViewById(R.id.image_passWordCheck);
        image_passWordConfirmCheck = findViewById(R.id.image_passWordConfirmCheck);
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());

        final LinearLayout linearLayout = findViewById(R.id.linearLayout_certification);

        final Button certification_button = findViewById(R.id.button_certification);
        certification_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                editText_certificatonNumber.setText("");
                if(!isEmail(id.getText().toString())){
                    Toast.makeText(getApplicationContext(),"이메일 형식으로 입력해주세요",Toast.LENGTH_SHORT).show();
                }else if(memberDatas.containsKey(id.getText().toString())){
                    Toast.makeText(getApplicationContext(),"이미 가입된 이메일 주소입니다",Toast.LENGTH_SHORT).show();
                }else{
                    if(threadTime == null||threadTime.getState().equals(Thread.State.NEW) || threadTime.getState().equals(Thread.State.TERMINATED)){

//                        if(certification_button.getText().toString().equals("재전송")){
//                            Toast.makeText(getApplicationContext(),"인증번호가 재전송되었습니다.",Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getApplicationContext(),"인증번호가 전송되었습니다.",Toast.LENGTH_SHORT).show();
//                        }

                        handlerTime = new Handler(Looper.getMainLooper());

                        test = "테스트";

                        threadTime = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                int time_hour = 7;
                                time = "7:00";

                                for(int i=0; i<421; i++) {

                                    if(i % 60 == 0){
                                        time = time_hour + ":00";
                                        time_hour = time_hour - 1;
                                    }else if(i % 60 > 50){
                                        time = time_hour +":" + "0"+(60 -i%60);
                                    }else {
                                        time = time_hour +":"+(60 - i%60);
                                    }
                                    try{Thread.sleep(1000);
                                    }catch(InterruptedException e){
                                      break;
                                    }catch(Exception e){}

                                    handlerTime.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            text_time.setText(time);
                                            if(time.equals("0:00")){
                                                certificationNumber = "000000";
                                                certification_button.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                                            }
                                        }
                                    });
                                }
                            }
                        });

                        threadTime.start();

                        clickCertification = true;
                        certification_button.setText("재전송");
                        certification_button.setBackgroundColor(Color.parseColor("#E1DDDD"));
                        linearLayout.setVisibility(View.VISIBLE);

                        final Handler handlerSendMail = new Handler(Looper.getMainLooper());

                        Thread threadSendMail = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                String message = "이메일 인증번호를 입력하시면 인증절차가 완료되어 가입을 진행하실 수 있습니다.\n" +
                                            "인증시간이 만료되었을 경우, 인증번호 재발송을 진행해주시기 바랍니다.";

                                certificationNumber = generateCertNumber.excuteGenerate();
                                try {
                                    GMailSender gMailSender = new GMailSender("sgo8308@gmail.com", "rnsrus0914*");
                                    gMailSender.sendMail("[질문 받습니다] 이메일 인증번호 : " + certificationNumber, message, id.getText().toString());
                                }catch (MessagingException e) {
                                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        threadSendMail.start();
                    }
                }
            }
        });

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isEmail(id.getText().toString())){
                    certification_button.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                    certification_button.setTextColor(Color.BLACK);
                }else{
                    certification_button.setBackgroundColor(Color.parseColor("#E1DDDD"));
                }

                if(clickCertification){
                    certification_button.setText("인증");
                    linearLayout.setVisibility(GONE);
                    threadTime.interrupt();
                    text_time.setText("7:00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isEmail(id.getText().toString())){
                    linearLayout.setVisibility(GONE);
                    certification_button.setText("인증");
                }
            }
        });

        final Button certification_button2 = findViewById(R.id.button_certification2);

        certification_button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(editText_certificatonNumber.getText().toString().length() == 6){
                    if(editText_certificatonNumber.getText().toString().equals(certificationNumber)){
                        Toast.makeText(getApplicationContext(),"인증되었습니다.",Toast.LENGTH_SHORT).show();
                        threadTime.interrupt();
                        id.setTextColor(Color.parseColor("#FFB1AEAE"));
                        image_idCheck.setVisibility(View.VISIBLE);
                        certification_button.setVisibility(View.INVISIBLE);
                        linearLayout.setVisibility(GONE);
                        emailCertification = true;
                        id.setFocusable(false);
                        id.setClickable(false);

                        if(emailCertification && passWordCheck && checkBox_agreeAll.isChecked() ){
                            button_signUp.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                            button_signUp.setTextColor(Color.parseColor("#000000"));
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"인증번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        editText_certificatonNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText_certificatonNumber.getText().toString().length() == 6){
                    certification_button2.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText_certificatonNumber.getText().toString().length() != 6){
                    certification_button2.setBackgroundColor(Color.parseColor("#E1DDDD"));
                }
            }
        });


        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passWord.getText().toString().length() > 5){
                    image_passWordCheck.setVisibility(View.VISIBLE);
                }
                else {
                    image_passWordCheck.setVisibility(View.GONE);
                    button_signUp.setBackgroundColor(Color.parseColor("#E1DDDD"));
                }
                if(passWord.getText().toString().equals(passWordConfirm.getText().toString()) && passWord.getText().toString().length() > 5){
                    image_passWordConfirmCheck.setVisibility(View.VISIBLE);
                    if(emailCertification&&checkBox_agreeAll.isChecked()){
                        button_signUp.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                        button_signUp.setTextColor(Color.parseColor("#000000"));
                    }
                }else {
                    image_passWordConfirmCheck.setVisibility(GONE);
                    button_signUp.setBackgroundColor(Color.parseColor("#E1DDDD"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                passWordConfirm.setText("");
                image_passWordConfirmCheck.setVisibility(GONE);
                if(emailCertification && passWordCheck && checkBox_agreeAll.isChecked() ){
                    button_signUp.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                    button_signUp.setTextColor(Color.parseColor("#000000"));
                }
                if(!passWord.getText().toString().equals(passWordConfirm.getText().toString())){
                    passWordCheck = false;
                    image_passWordConfirmCheck.setVisibility(GONE);
                    button_signUp.setBackgroundColor(Color.parseColor("#E1DDDD"));
                }
            }
        });

        passWordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((!passWord.getText().toString().equals("")) && passWord.getText().toString().equals(passWordConfirm.getText().toString()) && passWord.getText().toString().length() > 5){
                    image_passWordConfirmCheck.setVisibility(View.VISIBLE);
                    passWordCheck = true;
                }
                else {
                    image_passWordConfirmCheck.setVisibility(GONE);
                    passWordCheck = false;
                    button_signUp.setBackgroundColor(Color.parseColor("#E1DDDD"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(emailCertification && passWordCheck && checkBox_agreeAll.isChecked() ){
                    button_signUp.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                    button_signUp.setTextColor(Color.parseColor("#000000"));
                }
            }
        });
        checkBox_imAbove14 = findViewById(R.id.checkBox_imAbove14);
        checkBox_agreePersonalInfo =  findViewById(R.id.checkBox_agreePersonalInfo);
        checkBox_agreeServiceUseMnual =  findViewById(R.id.checkBox_agreeServiceUseMnual);
        checkBox_agreeAll = findViewById(R.id.checkBox_agreeAll);
        if(checkBox_imAbove14.isChecked()&& checkBox_agreePersonalInfo.isChecked()&& checkBox_agreeServiceUseMnual.isChecked()){

        }
        checkBox_agreeAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(emailCertification && passWordCheck && checkBox_agreeAll.isChecked() ){
                    button_signUp.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                    button_signUp.setTextColor(Color.parseColor("#000000"));
                }
                if (checkBox_agreeAll.isChecked()) {
                    checkBox_imAbove14.setChecked(true);
                    checkBox_agreePersonalInfo.setChecked(true);
                    checkBox_agreeServiceUseMnual.setChecked(true);
                } else {
                    checkBox_imAbove14.setChecked(false);
                    checkBox_agreePersonalInfo.setChecked(false);
                    checkBox_agreeServiceUseMnual.setChecked(false);
                }
            }
        });

        checkBox_imAbove14.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkBox_imAbove14.isChecked()&& checkBox_agreePersonalInfo.isChecked()&& checkBox_agreeServiceUseMnual.isChecked()){
                    checkBox_agreeAll.setChecked(true);
                }
                if(emailCertification && passWordCheck && checkBox_agreeAll.isChecked() ){
                    button_signUp.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                    button_signUp.setTextColor(Color.parseColor("#000000"));
                }
            }
        });

        checkBox_agreePersonalInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkBox_imAbove14.isChecked()&& checkBox_agreePersonalInfo.isChecked()&& checkBox_agreeServiceUseMnual.isChecked()){
                    checkBox_agreeAll.setChecked(true);
                }
                if(emailCertification && passWordCheck && checkBox_agreeAll.isChecked() ){
                    button_signUp.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                    button_signUp.setTextColor(Color.parseColor("#000000"));
                }
            }
        });

        checkBox_agreeServiceUseMnual.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkBox_imAbove14.isChecked()&& checkBox_agreePersonalInfo.isChecked()&& checkBox_agreeServiceUseMnual.isChecked()){
                    checkBox_agreeAll.setChecked(true);
                }
                if(emailCertification && passWordCheck && checkBox_agreeAll.isChecked() ){
                    button_signUp.setBackgroundColor(Color.parseColor("#E6F6DF21"));
                    button_signUp.setTextColor(Color.parseColor("#000000"));
                }
            }
        });

        button_signUp = findViewById(R.id.button_signUp);
        button_signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //이 부분은 임시로 쉽게 회원가입 위해서
                if(id.getText().toString().equals("1")){
                    Toast.makeText(getApplicationContext(),"회원 가입이 되었습니다.",Toast.LENGTH_SHORT).show();

                    memberDatas.put("sgo8308@naver.com",new MemberData("sgo8308@naver.com","","123123","",
                            "","",0,0,0,0,false));
                    memberDatas.put("test1",new MemberData("test1","","123123","",
                            "","",0,0,0,0,false));
                    memberDatas.put("test2",new MemberData("test2","","123123","",
                            "","",0,0,0,0,false));

                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);

                    Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                    startActivity(intent);
                }

                //이 부분이 제대로 회원가입
                if(emailCertification && passWordCheck && checkBox_agreeAll.isChecked() ){

                    Toast.makeText(getApplicationContext(),"회원 가입이 되었습니다.",Toast.LENGTH_SHORT).show();

                    memberDatas.put(id.getText().toString(),new MemberData(id.getText().toString(),"",passWord.getText().toString(),"",
                            "","",0,0,0,0,false));

                    SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);

                    Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                    startActivity(intent);

                }
            }
        });

        ImageButton button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button button_personalInfo = findViewById(R.id.button_agreePersonalInfo);
        button_personalInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PersonalInfoTerms.class);
                startActivity(intent);
            }
        });


        Button button_agreeServiceUseMnual = findViewById(R.id.button_agreeServiceUseMnual);
        button_agreeServiceUseMnual.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ServiceUseManualActivity.class));
            }
        });

    }




    public static boolean isEmail(String email){
        boolean returnValue = false;
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()){
            returnValue = true;
        }
        return returnValue;
    }

    private class GenerateCertNumber{
        private int certNumLength = 6;

        public String excuteGenerate() {
            Random random = new Random(System.currentTimeMillis());

            int range = (int)Math.pow(10,certNumLength);
            int trim = (int)Math.pow(10, certNumLength-1);
            int result = random.nextInt(range)+trim;

            if(result>range){
                result = result - trim;
            }

            return String.valueOf(result);
        }

        public int getCertNumLength() {
            return certNumLength;
        }

        public void setCertNumLength(int certNumLength) {
            this.certNumLength = certNumLength;
        }
    }

    public void turnOnTimer(){
        int time_hour = 7;
        time = "7:00";
        for(int i=0; i<421; i++) {

            if(i % 60 == 0){
                time = time_hour + ":00";
                time_hour = time_hour - 1;
            }else if(i%60 > 50){
                time = time_hour + "0"+":"+(60 -i%60);
            }else {
                time = time_hour +":"+(60 - i%60);
            }
            try{Thread.sleep(1000);
            }catch(Exception e){}


            handlerTime.postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    Log.i("알림","시간 보낸다 현재 시간"+ time);
                    text_time.setText(time);
                }
            });
        }
    }
}