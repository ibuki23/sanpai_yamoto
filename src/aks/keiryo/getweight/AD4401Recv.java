package aks.keiryo.getweight;

import aks.keiryo.nport.NPort;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A&amp;D AD-4401から連続出力される重量電文を受信・解析する。
 */
public class AD4401Recv implements AD4401Define {

    private static final Charset WIRE_CHARSET = Charset.forName("US-ASCII");
    private static final int MAX_RECEIVE_BUFFER = 4096;

    private static final Pattern STATUS_WEIGHT_PATTERN = Pattern.compile(
             "^(ST|US|OL).*?([+-])\\s*([0-9]+(?:\\.[0-9]+)?)\\s*(?:kg)?$",
             Pattern.CASE_INSENSITIVE);
    private static final Pattern SIMPLE_WEIGHT_PATTERN = Pattern.compile(
             "^([+-])\\s*([0-9]+(?:\\.[0-9]+)?)\\s*(?:kg)?$",
             Pattern.CASE_INSENSITIVE);
    private static final Pattern COMPACT_STATUS_PATTERN = Pattern.compile(
             "^([01])([+-])\\s*([0-9]+(?:\\.[0-9]+)?)$");

    private static final StringBuilder RECEIVE_BUFFER = new StringBuilder();

    public static String mData = "";
    public static String mDataStat = "";
    public static int mDataWeight = 0;
    public static boolean mDataWeightError = true;
    public static int err_code_Recv = RECV_ERR;

    public AD4401Recv(byte[] data) {
        SetData(data);
    }

    /**
     * CR/LF区切りの電文を1件受信する。
     * TCPで電文が分割・結合されても処理できるよう、未処理分を保持する。
     */
    public static synchronized int Recv(NPort nport, int tmout) {
        byte[] data = new byte[1024];

        try {
            while (true) {
                String frame = pollFrame();
                if (frame != null) {
                    SetData(frame.getBytes(WIRE_CHARSET));
                    err_code_Recv = mDataWeightError ? RECV_ERR : 0;
                    return 0;
                }

                int received = nport.Read(data, data.length, tmout);
                if (received == NPort.NPORT_ERR || received == 0) {
                    err_code_Recv = RECV_ERR;
                    return RECV_ERR;
                }

                RECEIVE_BUFFER.append(new String(data, 0, received, WIRE_CHARSET));
                if (RECEIVE_BUFFER.length() > MAX_RECEIVE_BUFFER) {
                    RECEIVE_BUFFER.setLength(0);
                    setParseError("");
                    err_code_Recv = RECV_ERR;
                    return RECV_ERR;
                }
            }
        } catch (Exception e) {
            setParseError("");
            err_code_Recv = RECV_ERR;
            System.out.println("AD4401Recv-Recv-exp:" + e);
            return RECV_ERR;
        }
    }

    public static boolean CheckData(byte[] data) {
        if (data == null) {
            return false;
        }
        for (byte value : data) {
            if (value == CMD_LF[0]) {
                return true;
            }
        }
        return false;
    }

    /**
     * AD-4401の標準的な符号付き重量形式と、添付された既存実装の
     * ステータス付き形式を解析する。
     */
    public static synchronized void SetData(byte[] data) {
        if (data == null) {
            setParseError("");
            return;
        }

        String frame = new String(data, WIRE_CHARSET)
                 .replace("\u0000", "")
                 .replace("\u0002", "")
                 .replace("\u0003", "")
                 .replace("\r", "")
                 .replace("\n", "")
                 .trim();

        mData = frame;
        Matcher matcher = STATUS_WEIGHT_PATTERN.matcher(frame);
        if (matcher.matches()) {
            String status = matcher.group(1).toUpperCase();
            if ("OL".equals(status)) {
                setParseError(frame);
                mDataStat = "US";
                return;
            }
            setWeight(status, matcher.group(2), matcher.group(3));
            return;
        }

        matcher = SIMPLE_WEIGHT_PATTERN.matcher(frame);
        if (matcher.matches()) {
            setWeight("ST", matcher.group(1), matcher.group(2));
            return;
        }

        matcher = COMPACT_STATUS_PATTERN.matcher(frame);
        if (matcher.matches()) {
            String status = "0".equals(matcher.group(1)) ? "ST" : "US";
            setWeight(status, matcher.group(2), matcher.group(3));
            return;
        }

        setParseError(frame);
    }

    private static String pollFrame() {
        int lf = RECEIVE_BUFFER.indexOf("\n");
        if (lf < 0) {
            return null;
        }

        String frame = RECEIVE_BUFFER.substring(0, lf);
        RECEIVE_BUFFER.delete(0, lf + 1);
        if (frame.endsWith("\r")) {
            frame = frame.substring(0, frame.length() - 1);
        }
        return frame;
    }

    private static void setWeight(String status, String sign, String value) {
        try {
            double parsed = Double.parseDouble(value);
            if ("-".equals(sign)) {
                parsed *= -1;
            }
            mDataStat = status;
            mDataWeight = (int) Math.round(parsed);
            mDataWeightError = false;
        } catch (NumberFormatException e) {
            setParseError(mData);
        }
    }

    private static void setParseError(String frame) {
        mData = frame;
        mDataStat = "US";
        mDataWeight = 0;
        mDataWeightError = true;
    }
}
