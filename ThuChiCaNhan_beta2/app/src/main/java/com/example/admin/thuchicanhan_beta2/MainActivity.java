package com.example.admin.thuchicanhan_beta2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

import static com.example.admin.thuchicanhan_beta2.R.id.buttonMucKhoanThu;

public class MainActivity extends AppCompatActivity {




    Button btnmuckhoanthu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitle("Thu Chi Cá Nhân");


        Button btnmuckhoanchi = (Button) findViewById(R.id.buttonMucKhoanChi);
        btnmuckhoanchi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, KhoanChi.class));
            }
        });

        Button btnmuckhoanthu = (Button) findViewById(buttonMucKhoanThu);
        btnmuckhoanthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, KhoanThu.class));
            }
        });


        Button btnTK = (Button) findViewById(R.id.buttonThongKe);
        btnTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ThongKe.class));
            }
        });





    }










}
