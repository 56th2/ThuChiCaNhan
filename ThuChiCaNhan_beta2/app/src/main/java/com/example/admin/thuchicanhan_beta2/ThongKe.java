package com.example.admin.thuchicanhan_beta2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThongKe extends AppCompatActivity {


    SQLiteDatabase database=null;
    List<InforData> listBook=null;
    List<InforData>listAuthor=null;
    InforData authorData=null;
    Show_List_TheLoaiChi adapter=null;
    int day, month, year;
    Button btnthongke,btn;
    Date d;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        final EditText edtDate=(EditText) findViewById(R.id.editTextNgayThongKe);

        toolbar();


        btnthongke = (Button) findViewById(R.id.buttonThongKe);
        btnthongke.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(112);

            }
        });
        btn = (Button) findViewById(R.id.btnChon);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatdate = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = formatdate.parse(edtDate.getText()+"");
                    date = formatdate.format(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                loadAllListTKchi(date);
                loadAllListTKthu(date);

            }
        });





    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub

       final Calendar c = Calendar.getInstance();

        int ngay = c.get(Calendar.DATE);
        int thang = c.get(Calendar.MONTH);
        int nam = c.get(Calendar.YEAR);
        if(id==112)
        {
            return new DatePickerDialog(this, dateChange, nam, thang, ngay);
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
            EditText edtDate=(EditText) findViewById(R.id.editTextNgayThongKe);
            edtDate.setText(day+"-"+(month+1)+"-"+year);

        }
    };

    public void loadAllListTKchi(String date)
    {  database = openOrCreateDatabase("mydataChi.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor cur=database.rawQuery("select * from tblKhoanChi kc JOIN tblTheLoaiChi tlc ON kc.idTLC =tlc.id  where dateadded = ?" ,new String[]{date});
        cur.moveToFirst();
        listBook=new ArrayList<InforData>();
        while(cur.isAfterLast()==false)
        {
            InforData d=new InforData();
            d.setField1(cur.getString(1));
            d.setField2(cur.getString(2));
            d.setField3(cur.getString(3));
            d.setField4(cur.getString(6));
            listBook.add(d);
            cur.moveToNext();
        }
        cur.close();
        adapter=new Show_List_TheLoaiChi(ThongKe.this, R.layout.show_list_theloaichi, listBook);
        ListView lv=(ListView) findViewById(R.id.listThongKechi);
        lv.setAdapter(adapter);

        if(lv.getAdapter().getCount()== 0)
            Toast.makeText(ThongKe.this, "Bạn cần thêm khoản chi", Toast.LENGTH_LONG).show();
    }

    public void loadAllListTKthu(String date)
    {  database = openOrCreateDatabase("mydataThu.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor cur=database.rawQuery("select * from tblKhoanThu kt JOIN tblTheLoaiThu tlt ON kt.idTLT =tlt.id where dateadded = ?" ,new String[]{date});
        cur.moveToFirst();
        listBook=new ArrayList<InforData>();
        while(cur.isAfterLast()==false)
        {
            InforData d=new InforData();
            d.setField1(cur.getString(1));
            d.setField2(cur.getString(2));
            d.setField3(cur.getString(3));
            d.setField4(cur.getString(6));
            listBook.add(d);
            cur.moveToNext();
        }
        cur.close();
        adapter=new Show_List_TheLoaiChi(ThongKe.this, R.layout.show_list_theloaichi, listBook);
        ListView lv=(ListView) findViewById(R.id.listThongKethu);
        lv.setAdapter(adapter);

        if(lv.getAdapter().getCount()== 0)
            Toast.makeText(ThongKe.this, "Bạn cần thêm khoản thu", Toast.LENGTH_LONG).show();
    }
    public void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbartk);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Thống Kê");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThongKe.this,MainActivity.class));
            }
        });

    }



}
