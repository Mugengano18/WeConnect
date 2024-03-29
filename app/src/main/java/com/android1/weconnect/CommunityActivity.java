package com.android1.weconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android1.weconnect.Adapters.MessageAdapter;
import com.android1.weconnect.Models.AllMethods;
import com.android1.weconnect.Models.Message;
import com.android1.weconnect.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messageDb;
    MessageAdapter messageAdapter;
    User u;
    List<Message> messages;
    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        init();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        u= new User();

        rvMessage = (RecyclerView) findViewById(R.id.rvMessage);
        etMessage = (EditText) findViewById(R.id.etMessage);
        imgButton = (ImageButton) findViewById(R.id.btnSend);
        imgButton.setOnClickListener(this);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (v==imgButton){
            if(!TextUtils.isEmpty(etMessage.getText().toString())){
                Message message = new Message(etMessage.getText().toString(),u.getName());
                etMessage.setText("");
                messageDb.push().setValue(message);
            }
            else {
                Toast.makeText(getApplicationContext(),"you cannot send blank message",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        u.setUid(currentUser.getUid());
        u.setEmail(currentUser.getEmail());
        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
                u.setUid(currentUser.getUid());
                AllMethods.name = u.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        messageDb = database.getReference("messages");
        messageDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();
                for (Message m: messages){
                    if (m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }
                    else {
                        newMessages.add(m);

                    }
                }
                messages = newMessages;

                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();

                for (Message m : messages){
                    if (!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();
    }

    private void displayMessages(List<Message> messages){
        rvMessage.setLayoutManager(new LinearLayoutManager(CommunityActivity.this));
        messageAdapter = new MessageAdapter(CommunityActivity.this,messages, messageDb);
        rvMessage.setAdapter(messageAdapter);
    }
}
