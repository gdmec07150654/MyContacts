package com.example.administrator.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yingzi on 2016/10/20.
 */
public class MyDB extends SQLiteOpenHelper {

    private static String DB_NAME="My_DB.db";
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;

    public MyDB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // 执行SQLite数据库连接
    public SQLiteDatabase openConnection(){
        if(!db.isOpen()){
            db = getWritableDatabase();
        }
        return db;
    }
    // 关闭SQLite数据库连接操作
    public void closeConnection(){
        if(db != null && db.isOpen()){
            try {
                db.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 创建表
    public boolean createTable(String createTableSql){
        try {
            openConnection();
            db.execSQL(createTableSql);
        }catch(Exception e){
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
    /**
     * 添加操作
     * @param tableName  表名
     * @param values 集合对象表示要插入表中的记录
     */
    public boolean sava(String tableName, ContentValues values){
        try{
            openConnection();
            db.insert(tableName, null, values);
        }catch (Exception e){
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
    /**
     * 更新操作
     */
    public boolean update(String table,ContentValues values,String whereClause,String []whereArgs){
        try{
            openConnection();
            db.update(table,values,whereClause,whereArgs);
        }catch (Exception e){
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
    /**
     * 删除
     * @param deleteSql 对应删除的条件
     * @param obj[] 对应删除条件的值
     */
    public boolean delete(String table,String deleterSql,String obj[]){
        try{
            openConnection();
            db.delete(table, deleterSql, obj);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }

    /**
     * 查询操作
     * @param findSql 对应查询字段，如
     *      select * from person limit where name=?
     * @param obj  对应查询字段?占位参数的取值，如：
     *       new String[]
     *       String.valueOf(sName)
     */
    public Cursor find(String findSql,String obj[]){
        try{
            openConnection();
            Cursor cursor = db.rawQuery(findSql,obj);
            return cursor;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 判断表是否存在
     */
    public boolean isTableExits(String tablename){
        try{
            openConnection();
            String str = "select count(*)xcount from " + tablename;
            db.rawQuery(str,null).close();
        }catch (Exception e){
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
}

