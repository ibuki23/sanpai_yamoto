package aks.industrialwaste.recycle.ui.master.agreement;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.recycle.ui.master.dis_office.*;
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
import aks.industrialwaste.recycle.ui.master.ZipDecompresser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.zip.ZipException;

public class AgreementTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 16;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS1 = 2;
    public static final int COLUMN_ADDRESS2 = 3;
    public static final int COLUMN_TEL = 4;
    public static final int COLUMN_KANA = 5;
    public static final int COLUMN_FINISH = 6;
    public static final int COLUMN_ENT_ID = 7;
    public static final int COLUMN_ENT_NAME = 8;
    public static final int COLUMN_ENT_ADDRESS1 = 9;
    public static final int COLUMN_START_DATE = 10;
    public static final int COLUMN_END_DATE = 11;
    public static final int COLUMN_AUTO_UPDATE_FLAG = 12;
    public static final int COLUMN_CONTRACT_DATE = 13;
    public static final int COLUMN_FILE_PATH = 14;
    public static final int COLUMN_REMARK = 15;
    public static final int COLUMN_BILL_NAME = 16;
    public static final int COLUMN_TRANS_NAME1 = 17;
    public static final int COLUMN_TRANS_NAME2 = 18;
    public static final int COLUMN_TRANS_NAME3 = 19;
    private static final String[] COLUMN_NAMES = {
        "id",
        "name",
        "address1",
        "address2",
        "tel",
        "kana",
        "finish_flag",
        "ent_id",
        "ent_name",
        "ent_address1",
        "contract_start_date",
        "contract_end_date",
        "auto_update_flag",
        "contract_date",
        "keiyakusyo_path",
        "remark",
        "bill_name",//16
        "trans_name01",//17
        "trans_name02",//18
        "trans_name03"//19
    };
    protected static Logger logger_
             = Logger.getLogger(Dis_officeTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public AgreementTableModel(Connection con) {
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
                              "v0_office",
                              "*",
                              (null == search) ? (new SearchCondition[0]) : (search),
                              (null == sort) ? (new SortCondition[0]) : (sort),
                              3000);
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
                                if (rs.getObject(i) != null) {
                                    columns.put(s, rs.getInt(i));
                                } else {
                                    columns.put(s, null);
                                }
                                break;
                            case Types.DOUBLE:
                                columns.put(s, rs.getDouble(i));
                                break;
                            case Types.BOOLEAN:
                                columns.put(s, rs.getBoolean(i));
                                break;
                            case Types.DATE:
                                columns.put(s, rs.getDate(i));
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

    public void refresh_limit(SearchCondition[] search, SortCondition[] sort, int limit_1, int limit_2) {
        clear();
        try {
            PreparedStatement ps
                     = DBManager.prepareStatement(
                              con_,
                              "v0_office",
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
                                if (rs.getObject(i) != null) {
                                    columns.put(s, rs.getInt(i));
                                } else {
                                    columns.put(s, null);
                                }
                                break;
                            case Types.DOUBLE:
                                columns.put(s, rs.getDouble(i));
                                break;
                            case Types.BOOLEAN:
                                columns.put(s, rs.getBoolean(i));
                                break;
                            case Types.DATE:
                                columns.put(s, rs.getDate(i));
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

    public int getNextID(int id) {
        int ret = 0;
        try {
            String s = "select max(id)as id from v0_office where ent_id = ? ;";
            PreparedStatement ps = con_.prepareStatement(s);
            try {
                ps.setObject(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ret = rs.getInt(1) + 1;
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
        }

        return ret;
    }

    public Connection getConnection() {
        return con_;
    }

    public boolean insert(Map<String, Object> newdata) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder().append("insert into m_discharge_office")
                         .append("(id, name, address1, address2, p_name, ")
                         .append("zip_code, tel, kana, finish_flag, ")
                         .append("ent_id, contract_start_date, contract_end_date, auto_update_flag, ")
                         .append("goods_unit01, goods_unit02, goods_unit03, goods_unit04, goods_unit05, ")
                         .append("goods_unit06, goods_unit07, goods_unit08, goods_unit09, goods_unit10, ")
                         .append("goods_unit11, goods_unit12, goods_unit13, goods_unit14, goods_unit15, ")
                         .append("goods_unit16, goods_unit17, goods_unit18, goods_unit19, goods_unit20, ")
                         .append("goods_unit21, goods_unit22, goods_unit23, goods_unit24, goods_unit25, ")
                         .append("goods_unit26, goods_unit27, goods_unit28, goods_unit29, goods_unit30, ")
                         .append("goods_unit31, goods_unit32, goods_unit33, goods_unit34, goods_unit35, ")
                         .append("goods_unit36, goods_unit37, goods_unit38, goods_unit39, goods_unit40, ")
                         .append("trans_id01, trans_id02, trans_id03, trans_id04, trans_id05, ")
                         .append("cont_id01, cont_id02, cont_id03, cont_id04, municipality_no, ")
                         .append("remark, tantou, keiyakusyo_path, bill_id, tantou_syozoku,keiyaku_no,contract_day) ")//tantou_syozoku　追加
                         .append("values(")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ")
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
                    ps.setObject(9, newdata.get("finish_flag"));

                    ps.setObject(10, newdata.get("ent_id"));
                    ps.setObject(11, (Date) newdata.get("contract_start_date"));
                    ps.setObject(12, (Date) newdata.get("contract_end_date"));
                    ps.setObject(13, newdata.get("auto_update_flag"));
                    ps.setObject(14, newdata.get("goods_unit01"));
                    ps.setObject(15, newdata.get("goods_unit02"));
                    ps.setObject(16, newdata.get("goods_unit03"));
                    ps.setObject(17, newdata.get("goods_unit04"));
                    ps.setObject(18, newdata.get("goods_unit05"));
                    ps.setObject(19, newdata.get("goods_unit06"));
                    ps.setObject(20, newdata.get("goods_unit07"));
                    ps.setObject(21, newdata.get("goods_unit08"));
                    ps.setObject(22, newdata.get("goods_unit09"));
                    ps.setObject(23, newdata.get("goods_unit10"));

                    ps.setObject(24, newdata.get("goods_unit11"));
                    ps.setObject(25, newdata.get("goods_unit12"));
                    ps.setObject(26, newdata.get("goods_unit13"));
                    ps.setObject(27, newdata.get("goods_unit14"));
                    ps.setObject(28, newdata.get("goods_unit15"));
                    ps.setObject(29, newdata.get("goods_unit16"));
                    ps.setObject(30, newdata.get("goods_unit17"));
                    ps.setObject(31, newdata.get("goods_unit18"));
                    ps.setObject(32, newdata.get("goods_unit19"));
                    ps.setObject(33, newdata.get("goods_unit20"));

                    ps.setObject(34, newdata.get("goods_unit21"));
                    ps.setObject(35, newdata.get("goods_unit22"));
                    ps.setObject(36, newdata.get("goods_unit23"));
                    ps.setObject(37, newdata.get("goods_unit24"));
                    ps.setObject(38, newdata.get("goods_unit25"));
                    ps.setObject(39, newdata.get("goods_unit26"));
                    ps.setObject(40, newdata.get("goods_unit27"));
                    ps.setObject(41, newdata.get("goods_unit28"));
                    ps.setObject(42, newdata.get("goods_unit29"));
                    ps.setObject(43, newdata.get("goods_unit30"));

                    ps.setObject(44, newdata.get("goods_unit31"));
                    ps.setObject(45, newdata.get("goods_unit32"));
                    ps.setObject(46, newdata.get("goods_unit33"));
                    ps.setObject(47, newdata.get("goods_unit34"));
                    ps.setObject(48, newdata.get("goods_unit35"));
                    ps.setObject(49, newdata.get("goods_unit36"));
                    ps.setObject(50, newdata.get("goods_unit37"));
                    ps.setObject(51, newdata.get("goods_unit38"));
                    ps.setObject(52, newdata.get("goods_unit39"));
                    ps.setObject(53, newdata.get("goods_unit40"));

                    ps.setObject(54, newdata.get("trans_id01"));
                    ps.setObject(55, newdata.get("trans_id02"));
                    ps.setObject(56, newdata.get("trans_id03"));
                    ps.setObject(57, newdata.get("trans_id04"));
                    ps.setObject(58, newdata.get("trans_id05"));

                    ps.setObject(59, newdata.get("cont_id01"));
                    ps.setObject(60, newdata.get("cont_id02"));
                    ps.setObject(61, newdata.get("cont_id03"));
                    ps.setObject(62, newdata.get("cont_id04"));
                    //municipality_no
                    ps.setObject(63, newdata.get("municipality_no"));
                    ps.setObject(64, newdata.get("remark"));
                    ps.setObject(65, newdata.get("tantou"));
                    //keiyakusyo_path
                    ps.setObject(66, newdata.get("keiyakusyo_path"));
                    //bill_id
                    ps.setObject(67, newdata.get("bill_id"));
                    ps.setObject(68, newdata.get("tantou_syozoku"));//　追加
                    ps.setObject(69, newdata.get("keiyaku_no"));//　追加
                    ps.setObject(70, newdata.get("contract_day"));//　追加
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

    public boolean update(Map<String, Object> updata) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder().append("update m_discharge_office set ")
                         .append("name = ?, address1 = ?, ")
                         .append("address2 = ?, p_name = ?, ")
                         .append("zip_code = ?, tel = ?, kana = ? ,")
                         .append("finish_flag = ?,  id = ?, ")
                         .append("goods_unit01 = ?,  goods_unit02 = ?, ")
                         .append("goods_unit03 = ?,  goods_unit04 = ?, ")
                         .append("goods_unit05 = ?,  goods_unit06 = ?, ")
                         .append("goods_unit07 = ?,  goods_unit08 = ?, ")
                         .append("goods_unit09 = ?,  goods_unit10 = ?,  ")
                         .append("goods_unit11 = ?,  goods_unit12 = ?, ")
                         .append("goods_unit13 = ?,  goods_unit14 = ?, ")
                         .append("goods_unit15 = ?,  goods_unit16 = ?, ")
                         .append("goods_unit17 = ?,  goods_unit18 = ?, ")
                         .append("goods_unit19 = ?,  goods_unit20 = ?,  ")
                         .append("goods_unit21 = ?,  goods_unit22 = ?, ")
                         .append("goods_unit23 = ?,  goods_unit24 = ?, ")
                         .append("goods_unit25 = ?,  goods_unit26 = ?, ")
                         .append("goods_unit27 = ?,  goods_unit28 = ?, ")
                         .append("goods_unit29 = ?,  goods_unit30 = ?,  ")
                         .append("goods_unit31 = ?,  goods_unit32 = ?, ")
                         .append("goods_unit33 = ?,  goods_unit34 = ?, ")
                         .append("goods_unit35 = ?,  goods_unit36 = ?, ")
                         .append("goods_unit37 = ?,  goods_unit38 = ?, ")
                         .append("goods_unit39 = ?,  goods_unit40 = ?,  ")
                         .append("contract_start_date = ?,  contract_end_date = ?,  ")
                         .append("auto_update_flag = ?,  ")
                         .append("trans_id01 = ?,  trans_id02 = ?,  ")
                         .append("trans_id03 = ?,  trans_id04 = ?,  ")
                         .append("trans_id05 = ?, ")
                         .append("cont_id01 = ?, cont_id02 = ?, cont_id03 = ?, cont_id04 = ?, municipality_no = ?, ")
                         .append("remark = ?, tantou = ?, ent_id = ?, keiyakusyo_path = ?, ")//　追加
                         .append("bill_id = ?, tantou_syozoku = ?, keiyaku_no = ?, contract_day = ? ")//　追加
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
                    ps.setObject(8, updata.get("finish_flag"));
                    ps.setObject(9, updata.get("id"));

                    ps.setObject(10, updata.get("goods_unit01"));
                    ps.setObject(11, updata.get("goods_unit02"));
                    ps.setObject(12, updata.get("goods_unit03"));
                    ps.setObject(13, updata.get("goods_unit04"));
                    ps.setObject(14, updata.get("goods_unit05"));
                    ps.setObject(15, updata.get("goods_unit06"));
                    ps.setObject(16, updata.get("goods_unit07"));
                    ps.setObject(17, updata.get("goods_unit08"));
                    ps.setObject(18, updata.get("goods_unit09"));
                    ps.setObject(19, updata.get("goods_unit10"));

                    ps.setObject(20, updata.get("goods_unit11"));
                    ps.setObject(21, updata.get("goods_unit12"));
                    ps.setObject(22, updata.get("goods_unit13"));
                    ps.setObject(23, updata.get("goods_unit14"));
                    ps.setObject(24, updata.get("goods_unit15"));
                    ps.setObject(25, updata.get("goods_unit16"));
                    ps.setObject(26, updata.get("goods_unit17"));
                    ps.setObject(27, updata.get("goods_unit18"));
                    ps.setObject(28, updata.get("goods_unit19"));
                    ps.setObject(29, updata.get("goods_unit20"));

                    ps.setObject(30, updata.get("goods_unit21"));
                    ps.setObject(31, updata.get("goods_unit22"));
                    ps.setObject(32, updata.get("goods_unit23"));
                    ps.setObject(33, updata.get("goods_unit24"));
                    ps.setObject(34, updata.get("goods_unit25"));
                    ps.setObject(35, updata.get("goods_unit26"));
                    ps.setObject(36, updata.get("goods_unit27"));
                    ps.setObject(37, updata.get("goods_unit28"));
                    ps.setObject(38, updata.get("goods_unit29"));
                    ps.setObject(39, updata.get("goods_unit30"));

                    ps.setObject(40, updata.get("goods_unit31"));
                    ps.setObject(41, updata.get("goods_unit32"));
                    ps.setObject(42, updata.get("goods_unit33"));
                    ps.setObject(43, updata.get("goods_unit34"));
                    ps.setObject(44, updata.get("goods_unit35"));
                    ps.setObject(45, updata.get("goods_unit36"));
                    ps.setObject(46, updata.get("goods_unit37"));
                    ps.setObject(47, updata.get("goods_unit38"));
                    ps.setObject(48, updata.get("goods_unit39"));
                    ps.setObject(49, updata.get("goods_unit40"));

                    ps.setObject(50, (Date) updata.get("contract_start_date"));
                    ps.setObject(51, (Date) updata.get("contract_end_date"));
                    ps.setObject(52, updata.get("auto_update_flag"));

                    ps.setObject(53, updata.get("trans_id01"));
                    ps.setObject(54, updata.get("trans_id02"));
                    ps.setObject(55, updata.get("trans_id03"));
                    ps.setObject(56, updata.get("trans_id04"));
                    ps.setObject(57, updata.get("trans_id05"));

                    ps.setObject(58, updata.get("cont_id01"));
                    ps.setObject(59, updata.get("cont_id02"));
                    ps.setObject(60, updata.get("cont_id03"));
                    ps.setObject(61, updata.get("cont_id04"));
                    //municipality_no
                    ps.setObject(62, updata.get("municipality_no"));
                    //20140401 備考欄追加
                    ps.setObject(63, updata.get("remark"));
                    ps.setObject(64, updata.get("tantou"));
                    ps.setObject(65, updata.get("ent_id"));
                    //keiyakusyo_path
                    ps.setObject(66, updata.get("keiyakusyo_path"));
                    //bill_id 
                    ps.setObject(67, updata.get("bill_id"));
                    ps.setObject(68, updata.get("tantou_syozoku"));//追加
                    ps.setObject(69, updata.get("keiyaku_no"));
                    ps.setObject(70, updata.get("contract_day"));
                    ps.setObject(71, updata.get("sno"));
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
                         .append("update m_discharge_office set delete_flag = true ")
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
                    ps.setObject(1, "m_discharge_office");
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
            File file = new File("H:\\master_csv\\m_discharge_office.zip");
            File file2 = new File("C:\\csv\\master_csv\\m_discharge_office.zip");
            za.unzip(file, file2);
        } catch (ZipException e) {
        } catch (IOException e) {
        }

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_discharge_office;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_discharge_office from E'C:\\\\csv\\\\master_csv\\\\m_discharge_office\\\\m_discharge_office.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_discharge_office_sno_seq', (select max(sno) from m_discharge_office)::bigint); ")
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
            File file = new File("H:\\master_csv\\m_discharge_office.zip");
            File file2 = new File("C:\\csv\\master_csv\\m_discharge_office.zip");
            za.unzip(file, file2);
        } catch (ZipException e) {
        } catch (IOException e) {
        }

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_discharge_office;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_discharge_office from E'C:\\\\csv\\\\master_csv\\\\m_discharge_office\\\\m_discharge_office.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_discharge_office_sno_seq', (select max(sno) from m_discharge_office)::bigint); ")
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
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_discharge_office to E'C:\\\\csv\\\\satou\\\\m_discharge_office.csv' CSV;")
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

    public void report(File f) {
        try {
            BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(
                              new FileOutputStream(f), "MS932"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                // タイトル
                out.write(
                         "取引先ID,取引先名称,取引先住所,現場ID,現場名称,現場正式名称,現場所在地1,現場TEL,現場終了,"
                         + "自動更新,契約開始日,契約終了日,契約状況,請求先ID,請求先名,備考,運搬業者１,運搬業者２,運搬業者３");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {
                        // id
                        Integer i = (Integer) columns.get("ent_id");
                        StringBuilder sb = new StringBuilder().append(i).append(",");
                        // 名称
                        String s = "";
                        if (columns.get("ent_name") != null) {
                            if (((String) columns.get("ent_name")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("ent_name") + "\"";
                            } else {
                                s = (String) columns.get("ent_name");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //ent_address1
                        if (columns.get("ent_address1") != null) {
                            s = (String) columns.get("ent_address1");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //現場コード
                        if (columns.get("id") != null) {
                            i = (Integer) columns.get("id");
                        } else {
                            i = 0;
                        }
                        sb = sb.append(i).append(",");
                        //現場名称
                        if (columns.get("name") != null) {
                            if (((String) columns.get("name")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("name") + "\"";
                            } else {
                                s = (String) columns.get("name");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 正式名称
                        if (columns.get("p_name") != null) {
                            if (((String) columns.get("p_name")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("p_name") + "\"";
                            } else {
                                s = (String) columns.get("p_name");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 所在地1
                        if (columns.get("address1") != null) {
                            s = (String) columns.get("address1");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // TEL
                        if (columns.get("tel") != null) {
                            s = (String) columns.get("tel");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //現場終了
                        if (columns.get("finish_flag") != null && (Boolean) columns.get("finish_flag")) {
                            s = "現場終了";
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 契約自動更新
                        if (columns.get("auto_update_flag") != null && (Boolean) columns.get("auto_update_flag")) {
                            s = "自動更新";
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 契約開始日
                        if (columns.get("contract_start_date") != null) {
                            s = (Date) columns.get("contract_start_date") != null ? sdf.format((Date) columns.get("contract_start_date")) : "";
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 契約終了日
                        if (columns.get("contract_end_date") != null) {
                            s = (Date) columns.get("contract_end_date") != null ? sdf.format((Date) columns.get("contract_end_date")) : "";
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //契約状況
                        if (columns.get("contract_date") != null) {
                            s = (String) columns.get("contract_date");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");

                        //請求先ID
                        if (columns.get("bill_id") != null) {
                            i = (Integer) columns.get("bill_id");
                        } else {
                            i = 0;
                        }
                        sb = sb.append(i).append(",");
                        //請求先名
                        if (columns.get("bill_name") != null) {
                            if (((String) columns.get("bill_name")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("bill_name") + "\"";
                            } else {
                                s = (String) columns.get("bill_name");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //備考
                        if (columns.get("remark") != null) {
                            if (((String) columns.get("remark")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("remark") + "\"";
                            } else {
                                s = (String) columns.get("remark");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");

                        //trans_name01
                        if (columns.get("trans_name01") != null) {
                            s = (String) columns.get("trans_name01");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //trans_name01
                        if (columns.get("trans_name02") != null) {
                            s = (String) columns.get("trans_name02");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //trans_name01
                        if (columns.get("trans_name03") != null) {
                            s = (String) columns.get("trans_name03");
                        } else {
                            s = "";
                        }
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
}
