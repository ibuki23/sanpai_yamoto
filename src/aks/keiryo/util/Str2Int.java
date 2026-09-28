package aks.keiryo.util;

public class Str2Int {

    //文字を数値に変換
    //data:変換文字
    //ret:数値 異常時は常に 0
    public static int get(String data) {
        int ret;

        try {
            if (data == null || data.length() <= 0) {
                ret = 0;
            } else {
                ret = (int) Double.parseDouble(data);
            }
        } catch (Exception e) {
            System.out.println("Str2Int-get-exp");
            ret = 0;
        }
        return ret;
    }
}
