package com.study.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateDb;
    private Button btnAddData;
    private Button btnUpdateData;
    private Button btnDeleteData;
    private Button btnQueryData;

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase mDatabase;

    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();  // 初始化控件

        // btnCreateDb点击事件
        btnCreateDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });

        // btnAddData点击事件
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values=new ContentValues();
                // 开始组装第一条数据
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price",16.96);
                mDatabase.insert("Book",null,values);  // 插入第一条数据
                values.clear();
                // 开始组装第二条数据
                values.put("name","The Lost Symbol");
                values.put("author","Dan Brown");
                values.put("pages",510);
                values.put("price",19.95);
                mDatabase.insert("Book",null,values);  // 插入第二条数据
            }
        });

        // btnUpdateData点击事件
        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values=new ContentValues();
                values.put("price",10.99);
                mDatabase.update("Book",values,"name=?",
                        new String[]{"The Da Vinci Code"});
            }
        });

        // btnDeleteData点击事件
        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.delete("Book","pages>?",new String[]{"500"});
            }
        });

        // btnQueryData点击事件
        btnQueryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=mDatabase.query("Book",null,
                        null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        // 遍历Cursor对象，取出数据并打印
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author=cursor.getString(cursor.getColumnIndex("author"));
                        int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG,"book name is: "+name);
                        Log.d(TAG,"book author is: "+author);
                        Log.d(TAG,"book pages is: "+pages);
                        Log.d(TAG,"book price is: "+price);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }

    private void initView(){
        btnCreateDb=findViewById(R.id.btn_create_db);
        btnAddData=findViewById(R.id.btn_add_data);
        btnUpdateData=findViewById(R.id.btn_update_data);
        btnDeleteData=findViewById(R.id.btn_delete_data);
        btnQueryData=findViewById(R.id.btn_query_data);
        initSQLite();
    }

    private void initSQLite(){
        dbHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
        mDatabase=dbHelper.getWritableDatabase();
    }
}