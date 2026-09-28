package aks.keiryo.util;

import java.util.*;

public class ByteStr {

    //バイト配列のNULL=0までの文字数を返す
    public static int length(byte[] data) {
        int ret = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                break;
            } else {
                ret++;
            }
        }
        return ret;
    }

    //バイト配列リストの結合
    public static byte[] strcat(List list) {
        int size = 0;
        for (int i = 0; i < list.size(); i++) {
            size += ByteStr.length((byte[]) list.get(i));
        }

        byte[] ret = new byte[size + 1];
        int k = 0;
        for (int i = 0; i < list.size(); i++) {
            byte[] tmp = (byte[]) list.get(i);
            for (int j = 0; j < ByteStr.length(tmp); j++) {
                ret[k] = tmp[j];
                k++;
            }
        }
        ret[k] = 0;

        return ret;
    }
}
