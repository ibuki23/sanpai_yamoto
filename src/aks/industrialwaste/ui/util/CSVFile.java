package aks.industrialwaste.ui.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Calendar;
import aks.industrialwaste.database.*;

public class CSVFile {

    private File file_;

    public CSVFile(File f) {
        if (null == f) {
            throw new IllegalArgumentException("'f' is null.");
        }

        file_ = f;
    }

    public void write(
             Connection con,
             Table table,
             SearchCondition[] search,
             SortCondition[] sort)
             throws IOException, SQLException {
        if (null == con) {
            throw new IllegalArgumentException("'con' is null.");
        }
        if (null == table) {
            throw new IllegalArgumentException("'table'' is null.");
        }
        if (null == search) {
            throw new IllegalArgumentException("'search' is null.");
        }
        if (null == sort) {
            throw new IllegalArgumentException("'sort' is null.");
        }

        StringBuilder sb = new StringBuilder();
        Column[] columns = table.getColumns();
        for (Column column : columns) {
            sb.append(column.getName()).append(",");
        }
        PreparedStatement statement = DBManager.prepareStatement(
                 con,
                 table.getName(),
                 sb.toString().replaceAll(",$", ""),
                 search,
                 sort,
                 0);
        try {
            ResultSet rs = statement.executeQuery();

            writeExec(file_, rs, table.getTitle(), columns);
        } finally {
            statement.close();
        }
    }

    /**
     * 指定のResulSetでアクセス可能なレコードのデータを指定のファイルにCSV形式で出 力する.
     *
     * @param file 出力するファイル
     * @param rs 出力するデータのResultSet
     * @param table 出力するテーブルのタイトル
     * @param columns 出力するカラム
     * @throws java.io.IOException I/Oエラーが発生した場合
     * @throws java.sql.SQLException データベースアクセスエラーが発生した場合
     */
    private void writeExec(
             File file,
             ResultSet rs,
             String table,
             Column[] columns)
             throws IOException, SQLException {
        BufferedWriter out;
        try {
            out = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(file), "MS932"));
        } catch (UnsupportedEncodingException e) {
            out = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(file)));
        }

        try {
            // 出力日時
            out.write(
                     String.format("%1$tY/%1$tm/%1$td %1$tH:%1$tM", Calendar.getInstance()));
            out.newLine();
            // テーブル名
            out.write(table);
            out.newLine();
            // タイトル
            StringBuilder sb = new StringBuilder();
            for (Column column : columns) {
                sb.append(column.getTitle()).append(",");
            }
            out.write(sb.toString().replaceAll(",$", ""));
            out.newLine();

            // データ
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                sb = new StringBuilder();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    Object o = rs.getObject(i + 1);
                    if (o instanceof java.sql.Timestamp) {
                        Calendar c = DBManager.convert((java.sql.Timestamp) o);
                        sb.append(String.format("%1$tY/%1$tm/%1$td %1$tH:%1$tM", c))
                                 .append(",");
                    } else if (o instanceof java.sql.Date) {
                        java.util.Date d = DBManager.convert((java.sql.Date) o);
                        sb.append(String.format("%1$tY/%1$tm/%1$td", d)).append(",");
                    } else if (o instanceof Boolean) {
                        sb.append(((Boolean) o) ? ("有り") : ("無し")).append(",");
                    } else {
                        if (null != o) {
                            sb.append(o.toString());
                        }
                        sb.append(",");
                    }
                }
                out.write(sb.toString().replaceAll(",$", ""));
                out.newLine();
            }
        } finally {
            out.close();
        }
    }
}
