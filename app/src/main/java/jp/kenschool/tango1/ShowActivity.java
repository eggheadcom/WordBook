package jp.kenschool.tango1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

import static jp.kenschool.tango1.MyOpenHelper.*;

public class ShowActivity extends AppCompatActivity {

    // フィールド――――――――――――――――
    TextView tv = null;
    BootstrapButton btnShowAll = null;
    BootstrapButton btnShowByUser = null;
    BootstrapButton btnBack = null;

    ManageDB mdb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        //findView
        tv = findViewById(R.id.tv_list_show);
        btnShowAll = findViewById(R.id.btn_all_show);
        btnShowByUser = findViewById(R.id.btn_user_show);
        btnBack = findViewById(R.id.btn_back_show);


        //DBオブ生成
        mdb = new ManageDB(this);

        // 全表示ボタン――――――――――――――――
        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全表示
                String sql = "SELECT * FROM " + TABLE_WORDS;
                ArrayList<Word> data = mdb.read(sql);
                showData(data);
            }
        });

        // ユーザーボタン――――――――――――――――
        btnShowByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ユーザーでの表示
                String sql = "SELECT * FROM " + TABLE_WORDS + " WHERE user_id =" + UserData.id;
                ArrayList<Word> data = mdb.read(sql);
                showData(data);
            }
        });


        // 戻るボタン――――――――――――――――
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //表示メソッド
    private void showData(ArrayList<Word> data){
        String str = "";
        for(Word d : data){
            str += d.toString();
            str += "\n";
        }
        tv.setText(str);
    }

}
