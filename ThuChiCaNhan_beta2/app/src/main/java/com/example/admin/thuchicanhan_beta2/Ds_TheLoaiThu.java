package com.example.admin.thuchicanhan_beta2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Ds_TheLoaiThu extends AppCompatActivity {


    List<InforData_Thu> list=new ArrayList<InforData_Thu>();
    InforData_Thu dataClick=null;
    SQLiteDatabase database=null;
    Show_List_TheLoaiThu adapter=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds__the_loai_thu);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Danh sách thể loại thu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Ds_TheLoaiThu.this,KhoanThu.class));
            }
        });

        updateUI();



    }



    public void updateUI()
    {
        database=openOrCreateDatabase("mydataThu.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        if(database!=null)
        {

            Cursor cursor=database.query("tblTheLoaiThu", null, null, null, null, null, null);
            startManagingCursor(cursor);
            InforData_Thu header=new InforData_Thu();
            header.setField1("STT");
            header.setField2("Tên Thể Loại Thu");
            list.add(header);
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                InforData_Thu data=new InforData_Thu();
                data.setField1(cursor.getInt(0));
                data.setField2(cursor.getString(1));
                list.add(data);
                cursor.moveToNext();
            }
            cursor.close();
            adapter = new Show_List_TheLoaiThu(Ds_TheLoaiThu.this, R.layout.show_list_theloaithu, list);
            final ListView lv= (ListView) findViewById(R.id.listViewShowDataThu);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    // TODO Auto-generated method stub

                    Intent intent=new Intent(Ds_TheLoaiThu.this, Add_TheLoaiThu.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("KEY", 1);
                    bundle.putString("getField1", list.get(arg2).getField1().toString());
                    bundle.putString("getField2", list.get(arg2).getField2().toString());
                    intent.putExtra("DATA", bundle);
                    dataClick=list.get(arg2);
                    startActivityForResult(intent, KhoanThu.OPEN_THELOAITHU_DIALOG);

                }
            });
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    final InforData_Thu data=list.get(arg2);
                    final int pos=arg2;
                    Toast.makeText(Ds_TheLoaiThu.this, "Edit-->"+data.toString(), Toast.LENGTH_LONG).show();
                    AlertDialog.Builder b=new AlertDialog.Builder(Ds_TheLoaiThu.this);
                    b.setTitle("Remove");
                    b.setMessage("Xóa ["+data.getField2() +"] hả?");
                    b.setPositiveButton("Có", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            int n=database.delete("tblTheLoaiThu", "id=?", new String[]{data.getField1().toString()});
                            if(n>0) {
                                Toast.makeText(Ds_TheLoaiThu.this, "Remove ok", Toast.LENGTH_LONG).show();
                                list.remove(pos);
                                adapter.notifyDataSetChanged();
                           }
                           else
                           {
                                Toast.makeText(Ds_TheLoaiThu.this, "Remove not ok", Toast.LENGTH_LONG).show();
                           }
                        }
                    });
                    b.setNegativeButton("Không", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                        }
                    });
                    b.show();
                    return false;
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==KhoanThu.SEND_DATA_FROM_THELOAITHU_ACTIVITY)
        {
            Bundle bundle=data.getBundleExtra("DATA_TheLoaiThu");
            String f2=bundle.getString("name");
            String f1=dataClick.getField1().toString();
            ContentValues values=new ContentValues();
            values.put("name", f2);
            if(database!=null)
            {
                int n=database.update("tblTheLoaiThu", values, "id=?", new String[]{f1});
                if(n>0)
                {
                    Toast.makeText(Ds_TheLoaiThu.this, "update ok ok ok ", Toast.LENGTH_LONG).show();
                    dataClick.setField3(f2);
                    if(adapter!=null)
                        adapter.notifyDataSetChanged();
                }
            }
        }
    }

}
