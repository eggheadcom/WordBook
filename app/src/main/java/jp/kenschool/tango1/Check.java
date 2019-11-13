package jp.kenschool.tango1;

public class Check {

    //数字チェック
    public static boolean isNumber(String text){
        return isNumber(text, 0, 1000);
    }

    //数字チェック（範囲付）
    public static boolean isNumber(String text, int min, int max){

        try {
            int num = Integer.parseInt(text);

            if (min <= num && num <= max) {
                return true;
            }
            return false;

        }catch (NumberFormatException e){
            return false;
        }
    }

    //文字チェック（英語:アルファベットのみ）
    public static boolean isEnglish(String text) {

        if (text.matches(".*[a-zA-Z].*")) {
            return true;
        }
        return false;
    }

    //文字チェック(日本語:英数以外)
    public static boolean isJapanese(String text) {

        if (text.matches(".*[^a-zA-Z0-9].*")) {
            return true;
        }
        return false;
    }

}
