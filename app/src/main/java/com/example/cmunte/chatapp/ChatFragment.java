package com.example.cmunte.chatapp;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.google.android.gms.common.util.WorkSourceUtil.TAG;


public class ChatFragment extends Fragment {
    private DatabaseReference chat;
    private String userName;
    private View root;
    private int index;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_chat, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        chat = database.getReference("global_chat");

        chat.addChildEventListener(chatListener);
        index = 0;

        userName = getArguments().getString("user");

        return root;
    }

    public void send() {
        EditText content = root.findViewById(R.id.user_message);
        String message = content.getText().toString();

        if (userName.equals("")) {
            Toast.makeText(getContext(), "Please set Display Name", Toast.LENGTH_SHORT).show();
        } else {
            Message user_message = new Message(userName, message, new Date());
            chat.push().setValue(user_message);
            content.setText("");
        }
    }

    //        for displaying messages

    private String format(Date date) {
        int hours = date.getHours() - 4;
        int day = date.getDate();
        if (hours < 0) {
            hours = 24 + hours;
            day--;
        }
        String minute;
        if (date.getMinutes() < 10) {
            minute = "0"+date.getMinutes();
        } else {
            minute = Integer.toString(date.getMinutes());
        }
        return hours+":"+minute+" "+(date.getMonth()+1)+"/"+day;
    }

    private void addMessage(Message message) {
        TableLayout table = root.findViewById(R.id.chat_container);
        index++;
        View row = View.inflate(getContext(), R.layout.chat_row, null);
        row.setTag(index);

        TextView time = row.findViewById(R.id.chat_time);
        TextView user = row.findViewById(R.id.chat_username);
        TextView content = row.findViewById(R.id.chat_message);

        Date date = new Date(message.timestamp); //I know, but it works
        time.setText(getResources().getString(R.string.message_time, format(date)));
        user.setText(getResources().getString(R.string.message_user, message.username));
        content.setText(getResources().getString(R.string.message_content, message.message));

        table.addView(row);
        scroll();
    }

    private void scroll() {
        final ScrollView scroll = root.findViewById(R.id.chat_messages);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    //        for getting messages from firebase

    ChildEventListener chatListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
            //we got a new message
            addMessage(dataSnapshot.getValue(Message.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            Toast.makeText(getContext(), "Failed to load comments.",
                    Toast.LENGTH_SHORT).show();
        }
    };
}
