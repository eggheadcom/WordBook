package jp.kenschool.tango1;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import static jp.kenschool.tango1.MyOpenHelper.*;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    TextView tv = null;
    BootstrapButton btnBack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tv = findViewById(R.id.tv_msg_info);


        ManageDB mdb = new ManageDB(this);

        int cnt = mdb.getCount(TABLE_WORDS, "");
        int userCnt = mdb.getCount(TABLE_WORDS, " WHERE user_id = " + UserData.id);


        StringBuilder sb = new StringBuilder();
        sb.append("■現在のVersion : ");
        sb.append(MainActivity.VERSION);
        sb.append("\n");
        sb.append("■総登録数は、");
        sb.append(cnt);
        sb.append("個です\n");
        sb.append("■");
        sb.append(UserData.name);
        sb.append("さんの登録件数は");
        sb.append(userCnt);
        sb.append("個です\n");
        tv.setText(sb.toString());

        // 戻るボタン――――――――――――――――
        btnBack = findViewById(R.id.btn_back_info);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
