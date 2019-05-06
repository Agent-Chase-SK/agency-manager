package cz.muni.fi.pv168.agentweb;

import cz.muni.fi.pv168.agencymanager.common.Main;
import cz.muni.fi.pv168.agencymanager.manager.AgentManagerImpl;
import cz.muni.fi.pv168.agencymanager.manager.MissionManagerImpl;
import java.io.IOException;
import java.time.Clock;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web application lifecycle listener.
 *
 * @author Jakub
 */
@WebListener
public class StartListener implements ServletContextListener {
    private final static Logger LOG = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("Web app initialized");
        ServletContext servletContext = servletContextEvent.getServletContext();
        try {
            DataSource dataSource = Main.getDataSource();
            servletContext.setAttribute("AgentManager", new AgentManagerImpl(dataSource));
            servletContext.setAttribute
                    ("MissionManager", new MissionManagerImpl(dataSource, Clock.systemDefaultZone()));
            LOG.info("Managers created and added into servletContext attributes");
        } catch (IOException e) {
            LOG.error("Error during creating db", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Web app ended");
    }
}
