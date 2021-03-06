package com.example.administrator.mycontacts;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyContactsActivity extends AppCompatActivity {

    private ListView listView;
    private BaseAdapter listViewAdapter;
    private User users[];//通讯录用户
    private int selectItem = 0;//当前选择的数据项
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("通讯录");
        listView = (ListView) findViewById(R.id.listView);
        loadContacts();
    }

    private void loadContacts() {
        // 获取所有通讯录联系人
        ContactsTable ct = new ContactsTable(MyContactsActivity.this);
        users = ct.getAllUser();
        // 为listView列表创建适配器
        listViewAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return users.length;
            }

            @Override
            public Object getItem(int position) {
                return users[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    TextView textView = new TextView(MyContactsActivity.this);
                    textView.setTextSize(22);
                    convertView = textView;
                }
                String mobile = users[position].getMobile() == null ? "" : users[position].getMobile();
                ((TextView) convertView).setText(users[position].getName() + "————" + mobile);
                if(position == selectItem){
                    convertView.setBackgroundColor(Color.YELLOW);
                }else{
                    convertView.setBackgroundColor(0);
                }
                return convertView;
            }
        };
        // 设置listView控件的适配器
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem = position;
                listViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "添加");
        menu.add(0, 2, 0, "编辑");
        menu.add(0, 3, 0, "查看信息");
        menu.add(0, 4, 0, "删除");
        menu.add(0, 5, 0, "查询");
        menu.add(0, 6, 0, "导入到手机电话簿");
        menu.add(0, 7, 0, "退出");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sHint;
        switch (item.getItemId()){
            case 1:
                // 添加
                Log.i("aaa","1");
                Intent intent = new Intent(MyContactsActivity.this, AddContactsActivity.class);
                startActivity(intent);
                break;
            case 2:
                // 编辑——根据数据库ID判断当前记录是或否可以操作
                if(users[selectItem].getId_DB() > 0){
                    intent = new Intent(MyContactsActivity.this, UpdateContactsActivity.class);
                    startActivity(intent);
                }else{
                    sHint = "无结果记录，无法操作!";
                    Toast.makeText(MyContactsActivity.this, sHint, Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                // 查看信息
                if(users[selectItem].getId_DB() > 0){
                    intent = new Intent(MyContactsActivity.this, ContactsMessageActivity.class);
                    intent.putExtra("user_ID",users[selectItem].getId_DB());
                    startActivity(intent);
                }else{
                    sHint = "无结果记录，无法操作!";
                    Toast.makeText(MyContactsActivity.this, sHint, Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                // 删除
                if(users[selectItem].getId_DB() > 0){
                    delete();
                }else{
                    sHint = "无结果记录，无法操作";
                    Toast.makeText(MyContactsActivity.this, sHint, Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                // 查询
                new FindDialog(this).show();
                break;
            case 6:
                // 导入到手机电话簿
                if(users[selectItem].getId_DB() > 0){
                    importPhone(users[selectItem].getName(), users[selectItem].getMobile());
                    sHint = "已经成功将'"+users[selectItem].getName()+"'导入电话簿";
                    Toast.makeText(MyContactsActivity.this, sHint, Toast.LENGTH_SHORT).show();
                }else{
                    sHint = "无结果记录，无法操作";
                    Toast.makeText(MyContactsActivity.this, sHint, Toast.LENGTH_SHORT).show();
                }
                break;
            case 7:
                // 退出
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新加载数据
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        // 刷新数据显示列表
        listViewAdapter.notifyDataSetChanged();
    }

    public class FindDialog extends Dialog{
        public FindDialog(Context context){
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.find);
            setTitle("联系人查询");
            Button find = (Button) findViewById(R.id.find);
            Button cancel = (Button) findViewById(R.id.cancel);
            find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText value = (EditText) findViewById(R.id.value);
                    ContactsTable ct = new ContactsTable(MyContactsActivity.this);
                    users = ct.findUserByKey(value.getText().toString());
                    for (int i = 0;i<users.length;i++) {
                        System.out.println("姓名是：" + users[i].getName() + "，电话是：" + users[i].getMobile());
                    }
                    listViewAdapter.notifyDataSetChanged();
                    selectItem = 0;
                    dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
    // 删除联系人
    public void delete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("系统信息");
        alert.setMessage("是否删除联系人？");
        alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactsTable ct = new ContactsTable(MyContactsActivity.this);
                // 删除联系人信息
                if(ct.deleteByUser(users[selectItem])){
                    // 重新获取数据
                    users = ct.getAllUser();
                    // 刷新列表
                    listViewAdapter.notifyDataSetChanged();
                    selectItem = 0;
                    Toast.makeText(MyContactsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MyContactsActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
    public void importPhone(String name,String phone){
        // 系统通讯录ContentProvider的URI
        Uri phoneURL = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        // 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri = this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        // 向data表插入姓名
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        this.getContentResolver().insert(phoneURL,values);
        // 向Data表插入电话号码
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURL, values);
    }
}
