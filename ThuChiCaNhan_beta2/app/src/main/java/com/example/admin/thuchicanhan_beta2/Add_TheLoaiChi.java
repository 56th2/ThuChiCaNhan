package com.example.admin.thuchicanhan_beta2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add_TheLoaiChi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__the_loai_chi);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Thêm thể loại chi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add_TheLoaiChi.this,KhoanChi.class));
            }
        });


        final Button btnInsert =(Button) findViewById(R.id.buttonInsert);
        final EditText txtname=(EditText) findViewById(R.id.editTextName);
        final Intent intent= getIntent();

        btnInsert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("name", txtname.getText().toString());
                intent.putExtra("DATA_TheLoaiChi", bundle);
                setResult(KhoanChi.SEND_DATA_FROM_THELOAICHI_ACTIVITY, intent);
                Add_TheLoaiChi.this.finish();
            }
        });

        final Button btnClear=(Button) findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txtname.setText("");
            }
        });

        Bundle bundle= intent.getBundleExtra("DATA");
        if(bundle!=null && bundle.getInt("KEY")==1)
        {
            String f2=bundle.getString("getField2");
            txtname.setText(f2);
            btnInsert.setText("Update");
            this.setTitle("View Detail");

        }




    }
}
