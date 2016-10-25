package com.example.administrator.mycontacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by yingzi on 2016/10/20.
 */
public class ContactsMessageActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView mobileTextView;
    private TextView qqTextView;
    private TextView danweiTextView;
    private TextView addressTextView;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        nameTextView = (TextView) findViewById(R.id.name);
        mobileTextView = (TextView) findViewById(R.id.mobile);
        qqTextView = (TextView) findViewById(R.id.qq);
        danweiTextView = (TextView) findViewById(R.id.danwei);
        addressTextView = (TextView) findViewById(R.id.address);

        Bundle localBundle = getIntent().getExtras();
        int id = localBundle.getInt("user_ID");
        ContactsTable ct = new ContactsTable(this);
        user = ct.getUserByID(id);
        nameTextView.setText("姓名："+user.getName());
        mobileTextView.setText("电话："+user.getMobile());
        qqTextView.setText("QQ："+user.getQq());
        danweiTextView.setText("单位："+user.getDanwei());
        addressTextView.setText("地址："+user.getAddress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "返回");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
