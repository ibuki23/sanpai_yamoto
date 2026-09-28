package aks.keiryo.status;

public class IDEnvStruct {

    public String mAddress;
    public int mPort;
    public int mTimeout;
    public String mAddress2;
    public int mPort2;
    public int mTimeout2;

    public IDEnvStruct() {
        Clear();
    }

    public void Clear() {
        mAddress = "";
        mPort = 0;
        mTimeout = 0;
        mAddress2 = "";
        mPort2 = 0;
        mTimeout2 = 0;
    }
}
