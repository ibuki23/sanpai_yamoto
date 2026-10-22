package aks.industrialwaste.recycle.ui.master.bill;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import aks.industrialwaste.recycle.ui.master.ZipDecompresser;
import java.io.IOException;
import java.util.zip.ZipException;

public class BillTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 16;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS1 = 2;
    public static final int COLUMN_ADDRESS2 = 3;
    public static final int COLUMN_CON_TEL = 4;
    public static final int COLUMN_OFFICE_P_NAME = 5;
    public static final int COLUMN_ZIP = 6;
    public static final int COLUMN_KANA = 7;
    public static final int COLUMN_BUNDLE = 8;
    public static final int COLUMN_PAY = 9;
    public static final int COLUMN_TAX_NO = 10;
    public static final int COLUMN_TAX_NO_NAME = 11;
    public static final int COLUMN_PAY_CLASS = 12;
    public static final int COLUMN_KEISYO = 13;
    public static final int COLUMN_FIXED_FLAG = 14;
    public static final int COLUMN_FIXED_VALUE = 15;
    public static final int COLUMN_売上区分 = 16;

    private static final String[] COLUMN_NAMES = {
        "id",
        "name",
        "address1",
        "address2",
        "tel", "p_name",
        "zip_code",
        "kana",
        "bundle_day",
        "pay_day",
        "tax_no",
        "tax_no_name",
        "pay_class_name",
        "keisyo",
        "fixed_flag",
        "fixed_value",
        "売上区分"};
    protected static Logger logger_
             = Logger.getLogger(BillTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public BillTableModel(Connection con) {
        con_ = con;
    }

    public int getRowCount() {
        return rows_.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int row, int column) {
        return rows_.get(row).get(COLUMN_NAMES[column]);
    }

    public Object getValueAt(int row, String columnName) {
        return rows_.get(row).get(columnName);
    }

    public Map<String, Object> getColumns(int row) {
        return rows_.get(row);
    }

    public void clear() {
        rows_.clear();
    }

    public void refresh(SearchCondition[] search, SortCondition[] sort) {
        clear();
        try {
            PreparedStatement ps
                     = DBManager.prepareStatement(
                              con_,
                              "v0_bill",
                              "*",
                              (null == search) ? (new SearchCondition[0]) : (search),
                              (null == sort) ? (new SortCondition[0]) : (sort),
                              0);
            try {
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData md = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> columns = new HashMap<>();
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        String s = md.getColumnName(i);
                        switch (md.getColumnType(i)) {
                            case Types.VARCHAR:
                                columns.put(s, rs.getString(i));
                                break;
                            case Types.INTEGER:
                                columns.put(s, rs.getInt(i));
                                break;
                            case Types.DOUBLE:
                                columns.put(s, rs.getDouble(i));
                                break;
                            case Types.BOOLEAN:
                                columns.put(s, rs.getBoolean(i));
                                break;
                            case Types.TIMESTAMP:
                                columns.put(s, DBManager.convert(rs.getTimestamp(i)));
                                break;
                            default:
                                columns.put(s, rs.getObject(i));
                        }
                    }

                    rows_.add(columns);
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            logger_.warning(e.toString());
        }

        fireTableDataChanged();
    }

    public Connection getConnection() {
        return con_;
    }

    public boolean insert(Map<String, Object> newdata, GoodsListTableModel model) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("insert into m_bill ")
                         .append("(id, name, address1, ")
                         .append("address2, p_name, zip_code, tel, ")
                         .append("kana, bundle_day, pay_class, tax_no, keisyo, fixed_flag, fixed_value, 売上区分, price_category)")
                         .append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, newdata.get("id"));
                    ps.setObject(2, newdata.get("name"));
                    ps.setObject(3, newdata.get("address1"));
                    ps.setObject(4, newdata.get("address2"));
                    ps.setObject(5, newdata.get("p_name"));
                    ps.setObject(6, newdata.get("zip_code"));
                    ps.setObject(7, newdata.get("tel"));
                    ps.setObject(8, newdata.get("kana"));
                    ps.setObject(9, newdata.get("bundle_day"));
                    ps.setObject(10, newdata.get("pay_class"));
                    ps.setObject(11, newdata.get("tax_no"));
                    ps.setObject(12, newdata.get("keisyo"));

                    ps.setObject(13, newdata.get("fixed_flag"));
                    ps.setObject(14, newdata.get("fixed_value"));
                    ps.setObject(15, newdata.get("売上区分"));
                    ps.setObject(16, newdata.get("price_category"));
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }

        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    public boolean update(Map<String, Object> updata, GoodsListTableModel model) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_bill set ")
                         .append("name = ?, address1 = ?, ")
                         .append("address2 = ?, p_name = ?, ")
                         .append("zip_code = ?, tel = ?, kana = ?, ")
                         .append("bundle_day = ?, pay_class = ? , id = ?, tax_no = ?, keisyo = ?, ")
                         .append("fixed_flag = ?, fixed_value = ?, 売上区分 = ?, price_category = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("name"));
                    ps.setObject(2, updata.get("address1"));
                    ps.setObject(3, updata.get("address2"));
                    ps.setObject(4, updata.get("p_name"));
                    ps.setObject(5, updata.get("zip_code"));
                    ps.setObject(6, updata.get("tel"));
                    ps.setObject(7, updata.get("kana"));
                    ps.setObject(8, updata.get("bundle_day"));
                    ps.setObject(9, updata.get("pay_class"));
                    ps.setObject(10, updata.get("id"));
                    ps.setObject(11, updata.get("tax_no"));
                    ps.setObject(12, updata.get("keisyo"));
                    ps.setObject(13, updata.get("fixed_flag"));
                    ps.setObject(14, updata.get("fixed_value"));
                    ps.setObject(15, updata.get("売上区分"));
                    ps.setObject(16, updata.get("price_category"));
                    ps.setObject(17, updata.get("sno"));
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    public boolean delete(int row) {
        try {
            Map<String, Object> columns = getColumns(row);

            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_bill ")
                         .append("set delete_flag = true ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, columns.get("sno"));
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    public void report(File f) {
        try {
            BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(
                              new FileOutputStream(f), "MS932"));
            try {
                // タイトル
                out.write(
                         "ＩＤ,名称,正式名称,所在地1,所在地2,TEL,郵便番号,端数処理,敬称,締日,売上区分");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {
                        // id
                        Integer i = (Integer) columns.get("id");
                        StringBuilder sb = new StringBuilder().append(i).append(",");
                        // 名称
                        String s = (String) columns.get("name");
                        sb = sb.append(s).append(",");
                        // 正式名称
                        s = (String) columns.get("p_name");
                        sb = sb.append(s).append(",");
                        // 所在地1
                        s = (String) columns.get("address1");
                        sb = sb.append(s).append(",");
                        // 所在地2
                        s = (String) columns.get("address2");
                        sb = sb.append(s).append(",");
                        // 業者TEL
                        s = (String) columns.get("tel");
                        sb = sb.append(s).append(",");
                        // 業者郵便番号
                        s = (String) columns.get("zip_code");
                        sb = sb.append(s).append(",");
                        // 端数処理
                        s = (String) columns.get("tax_no_name");
                        sb = sb.append(s).append(",");
                        // 敬称
                        s = (String) columns.get("keisyo");
                        sb = sb.append(s).append(",");
                        // 締日
                        i = (Integer) columns.get("bundle_day");
                        sb = sb.append(i).append(",");

                        s = (String) columns.get("売上区分");
                        sb = sb.append(s);

                        out.write(sb.toString());
                        out.newLine();
                    }
                }
            } finally {
                out.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
        }
    }

    public int import_master() {
        int ret = 0;
        try {

            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("select get_master_data_changed(?::regclass, ?::text, ?::text, ?::text)as cnt;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, "m_bill");
                    ps.setObject(2, DBManager.host1_);
                    ps.setObject(3, DBManager.dbname1_);
                    ps.setObject(4, DBManager.dbname2_);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        ret = rs.getInt("cnt");
                    }
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return -1;
        }

        return ret;
    }

    //既設プラント契約マスタ取込
    public boolean import_master1() {

        ZipDecompresser za = new ZipDecompresser();
        try {
            File file = new File("H:\\master_csv\\m_bill.zip");
            File file2 = new File("C:\\csv\\master_csv\\m_bill.zip");
            za.unzip(file, file2);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_bill;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_bill from E'C:\\\\csv\\\\master_csv\\\\m_bill\\\\m_bill.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_bill_sno_seq', (select max(sno) from m_bill)::bigint); ")
                         .toString();
                PreparedStatement ps_cnt = con_.prepareStatement(s);
                try {
                    ps_cnt.executeQuery();
                } finally {
                    ps_cnt.close();
                }

            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    //新設プラント契約マスタ取込
    public boolean import_master2() {

        ZipDecompresser za = new ZipDecompresser();
        try {
            File file = new File("H:\\master_csv\\m_bill.zip");
            File file2 = new File("C:\\csv\\master_csv\\m_bill.zip");
            za.unzip(file, file2);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_bill;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_bill from E'C:\\\\csv\\\\master_csv\\\\m_bill\\\\m_bill.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_bill_sno_seq', (select max(sno) from m_bill)::bigint); ")
                         .toString();
                PreparedStatement ps_cnt = con_.prepareStatement(s);
                try {
                    ps_cnt.executeQuery();
                } finally {
                    ps_cnt.close();
                }

            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    //本社マスタ出力
    public boolean export_master1() {

        try {
            Statement st = con_.createStatement();
            try {
                //本社側のC:\csv\satouにファイル出力
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_bill to E'C:\\\\csv\\\\satou\\\\m_bill.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }
            } catch (SQLException e) {
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }
        return true;
    }
}
