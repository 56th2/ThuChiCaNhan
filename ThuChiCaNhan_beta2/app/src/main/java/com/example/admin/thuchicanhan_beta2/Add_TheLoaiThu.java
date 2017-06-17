package com.example.admin.thuchicanhan_beta2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add_TheLoaiThu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__the_loai_thu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Thêm Thể Loại Thu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add_TheLoaiThu.this,Ds_TheLoaiThu.class));
            }
        });


        final Button btnInsertthu =(Button) findViewById(R.id.buttonInsertThu);

        final EditText txttheloaithu=(EditText) findViewById(R.id.editTextTheLoaiThu);
        final Intent intent= getIntent();

        btnInsertthu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("name", txttheloaithu.getText().toString());
                intent.putExtra("DATA_TheLoaiThu", bundle);
                setResult(KhoanThu.SEND_DATA_FROM_THELOAITHU_ACTIVITY, intent);
                Add_TheLoaiThu.this.finish();
            }
        });

        final Button btnClear=(Button) findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txttheloaithu.setText("");
            }
        });

        Bundle bundle= intent.getBundleExtra("DATA");
        if(bundle!=null && bundle.getInt("KEY")==1)
        {

            String f2=bundle.getString("getField2");

            txttheloaithu.setText(f2);
            btnInsertthu.setText("Update");
            this.setTitle("View Detail");

        }

    }
}
