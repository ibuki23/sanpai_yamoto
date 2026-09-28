package aks.industrialwaste.recycle.printer;

import aks.keiryo.status.ShipmentStruct;
import aks.keiryo.util.ByteStr;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 現行の計量データからMPT-130S用の計量票コマンドを組み立てる。
 */
class MPT130SPrintCommandBuilder implements MPT130SDefine {

    private static final Charset PRINT_CHARSET = Charset.forName("MS932");
    private static final String FULL_WIDTH_SPACE = "　";

    private MPT130SPrintCommandBuilder() {
    }

    static List<byte[]> build(ShipmentStruct shipment) {
        List<byte[]> commands = new ArrayList<byte[]>();

        commands.add(ByteStr.strcat(Arrays.asList(
                 MTP_ESC, MTP_AT,
                 MTP_ESC, MTP_AND,
                 MTP_SUB, MTP_W, MTP3,
                 MTP_ESC, MTP_Z, MTP_6,
                 bytes(FULL_WIDTH_SPACE), MTP_CR,
                 MTP_ESC, MTP_Z, MTP_18,
                 bytes(FULL_WIDTH_SPACE), MTP_CR,
                 bytes(FULL_WIDTH_SPACE), MTP_CR,
                 bytes(FULL_WIDTH_SPACE), MTP_CR,
                 MTP_ESC, MTP_Z, MTP_24,
                 MTP_ESC, MTP_GS, MTP_B0, MTP_SOH,
                 MTP_LF)));

        commands.add(line(FULL_WIDTH_SPACE));

        String coefficient = String.format(Locale.JAPAN, "%.2f", shipment.GetDouble("ryube"));
        String volume = String.format(Locale.JAPAN, "%.2f", shipment.GetDouble("m3"));
        commands.add(line("　　　　　　"
                 + rightAlign(toFullWidthAlphaNumeric(coefficient), 8)
                 + rightAlign(toFullWidthAlphaNumeric(volume), 22)));

        if (shipment.GetInt("net_weight") != 0) {
            commands.add(line(weightLine(shipment.GetInt("net_weight"))));
        } else {
            commands.add(line(FULL_WIDTH_SPACE));
        }
        commands.add(line(weightLine(shipment.GetInt("empty_weight"))));
        commands.add(line(weightLine(shipment.GetInt("total_weight"))));
        commands.add(line(FULL_WIDTH_SPACE));

        commands.add(line(rightAlign(toFullWidthAlphaNumeric(
                 replaceCompanyMarks(shipment.GetString("goods_name"))), 40)));
        commands.add(line(rightAlign(toFullWidthAlphaNumeric(
                 replaceCompanyMarks(shipment.GetString("transporter_name"))), 40)));
        commands.add(line(rightAlign(toFullWidthAlphaNumeric(
                 replaceCompanyMarks(shipment.GetString("office_name"))), 40)));

        commands.add(line("　　　　　　" + rightAlign(
                 toFullWidthAlphaNumeric(shipment.GetString("car_no")), 8)));

        Object dateValue = shipment.mValue.get("gate_out_date");
        String date = "";
        if (dateValue instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat(
                     "yyyy'年'MM'月'dd'日　'HH'：'mm", Locale.JAPAN);
            date = format.format((Date) dateValue);
        }
        commands.add(line(rightAlign(toFullWidthAlphaNumeric(date), 40)));

        commands.add(ByteStr.strcat(Arrays.asList(MTP_VT, MTP_CR)));
        return commands;
    }

    private static String weightLine(int weight) {
        return rightAlign(toFullWidthAlphaNumeric(String.valueOf(weight) + "ｋｇ"), 40);
    }

    private static byte[] line(String value) {
        return ByteStr.strcat(Arrays.asList(bytes(value), MTP_CR, MTP_CR));
    }

    private static byte[] bytes(String value) {
        return value.getBytes(PRINT_CHARSET);
    }

    static String rightAlign(String value, int byteWidth) {
        String result = value == null ? "" : value;
        result = truncateToBytes(result, byteWidth);
        int currentWidth = result.getBytes(PRINT_CHARSET).length;

        while (currentWidth + 2 <= byteWidth) {
            result = FULL_WIDTH_SPACE + result;
            currentWidth += 2;
        }
        if (currentWidth < byteWidth) {
            result = " " + result;
        }
        return result;
    }

    private static String truncateToBytes(String value, int byteWidth) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            String candidate = result.toString() + value.charAt(i);
            if (candidate.getBytes(PRINT_CHARSET).length > byteWidth) {
                break;
            }
            result.append(value.charAt(i));
        }
        return result.toString();
    }

    static String toFullWidthAlphaNumeric(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder result = new StringBuilder(value);
        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if ((c >= '0' && c <= '9')
                     || (c >= 'A' && c <= 'Z')
                     || (c >= 'a' && c <= 'z')) {
                result.setCharAt(i, (char) (c + 0xFEE0));
            }
        }
        return result.toString();
    }

    private static String replaceCompanyMarks(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("㈱", "（株）").replace("㈲", "（有）");
    }
}
