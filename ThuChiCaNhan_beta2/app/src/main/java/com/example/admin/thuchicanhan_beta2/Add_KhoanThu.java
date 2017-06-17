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

public class Add_KhoanThu extends AppCompatActivity {



    SQLiteDatabase database=null;
    List<InforData_Thu> listThu=null;
    List<InforData_Thu>listTheLoaiThu=null;
    InforData_Thu TLT_Data=null;
    Show_List_TheLoaiThu adapter=null;
    int day,month,year;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__khoan_thu);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Thêm Khoản Thu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add_KhoanThu.this,KhoanThu.class));
            }
        });

        Spinner pinner = (Spinner) findViewById(R.id.spinner1);
        listTheLoaiThu = new ArrayList<InforData_Thu>();
        InforData_Thu d1 = new InforData_Thu();
        d1.setField1(" ");
        d1.setField2("Show All");
        d1.setField3(" ");
        d1.setField4(" ");

        listTheLoaiThu.add(d1);
        //Lệnh xử lý đưa dữ liệu là Tác giả và Spinner
        database = openOrCreateDatabase("mydataThu.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        if (database != null) {

            Cursor cursor = database.query("tblTheLoaiThu", null, null, null, null, null, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                InforData_Thu d = new InforData_Thu();
                d.setField1(cursor.getString(0));
                d.setField2(cursor.getString(1));
                d.setField3(" ");
                d.setField4(" ");
                listTheLoaiThu.add(d);
                cursor.moveToNext();
            }
            cursor.close();
        }
        adapter = new Show_List_TheLoaiThu(Add_KhoanThu.this, R.layout.show_list_theloaithu, listTheLoaiThu);
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
                    TLT_Data = null;
                    loadAllListThu();
                } else {
                    //Hiển thị sách theo tác giả chọn trong Spinner
                    TLT_Data = listTheLoaiThu.get(arg2);
                    loadListThuByTLT(TLT_Data.getField1().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                TLT_Data = null;
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
        Button btnInserthu = (Button) findViewById(R.id.buttonInsertThu);
        btnInserthu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TLT_Data == null) {
                    Toast.makeText(Add_KhoanThu.this, "Hãy Chọn Thể Loại Thu", Toast.LENGTH_LONG).show();
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
                values.put("idTLT", TLT_Data.getField1().toString());
                long bId = database.insert("tblKhoanThu", null, values);

                if (bId > 0) {
                    Toast.makeText(Add_KhoanThu.this, "Insert Thu OK", Toast.LENGTH_LONG).show();
                    loadListThuByTLT(TLT_Data.getField1().toString());
                } else {
                    Toast.makeText(Add_KhoanThu.this, "Insert Thu Failed", Toast.LENGTH_LONG).show();

                }
                startActivity(new Intent(Add_KhoanThu.this, KhoanThu.class));
            }
        });
        }
    public void loadAllListThu()
    {
        Cursor cur=database.query("tblKhoanThu", null, null, null, null, null, null);
        cur.moveToFirst();
        listThu=new ArrayList<InforData_Thu>();
        while(cur.isAfterLast()==false)

        {
            InforData_Thu d=new InforData_Thu();
            d.setField1(cur.getInt(0));
            d.setField2(cur.getString(1));
            d.setField3(cur.getString(2));
            d.setField4(cur.getString(3));
            listThu.add(d);
            cur.moveToNext();
        }
        cur.close();
        adapter=new Show_List_TheLoaiThu(Add_KhoanThu.this, R.layout.show_list_theloaithu, listThu);
        ListView lv=(ListView) findViewById(R.id.listViewThu);
        lv.setAdapter(adapter);
    }
    public void loadListThuByTLT(String idTLT)
    {
        Cursor cur=database.query("tblKhoanThu", null, "idTLT=?", new String[]{idTLT}, null, null, null);
        cur.moveToFirst();
        listThu=new ArrayList<InforData_Thu>();
        while(cur.isAfterLast()==false)
        {
            InforData_Thu d=new InforData_Thu();
            d.setField1(cur.getInt(0));
            d.setField2(cur.getString(1));
            d.setField3(cur.getString(2));
            d.setField4(cur.getString(3));
            listThu.add(d);
            cur.moveToNext();
        }
        cur.close();
        adapter=new Show_List_TheLoaiThu(Add_KhoanThu.this, R.layout.show_list_theloaithu, listThu);
        ListView lv=(ListView) findViewById(R.id.listViewThu);
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
