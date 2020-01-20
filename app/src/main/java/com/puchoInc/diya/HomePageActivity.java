package com.puchoInc.diya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
     Button speak;
     Button Listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        init();
    }
    public void init()
    {
        speak=(Button) findViewById(R.id.btn_speak);
        Listen=(Button) findViewById(R.id.btn_listen);
        speak.setOnClickListener(this);
        Listen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_speak:
                Intent intent=new Intent(HomePageActivity.this,SpeakActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_listen:
                Intent intent1=new Intent(HomePageActivity.this,ListenActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }
}
