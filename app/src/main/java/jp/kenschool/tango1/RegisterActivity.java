package jp.kenschool.tango1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

import static jp.kenschool.tango1.MyOpenHelper.*;

public class RegisterActivity extends AppCompatActivity {

    //フィールド――――――――――――――――
    TextView tvMsg = null;
    TextView tvErr = null;
    EditText inputJpn = null;
    EditText inputEng = null;
    BootstrapButton btnAdd = null;
    BootstrapButton btnReset = null;
    BootstrapButton btnBack = null;
    RadioGroup rGroup = null;

    ManageDB mdb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvMsg = findViewById(R.id.tv_msg_regist);
        tvErr = findViewById(R.id.tv_err_msg_regist);
        inputJpn = findViewById(R.id.ed_jpn_regist);
        inputEng = findViewById(R.id.ed_eng_regist);
        btnAdd = findViewById(R.id.btn_add_regist);
        btnReset = findViewById(R.id.btn_reset_regist);
        btnBack = findViewById(R.id.btn_back_regist);
        rGroup = findViewById(R.id.rbgroup_regist);
        mdb = new ManageDB(this);

        //カテゴリ取得
        ArrayList<String> categories = mdb.getCateList();

        //カテゴリに合わせてラジオボタン生成
        for(int i = 0; i < categories.size(); i++){
            RadioButton rb = new RadioButton(this);
            rb.setText(categories.get(i));
            rb.setTag(i+1);
            rGroup.addView(rb);
            if (i == 0){
                rb.setChecked(true); // 初期チェック
            }

        }

        //登録ボタン――――――――――――――――
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //errメッセージ準備
                StringBuilder errorMsg = new StringBuilder();
                tvErr.setText("");

                //入力チェック
                boolean inputCheck = true;
                String jpn = inputJpn.getText().toString();
                String eng = inputEng.getText().toString();

                if(jpn.equals("")){
                    errorMsg.append("日本語が入力されていません\n");
                    inputCheck = false;
                }else{
                    if(!Check.isJapanese(jpn)){
                        inputCheck = false;
                        errorMsg.append("日本語には、英数字は入力できません\n");
                    }
                }

                if(eng.equals("")){
                    errorMsg.append("英語が入力されていません\n");
                    inputCheck = false;
                }else{
                    if(!Check.isEnglish(eng)) {
                        inputCheck = false;
                        errorMsg.append("英語には、アルファベット以外入力できません\n");
                    }
                }

                //入力チェックが全て通れば実行
                if(inputCheck){

                    int checkedID = rGroup.getCheckedRadioButtonId(); //チェックされたID取得
                    int cateID = (int) findViewById(checkedID).getTag(); //IDでfindViewして、そのタグでcateID取得

                    String sql = "INSERT INTO "+ TABLE_WORDS +" (jpn, eng, cate_id, user_id) VALUES (?, ?, ?, ?)";

                    Object[] bind = {
                            jpn,
                            eng,
                            cateID,
                            UserData.id
                    };

                    //write()メソッドにSQL文とバインド内容の配列を渡す。返り値で成功か判別。
                    if (mdb.write(sql, bind)) {
                        String str = "日本語:" + jpn + "  英語:" + eng + "  を追加しました\n";
                        tvMsg.setText(str);
                        inputJpn.setText("");
                        inputEng.setText("");
                    } else {
                        errorMsg.append("登録に失敗しました\n");
                    }

                }

                tvErr.setText(errorMsg); //エラーメッセージ表示
            }
        });

        // リセットボタン――――――――――――――――
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputJpn.setText("");
                inputEng.setText("");
                rGroup.clearCheck();
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
}
