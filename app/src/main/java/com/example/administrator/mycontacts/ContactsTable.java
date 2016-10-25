package com.example.administrator.mycontacts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Vector;

/**
 * Created by yingzi on 2016/10/20.
 */
public class ContactsTable {
    private final static String TABLENAME = "contactsTable";
    private MyDB db;
    // 联系人表操作
    public ContactsTable(Context context){
        db = new MyDB(context);
        if(!db.isTableExits(TABLENAME)){
            String createTableSql = "CREATE TABLE IF NOT EXISTS " +
                    TABLENAME + "(id_DB integer " +
                    "primary key AUTOINCREMENT," +
                    User.NAME + " VARCHAR," +
                    User.MOBILE + " VARCHAR," +
                    User.QQ + " VARCHAR," +
                    User.DANWEI + " VARCHAR," +
                    User.ADDRESS + " VARCHAR)";
            db.createTable(createTableSql);
        }
    }
    // 添加数据到联系人表
    public boolean addData(User user){
        ContentValues values = new ContentValues();
        values.put(User.NAME, user.getName());
        values.put(User.ADDRESS, user.getAddress());
        values.put(User.DANWEI, user.getDanwei());
        values.put(User.MOBILE, user.getDanwei());
        values.put(User.QQ, user.getQq());
        Log.i("aaa","3");
        return db.sava(TABLENAME, values);
    }
    // 获取联系人表数据
    public User[] getAllUser(){
        Vector<User> v = new Vector<User>();
        Cursor cursor = null;
        try{
            cursor = db.find("select * from "+ TABLENAME,null);
            while(cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                v.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        if(v.size() > 0){
            return v.toArray(new User[]{});
        }else{
            User [] users = new User[1];
            User user = new User();
            user.setName("无结果");
            users[0] = user;
            return users;
        }
    }
    // 根据数据库改变主键ID来获取联系人
    public User getUserByID(int id){
        Cursor cursor = null;
        try{
            cursor = db.find("select * from " + TABLENAME + " where id_DB = ?", new String[]{id + ""});
            User temp = new User();
            cursor.moveToNext();
            temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
            temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
            temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
            temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
            temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
            temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
            return temp;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        return null;
    }
    // 模糊查询
    public User[] findUserByKey(String key) {
        Vector<User> v = new Vector<User>();
        Cursor cursor = null;
        try {
            cursor = db.find("select * from " + TABLENAME + " where "
                    + User.NAME + "like '%" + key + "%'" +
                    " or " + User.MOBILE + "like '%" + key + "%'" +
                    " or " + User.QQ + "like '%" + key + "%'", null);
            while (cursor.moveToNext()) {
                User temp = new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                v.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.closeConnection();
        }
        if (v.size() > 0) {
            return v.toArray(new User[]{});
        } else {
            User[] users = new User[1];
            User user = new User();
            user.setName("无结果");
            users[0] = user;
            return users;
        }
    }
    // 修改联系人数据
    public boolean updateUser(User user){
        ContentValues values = new ContentValues();
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.QQ, user.getQq());
        values.put(User.DANWEI, user.getDanwei());
        values.put(User.ADDRESS, user.getAddress());
        return db.update(TABLENAME, values, "id_DB=?", new String[]{user.getId_DB() + ""});
    }
    // 删除联系人
    public boolean deleteByUser(User user){
        return db.delete(TABLENAME, "id_DB=?", new String[]{user.getId_DB() + ""});
    }
}
