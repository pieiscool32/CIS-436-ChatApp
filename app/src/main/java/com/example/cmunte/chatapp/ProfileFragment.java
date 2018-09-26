package com.example.cmunte.chatapp;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import static java.lang.Math.toIntExact;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setResources(view);

        initUI(view);
    }

    private void initUI(View view){
        final Switch notificationSwitch = (Switch)view.findViewById(R.id.notification_switch);
        final EditText userName  = (EditText)view.findViewById(R.id.userName_editText);
       // final EditText userEmail  = (EditText)view.findViewById(R.id.email_editText);
        final Button updateButton = (Button)view.findViewById(R.id.update_Button);
        final Spinner profilePictureSpinner = (Spinner)view.findViewById(R.id.picture_spinner);
        final ImageView profilePicture = (ImageView) view.findViewById(R.id.userPicture_imageView);


        // populate picture spinner
        ArrayAdapter<String> temp = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, ProfilePictures.titles);
        temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profilePictureSpinner.setAdapter(temp);

        notificationSwitch.setChecked(FirebaseClient.getNotifications);

        if (FirebaseClient.user.getDisplayName() != null) {
            userName.setText(FirebaseClient.user.getDisplayName());
        } else {
            userName.setText(R.string.default_username);
        }

        if (FirebaseClient.user.getEmail() != null) {
            userName.setText(FirebaseClient.user.getEmail());
        } else {
            userName.setText(R.string.default_user);
        }

        if(FirebaseClient.user.getPhotoUrl() != null) {
            String url = FirebaseClient.user.getPhotoUrl().toString();
            profilePictureSpinner.setSelection(ProfilePictures.getPictureIndex(url));
            Picasso.get().load(FirebaseClient.user.getPhotoUrl()).resize(192,192).into(profilePicture);
        }

    }

    private void setResources(View view) {

        final Switch notificationSwitch = (Switch)view.findViewById(R.id.notification_switch);
        final EditText userName  = (EditText)view.findViewById(R.id.userName_editText);
       // final EditText userEmail  = (EditText)view.findViewById(R.id.email_editText);
        final Button updateButton = (Button)view.findViewById(R.id.update_Button);
        final Spinner profilePictureSpinner = (Spinner)view.findViewById(R.id.picture_spinner);
        final ImageView profilePicture = (ImageView) view.findViewById(R.id.userPicture_imageView);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText() == null) {
                    if (userName.getText().toString() != "") {
                        Toast.makeText(getActivity(), "Please enter a username.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                FirebaseClient.updateProfile(profilePictureSpinner.getSelectedItemPosition(),userName.getText().toString());
            }
        });

        profilePictureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //TODO: set image resource upon spinner change (requires R resource)
                String photoURI = ProfilePictures.pictures[position];
                //profilePicture.setImageResource();
                Picasso.get().load(photoURI).resize(192,192).into(profilePicture);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FirebaseClient.changeNotificationStatus(true);
                } else {
                    FirebaseClient.changeNotificationStatus(false);
                }
            }
        });

        userName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    if (s.length() > 15){
                        Toast.makeText(getActivity(), "Please enter a username under 15 characters.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

       /* TODO: IMPLEMENT IN CHAT FRAGMENT
        userName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // send message
                    return true;
                }
                return false;
            }
        });
```````*/
    }

}

