package com.jiowoji.askme;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;

public class SettingFragment extends Fragment implements AutoPermissionsListener {

    TextView text_id ;
    TextView text_job ;
    TextView text_coinCount ;
    TextView shop ;
    Button button_logOut ;
    CircleImageView image_profile;
    FloatingActionButton button_setPhoto;
    Bitmap imageBitmap;
    public static final int REQUEST_CODE_PROFILE_SETTING = 101;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;
    static final int REQUEST_COINSTORE = 3;
    HashMap<String,MemberData> memberDatas = new HashMap<>();
    SharedPreferences sharedPreferences2;
    String nowLogInId;
    static final int PAYMENTHISTORY_CODE = 101;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        sharedPreferences2 = getActivity().getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);
        nowLogInId = sharedPreferences2.getString("id","");

        text_id = rootView.findViewById(R.id.text_id);
        text_job = rootView.findViewById(R.id.text_job);
        text_coinCount = rootView.findViewById(R.id.text_coinCount);
        button_logOut = rootView.findViewById(R.id.button_logOut);
        image_profile = rootView.findViewById(R.id.image_profile);
        button_setPhoto = rootView.findViewById(R.id.button_setPhoto);
        shop = rootView.findViewById(R.id.text_shop);

        //셰어드에 저장된 정보로부터 값 세팅
        sharedPreferences2 = getActivity().getSharedPreferences(SharedPreferencesFileNameData.NowLogInId,MODE_PRIVATE);

        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getContext());

        text_id.setText(memberDatas.get(nowLogInId).getNickName());
        text_job.setText(memberDatas.get(nowLogInId).getJob());
        text_coinCount.setText(Integer.toString(memberDatas.get(nowLogInId).getCoinCount()));
        if(!memberDatas.get(nowLogInId).getProfilePhoto().equals("")){
//            image_profile.setImageBitmap(StringToBitmap(memberDatas.get(nowLogInId).getProfilePhoto()));
            Glide
                    .with(getActivity())
                    .load(Uri.parse(memberDatas.get(nowLogInId).getProfilePhoto()))
                    .into(image_profile);
        }
        if(text_job.getText().toString().equals("")){
            shop.setVisibility(GONE);
        }

        //버튼들 리스너 세팅
        button_setPhoto = rootView.findViewById(R.id.button_setPhoto);
        button_setPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AutoPermissions.Companion.loadAllPermissions(getActivity(),101); // 권한부여
                openGallery();
                //일단 갤러리만 가능하게 팝업메뉴 숨김
//                PopupMenu popupMenu = new PopupMenu(getContext(),v);
//                MenuInflater menuInflater = new MenuInflater(getContext());
//                menuInflater.inflate(R.menu.menu_profile_photo,popupMenu.getMenu());
//
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId())
//                        {
//                            case R.id.menu_camera :
//                                dispatchTakePictureIntent();
//                                break;
//                            case R.id.menu_gallery :
//                                openGallery();
//                                break;
//                        }
//                        return true;
//                    }
//                });
//                popupMenu.show();
            }
        });
        
        Button profileSettingButton = rootView.findViewById(R.id.button_profileSetting);
        profileSettingButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getContext(), ProfileSetting2Activity.class);
                startActivityForResult(intent,REQUEST_CODE_PROFILE_SETTING);
            }
        });


        Button coinButton  = rootView.findViewById(R.id.coinButton);
        coinButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getContext(), CoinStoreActivity.class);
                startActivityForResult(intent,REQUEST_COINSTORE);
            }
        });

        Button logOutButton = rootView.findViewById(R.id.button_logOut);
        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),LogInActivity.class);
                Toast.makeText(getContext(),"로그아웃 되었습니다",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");//MIME타입이 IMAGE로 시작하는 데이터를 가져오라는 의미
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_GALLERY);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {}
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getContext());
        MemberData memberData = memberDatas.get(nowLogInId);
                
        if(requestCode == REQUEST_CODE_PROFILE_SETTING && data != null){
            text_job.setText(data.getStringExtra("job"));
            text_id.setText(data.getStringExtra("nickName"));

        }else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null ){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            image_profile.setImageBitmap(imageBitmap);
            memberData.setProfilePhoto(BitmapToString(imageBitmap));
        }else if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null){
            Uri fileUri = data.getData();
            Glide
                    .with(getActivity())
                    .load(fileUri)
                    .into(image_profile);
            memberData.setProfilePhoto(fileUri.toString());
        }else if(requestCode == REQUEST_COINSTORE || requestCode == PAYMENTHISTORY_CODE){
            memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getActivity());
            text_coinCount.setText(Integer.toString(memberDatas.get(nowLogInId).getCoinCount()));
        }
        if(imageBitmap != null){
            memberDatas.get(sharedPreferences2.getString("id","noId")).setProfilePhoto(BitmapToString(imageBitmap));
        }
        memberDatas.put(nowLogInId,memberData);
        SharedPreferencesHandler.saveData(getContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(getActivity(), requestCode, permissions, this); }

    @Override public void onDenied(int requestCode, String[] permissions) {
        showToast("permissions denied : " + permissions.length);
    }

    @Override public void onGranted(int requestCode, String[] permissions) {
        showToast("permissions granted : " + permissions.length); }
    public void showToast(String data) {
        Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
    }

}
