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

public class KhoanThu extends AppCompatActivity {

    public static final int OPEN_THELOAITHU_DIALOG=1;
    public static final int SEND_DATA_FROM_THELOAITHU_ACTIVITY=2;
    SQLiteDatabase database = null;
    List<InforData> listThu = null;
    Show_List_TheLoaiChi adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoan_thu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KhoanThu.this, MainActivity.class));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KhoanThu.this, Add_KhoanThu.class));
            }
        });
        loadAllListThu();
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
            database=openOrCreateDatabase("mydataThu.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if(database!=null)
            {
                if(isTableExists(database,"tblTheLoaiThu"))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sqlTheLoaiThu="create table tblTheLoaiThu ("
                        +"id integer primary key autoincrement,"
                        +"name text)";
                database.execSQL(sqlTheLoaiThu);
                String sqlKhoanThu="create table tblKhoanThu ("
                        +"id integer primary key autoincrement,"
                        +"account text,"
                        +"dateadded date,"
                        +"money text,"
                        +"idTLT integer not null constraint idTLT references tblTheLoaiThu(id) on delete cascade)";
                database.execSQL(sqlKhoanThu);
                //Cách tạo trigger khi nhập dữ liệu sai ràng buộc quan hệ
                String sqlTrigger="create trigger fk_insert_khoanthu before insert on tblKhoanThu "
                        +" for each row "
                        +" begin "
                        +" 	select raise(rollback,'them du lieu tren bang tblKhoanThu bi sai') "
                        +" 	where (select id from tblTheLoaiThu where id=new.idTLT) is null ;"
                        +" end;";
                database.execSQL(sqlTrigger);
                Toast.makeText(KhoanThu.this, "OK OK", Toast.LENGTH_LONG).show();
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
            Toast.makeText(KhoanThu.this, "WelCome", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==SEND_DATA_FROM_THELOAITHU_ACTIVITY)
        {
            Bundle bundle= data.getBundleExtra("DATA_TheLoaiThu");
            String name=bundle.getString("name");
            ContentValues content=new ContentValues();
            content.put("name", name);
            if(database!=null)
            {
                long idTLT=database.insert("tblTheLoaiThu", null, content);
                if(idTLT==-1)
                {
                    Toast.makeText(KhoanThu.this,idTLT+" - "+" - "+name +" ==> insert error!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(KhoanThu.this,idTLT+" - " +" - "+name +" ==>insert OK!", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    public void loadAllListThu() {
        database = openOrCreateDatabase("mydataThu.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        if (database != null) {
            Cursor cur = database.rawQuery("select * from tblKhoanThu kt JOIN tblTheLoaiThu tlt ON kt.idTLT =tlt.id ", null);
            cur.moveToFirst();
            listThu = new ArrayList<InforData>();
            while (cur.isAfterLast() == false) {
                InforData d = new InforData();
                d.setField1(cur.getString(1));
                d.setField2(cur.getString(2));
                d.setField3(cur.getString(3));
                d.setField4(cur.getString(6));
                listThu.add(d);
                cur.moveToNext();
            }
            cur.close();
        }
        adapter = new Show_List_TheLoaiChi(KhoanThu.this, R.layout.show_list_theloaichi, listThu);
        ListView lvkc = (ListView) findViewById(R.id.lvkt);
        lvkc.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu_thu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.themtlt:
                Intent intent=new Intent(KhoanThu.this, Add_TheLoaiThu.class);
                startActivityForResult(intent, OPEN_THELOAITHU_DIALOG);
                break;
            case R.id.xemtlt:
                startActivity(new Intent(KhoanThu.this,Ds_TheLoaiThu.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
