package com.jiowoji.askme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment peopleFragment;
    private Fragment settingFragment;
    private Fragment myPostFragment;
    private Fragment myQuestionFragement;
    private Fragment experienceFragment;

    Intent intentGot;

    ActionBar actionBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        intentGot = getIntent();
        peopleFragment = new PeopleFragment();
        myPostFragment = new MyPostsFragment();
        settingFragment = new SettingFragment();
        experienceFragment = new PostsFragment();
        myQuestionFragement = new MyQuestionFragment();


        final TextView title = findViewById(R.id.titleText);
        title.setText("질문 받습니다");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, peopleFragment).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.tab1:
                        title.setText("질문 받습니다");
                        actionBar.show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, peopleFragment).commit();

                        return true;
                    case R.id.tab2:
                        title.setText("경험 공유합니다");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, experienceFragment).commit();
                        actionBar.show();
                        return true;
                    case R.id.tab3:
                        title.setText("내 게시글");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myPostFragment).commit();
                        actionBar.show();
                        return true;
                    case R.id.tab4:
                        title.setText("내 질문");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,myQuestionFragement ).commit();
                        actionBar.show();
                        return true;
                    case R.id.tab5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
                        title.setText("");
                        actionBar.hide();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("알림","온액티비티 리졸트 메인에서 실행 되었음");
        super.onActivityResult(requestCode, resultCode, data);
    }

//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.search,menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(@NonNull MenuItem item){
//        int curId = item.getItemId();
//        if(curId == R.id.menu_search){
//            EditText editText;
//        }
//        return true;
//    }

}
