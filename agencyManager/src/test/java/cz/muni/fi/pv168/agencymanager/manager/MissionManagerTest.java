/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Mission;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jakub Suslik
 */
public class MissionManagerTest {
    
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createMission method, of class MissionManager.
     */
    @Test
    public void testCreateMission() {
        System.out.println("createMission");
        Mission mission = null;
        MissionManager instance = new MissionManagerImpl();
        instance.createMission(mission);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateMission method, of class MissionManager.
     */
    @Test
    public void testUpdateMission() {
        System.out.println("updateMission");
        Mission mission = null;
        MissionManager instance = new MissionManagerImpl();
        instance.updateMission(mission);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteMission method, of class MissionManager.
     */
    @Test
    public void testDeleteMission() {
        System.out.println("deleteMission");
        Mission mission = null;
        MissionManager instance = new MissionManagerImpl();
        instance.deleteMission(mission);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findMissionById method, of class MissionManager.
     */
    @Test
    public void testFindMissionById() {
        System.out.println("findMissionById");
        Long id = null;
        MissionManager instance = new MissionManagerImpl();
        Mission expResult = null;
        Mission result = instance.findMissionById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAllMissions method, of class MissionManager.
     */
    @Test
    public void testFindAllMissions() {
        System.out.println("findAllMissions");
        MissionManager instance = new MissionManagerImpl();
        List<Mission> expResult = null;
        List<Mission> result = instance.findAllMissions();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
