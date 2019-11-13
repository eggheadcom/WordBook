package jp.kenschool.tango1;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static jp.kenschool.tango1.MyOpenHelper.TABLE_CATEGORIES;

import java.util.ArrayList;

public class ManageDB {

    // フィールド――――――――――――――――
    private MyOpenHelper helper = null;
    private SQLiteDatabase db = null;

    private final int WORD_ID = 0;
    private final int  JPN = 1;
    private final int  ENG = 2;
    private int CNT = 3;
    private int CORRECT = 4;
    private int LAST_ANS = 5;
    private int RATE = 6;
    private int CREATED_BY = 7;
    private int CATE_ID = 8;
    private int USER_ID = 9;

    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
       コンストラクタ
     ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    //DBオブジェクトの生成 (contextを受け取りそれを元にヘルパー生成)
    ManageDB(Context context) {
        helper = new MyOpenHelper(context);
    }


    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
         SELECT文の結果を全て返す (MyDatasetのArrayListが返り値)
     ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    public ArrayList<Word> read(String sql){

        ArrayList<Word> data = new ArrayList<>();
        Cursor c = null;
        Log.e("egg", "read() : try 開始直前");

        try {
            db = helper.getReadableDatabase();
            Log.e("egg", "read() : getReadableDatabase直後");
            c = db.rawQuery(sql, null);

            Log.e("egg", "read() : rawQuery直後");
            while (c.moveToNext()) {

                Word record = new Word();

                record.setWordID(c.getInt(WORD_ID));
                record.setJpn(c.getString(JPN));
                record.setEng(c.getString(ENG));
                record.setCnt(c.getInt(CNT));
                record.setCorrect(c.getInt(CORRECT));
                record.setPrevious(c.getInt(LAST_ANS));
                record.setRate(c.getInt(RATE));
                record.setCreatedBy(c.getString(CREATED_BY));
                record.setCateID(c.getInt(CATE_ID));
                record.setUserID(c.getInt(USER_ID));

                data.add(record);
            }

        } catch (SQLException e) {
            Log.e("egg", "SQLException : read()");
        } finally {
            if(c != null) c.close();
            if(db != null) db.close();
        }

        return data;
    }

     /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
         SELECT文の結果をArrayList<String>で返す
         ハッシュマップの方がいい？
         要検討
     ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
     public ArrayList<String> read(String sql, int column){

         ArrayList<String> data = new ArrayList<>();
         Cursor c = null;

         try {
             db = helper.getReadableDatabase();
             c = db.rawQuery(sql, null);

             while (c.moveToNext()) {
                 for(int i = 0; i<column; i++){
                    data.add(c.getString(i));
                 }
             }

         } catch (SQLException e) {
             Log.e("egg", "SQLException : read(sql, column)");
         } finally {
             if(c != null) c.close();
             if(db != null) db.close();
         }

         return data;
     }

    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
         INSERT, UPDATE, DELETE
     ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    public boolean write(String sql){

        try {
            db = helper.getWritableDatabase();
            db.execSQL(sql); //返り値は？
            return true;

        }catch (SQLException e) {
            Log.e("egg", "SQLException : write()");
            return false;

        } finally {
            if(db != null)  db.close();
        }

    }

    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
        INSERT, UPDATE, DELETE  （bind有りversion）
    ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    public boolean write(String sql, Object[] bind){

//      String insertSql = "INSERT INTO " + Word.TABLE_NAME + " (name,age) values (?,?);";
//      Object[] bind = {strIn, intIn};
        Log.e("egg", "write(): ");
        try {
            db = helper.getWritableDatabase();
            db.execSQL(sql, bind); //返り値は？
            return true;

        }catch (SQLException e) {
            Log.e("egg", "SQLException : write()");
            return false;

        } finally {
            if(db != null)  db.close();
        }
    }


    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
        カウント
    ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    public int getCount(String table, String where) {
        //調整
        Log.e("egg", "getCount: start");
        String sql = "SELECT * FROM " + table + where;
        int cnt = read(sql).size();
        Log.e("egg", "getCount: end");
        return cnt;
    }


    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
        Categoryリスト
    ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    public ArrayList<String> getCateList() {
        //まだ
        Log.e("egg", "getCateList: ");
        String sql = "SELECT cate_name FROM " + TABLE_CATEGORIES;
        return read(sql,1);
    }




}