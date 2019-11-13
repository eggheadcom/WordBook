package jp.kenschool.tango1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


//*** データベースヘルパークラス ***
public class MyOpenHelper extends SQLiteOpenHelper {

    // DB名等の定数
    static final int DB_VERSION = 14;
    static final String DB_NAME = "wordbook.db";
    static final String TABLE_CATEGORIES = "categories";
    static final String TABLE_WORDS = "words";
    static final String TABLE_USERS = "users";


    // テーブル作成SQL（定義はここに追加。半角スペースに注意）
    private static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + TABLE_CATEGORIES
                    + " ("
                    + "cate_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "cate_name TEXT  NOT NULL)";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS
                    + " ("
                    + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_name TEXT NOT NULL,"
                    + "password TEXT NOT NULL"
                    //+ ",created_by TIMESTAMP DEFAULT CURRENT_DATE"
                    // + "created_by TIMESTAMP DEFAULT (DATETIME('now','localtime')),"
                    + ")";

    private static final String SQL_CREATE_WORDS =
            "CREATE TABLE " + TABLE_WORDS
                    + " ("
                    + "word_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "jpn TEXT NOT NULL," //UNIQUE外した
                    + "eng TEXT NOT NULL,"
                    + "cnt INTEGER DEFAULT 0,"
                    + "correct INTEGER DEFAULT 0,"
                    + "previous INTEGER DEFAULT 0,"
                    + "rate INTEGER DEFAULT 0,"
                    + "created_by DATE DEFAULT CURRENT_DATE,"
                 // + "created_by TIMESTAMP DEFAULT (DATETIME('now','localtime')),"
                    + "cate_id INTEGER DEFAULT 1,"
                    + "user_id INTEGER NOT NULL,"
                    + "FOREIGN KEY(cate_id) REFERENCES categories(cate_id),"
                    + "FOREIGN KEY(user_id) REFERENCES users(user_id)"
                    + ")";


    // テーブル削除SQL
    public static final String SQL_DELETE_CATEGORIES =
            "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;

    public static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + TABLE_USERS;

    public static final String SQL_DELETE_WORDS =
            "DROP TABLE IF EXISTS " + TABLE_WORDS;


    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
        コンストラクタ
     ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    // 呼び出し元のアクティビティを引数で受け、親のコンストラクタを呼び出す
    // 渡す引数２はDB名、引数３は基本的にはnullでOK、引数４はバージョン
    MyOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
        生成
     ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    // 指定されたデータベースが存在しない時にだけ呼ばれる（存在してると実行されない）
    // Create文やデフォルトで挿入しておきたい初期レコードなど記述
    @Override
    public void onCreate(SQLiteDatabase db){

        Log.e("egg", "onCreate: start地点");

        //テーブル作成
        db.execSQL(SQL_CREATE_CATEGORIES);
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_WORDS);

        Log.i("egg", "onCreate: テーブル作成と初期レコードの間");

        //初期レコード
        String insertSql =
                "INSERT INTO " + TABLE_CATEGORIES + " VALUES " +
                        " (null, '未分類')," +
                        " (null, '名詞')," +
                        " (null, '動詞')," +
                        " (null, '形容詞')," +
                        " (null, '副詞');";
        db.execSQL(insertSql);

//        String insertSql =
//                "INSERT INTO " + TABLE_CATEGORIES + " VALUES " +
//                " (null, 'カテゴリ未設定')," +
//                " (null, '生き物')," +
//                " (null, '食べ物')," +
//                " (null, '乗り物')," +
//                " (null, '感情');";
//        db.execSQL(insertSql);

        Log.i("egg", "onCreate: insert users の前");
        insertSql =
                "INSERT INTO " + TABLE_USERS +
                        " (user_name, password)" + " VALUES " +
                        " ('user1','aaa')," +
                        " ('user2','aaa')," +
                        " ('user3','aaa')," +
                        " ('user4','aaa')," +
                        " ('user5','aaa')";
        db.execSQL(insertSql);

        Log.i("egg", "onCreate: insert wordsの前");
        insertSql =
                "INSERT INTO " + TABLE_WORDS +
                        " (jpn, eng, cate_id, user_id)" + " VALUES" +
                        " ('知識', 'knowledgee', 2, 5)," +
                        " ('振る舞い', ' behavior', 2, 1)," +
                        " ('要因', 'factor', 2, 2)," +
                        " ('消費者', 'consumer', 2, 5)," +
                        " ('識別子', 'identifier', 2, 1)," +
                        " ('改良', 'improvement', 2, 3)," +
                        " ('提供する', 'provide', 3, 2)," +
                        " ('勧める', 'recommend', 3, 3)," +
                        " ('謝る', 'apologize', 3, 1)," +
                        " ('減少する', 'decrease', 3, 5)," +
                        " ('許す', 'allow', 3, 4)," +
                        " ('必要とする', 'require', 3, 2)," +
                        " ('効率的な', 'efficient', 4, 2)," +
                        " ('適当な', 'suitable', 4, 2)," +
                        " ('有害な', 'harmful', 4, 2)," +
                        " ('個々の', 'individual', 4, 2)," +
                        " ('利用できる', 'available', 4, 2)," +
                        " ('絶対的に', 'absolutely', 5, 2)," +
                        " ('頻繁に', 'frequently', 5, 2)," +
                        " ('実際に', 'actually', 5, 2)," +
                        " ('個々の', 'individual', 5, 3)";
        db.execSQL(insertSql);

        Log.i("egg", "onCreate: end");
    }

    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
       バージョン更新メソッド
    ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    // このオブジェクトを使ってDBをオープンすると、コンストラクタで渡した引数４のバージョンと
    // 実際のデータベースのバージョンが違う時に呼び出され、更新される。
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_CATEGORIES);
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_WORDS);
        onCreate(db);
    }



}