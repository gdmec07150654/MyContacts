package com.example.administrator.mycontacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateContactsActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText mobileEditText;
    private EditText qqEditText;
    private EditText danweiEditText;
    private EditText addressEditText;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        setTitle("修改联系人");
        nameEditText = (EditText) findViewById(R.id.name);
        mobileEditText = (EditText) findViewById(R.id.mobile);
        qqEditText = (EditText) findViewById(R.id.qq);
        danweiEditText = (EditText) findViewById(R.id.danwei);
        addressEditText = (EditText) findViewById(R.id.address);
        // 将要修改的联系人数据赋值到用户界面进行显示
        Bundle localBundle = getIntent().getExtras();
        int id = localBundle.getInt("user_ID");
        ContactsTable ct = new ContactsTable(this);
        user = ct.getUserByID(id);
        nameEditText.setText(user.getName());
        mobileEditText.setText(user.getMobile());
        qqEditText.setText(user.getQq());
        danweiEditText.setText(user.getDanwei());
        addressEditText.setText(user.getAddress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "保存");
        menu.add(0, 2, 0, "取消");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:// 保存
                if(!nameEditText.getText().equals("")){
                    user.setName(nameEditText.getText().toString());
                    user.setMobile(mobileEditText.getText().toString());
                    user.setQq(qqEditText.getText().toString());
                    user.setDanwei(danweiEditText.getText().toString());
                    user.setAddress(addressEditText.getText().toString());
                    ContactsTable ct = new ContactsTable(UpdateContactsActivity.this);
                    if(ct.updateUser(user)){
                        Toast.makeText(UpdateContactsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UpdateContactsActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateContactsActivity.this, "数据不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
