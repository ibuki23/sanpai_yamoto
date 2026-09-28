package aks.industrialwaste.recycle.ui.sql;

import aks.industrialwaste.database.DBManager;
import java.util.*;
import static aks.industrialwaste.recycle.ui.sql.SQLDefine.SQL_ERR;
import java.sql.ResultSetMetaData;
import java.sql.Types;

public class SQLCommon implements SQLDefine {

    private SQL mSql;

    //コンストラクタ
    public SQLCommon(String url, String user, String pwd) {
        mSql = new SQL(url, user, pwd);
        mSql.Open();
    }

    public void GetMasterData(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 ;", table_name));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//name
            ms[2] = new String[cnt];//zip_code
            ms[3] = new String[cnt];//tel
            ms[4] = new String[cnt];//address1
            ms[5] = new String[cnt];//address2
            ms[6] = new String[cnt];//p_name
            sql = String.format("select id, name, zip_code, tel, address1, address2, p_name from %s where sno >= 0 order by id;",
                     table_name);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf((Integer) mSql.mResultSet.getObject("id"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("name");
                    ms[2][i] = (String) mSql.mResultSet.getObject("zip_code");
                    ms[3][i] = (String) mSql.mResultSet.getObject("tel");
                    ms[4][i] = (String) mSql.mResultSet.getObject("address1");
                    ms[5][i] = (String) mSql.mResultSet.getObject("address2");
                    ms[6][i] = (String) mSql.mResultSet.getObject("p_name");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public void GetMasterDataOffice(String table_name, String[][] ms, int ent_id) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 and ent_id = %s;", table_name, ent_id));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            if (cnt > 0) {
                ms[0] = new String[cnt + 1];//id
                ms[1] = new String[cnt + 1];//name
                ms[2] = new String[cnt + 1];//zip_code
                ms[3] = new String[cnt + 1];//tel
                ms[4] = new String[cnt + 1];//address1
                ms[5] = new String[cnt + 1];//address2
                ms[6] = new String[cnt + 1];//p_name
                ms[7] = new String[cnt + 1];//tantou
            } else {
                ms[0] = new String[1];//id
                ms[1] = new String[1];//name
                ms[2] = new String[1];//zip_code
                ms[3] = new String[1];//tel
                ms[4] = new String[1];//address1
                ms[5] = new String[1];//address2
                ms[6] = new String[1];//p_name
                ms[7] = new String[1];//tantou
                ms[0][0] = "0";
                ms[1][0] = "";
                ms[2][0] = "";
                ms[3][0] = "";
                ms[4][0] = "";
                ms[5][0] = "";
                ms[6][0] = "";
                ms[7][0] = "";
            }
            sql = String.format("select id, name, zip_code, tel, address1, address2, p_name, tantou from %s where sno >= 0 and ent_id = %s order by id;",
                     table_name, ent_id);
            if (mSql.Query(sql) != SQL_ERR) {
                Integer id_ = 0;
                while (mSql.mResultSet.next()) {
                    id_ = (Integer) mSql.mResultSet.getObject("id");
                    if (!id_.equals(0) && i == 0) {
                        ms[0][0] = "0";
                        ms[1][0] = "";
                        ms[2][0] = "";
                        ms[3][0] = "";
                        ms[4][0] = "";
                        ms[5][0] = "";
                        ms[6][0] = "";
                        ms[7][0] = "";
                        i++;
                        ms[0][i] = String.valueOf((Integer) mSql.mResultSet.getObject("id"));
                        ms[1][i] = (String) mSql.mResultSet.getObject("name");
                        ms[2][i] = (String) mSql.mResultSet.getObject("zip_code");
                        ms[3][i] = (String) mSql.mResultSet.getObject("tel");
                        ms[4][i] = (String) mSql.mResultSet.getObject("address1");
                        ms[5][i] = (String) mSql.mResultSet.getObject("address2");
                        ms[6][i] = (String) mSql.mResultSet.getObject("p_name");
                        ms[7][i] = (String) mSql.mResultSet.getObject("tantou");
                        i++;
                    } else {
                        ms[0][i] = String.valueOf((Integer) mSql.mResultSet.getObject("id"));
                        ms[1][i] = (String) mSql.mResultSet.getObject("name");
                        ms[2][i] = (String) mSql.mResultSet.getObject("zip_code");
                        ms[3][i] = (String) mSql.mResultSet.getObject("tel");
                        ms[4][i] = (String) mSql.mResultSet.getObject("address1");
                        ms[5][i] = (String) mSql.mResultSet.getObject("address2");
                        ms[6][i] = (String) mSql.mResultSet.getObject("p_name");
                        ms[7][i] = (String) mSql.mResultSet.getObject("tantou");
                        i++;
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public void GetMasterDataTransTantou(String table_name, String[][] ms, int trans_id) {
        String sql = "";

        try {
            int i = 1;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 and trans_id = %s;", table_name, trans_id));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt + 1];//id
            ms[1] = new String[cnt + 1];//name
            ms[2] = new String[cnt + 1];//zip_code
            ms[3] = new String[cnt + 1];//tel
            ms[4] = new String[cnt + 1];//address1
            ms[0][0] = "";
            ms[1][0] = "";
            ms[2][0] = "";
            ms[3][0] = "";
            ms[4][0] = "";
            sql = String.format("select id, name, trans_id, trans_name, trans_name1 from %s where sno >= 0 and trans_id = %s order by id;",
                     table_name, trans_id);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf((Integer) mSql.mResultSet.getObject("trans_id"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("trans_name1");
                    ms[2][i] = String.valueOf((Integer) mSql.mResultSet.getObject("id"));
                    ms[3][i] = (String) mSql.mResultSet.getObject("name");
                    ms[4][i] = (String) mSql.mResultSet.getObject("trans_name");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public void GetMasterData2(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 ;", table_name));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//name
            ms[2] = new String[cnt];//zip_code
            ms[3] = new String[cnt];//tel
            ms[4] = new String[cnt];//address1
            ms[5] = new String[cnt];//address2
            ms[6] = new String[cnt];//p_name
            ms[7] = new String[cnt];//office_name
            ms[8] = new String[cnt];//office_zip_code
            ms[9] = new String[cnt];//office_tel
            ms[10] = new String[cnt];//office_address1
            ms[11] = new String[cnt];//office_address2
            ms[12] = new String[cnt];//office_p_namecont_tantou
            ms[13] = new String[cnt];//cont_tantou 20151010 担当者追加
            ms[14] = new String[cnt];//last_tantou 20151019 最終処分担当者追加

            sql = String.format("select id, name, con_zip_code, con_tel, con_address1, con_address2, con_p_name, "
                     + "office_name, office_zip_code, office_tel, office_address1, office_address2, office_p_name, cont_tantou, last_tantou "
                     + "from %s where sno >= 0 order by id;",
                     table_name);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf((Integer) mSql.mResultSet.getObject("id"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("name");
                    ms[2][i] = (String) mSql.mResultSet.getObject("con_zip_code");
                    ms[3][i] = (String) mSql.mResultSet.getObject("con_tel");
                    ms[4][i] = (String) mSql.mResultSet.getObject("con_address1");
                    ms[5][i] = (String) mSql.mResultSet.getObject("con_address2");
                    ms[6][i] = (String) mSql.mResultSet.getObject("con_p_name");
                    ms[7][i] = (String) mSql.mResultSet.getObject("office_name");
                    ms[8][i] = (String) mSql.mResultSet.getObject("office_zip_code");
                    ms[9][i] = (String) mSql.mResultSet.getObject("office_tel");
                    ms[10][i] = (String) mSql.mResultSet.getObject("office_address1");
                    ms[11][i] = (String) mSql.mResultSet.getObject("office_address2");
                    ms[12][i] = (String) mSql.mResultSet.getObject("office_p_name");
                    ms[13][i] = (String) mSql.mResultSet.getObject("cont_tantou");
                    ms[14][i] = (String) mSql.mResultSet.getObject("last_tantou");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData2-exp");
        }
    }

    public void GetMasterData4(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 ;", table_name));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//name
            ms[2] = new String[cnt];//zip_code
            ms[3] = new String[cnt];//tel
            ms[4] = new String[cnt];//address1
            ms[5] = new String[cnt];//address2

            sql = String.format("select id, name, zip_code, tel, address1, address2 from %s where sno >= 0 order by id;",
                     table_name);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf((Integer) mSql.mResultSet.getObject("id"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("name");
                    ms[2][i] = (String) mSql.mResultSet.getObject("address1");
                    ms[3][i] = (String) mSql.mResultSet.getObject("address2");
                    ms[4][i] = (String) mSql.mResultSet.getObject("zip_code");
                    ms[5][i] = (String) mSql.mResultSet.getObject("tel");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public void GetMasterData4_1(String table_name, String[][] ms, int cont_id) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 and (cont_id = %s or sno = 0);", table_name, cont_id));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//name
            ms[2] = new String[cnt];//zip_code
            ms[3] = new String[cnt];//tel
            ms[4] = new String[cnt];//address1
            ms[5] = new String[cnt];//address2

            sql = String.format("select id, name, zip_code, tel, address1, address2 from %s where sno >= 0 and (cont_id = %s or sno = 0) order by sno;",
                     table_name, cont_id);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf(mSql.mResultSet.getObject("id"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("name");
                    ms[2][i] = (String) mSql.mResultSet.getObject("address1");
                    ms[3][i] = (String) mSql.mResultSet.getObject("address2");
                    ms[4][i] = (String) mSql.mResultSet.getObject("zip_code");
                    ms[5][i] = (String) mSql.mResultSet.getObject("tel");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public void GetMasterData6(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 ;", table_name));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//name
            sql = String.format("select sno, grant_name from %s where sno >= 0 order by sno;",
                     table_name);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf((Long) mSql.mResultSet.getObject("sno"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("grant_name");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public void GetMasterData7(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 ;", table_name));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//company_name
            ms[2] = new String[cnt];//trust_name1
            ms[3] = new String[cnt];//trust_name2
            ms[4] = new String[cnt];//trust_name3
            ms[5] = new String[cnt];//trust_name4
            ms[6] = new String[cnt];//trust_name1
            ms[7] = new String[cnt];//trust_name2
            ms[8] = new String[cnt];//trust_name3
            ms[9] = new String[cnt];//trust_name4
            sql = String.format("select * from %s where sno >= 0 order by sno;",
                     table_name);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf((Long) mSql.mResultSet.getObject("sno"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("company_name");
                    ms[2][i] = (String) mSql.mResultSet.getObject("trust_name1");
                    ms[3][i] = (String) mSql.mResultSet.getObject("trust_name2");
                    ms[4][i] = (String) mSql.mResultSet.getObject("trust_name3");
                    ms[5][i] = (String) mSql.mResultSet.getObject("trust_name4");
                    ms[6][i] = (String) mSql.mResultSet.getObject("trans_name1");
                    ms[7][i] = (String) mSql.mResultSet.getObject("trans_name2");
                    ms[8][i] = (String) mSql.mResultSet.getObject("trans_name3");
                    ms[9][i] = (String) mSql.mResultSet.getObject("trans_name4");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public void GetMasterData8(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 ;", table_name));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//name
            sql = String.format("select sno, name from %s where sno >= 0 order by sno;",
                     table_name);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf((Long) mSql.mResultSet.getObject("sno"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("name");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public void GetMasterData8_1(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from %s where sno >= 0 ;", table_name));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//name
            sql = String.format("select id, name from %s where sno >= 0 order by id;",
                     table_name);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = String.valueOf((Integer) mSql.mResultSet.getObject("id"));
                    ms[1][i] = (String) mSql.mResultSet.getObject("name");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public String GetMasterDataName(String table_name, int id) {
        String sql = "";
        String ret = "";

        try {

            sql = String.format("select name from %s where id = %s order by id;",
                     table_name, id);
            if (mSql.Query(sql) != SQL_ERR && mSql.mResultSet.next()) {
                ret = (String) mSql.mResultSet.getObject("name");
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterDataName-exp");
        }

        return ret;
    }

    public void Get按分Data(Long weight_sno_, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 10;

            ms[0] = new String[cnt];//按分品目id
            ms[1] = new String[cnt];//按分品目名
            ms[2] = new String[cnt];//按分正味重量
            ms[3] = new String[cnt];//按分m3
            ms[4] = new String[cnt];//method_id1
            ms[5] = new String[cnt];//method_id2
            sql = String.format("select * from v1_weigh_data_anbun where weight_sno = %d order by row_no;",
                     weight_sno_);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[0][i] = (String) mSql.mResultSet.getString("按分品目id");
                    ms[1][i] = (String) mSql.mResultSet.getString("按分品目名");
                    ms[2][i] = (String) mSql.mResultSet.getString("按分正味重量");
                    ms[3][i] = (String) mSql.mResultSet.getString("按分m3");
                    ms[4][i] = (String) mSql.mResultSet.getString("method_id1");
                    ms[5][i] = (String) mSql.mResultSet.getString("method_id2");
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }
    }

    public int GetMasterDataID(String table_name, String name) {
        String sql = "";
        int ret = 0;

        try {

            sql = String.format("select id from %s where name like '%%%s%%' order by id;",
                     table_name, name);
            if (mSql.Query(sql) != SQL_ERR && mSql.mResultSet.next()) {
                ret = (Integer) mSql.mResultSet.getObject("id");
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterDataName-exp");
        }

        return ret;
    }

    public List GetZaikoData1(Date d, int goods_id) {
        String sql = "";
        List<Map<String, Object>> ms
                 = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

        try {
            sql = String.format("select * from zaiko.d_plan where sno >= 0 and 所属月 = date_trunc('month'::text, '%s'::date)::date and 商品コード = %d order by 業者コード;", d.toString(), goods_id);
            if (mSql.Query(sql) != SQL_ERR) {
                ResultSetMetaData md = mSql.mResultSet.getMetaData();
                while (mSql.mResultSet.next()) {
                    Map<String, Object> columns = new HashMap<String, Object>();

                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        String s = md.getColumnName(i);
                        switch (md.getColumnType(i)) {
                            case Types.VARCHAR:
                                columns.put(s, mSql.mResultSet.getString(i));
                                break;
                            case Types.INTEGER:
                                columns.put(s, mSql.mResultSet.getInt(i));
                                break;
                            case Types.DOUBLE:
                                columns.put(s, mSql.mResultSet.getDouble(i));
                                break;
                            case Types.BOOLEAN:
                                columns.put(s, mSql.mResultSet.getBoolean(i));
                                break;
                            case Types.TIMESTAMP:
                                columns.put(s, DBManager.convert(mSql.mResultSet.getTimestamp(i)));
                                break;
                            default:
                                columns.put(s, mSql.mResultSet.getObject(i));
                        }
                    }
                    ms.add(columns);
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetZaikoData1-exp");
        }

        return ms;
    }

    public Object[][] GetZaikoData2(Date d, int goods_id) {
        String sql = "";
        Object[][] ms = null;

        try {
            int i = 0;
            int cnt = 0;
            mSql.Select(String.format("select count(*) as cnt from zaiko.d_result where sno >= 0 and date_trunc('month'::text, 所属日::date)::date = date_trunc('month'::text, '%s'::date)::date and 商品コード = %d;", d.toString(), goods_id));
            if (mSql.mResultSet2.next()) {
                //マスタの登録件数を返す
                cnt = mSql.mResultSet2.getInt("cnt");
            }
            ms = new Object[cnt][16];

            sql = String.format("select * from zaiko.d_result where sno >= 0 and date_trunc('month'::text, 所属日::date)::date = date_trunc('month'::text, '%s'::date)::date and 商品コード = %d order by 所属日;", d.toString(), goods_id);
            if (mSql.Query(sql) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    ms[i][0] = mSql.mResultSet.getDate("所属日");
                    ms[i][1] = mSql.mResultSet.getInt("商品コード");
                    ms[i][2] = mSql.mResultSet.getString("商品名");
                    ms[i][3] = mSql.mResultSet.getBoolean("搬入作業有f");
                    ms[i][4] = String.valueOf(mSql.mResultSet.getDouble("搬入予定数量"));
                    ms[i][5] = mSql.mResultSet.getInt("処理予定数量");
                    ms[i][6] = String.valueOf(mSql.mResultSet.getDouble("搬入実績数量"));
                    ms[i][7] = mSql.mResultSet.getInt("搬入実績台数");
                    ms[i][8] = String.valueOf(mSql.mResultSet.getDouble("搬入実績累計"));
                    ms[i][9] = mSql.mResultSet.getInt("処理実績数量");
                    ms[i][10] = String.valueOf(mSql.mResultSet.getDouble("在庫加減数"));
                    ms[i][11] = String.valueOf(mSql.mResultSet.getDouble("在庫実数"));
                    ms[i][12] = String.valueOf(mSql.mResultSet.getDouble("プラス範囲"));
                    ms[i][13] = String.valueOf(mSql.mResultSet.getDouble("在庫上限数量"));
                    ms[i][14] = mSql.mResultSet.getBoolean("警報ランプf");
                    ms[i][15] = true;

                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetZaikoData1-exp");
        }
        return ms;
    }
}
