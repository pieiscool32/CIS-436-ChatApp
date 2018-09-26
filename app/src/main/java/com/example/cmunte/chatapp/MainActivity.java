package com.example.cmunte.chatapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.Date;

import static com.google.android.gms.common.util.WorkSourceUtil.TAG;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ChatFragment chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        new FirebaseClient(this);

        //auto-generated, don't touch
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void send(View view) {
        //I'm too lazy to make a proper onclick listener in the fragment
        chat.send();
    }

    public ChatFragment createChat() {
        Bundle args = new Bundle();
        args.putString("user", FirebaseClient.user.getDisplayName());
        chat = new ChatFragment();
        chat.setArguments(args);
        return chat;
    }


    //        for updating the nav header

    private void updateHeader() {
        NavigationView nav = findViewById(R.id.nav_view);
        View header = nav.getHeaderView(0);
        TextView name = header.findViewById(R.id.user_name);
        TextView username = header.findViewById(R.id.user_username);
        ImageView profile = header.findViewById(R.id.profile_picture);


        if (!FirebaseClient.user.getDisplayName().equals("")) {
            name.setText(FirebaseClient.user.getDisplayName());
        } else {
            name.setText(getString(R.string.default_user));
        }

        if(FirebaseClient.user.getEmail() != null) {
            username.setText(FirebaseClient.user.getEmail());
        } else {
            username.setText(getString(R.string.default_username));
        }

        if(FirebaseClient.user.getPhotoUrl() != null) {
           Picasso.get().load(FirebaseClient.user.getPhotoUrl()).resize(192,192).into(profile);
        } else {
            profile.setImageResource(R.mipmap.ic_launcher);
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(myIntent);
    }

    //        for handling login in a nice-ish way

    @Override
    public void onResume(){
        super.onResume();

        FirebaseClient.user = FirebaseClient.auth.getCurrentUser();
        if(FirebaseClient.user != null) {
            Toast.makeText(this, "Welcome back, "+FirebaseClient.user.getEmail(), Toast.LENGTH_SHORT).show();
            updateHeader();
            //stop getting notifications when in app
            if (FirebaseClient.getNotifications) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("global-chat");
            }
            //load up chat by default
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_container, createChat(), "chat_fragment")
                    .commit();
        } else {
            //there is no user, being login
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //start getting notifications in background
        if(FirebaseClient.getNotifications) {
            FirebaseMessaging.getInstance().subscribeToTopic("global-chat");
        }
    }

    //        auto generated and slightly modified

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        Toolbar toolbar = findViewById(R.id.toolbar);
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (id == R.id.nav_profile) {
            ft.replace(R.id.main_container, new ProfileFragment());
            toolbar.setTitle("Account Settings");
        } else if (id == R.id.nav_sign_out) {
            signOut();
        } else if (id == R.id.nav_home) {
            ft.replace(R.id.main_container, createChat(), "chat_fragment");
            toolbar.setTitle("Annoy Your Friends");
        }

        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
