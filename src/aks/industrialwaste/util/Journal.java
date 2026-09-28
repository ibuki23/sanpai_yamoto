package aks.industrialwaste.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Logger;
import aks.industrialwaste.database.DBManager;

public class Journal {

    protected static Logger logger_ = Logger.getLogger(Journal.class.getName());

    public Journal() {
    }

    public static void recordJournal(
             Connection con,
             String operation,
             String contents) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("insert into operation_journal")
                         .append("(register_date, operation, contents) ")
                         .append("values(?, ?, ?);")
                         .toString();
                PreparedStatement ps = con.prepareStatement(s);
                try {
                    ps.setObject(1, DBManager.convert(Calendar.getInstance()));
                    ps.setObject(2, operation);
                    ps.setObject(3, contents);
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
        } catch (SQLException e) {
            logger_.warning(e.toString());
        }
    }

}
