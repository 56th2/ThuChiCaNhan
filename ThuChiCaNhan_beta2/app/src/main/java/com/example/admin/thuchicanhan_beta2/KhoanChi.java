package com.example.admin.thuchicanhan_beta2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.admin.thuchicanhan_beta2.R.layout.activity_khoan_chi;

public class KhoanChi extends AppCompatActivity {

    public static final int OPEN_THELOAICHI_DIALOG=1;
    public static final int SEND_DATA_FROM_THELOAICHI_ACTIVITY=2;
    SQLiteDatabase database = null;
    List<InforData> listChi = null;
    Show_List_TheLoaiChi adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_khoan_chi);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KhoanChi.this, MainActivity.class));
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KhoanChi.this, Add_KhoanChi.class));
            }
        });

        loadAllListChi();
    }


    public boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    public SQLiteDatabase getDatabase()
    {
        try
        {
            database=openOrCreateDatabase("mydataChi.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if(database!=null)
            {
                if(isTableExists(database,"tblTheLoaiChi"))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sqlTheLoaiChi="create table tblTheLoaiChi ("
                        +"id integer primary key autoincrement,"
                        +"name text)";
                database.execSQL(sqlTheLoaiChi);
                String sqlKhoanChi="create table tblKhoanChi ("
                        +"id integer primary key autoincrement,"
                        +"account text,"
                        +"dateadded date,"
                        +"money text,"
                        +"idTLC integer not null constraint idTLC references tblTheLoaiChi(id) on delete cascade)";
                database.execSQL(sqlKhoanChi);
                //Cách tạo trigger khi nhập dữ liệu sai ràng buộc quan hệ
                String sqlTrigger="create trigger fk_insert_KhoanChi before insert on tblKhoanChi "
                        +" for each row "
                        +" begin "
                        +" 	select raise(rollback,'them du lieu tren bang tblKhoanChi bi sai') "
                        +" 	where (select id from tblTheLoaiChi where id=new.idTLC) is null ;"
                        +" end;";
                database.execSQL(sqlTrigger);
                Toast.makeText(KhoanChi.this, "OK OK", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return database;
    }
    public void createDatabaseAndTrigger()
    {
        if(database==null)
        {
            getDatabase();
            Toast.makeText(KhoanChi.this, "OK OK", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==SEND_DATA_FROM_THELOAICHI_ACTIVITY)
        {
            Bundle bundle= data.getBundleExtra("DATA_TheLoaiChi");
            String name=bundle.getString("name");
            ContentValues content=new ContentValues();
            content.put("name", name);
            if(database!=null)
            {
                long idTLC=database.insert("tblTheLoaiChi", null, content);
                if(idTLC==-1)
                {
                    Toast.makeText(KhoanChi.this,idTLC+" - "+" - "+name +" ==> insert error!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(KhoanChi.this, idTLC+" - " +" - "+name +" ==>insert OK!", Toast.LENGTH_LONG).show();
                }
            }

        }
    }



    public void loadAllListChi() {
        database = openOrCreateDatabase("mydataChi.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        if (database != null) {
            Cursor cur = database.rawQuery("select * from tblKhoanChi kc JOIN tblTheLoaiChi tlc ON kc.idTLC =tlc.id ", null);
            cur.moveToFirst();
            listChi = new ArrayList<InforData>();
            while (cur.isAfterLast() == false) {
                InforData d = new InforData();
                d.setField1(cur.getString(1));
                d.setField2(cur.getString(2));
                d.setField3(cur.getString(3));
                d.setField4(cur.getString(6));
                listChi.add(d);
                cur.moveToNext();
            }
            cur.close();
        }
        adapter = new Show_List_TheLoaiChi(KhoanChi.this, R.layout.show_list_theloaichi, listChi);
        ListView lvkc = (ListView) findViewById(R.id.lvkc);
        lvkc.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_chi, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
             case R.id.themtlc:
                 Intent intent=new Intent(KhoanChi.this, Add_TheLoaiChi.class);
                 startActivityForResult(intent, OPEN_THELOAICHI_DIALOG);
                     break;
            case R.id.xemtlc:
                 startActivity(new Intent(KhoanChi.this,Ds_TheLoaiChi.class));
                     break;
        }

        return super.onOptionsItemSelected(item);
    }
}


