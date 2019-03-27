package com.example.amangoyal.funchat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.amangoyal.funchat.Fragments.ChatsFragment;
import com.example.amangoyal.funchat.Fragments.FriendsFragment;
import com.example.amangoyal.funchat.Fragments.RequestsFragment;
import com.example.amangoyal.funchat.loginAndRegisterActivity.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    private DatabaseReference mRootRef;
    private List<Fragment> mainFragmentList = new ArrayList<>();    /* This is the main activity where all the chats and friends are shown in different fragments */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("FUN CHAT");

        mainFragmentList.add(new RequestsFragment());
        mainFragmentList.add(new ChatsFragment());
        mainFragmentList.add(new FriendsFragment());

        mViewPager = findViewById(R.id.tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mainFragmentList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout = findViewById(R.id.main_tabs);


        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(1);
    }


    public void sendToStart() {
        startActivity(new Intent(this, StartActivity.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_logout_button) {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if (item.getItemId() == R.id.main_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        if (item.getItemId() == R.id.allusers) {
            startActivity(new Intent(this, AllUserActivity.class));
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToStart();
            finish();
        } else {
            mRootRef.child("users").child(currentUser.getUid()).child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            mRootRef.child("users").child(mUser.getUid()).child("online").setValue(ServerValue.TIMESTAMP);
        }
    }
}
