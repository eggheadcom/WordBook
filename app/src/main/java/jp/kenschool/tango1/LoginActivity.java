package jp.kenschool.tango1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import static jp.kenschool.tango1.MyOpenHelper.*;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    //フィールド
    TextView tvErr = null;
    EditText edName = null;
    EditText edPass = null;
    EditText edPassAgain = null;
    BootstrapButton btnLogin = null;
    BootstrapButton btnNewUser = null;
    Button btnPassAgain = null;
    Button btnCancel = null;

    String inputName;
    String inputPass;
    String inputPassAgain;
    String userName;
    String userPass;
    String userId;

    ManageDB mdb = null;
    StringBuilder errMsg = null;
    boolean inputCheck;
    SharedPreferences pref = null;
    SharedPreferences.Editor editor = null;

    final int VIEW_NOMAL = 1;
    final int VIEW_PASS_CHECK = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //findView群
        tvErr = findViewById(R.id.tv_debug_login);
        tvErr.setTextColor(Color.RED);
        edName  = findViewById(R.id.ed_login_name);
        edPass = findViewById(R.id.ed_login_pass);
        edPassAgain = findViewById(R.id.ed_login_pass2);
        btnLogin = findViewById(R.id.btn_login_login);
        btnNewUser = findViewById(R.id.btn_new_user_login);
        btnPassAgain = findViewById(R.id.btn_again_login);
        btnCancel = findViewById(R.id.btn_cancel_login);



        mdb = new ManageDB(this);
        errMsg = new StringBuilder();
        inputCheck = true;

        pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        userName = pref.getString("name","");
        userPass = pref.getString("pass","");
        userId = pref.getString("id","");
        edName.setText(userName);
        edPass.setText(userPass);

        //ログインボタン
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //データ読み込み
                setInputData();

                //ユーザー名とパスを使用してSQL生成
                if(inputCheck) {
                    String sql = "SELECT user_id" +
                            " FROM " + TABLE_USERS +
                            " WHERE user_name = '" + inputName
                            +"' AND password = '" + inputPass + "'";

                    ArrayList<String> result = mdb.read(sql, 1);

                    if(result.size() != 0) {
                        userId = result.get(0); //結果からIDをセット

                        //プリファレンスに登録
                        pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        editor = pref.edit();

                        editor.putString("name", inputName);
                        editor.putString("pass", inputPass);
                        editor.putString("id", userId);
                        editor.commit();

                        UserData.name = inputName;
                        UserData.id = userId;
                        UserData.init = true;

                        //メインアクティビティへ遷移
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        finish();

                    }else { //結果が0件
                        errMsg.append("ユーザー名かパスワードが間違っています\n");
                        edName.setText("");
                        edPass.setText("");
                    }
                }

                //エラーメッセージを表示
                tvErr.setText(errMsg.toString());
            }
        });

        //新規登録ボタン
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setInputData(); //データ取得

                if(inputCheck){

                    //SQL作成
                    String sql = "SELECT user_name " +
                            " FROM " + TABLE_USERS +
                            " WHERE user_name = '" + inputName +"'";

                    ArrayList<String> result = mdb.read(sql, 1);

                    if(result.size() != 0){
                        errMsg.append("既に使用されているユーザー名です\n");

                    }else{
                        //パス再入力モードに
                        changeView(VIEW_PASS_CHECK);
                    }
                }
                tvErr.setText(errMsg.toString());

            }
        });

        //パス再入力OKボタン
        btnPassAgain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                errMsg.delete(0,errMsg.length());
                inputPassAgain = edPassAgain.getText().toString();

                if(inputPassAgain.equals("")){
                    errMsg.append("入力されていません\n");

                }else if(!inputPassAgain.equals(inputPass)){
                    errMsg.append("パスワードが一致しません\n");

                }else{

                    //ここから本処理
                    String sql = "INSERT INTO " +
                            TABLE_USERS +
                            "(user_name, password) " +
                            "VALUES('" + inputName + "','" + inputPass +"')";

                    if(mdb.write(sql)){
                        changeView(VIEW_NOMAL);
                        errMsg.append("Name:"+inputName + " Pass:" + inputPass + "で登録完了!");
                    }
                    //結果を表示（ログイン促す）

                }

                tvErr.setText(errMsg.toString());

            }
        });


    //キャンセルボタン
    btnCancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            changeView(VIEW_NOMAL);
        }
    });

    }


    //EditTextの中身をチェックしてフィールドにセット
    private void setInputData(){

        errMsg.delete(0,errMsg.length());   //エラーメッセージをクリア
        inputCheck = true;                  //inputCheckを戻す

        //Name
        inputName = edName.getText().toString();
        if(!Check.isEnglish(inputName)){
            errMsg.append("Nameの入力が不正です\n");
            inputCheck = false;
        }

        //Pass
        inputPass = edPass.getText().toString();
        if(!Check.isEnglish(inputPass)){
            errMsg.append("Passの入力が不正です\n");
            inputCheck = false;
        }

    }

    private void changeView(int mode){

        switch (mode){

            case VIEW_NOMAL: //通常モード戻す

                edPassAgain.setVisibility(View.INVISIBLE);
                btnPassAgain.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.INVISIBLE);
                btnNewUser.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                edName.setEnabled(true);
                edPass.setEnabled(true);

                errMsg.delete(0,errMsg.length());
                tvErr.setText("");

                break;

            case VIEW_PASS_CHECK:   //パス再入力モードに変更

                errMsg.append("パスワードを再入力してください");
                edName.setEnabled(false);
                edPass.setEnabled(false);
                edPassAgain.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                btnNewUser.setVisibility(View.INVISIBLE);
                btnPassAgain.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                tvErr.setText(errMsg.toString());

            break;

        }

    }
}
