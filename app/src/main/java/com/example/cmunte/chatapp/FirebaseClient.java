package com.example.cmunte.chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseClient {

    public static FirebaseAuth auth;
    public static FirebaseUser user;
    public static boolean getNotifications;
    private static SharedPreferences notificationPref;

    final static private String NOTIFICATION_VALUE = "NOTIFICATION_VALUE";
    final static private String PREFERENCES_PACKAGE = "com.example.cmunte.chatapp";

    FirebaseClient(Activity a){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        notificationPref = a.getSharedPreferences(PREFERENCES_PACKAGE, Context.MODE_PRIVATE);

        getNotifications = notificationPref.getBoolean(NOTIFICATION_VALUE,false);

    }

    public static void updateProfile(int pictureUri, String userName) {

        if (user != null) {
            UserProfileChangeRequest.Builder change = new UserProfileChangeRequest.Builder();
            change.setDisplayName(userName);
            change.setPhotoUri(Uri.parse(ProfilePictures.pictures[pictureUri]));
            user.updateProfile(change.build());

        }
    }

    public static void changeNotificationStatus(boolean b) {

        notificationPref.edit().putBoolean(NOTIFICATION_VALUE, b).apply();
        getNotifications = b;

    }
}
