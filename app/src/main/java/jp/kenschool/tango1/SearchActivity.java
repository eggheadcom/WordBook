package jp.kenschool.tango1;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import static jp.kenschool.tango1.MyOpenHelper.*;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    // フィールド――――――――――――――――
    TextView tvDebug = null;
    TextView tvDelete = null;
    EditText edKeyword = null;
    BootstrapButton btnSearch = null;
    BootstrapButton btnReset = null;
    BootstrapButton btnBack = null;
    Button btnDelete = null;
    CheckBox check = null;
    RadioGroup rGroup = null;
    ListView listView = null;

    ArrayList<Word> data = null;
    ManageDB mdb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // *** FindView ***
        tvDebug = findViewById(R.id.tv_debug_search);
        tvDelete = findViewById(R.id.tv_delete_search);
        edKeyword = findViewById(R.id.ed_key_search);
        btnSearch = findViewById(R.id.btn_do_search);
        btnReset = findViewById(R.id.btn_reset_search);
        btnDelete = findViewById(R.id.btn_delete_search);
        btnBack = findViewById(R.id.btn_back_search);
        check = findViewById(R.id.check_category_search);
        rGroup = findViewById(R.id.rbgroup_search);
        listView = findViewById(R.id.lv_search);

        mdb = new ManageDB(this);

        //カテゴリ項目のチェック判定
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // チェックステータス取得
                if(check.isChecked()){
                    visibleCategory(); //カテゴリ表示メソッド
                }
                else{
                    rGroup.removeAllViews(); //チェック外れたらラジオグループをクリア
                }
            }
        });

        /*―――――――――――――――――――――――――――――――――――――――――――――
            検索ボタン
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
        // 検索ボタン――――――――――――――――
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sql = createSelect();        //SQLの生成
                String[] lvData = getListData(sql); //実行して結果取得
                setListView(lvData);                //リストビューにセット

            }
        });

        /*―――――――――――――――――――――――――――――――――――――――――――――
            リストビュー
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // clickされた位置のWordを取得
                Word selected = data.get(position);
                String msg = selected.getJpn() + ":"+selected.getEng()+" を削除してよろしいですか";

                String[] sql  = {  //削除sql
                        "DELETE FROM " + TABLE_WORDS
                                + " WHERE word_id = " + selected.getWordID()
                };

                deleteAlert(msg, sql);  //削除確認用のアラートダイアログ
            }
        });

        /*―――――――――――――――――――――――――――――――――――――――――――――
            リセットボタン
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edKeyword.setText("");
                check.setChecked(false);
                rGroup.clearCheck();
            }
        });

        /*―――――――――――――――――――――――――――――――――――――――――――――
            全削除ボタン
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = data.size() + "件のデータを削除してよろしいですか？";
                String[] sql = new String[data.size()];

                for (int i = 0; i<data.size(); i++) {
                    sql[i] = "DELETE FROM " + TABLE_WORDS
                            + " WHERE word_id = " + data.get(i).getWordID();
                }
                deleteAlert(msg, sql);
            }
        });

        /*―――――――――――――――――――――――――――――――――――――――――――――
            戻るボタン
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /*―――――――――――――――――――――――――――――――――――――――――――――
        Select文の生成
    ―――――――――――――――――――――――――――――――――――――――――――――――*/
    private String createSelect(){

        String key = edKeyword.getText().toString();
        String category = "";

        if(check.isChecked()){
            int checkedID = rGroup.getCheckedRadioButtonId(); //チェックされたID取得
            int cateID = (int) findViewById(checkedID).getTag();
            category = "AND cate_id = " + cateID;
        }
        String where = " WHERE "
                + "(jpn LIKE " + "'%" + key + "%'"
                + "OR "
                + "eng LIKE " + "'%" + key + "%')"
                + category;
        String sql = "SELECT * FROM " + TABLE_WORDS + where;

        return sql;
    }

    /*―――――――――――――――――――――――――――――――――――――――――――――
        リストデータ生成メソッド（SQLの結果をString配列にして返す）
    ―――――――――――――――――――――――――――――――――――――――――――――――*/
    private String[] getListData(String sql){

        //sql実行して結果を配列に代入
        data = mdb.read(sql);
        String[] lvData = new String[data.size()];

        if(data.size() == 0){                           //1件もないとき
            btnDelete.setVisibility(View.INVISIBLE);
        }else{
            btnDelete.setVisibility(View.VISIBLE);      //削除ボタン表示
            tvDelete.setText("\n*リスト項目をタップで１件ずつ削除可能");
            for(int i = 0; i<data.size(); i++){
                lvData[i] = data.get(i).getJpn() + " : " + data.get(i).getEng();
            }
        }
        return lvData;
    }

    /*―――――――――――――――――――――――――――――――――――――――――――――
        リストビューにセットするメソッド
    ―――――――――――――――――――――――――――――――――――――――――――――――*/
    private void setListView(String[] lvData){
        // simple_list_item_1 は、 もともと用意されている定義済みのレイアウトファイルのID
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, lvData);
        listView.setAdapter(arrayAdapter);
        tvDebug.setVisibility(View.VISIBLE); //Delete上のテキストビューを表示
    }

    /*―――――――――――――――――――――――――――――――――――――――――――――
        カテゴリ表示メソッド
    ―――――――――――――――――――――――――――――――――――――――――――――――*/
    private void visibleCategory(){
        //カテゴリ取得してラジオボタン生成
        ArrayList<String> categories = mdb.getCateList();
        for(int i = 0; i < categories.size(); i++){
            RadioButton rb = new RadioButton(this);
            rb.setText(categories.get(i));
            rb.setTag(i+1);
            rGroup.addView(rb);
        }
    }

    /*―――――――――――――――――――――――――――――――――――――――――――――
         削除アラートダイアログ
         引数１でメッセージ、引数２で削除用のsql文配列を受ける
    ―――――――――――――――――――――――――――――――――――――――――――――――*/
    private void deleteAlert(String msg, final String[] sql){

        AlertDialog.Builder alBuilder = new AlertDialog.Builder(SearchActivity.this);
        alBuilder.setTitle("Delete");
        alBuilder.setMessage(msg);
        alBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                for(String s : sql){
                    mdb.write(s);
                }
                //listView再生成
                String sql = createSelect();
                String[] lvData = getListData(sql);
                setListView(lvData);
            }
        });
        alBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //キャンセルの時は特になし
            }
        });
        AlertDialog al = alBuilder.create();
        al.show();
    }
}
