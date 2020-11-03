package com.example.firstapp;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class ProfileSetting1Activity extends AppCompatActivity implements AutoPermissionsListener {
    Intent intentGot;
    EditText introduction;
    EditText job;
    EditText editText_nickName;

    TextView shop;
    HashMap<String,MemberData> memberDatas;
    FloatingActionButton button_setPhoto;
    ImageView image_profile_photo;
    Bitmap imageBitmap;
    String nowLogInId;
    MemberData memberData;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting1);

        intentGot = getIntent();
        nowLogInId = intentGot.getStringExtra("id");

        introduction = findViewById(R.id.editText_introduction);
        editText_nickName = findViewById(R.id.editText_nickName);
        job = findViewById(R.id.editText_job);
        image_profile_photo = findViewById(R.id.image_profile);
        shop = findViewById(R.id.text_shop);

        memberDatas = SharedPreferencesHandler.getMemberDataHashMap(getApplicationContext());
        memberData = memberDatas.get(nowLogInId);

        button_setPhoto = findViewById(R.id.button_setPhoto);
        button_setPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AutoPermissions.Companion.loadAllPermissions(ProfileSetting1Activity.this,101);//권한부여

                openGallery();
                // 일단 갤러리만 가능하게 팝업 메뉴는 숨김
//                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
//                MenuInflater menuInflater = new MenuInflater(getApplicationContext());
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

        Button button_finish = findViewById(R.id.button_finish);
        button_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(!editText_nickName.equals("")){
                    if(editText_nickName.getText().toString().length()>11){
                        Toast.makeText(getApplicationContext(),"10자 이하로 닉네임을 설정해주세요",Toast.LENGTH_SHORT).show();
                    }else if(job.getText().toString().length() > 11){
                        Toast.makeText(getApplicationContext(),"10자 이하로 직업을 설정해주세요",Toast.LENGTH_SHORT).show();
                    }else {
                        memberData.setProfileSetting(true);
                        if(imageBitmap != null){
                            memberData.setProfilePhoto(BitmapToString(imageBitmap));
                        }
                        memberData.setJob(job.getText().toString());
                        memberData.setIntroduction(introduction.getText().toString());
                        memberData.setNickName(editText_nickName.getText().toString());
                        memberData.setJob(job.getText().toString());

                        SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberDatas);

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"닉네임을 설정해주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this); }

    @Override public void onDenied(int requestCode, String[] permissions) {
        showToast("permissions denied : " + permissions.length);
    }

    @Override public void onGranted(int requestCode, String[] permissions) {
        showToast("permissions granted : " + permissions.length);
    }

    public void showToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            image_profile_photo.setImageBitmap(imageBitmap);
        }else if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){
            Uri fileUri = data.getData();
            ContentResolver resolver = getContentResolver();
            Glide
                    .with(this)
                    .load(fileUri)
                    .into(image_profile_photo);
            memberData.setProfilePhoto(fileUri.toString());
            memberDatas.put(nowLogInId,memberData);
            SharedPreferencesHandler.saveData(getApplicationContext(),SharedPreferencesFileNameData.MemberDatas,memberData);
        }
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

}