package aks.industrialwaste.recycle.printer;

interface MPT130STransport {

    boolean open();

    boolean send(byte[] data);

    void close();

    String getDescription();
}
