/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Agent;
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
public class AgencyManagerTest {
    
    public AgencyManagerTest() {
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
     * Test of findMissionsWithAgent method, of class AgencyManager.
     */
    @Test
    public void testFindMissionsWithAgent() {
        System.out.println("findMissionsWithAgent");
        Agent agent = null;
        AgencyManager instance = new AgencyManagerImpl();
        List<Mission> expResult = null;
        List<Mission> result = instance.findMissionsWithAgent(agent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAgentOnMission method, of class AgencyManager.
     */
    @Test
    public void testFindAgentOnMission() {
        System.out.println("findAgentOnMission");
        Mission mission = null;
        AgencyManager instance = new AgencyManagerImpl();
        Agent expResult = null;
        Agent result = instance.findAgentOnMission(mission);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of assignAgentToMission method, of class AgencyManager.
     */
    @Test
    public void testAssignAgentToMission() {
        System.out.println("assignAgentToMission");
        Agent agent = null;
        Mission mission = null;
        AgencyManager instance = new AgencyManagerImpl();
        instance.assignAgentToMission(agent, mission);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class AgencyManagerImpl implements AgencyManager {

        public List<Mission> findMissionsWithAgent(Agent agent) {
            return null;
        }

        public Agent findAgentOnMission(Mission mission) {
            return null;
        }

        public void assignAgentToMission(Agent agent, Mission mission) {
        }
    }
    
}
