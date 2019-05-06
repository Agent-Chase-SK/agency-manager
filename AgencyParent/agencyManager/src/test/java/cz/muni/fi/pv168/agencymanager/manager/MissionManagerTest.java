package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.common.DBUtils;
import cz.muni.fi.pv168.agencymanager.common.ServiceException;
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
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Jakub Suslik
 */
public class MissionManagerTest {
    private MissionManager manager;
    private AgentManager agentManager;
    private DataSource ds;
    
    private final static ZonedDateTime NOW
            = LocalDateTime.of(2019, Month.JANUARY, 2, 12, 00).atZone(ZoneId.of("UTC"));
    
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,AgentManager.class.getResourceAsStream("createTables.sql"));
        manager = new MissionManagerImpl(ds, prepareClockMock(NOW));
        agentManager = new AgentManagerImpl(ds);
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,AgentManager.class.getResourceAsStream("dropTables.sql"));
        manager = null;
        agentManager = null;
    }
    
    private static DataSource prepareDataSource() {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:agencymgr-test");
        ds.setCreateDatabase("create");
        return ds;
    }
    
    private static Clock prepareClockMock(ZonedDateTime now) {
        return Clock.fixed(now.toInstant(), now.getZone());
    }
    
    private Agent createAgentBond() {
        Agent agent = new Agent();
        agent.setCodeName("007");
        agent.setStatus(AgentStatus.ACTIVE);
        return agent;
    }
    
    private Mission createMissionMetro() {
        Mission mission = new Mission();
        mission.setCodeName("2033");
        mission.setDate(LocalDate.of(2033,Month.DECEMBER,10));
        mission.setLocation("Moscow");
        mission.setStatus(MissionStatus.SCHEDULED);
        Agent agent = createAgentBond();
        agentManager.createAgent(agent);
        mission.setAgentId(agent.getId());
        return mission;
    }
    
    private Mission createMissionOrder() {
        Mission mission = new Mission();
        mission.setCodeName("Order");
        mission.setDate(LocalDate.of(1886,Month.AUGUST,16));
        mission.setLocation("London");
        mission.setStatus(MissionStatus.FAILED);
        Agent agent = createAgentBond();
        agentManager.createAgent(agent);
        mission.setAgentId(agent.getId());
        return mission;
    }

    /**
     * Tests of createMission method, of class MissionManager.
     */
    @Test(expected = ValidationException.class)
    public void testCreateMissionNullMission() {
        manager.createMission(null);
    }
    
    @Test
    public void testCreateMissionNullName() {
        Mission mission = createMissionMetro();
        mission.setCodeName(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionNullStatus() {
        Mission mission = createMissionMetro();
        mission.setStatus(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionNullLocation() {
        Mission mission = createMissionMetro();
        mission.setLocation(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionNullDate() {
        Mission mission = createMissionMetro();
        mission.setDate(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionGotId() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        assertThat(mission.getId()).isNotNull();
    }
    
    @Test
    public void testCreateMissionWithId() {
        Mission mission = createMissionMetro();
        mission.setId(0L);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionCreatesCopy() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        assertThat(manager.findMissionById(mission.getId()))
                .isNotSameAs(mission)
                .isEqualToComparingFieldByField(mission);
    }
    
    @Test
    public void testCreateMissionNotScheduledInPast() {
        Mission mission = createMissionOrder();
        mission.setStatus(MissionStatus.SCHEDULED);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionScheduledInFuture() {
        Mission mission = createMissionMetro();
        mission.setStatus(MissionStatus.FAILED);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionNotScheduledYesterday() {
        Mission mission = createMissionOrder();
        mission.setDate(LocalDate.of(2019, Month.JANUARY, 1));
        mission.setStatus(MissionStatus.SCHEDULED);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionScheduledTomorrow() {
        Mission mission = createMissionMetro();
        mission.setDate(LocalDate.of(2019, Month.JANUARY, 3));
        mission.setStatus(MissionStatus.FAILED);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateMissionNullAgentId() {
        Mission mission = createMissionMetro();
        mission.setAgentId(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(ValidationException.class);
    }

    /**
     * Tests of updateMission method, of class MissionManager.
     */
    @Test(expected = ValidationException.class)
    public void testUpdateMissionNullMission() {
        manager.updateMission(null);
    }
    
    @Test
    public void testUpdateMissionNotCreated() {
        Mission mission = createMissionMetro();
        mission.setId(20L);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(ServiceException.class);
    }
    
    private void testUpdateMissionOperation(Operation<Mission> updateOperation) {
        Mission mission1 = createMissionMetro();
        Mission mission2 = createMissionOrder();
        
        manager.createMission(mission1);
        manager.createMission(mission2);
        
        updateOperation.callOn(mission2);
        
        manager.updateMission(mission2);
        
        assertThat(manager.findMissionById(mission1.getId()))
                .isEqualToComparingFieldByField(mission1);
        assertThat(manager.findMissionById(mission2.getId()))
                .isEqualToComparingFieldByField(mission2);
    }
    
    @Test
    public void testUpdateMissionCodeName() {
        testUpdateMissionOperation((mission) -> mission.setCodeName("Exodus"));
    }
    
    @Test
    public void testUpdateMissionStatus() {
        testUpdateMissionOperation((mission) -> mission.setStatus(MissionStatus.SUCCESSFUL));
    }
    
    @Test
    public void testUpdateMissionLocation() {
        testUpdateMissionOperation((mission) -> mission.setLocation("Paris"));
    }
    
    @Test
    public void testUpdateMissionDate() {
        testUpdateMissionOperation((mission) -> mission.setDate(LocalDate.of(1000, Month.MARCH, 11)));
    }
    
    @Test
    public void testUpdateMissionAgentId() {
        Agent agent = createAgentBond();
        agentManager.createAgent(agent);
        testUpdateMissionOperation((mission) -> mission.setAgentId(agent.getId()));
    }
    
    @Test
    public void testUpdateMissionNullName() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setAgentId(10L);
        mission.setCodeName(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testUpdateMissionNullStatus() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setAgentId(10L);
        mission.setStatus(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testUpdateMissionNullLocation() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setAgentId(10L);
        mission.setLocation(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testUpdateMissionNullDate() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setAgentId(10L);
        mission.setDate(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void testUpdateMissionNullAgentId(){
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setAgentId(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void testUpdateMissionScheduledYesterday() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setAgentId(10L);
        mission.setDate(LocalDate.of(2019, Month.JANUARY, 1));
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testUpdateMissionNotScheduledTomorrow() {
        Mission mission = createMissionOrder();
        manager.createMission(mission);
        mission.setAgentId(10L);
        mission.setDate(LocalDate.of(2019, Month.JANUARY, 3));
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(ValidationException.class);
    }

    /**
     * Tests of findMissionById method, of class MissionManager.
     */
    @Test(expected = ValidationException.class)
    public void testFindMissionByIdNullId() {
        manager.findMissionById(null);
    }
    
    @Test
    public void testFindMissionByIdWrongId() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        assertThat(manager.findMissionById(mission.getId()+1)).isNull();
    }
    
    @Test
    public void testFindMissionByIdReturnRightOne() {
        Mission mission1 = createMissionMetro();
        Mission mission2 = createMissionOrder();
        
        manager.createMission(mission1);
        manager.createMission(mission2);
        
        Mission missionF = manager.findMissionById(mission2.getId());
        assertThat(missionF).isEqualToComparingFieldByField(mission2);
    }

    /**
     * Tests of findAllMissions method, of class MissionManager.
     */
    @Test
    public void testFindAllMissionsEmpty() {
        assertThat(manager.findAllMissions()).isEmpty();
    }
    
    @Test
    public void testFindAllMissionsAllAndSame() {
        Mission mission1 = createMissionMetro();
        Mission mission2 = createMissionOrder();
        
        manager.createMission(mission1);
        manager.createMission(mission2);
        
        assertThat(manager.findAllMissions())
                .usingFieldByFieldElementComparator()
                .containsOnly(mission1,mission2);
    }
}
