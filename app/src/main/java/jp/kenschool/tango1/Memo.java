package jp.kenschool.tango1;

public class Memo {

    /*

    ―――――――――――― 使用ファイル一覧 ――――――――――――――――

    ◆AndroidManifest.sml : 定義ファイル
             <activity> </activity> に各アクティビティ名を追加
             android:name=".BootStrapApplication"  レイアウト用

    ◆build.gradle(app) ： gradleビルドに関する依存関係など
       dependenciesに以下を追加
            implementation 'com.android.support:recyclerview-v7:28.0.0'
            implementation "com.android.support:cardview-v7:28.0.0"
            implementation 'com.beardedhen:androidbootstrap:2.3.2'


    ◆Javaファイル
        MainActivity : メイン。初回のみログイン画面に飛ばし、以降は各メニューを選択で遷移。
        LoginActivity : ログイン画面。新規登録と既存のログインを同一画面で行う。ボタン変化などで処理分岐。
        RegisterActivity : 新規登録画面。insert
        TestActivity : 出題の条件セッティング画面。条件に合うデータを取得してQuestion画面に遷移させる。
        QuestionActivity : 出題ビュー。ボタンで画面状態を変化させ再利用しながら問題数分繰り返す。最後にダイアログ。
        CardsActivity : 暗記カード。リサイクラービューを生成して動かす。
        CardsAdapter : RecyclerViewの処理部分。データからholderを生成。タップからの処理なども。
        CardsSettingActivity : 表示するカードの条件セッティング画面。TestSettingとほぼ同じ。
        SearchActivity : 検索画面。ここで削除も行っているが、別途編集画面などに分けたい。
        ShowActivity : 一覧を表示。全表示とユーザー分表示。
        InfoActivity : 情報を表示。

        MyOpenHelper : DBの生成やバージョン更新を行う。TABLE定義もここに記述。
        ManageDB : DBを操作するためのクラス。read(),write()メソッドとそのオーバーロードで各種SQLを実行。
                   try-catchやCursorのクローズ等も全てこの中で行う。

        Check : 入力チェックを行うクラス。
        Word  : 単語型(単体)のデータを保持する独自クラス。これをリストにして単語セットを扱う。
        UserData : ログイン中のユーザーデータを保持。staticでアクセス。

        BootStrapApplication : レイアウト用。このクラスをManifestに登録してあり、アイコン等を利用。
        todo     : 自分用メモ。

    ◆レイアウトファイル
        activity_main :
        activity_login :
        activity_register :
        activity_test :
        activity_question :
        activity_cards : リサイクラービューなどを乗せるカード画面の土台部分。
        cards : カード1枚にあたる部分のレイアウト
        activity_search :
        activity_show :
        activity_info :


    ◆Valuesファイル
        colors : 色の定義
        dimens : サイズの定義
        strings : 文字列の定義
        styles : スタイルの定義


    ◆他
       BootStrap : https://github.com/Bearded-Hen/Android-Bootstrap

     */



}
