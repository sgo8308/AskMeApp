package com.example.firstapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class CoinStoreActivity extends AppCompatActivity {
    BootUser bootUser;
    BootExtra bootExtra;
    private int stuck = 10;

    HashMap<String, MemberData> memberDatas;
    SharedPreferences sharedPreferences;
    String nowLogInId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);
        sharedPreferences = getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences.getString("id","");
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(this);

        bootUser = new BootUser().setPhone("010-XXXX-XXXX"); //자기 전화번호
        bootExtra = new BootExtra().setQuotas(new int[]{0, 2, 3});

        ImageButton imageButton = findViewById(R.id.button_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                finish();
            }
        });

        Button button_10coin = findViewById(R.id.button_10coin);
        button_10coin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                requestPay(10);
            }
        });
        Button button_50coin = findViewById(R.id.button_50coin);
        button_50coin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                requestPay(50);
            }
        });
        Button button_100coin = findViewById(R.id.button_100coin);
        button_100coin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                requestPay(100);
            }
        });


    }


    public void requestPay(final int coinCount){
        Bootpay.init(getFragmentManager())
                .setApplicationId("5f9f927418e1ae001d4f466c") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.KAKAO) // 결제할 PG 사
                .setMethod(Method.EASY) // 결제수단
                .setContext(CoinStoreActivity.this)
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setUX(UX.PG_DIALOG)
//                      .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setName(coinCount + "COIN") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
                .setPrice(100 * coinCount) // 결제할 금액

                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {

                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        MemberData memberData = memberDatas.get(nowLogInId);
                        memberData.setCoinCount(memberData.getCoinCount() + coinCount);
                        memberDatas.put(nowLogInId,memberData);
                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);

                        Log.d("done", message);
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {

                        Toast.makeText(getApplicationContext(),"결제가 취소되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d("close", "close");
                            }
                        })
                .request();
    }

}