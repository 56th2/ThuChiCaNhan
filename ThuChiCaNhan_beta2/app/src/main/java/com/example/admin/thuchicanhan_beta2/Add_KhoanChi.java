package com.example.admin.thuchicanhan_beta2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Add_KhoanChi extends AppCompatActivity {


    SQLiteDatabase database=null;
    List<InforData> listChi=null;
    List<InforData> listTheLoaiChi=null;
    InforData  TLC_Data=null;
    Show_List_TheLoaiChi adapter=null;
    int day,month,year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__khoan_chi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Thêm Khoản Chi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add_KhoanChi.this,KhoanChi.class));
            }
        });



        Spinner pinner = (Spinner) findViewById(R.id.spinner1);
        listTheLoaiChi = new ArrayList<InforData>();
        InforData d1 = new InforData();
        d1.setField1(" ");
        d1.setField2("Show All");
        d1.setField3(" ");
        d1.setField4(" ");

        listTheLoaiChi.add(d1);
        //Lệnh xử lý đưa dữ liệu là Tác giả và Spinner
        database = openOrCreateDatabase("mydataChi.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        if (database != null) {

            Cursor cursor = database.query("tblTheLoaiChi", null, null, null, null, null, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                InforData d = new InforData();
                d.setField1(cursor.getString(0));
                d.setField2(cursor.getString(1));
                d.setField3(" ");
                d.setField4(" ");
                listTheLoaiChi.add(d);
                cursor.moveToNext();
            }
            cursor.close();
        }
        adapter = new Show_List_TheLoaiChi(Add_KhoanChi.this, R.layout.show_list_theloaichi, listTheLoaiChi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pinner.setAdapter(adapter);
        //Xử lý sự kiện chọn trong Spinner
        //chọn tác giả nào thì hiển thị toàn bộ sách của tác giả đó mà thôi
        //Nếu chọn All thì hiển thị toàn bộ không phân hiệt tác giả

        pinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (arg2 == 0) {
                    //Hiển thị mọi sách  trong CSDL
                    TLC_Data = null;
                    loadAllListChi();
                } else {
                    //Hiển thị sách theo tác giả chọn trong Spinner
                    TLC_Data = listTheLoaiChi.get(arg2);
                    loadListChiByTLC(TLC_Data.getField1().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                TLC_Data = null;
            }
        });
        setCurrentDateOnView();
        //lệnh xử lý DatePickerDialog
        Button bChangeDate = (Button) findViewById(R.id.buttonDate);
        bChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(113);
            }
        });
        Button btnInsertkhoanchi = (Button) findViewById(R.id.buttonInsertKhoanChi);
        btnInsertkhoanchi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TLC_Data == null) {
                    Toast.makeText(Add_KhoanChi.this, "Hãy chọn thể loại chi", Toast.LENGTH_LONG).show();
                    return;
                }
                EditText txtaccount = (EditText) findViewById(R.id.editTextAccount);
                EditText txtmoney = (EditText) findViewById(R.id.editTextSoTien);
                ContentValues values = new ContentValues();
                values.put("account", txtaccount.getText().toString());
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                SimpleDateFormat dfmt = new SimpleDateFormat("dd-MM-yyyy");
                values.put("dateadded", dfmt.format(c.getTime()));
                values.put("money", txtmoney.getText().toString());
                values.put("idTLC", TLC_Data.getField1().toString());
                long bId = database.insert("tblKhoanChi", null, values);
               if (bId > 0) {
                    Toast.makeText(Add_KhoanChi.this, "Insert Khoản Chi OK", Toast.LENGTH_LONG).show();
                    loadListChiByTLC(TLC_Data.getField1().toString());
               } else {
                   Toast.makeText(Add_KhoanChi.this, "Insert Khoản Chi Failed", Toast.LENGTH_LONG).show();

                }
                startActivity(new Intent(Add_KhoanChi.this,KhoanChi.class));
            }
        });
    }
    public void loadAllListChi()
    {
        Cursor cur=database.query("tblKhoanChi", null, null, null, null, null, null);
        cur.moveToFirst();
        listChi=new ArrayList<InforData>();
        while(cur.isAfterLast()==false)

        {
            InforData d=new InforData();
            d.setField1(cur.getInt(0));
            d.setField2(cur.getString(1));
            d.setField3(cur.getString(2));
            d.setField4(cur.getString(3));
            listChi.add(d);
            cur.moveToNext();
        }
        cur.close();
        adapter=new Show_List_TheLoaiChi(Add_KhoanChi.this, R.layout.show_list_theloaichi, listChi);
        ListView lv=(ListView) findViewById(R.id.listViewKhoanChi);
        lv.setAdapter(adapter);
    }
    public void loadListChiByTLC(String idTLC)
    {
        Cursor cur=database.query("tblKhoanChi", null, "idTLC=?", new String[]{idTLC}, null, null, null);
        cur.moveToFirst();
        listChi=new ArrayList<InforData>();
        while(cur.isAfterLast()==false)
        {
            InforData d=new InforData();
            d.setField1(cur.getInt(0));
            d.setField2(cur.getString(1));
            d.setField3(cur.getString(2));
             d.setField4(cur.getString(3));
            listChi.add(d);
            cur.moveToNext();
        }
        cur.close();
        adapter=new Show_List_TheLoaiChi(Add_KhoanChi.this, R.layout.show_list_theloaichi, listChi);
        ListView lv=(ListView) findViewById(R.id.listViewKhoanChi);
        lv.setAdapter(adapter);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if(id==113)
        {
            return new DatePickerDialog(this, dateChange, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener dateChange= new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year1, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub


            year=year1;
            month=monthOfYear;
            day=dayOfMonth;
            EditText eDate=(EditText) findViewById(R.id.editTextDate);
            eDate.setText(day+"-"+(month+1)+"-"+year);
        }
    };
    public void setCurrentDateOnView()
    {
        EditText eDate=(EditText) findViewById(R.id.editTextDate);
        Calendar cal=Calendar.getInstance();
        day=cal.get(Calendar.DAY_OF_MONTH);
        month=cal.get(Calendar.MONTH);
        year=cal.get(Calendar.YEAR);
        eDate.setText(day+"-"+(month+1)+"-"+year);
    }


}
