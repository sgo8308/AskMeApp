//package com.example.firstapp;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.SharedPreferences;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.firstapp.bootpay.Cancel;
//
//
//import org.apache.commons.io.IOUtils;
//import org.apache.http.HttpResponse;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class PaymentHistoryActivity extends AppCompatActivity {
//
//    static RequestQueue requestQueue;
//    PaymentHistoryAdapter adapter = new PaymentHistoryAdapter();
//    SharedPreferences sharedPreferences;
//    String nowLogInId;
//    PaymentHistoryData item;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment_history);
//        sharedPreferences = getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
//        nowLogInId = sharedPreferences.getString("id","");
//
//        ImageButton button_back = findViewById(R.id.button_back);
//        button_back.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                cancelPayment();
////                finish();
//            }
//        });
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        //레이아웃매니저는 3가지 종류가 있고 이것은 그 중 하나인 LinearLayoutManager이다.두번째 인자를 통해 리스트의 방향을 설정하고 3번째 인자를 true로 놓으면 레이아웃이 반대로 된다.
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        //리싸이클러뷰에 레이아웃매니저를 세팅한다.
//        recyclerView.setLayoutManager(layoutManager);
//        //어댑터에 어레이리스트를 부착한다.
//        HashMap<String, ArrayList<PaymentHistoryData>> paymentDatas = SharedPreferencesHandler.getPaymentHistoryDatasHashMap(getApplicationContext());
//        ArrayList<PaymentHistoryData> paymentData = paymentDatas.get(nowLogInId);
//        adapter.setmItems(paymentData);
//        //리싸이클러뷰에 어댑터를 세팅한다.
//        recyclerView.setAdapter(adapter);
//        //아이템에 리스너를 세팅한다. 이 때 따로 만들어준 OnTestItemClickListener 인터페이스를 새로 정의해서 사용한다.
//        adapter.setOnItemClickListener(new OnCancelPayClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                item = adapter.getItem(position);
//                Toast.makeText(getApplicationContext(),item.getReceipt_id()+ " 선택되었음",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
////    public void getAceesToken(){
////            if (requestQueue == null) {
////                requestQueue = Volley.newRequestQueue(getApplicationContext());
////            }
////            String url = "https://api.bootpay.co.kr/request/token";
////
////            StringRequest request = new StringRequest(
////                    Request.Method.POST ,
////                    url,
////                    new Response.Listener<String>() {
////                        @Override
////                        public void onResponse(String response) {
////                            Log.i("알림","정상 응답 ->" + response);
////                            Toast.makeText(getApplicationContext(),"정상응답",Toast.LENGTH_SHORT).show();
////                        }
////                    },
////                    new Response.ErrorListener() {
////                        @Override
////                        public void onErrorResponse(VolleyError error) {
////                            Log.i("알림","에러 응답 ->" + error.toString());
////                            Toast.makeText(getApplicationContext(),"에러",Toast.LENGTH_SHORT).show();
////
////                            //TODO 에러응답을 받았을 때 취할 행동
////                        }
////                    }
////            ) {
////                @Override
////                public Map<String,String> getHeaders() throws AuthFailureError {
////                    Map<String,String> params = new HashMap();
////                    params.put("application_id","5f9f927418e1ae001d4f466e");
////                    params.put("private_key","DIKTxYpSOMxpBCpogT0ohNViior67bMnOlv57Tuvx4s=");
////                    return params;
////                }
////
////                @Override
////                public String getBodyContentType() {
////                    return "application/json";
////                }
////
//////                @Override
//////                protected Map<String, String> getParams() throws AuthFailureError {
//////                    Map<String,String> params = new HashMap();
//////                    params.put("application_id","5f9f927418e1ae001d4f466e");
//////                    params.put("private_key","DIKTxYpSOMxpBCpogT0ohNViior67bMnOlv57Tuvx4s=");
//////                    return super.getParams();
//////                }
////            };
////            request.setShouldCache(false); // 이전에 받은 응답 결과가 있어도 새로 응답받고 싶을 때 false
////            requestQueue.add(request);
////        }
//
////    public void cancelPayment(){
////            if (requestQueue == null) {
////                requestQueue = Volley.newRequestQueue(getApplicationContext()); // 이거 전에 위에다 RequestQueue requestQueue ;  이렇게 선언해줌
////            }
////            String url = "https://api.bootpay.co.kr/cancel";
////
////            StringRequest request = new StringRequest(
////                    Request.Method.POST,
////                    url,
////                    new com.android.volley.Response.Listener<String>() {
////                        @Override
////                        public void onResponse(String response) {
////                            Log.i("알림", "정상 응답 ->" + response);
////                            //TODO 정상 응답을 받았을 때 취할 행동
////                        }
////                    },
////                    new Response.ErrorListener() {
////                        @Override
////                        public void onErrorResponse(VolleyError error) {
////                            Log.i("알림", "에러 응답 ->" + error);
////
////                        }
////                    }
////            ) {
////                @Override
////                public Map<String,String> getHeaders() throws AuthFailureError {
////                    Map<String,String> params = new HashMap();
////                    params.put("Authorization","5f9f927418e1ae001d4f466e");
////                    return params;
////                }
////
////                @Override
////                public String getBodyContentType() {
////                    return "application/json";
////                }
////
////                @Override
////                protected Map<String, String> getParams() throws AuthFailureError {
////                    Map<String,String> params = new HashMap();
////                    //TODO : 만약 포스트 방식이라면 이 부분에 바디에 넣어줄 KEY VALUE를 params에 put해줌
////                    return super.getParams();
////                }
////            };
////            request.setShouldCache(false); // 이전에 받은 응답 결과가 있어도 새로 응답받고 싶을 때 false
////            requestQueue.add(request);
////        }
//
//        public void cancelPayment(){
//            BootpayApi api = new BootpayApi(
//                    "[[ application_id ]]",
//                    "[[ private_key ]]"
//            );
//            try{
//                api.getAccessToken();
//            }catch(Exception e){
//                Log.i("알림",e.toString());
//              e.printStackTrace();
//            }
//
//            Cancel cancel = new Cancel();
//            cancel.receipt_id = item.getReceipt_id();
//            Log.i("알림",item.getReceipt_id());
//            cancel.name = "관리자 홍길동";
//            cancel.reason = "기타";
//
//            try {
//                HttpResponse res = api.cancel(cancel);
//                String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
//                System.out.println(str);
//                Log.i("알림",str);
//            } catch (Exception e) {
//                Log.i("알림",e.toString());
//                e.printStackTrace();
//            }
//        }
//}
