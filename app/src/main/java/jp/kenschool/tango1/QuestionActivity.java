package jp.kenschool.tango1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.BootstrapProgressBarGroup;

import static jp.kenschool.tango1.MyOpenHelper.*;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    TextView tvQuestion = null;
    TextView tvCount = null;
    TextView edAnswer = null;
    TextView tvDebug = null;
    BootstrapButton btnCheck = null;
    BootstrapButton btnQuit = null;
    BootstrapProgressBar bar = null;
    BootstrapProgressBarGroup barGroup = null;

    int btnMode = 0;
    ManageDB mdb = null;
    ArrayList<Word> data = null;//単語リスト
    Word word = null;           //１単語
    int type = 0;               //言語タイプ

    int allCount = 0;   //総問題数（変化しない）
    int now = 1;        //何問目か
    int good = 0;       //正解数（このテストにおいての）
    int cnt = 0;        //DBのcnt （過去のこの問題を挑戦した回数)
    int correct = 0;    //DBのcorrect (過去にこの問題を正解した回数)
    int prev = 0;       //DBのprevious（前回正解した 0:不正解 1:正解）


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        tvQuestion = findViewById(R.id.tv_question_ques);
        tvCount = findViewById(R.id.tv_ques_count_test);
        edAnswer = findViewById(R.id.ed_ans_ques);
        tvDebug = findViewById(R.id.tv_debug_ques);
        btnCheck = findViewById(R.id.btn_check_next_ques);
        btnQuit = findViewById(R.id.btn_quit_ques);
        bar = findViewById(R.id.prgress_ques);
        barGroup = findViewById(R.id.prgress_grp_ques);

        //全画面からデータセットと言語タイプを受け取る
        Intent intent = getIntent();
        data = (ArrayList<Word>) intent.getSerializableExtra("data");
        type = intent.getIntExtra("lang", 1);

        mdb = new ManageDB(this);

        //debug------------------------------------------------
        String res = "結果:\n";
        res += data.get(0).toString();
        res += "\n";
        res += "件数:" + data.size() + "\n";
        res += "言語タイプ:" + type;
        //tvDebug.setText(res);
        //------------------------------------------------

        //問題数を代入
        allCount = data.size();
        barGroup.setMaxProgress(allCount);

        //現在の問題のWordデータを取得
        word = data.get(now-1);

        //問題を表示
        tvQuestion.setText(word.getLang(type)); //type(1=JPN,2=ENG)の値を持ってくる

        //問題のカウントを表示
        String str = allCount + "問中 "+ now +"問目";
        tvCount.setText(str);

        //解答ボタン――――――――――――――――
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnMode == 0){

                    //入力された答え
                    String userAns = edAnswer.getText().toString();
                    //実際の答え
                    String ans = word.getLang(type +1);

                    //正解判定と結果表示
                    if(userAns.equals(ans)){
                        tvDebug.setText("正解!! 天才!!");
                        //updateなどの処理
                        cnt = word.getCnt();
                        correct = word.getCorrect();
                        prev = word.getPrevious();
                        correct++;
                        good++;
                        prev = 1;
                    } else {
                        tvDebug.setText("不正解...");
                        prev = 0;
                    }
                    cnt++;

                    //UPDATE文の生成
                    String sql = "UPDATE " + TABLE_WORDS + " SET "
                            + "cnt = " + cnt
                            + ", correct = " + correct
                            + ", previous = " + prev
                            + ", rate = " + (100 * correct / cnt)
                            + " WHERE word_id = " + word.getWordID();

                    //tvDebug.setText(sql);
                    if(mdb.write(sql)){
                        //tvDebug.setText("Yes!!");
                    }else{
                        //tvDebug.setText("Noooo");
                    }

                    now++;

                    //ボタンの状態変更
                    btnCheck.setText("次へ");
                    btnMode = 1;

                }
                else if(btnMode == 1){

                    //Viewをクリア
                    tvDebug.setText("");
                    edAnswer.setText("");

                    //最後の問題かチェック
                    if(now <= allCount){

                        //次の問題を取得
                        word = data.get(now-1);
                        //問題を表示
                        tvQuestion.setText(word.getLang(type));

                        //問題カウント表示
                        String str = allCount + "問中 "+ now +"問目";
                        bar.setProgress(now);
                        tvCount.setText(str);

                        //ボタンの状態変更
                        btnCheck.setText("答える");
                        btnMode = 0;

                    }else{
                        //終了処理
                        finishTest();
                    }
                }
            }
        });


        //終了ボタン――――――――――――――――
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTest();
            }
        });

    }

    //終了メソッド（正解率をダイアログで表示。OKでアクティビティ終了）
    private void finishTest(){

        int rate = 0;
        if(now > 1){            //1問も解いてないときは何もしない
            rate = 100 * good / (now-1);
        }
        String msg = (now-1) + "問中" + good + "問正解しました！\n"
                    + "正解率は" + rate + "%です。";

        AlertDialog.Builder alBuilder = new AlertDialog.Builder(QuestionActivity.this);
        alBuilder.setTitle("結果");
        alBuilder.setMessage(msg);
        alBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

                Intent intent = new Intent(QuestionActivity.this, TestActivity.class);
                //渡すデータあればここで
                startActivity(intent);
                finish();

            }
        });
        AlertDialog al = alBuilder.create();
        al.show();

    }

}
