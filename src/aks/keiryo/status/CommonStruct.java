package aks.keiryo.status;

import java.util.*;
import aks.keiryo.util.*;

public class CommonStruct implements Cloneable {

    public final static int TYPE_INTEGER = 0;
    public final static int TYPE_STRING = 1;
    public final static int TYPE_BOOLEAN = 2;
    public final static int TYPE_LONG = 3;
    public final static int TYPE_DOUBLE = 4;

    public Map mValue = new LinkedHashMap();
    public Map mType = new LinkedHashMap();

    //指定キーの型を返す
    //key:キー
    //ret:型 TYPE_*
    public int GetType(String key) {
        return Str2Int.get((String) mType.get(key).toString());
    }

    //指定キーの値を返す
    //key:キー
    //ret:値
    public int GetInt(String key) {
        if (mValue.get(key) == null || mValue.get(key).toString().length() <= 0) {
            return 0;
        } else {
            return Str2Int.get((String) mValue.get(key).toString());
        }
    }

    //指定キーの値を返す
    //key:キー
    //ret:値
    public long GetLong(String key) {
        if (mValue.get(key) == null || mValue.get(key).toString().length() <= 0) {
            return 0;
        } else {
            return Long.parseLong((String) mValue.get(key).toString());
        }
    }

    public double GetDouble(String key) {
        if (mValue.get(key) == null || mValue.get(key).toString().length() <= 0) {
            return 0.;
        } else {
            return Double.parseDouble((String) mValue.get(key).toString());
        }
    }

    public String GetString(String key) {
        if (mValue.get(key) == null) {
            return "";
        } else {
            return mValue.get(key).toString();
        }
    }

    public boolean GetBoolean(String key) {
        if (mValue.get(key) == null || mValue.get(key).toString().length() <= 0) {
            return false;
        } else {
            return Boolean.parseBoolean((String) mValue.get(key).toString());
        }
    }

    //指定インデックスのキー文字列を返す
    //index:インデックス
    //ret:文字列
    public String GetKey(int index) {
        if (index < 0 || index >= mValue.size()) {
            return "";
        } else {
            return mValue.keySet().toArray()[index].toString();
        }
    }

    //拡張put
    public void putEx(String key, String val, int type) {
        mValue.put(key, val);
        mType.put(key, type);
    }

    //Int拡張put
    public void putInt(String key, Integer val, int type) {
        mValue.put(key, val);
        mType.put(key, type);
    }

    //Double拡張put
    public void putDouble(String key, Double val, int type) {
        mValue.put(key, val);
        mType.put(key, type);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
