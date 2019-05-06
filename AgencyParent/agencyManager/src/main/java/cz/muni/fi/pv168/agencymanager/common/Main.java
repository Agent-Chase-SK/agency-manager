package cz.muni.fi.pv168.agencymanager.common;

import com.zaxxer.hikari.HikariDataSource;
import cz.muni.fi.pv168.agencymanager.manager.AgencyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static DataSource getDataSource() throws IOException {
        HikariDataSource ds = new HikariDataSource();

        Properties p = new Properties();
        
        InputStream is = Main.class.getResourceAsStream("/jdbc.properties");
        p.load(is);
        
        ds.setDriverClassName(p.getProperty("jdbc.driver"));
        ds.setJdbcUrl(p.getProperty("jdbc.url"));
        ds.setUsername(p.getProperty("jdbc.user"));
        ds.setPassword(p.getProperty("jdbc.password"));

//        new ResourceDatabasePopulator(
//                new ClassPathResource("schema-javadb.sql")/**,
//                new ClassPathResource("test-data.sql")**/)
//                .execute(ds);
        return ds;
    }

}

