package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.common.DBUtils;
import cz.muni.fi.pv168.agencymanager.common.ValidationException;
import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.entity.Mission;
import cz.muni.fi.pv168.agencymanager.status.AgentStatus;
import cz.muni.fi.pv168.agencymanager.status.MissionStatus;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Jakub Suslik
 */
public class AgencyManagerTest {

    private AgencyManager manager;
    private AgentManager agentManager;
    private MissionManager missionManager;
    private DataSource ds;
    
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,AgentManager.class.getResourceAsStream("createTables.sql"));
        manager = new AgencyManagerImpl(ds);
        agentManager = new AgentManagerImpl(ds);
        missionManager = new MissionManagerImpl(ds, prepareClockMock(NOW));
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,AgentManager.class.getResourceAsStream("dropTables.sql"));
        manager = null;
        agentManager = null;
        missionManager = null;
    }

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:agencymgr-test");
        ds.setCreateDatabase("create");
        return ds;
    }
    
    private final static ZonedDateTime NOW
            = LocalDateTime.of(2019, Month.JANUARY, 2, 12, 00).atZone(ZoneId.of("UTC"));
    
    private static Clock prepareClockMock(ZonedDateTime now) {
        return Clock.fixed(now.toInstant(), now.getZone());
    }
    
    private Agent createAgentBond() {
        Agent agent = new Agent();
        agent.setCodeName("007");
        agent.setStatus(AgentStatus.ACTIVE);
        return agent;
    }

    private Mission createMissionSample() {
        Mission mission = new Mission();
        mission.setCodeName("mission");
        mission.setStatus(MissionStatus.ACTIVE);
        mission.setDate(LocalDate.of(1886,Month.AUGUST,16));
        mission.setLocation("there");
        return mission;
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFindMissionsWithAgentNullAgent(){
        Agent agent = null;
        expectedException.expect(ValidationException.class);
        List<Mission> result = manager.findMissionsWithAgent(agent);
    }

    @Test
    public void testFindAgentOnMissionNullMission(){
        Mission mission = null;
        expectedException.expect(ValidationException.class);
        Agent agent = manager.findAgentOnMission(mission);
    }

    @Test
    public void assignAgentToMissionNullAgent(){
        expectedException.expect(ValidationException.class);
        manager.assignAgentToMission(null, this.createMissionSample());
    }

    @Test
    public void assignAgentToMissionNullMission(){
        expectedException.expect(ValidationException.class);
        manager.assignAgentToMission(this.createAgentBond(), null);
    }

    @Test
    public void findMissionsWithAgentTest(){
        Agent agent = this.createAgentBond();
        agentManager.createAgent(agent);
        List<Mission> missions = manager.findMissionsWithAgent(agent);
        assertThat(missions).isEmpty();

        Mission mission = this.createMissionSample();
        missionManager.createMission(mission);
        manager.assignAgentToMission(agent, mission);
        assertThat(manager.findMissionsWithAgent(agent)).containsOnly(mission);
    }

    @Test
    public void findAgentOnMissionTest(){
        Mission mission = this.createMissionSample();
        missionManager.createMission(mission);
        Agent agent = manager.findAgentOnMission(mission);
        assertThat(agent).isNull();
    }

    @Test
    public void assignAgentToMissionTest(){
        Agent agent = this.createAgentBond();
        Agent secondAgent = new Agent();
        secondAgent.setCodeName("009");
        secondAgent.setStatus(AgentStatus.ACTIVE);
        Mission mission = this.createMissionSample();
        
        agentManager.createAgent(agent);
        agentManager.createAgent(secondAgent);
        missionManager.createMission(mission);

        manager.assignAgentToMission(agent,mission);
        assertThat(mission.getAgentId()).isEqualTo(agent.getId());
        manager.assignAgentToMission(secondAgent,mission);
        assertThat(mission.getAgentId()).isEqualTo(secondAgent.getId());
    }
}
