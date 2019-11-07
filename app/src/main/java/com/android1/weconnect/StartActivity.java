package com.android1.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.patient) Button normal;
    @BindView(R.id.specailist) Button special;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);

        normal.setOnClickListener(this);
        special.setOnClickListener(this);}

    @Override
    public void onClick(View v) {
        if (v==normal){
            Intent intent=new Intent(StartActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        if(v==special){
            Intent intent1=new Intent(StartActivity.this,LoginCounselorActivity.class);
            startActivity(intent1);
        }

    }
}
