/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Mission;
import cz.muni.fi.pv168.agencymanager.status.MissionStatus;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Jakub Suslik
 */
public class MissionManagerTest {
    private MissionManager manager;
    
    private final static ZonedDateTime NOW
            = LocalDateTime.of(2019, Month.JANUARY, 2, 12, 00).atZone(ZoneId.of("UTC"));
    
    public MissionManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        manager = new MissionManagerImpl();
    }
    
    @After
    public void tearDown() {
    }
    
    private static Clock prepareClockMock(ZonedDateTime now) {
        return Clock.fixed(now.toInstant(), now.getZone());
    }
    
    private Mission createMissionMetro() {
        Mission mission = new Mission();
        mission.setCodeName("2033");
        mission.setDate(LocalDate.of(2033,Month.DECEMBER,10));
        mission.setLocation("Moscow");
        mission.setStatus(MissionStatus.SCHEDULED);
        return mission;
    }
    
    private Mission createMissionOrder() {
        Mission mission = new Mission();
        mission.setCodeName("Order");
        mission.setDate(LocalDate.of(1886,Month.AUGUST,16));
        mission.setLocation("London");
        mission.setStatus(MissionStatus.FAILED);
        return mission;
    }

    /**
     * Tests of createMission method, of class MissionManager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMissionNullMission() {
        manager.createMission(null);
    }
    
    @Test
    public void testCreateMissionNullName() {
        Mission mission = createMissionMetro();
        mission.setCodeName(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateMissionNullStatus() {
        Mission mission = createMissionMetro();
        mission.setStatus(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateMissionNullLocation() {
        Mission mission = createMissionMetro();
        mission.setLocation(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateMissionNullDate() {
        Mission mission = createMissionMetro();
        mission.setDate(null);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
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
        mission.setId(Long.valueOf(0));
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
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
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateMissionScheduledInFuture() {
        Mission mission = createMissionMetro();
        mission.setStatus(MissionStatus.FAILED);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateMissionNotScheduledYesterday() {
        Mission mission = createMissionOrder();
        mission.setDate(LocalDate.of(2019, Month.JANUARY, 1));
        mission.setStatus(MissionStatus.SCHEDULED);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateMissionScheduledTomorrow() {
        Mission mission = createMissionMetro();
        mission.setDate(LocalDate.of(2019, Month.JANUARY, 3));
        mission.setStatus(MissionStatus.FAILED);
        assertThatThrownBy(() -> manager.createMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests of updateMission method, of class MissionManager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateMissionNullMission() {
        manager.updateMission(null);
    }
    
    @Test
    public void testUpdateMissionNotCreated() {
        Mission mission = createMissionMetro();
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
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
    public void testUpdateMissionNullName() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setCodeName(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testUpdateMissionNullStatus() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setStatus(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testUpdateMissionNullLocation() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setLocation(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testUpdateMissionNullDate() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        mission.setDate(null);
        assertThatThrownBy(() -> manager.updateMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests of deleteMission method, of class MissionManager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMissionNullMission() {
        manager.deleteMission(null);
    }
    
    @Test
    public void testDeleteMissionNotCreated() {
        Mission mission = createMissionMetro();
        assertThatThrownBy(() -> manager.deleteMission(mission))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testDeleteAgentOnlyOne() {
        Mission mission1 = createMissionMetro();
        Mission mission2 = createMissionOrder();
        
        manager.createMission(mission1);
        manager.createMission(mission2);
        
        assertThat(mission1.getId()).isNotNull();
        assertThat(mission2.getId()).isNotNull();
        
        manager.deleteMission(mission2);
        
        assertThat(mission1.getId()).isNotNull();
        assertThat(mission2.getId()).isNull();
    }

    /**
     * Tests of findMissionById method, of class MissionManager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindMissionByIdNullId() {
        manager.findMissionById(null);
    }
    
    @Test
    public void testFindMissionByIdWrongId() {
        Mission mission = createMissionMetro();
        manager.createMission(mission);
        assertThatThrownBy(() -> manager.findMissionById(mission.getId()+1))
                .isInstanceOf(IllegalArgumentException.class);
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

    public class MissionManagerImpl implements MissionManager {

        public void createMission(Mission mission) {
        }

        public void updateMission(Mission mission) {
        }

        public void deleteMission(Mission mission) {
        }

        public Mission findMissionById(Long id) {
            return null;
        }

        public List<Mission> findAllMissions() {
            return null;
        }
    }
    
}
