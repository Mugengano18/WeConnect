package com.android1.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class savedCouns extends AppCompatActivity {
    @BindView(R.id.list_item)
    ListView Item;
    DatabaseReference consellor1;
    List<Counsellor> counsellorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_couns);
        ButterKnife.bind(this);
        counsellorList=new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        consellor1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                counsellorList.clear();
                for (DataSnapshot counsellSnapshot:dataSnapshot.getChildren()){
                    Counsellor counsellor6=counsellSnapshot.getValue(Counsellor.class);
                    counsellorList.add(counsellor6);
                }

                consListadapter consListadapter=new consListadapter(savedCouns.this,counsellorList);
                Item.setAdapter(consListadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
