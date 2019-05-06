
package cz.muni.fi.pv168.agencymanager.common;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import javax.sql.DataSource;

/**
 *
 * @author Jakub Suslik
 */
public class DBUtils {
    public static void executeSqlScript(DataSource ds, InputStream is) throws SQLException {
        if(is == null){
            System.err.println("null");
        }
        try (Connection c = ds.getConnection()) {
            Scanner s = new Scanner(is).useDelimiter(";");
            while (s.hasNext()) {
                String sql = s.next().trim();
                if (sql.isEmpty()) continue;
                try (PreparedStatement st1 = c.prepareStatement(sql)) {
                    st1.execute();
                }
            }
        }
    }
}
