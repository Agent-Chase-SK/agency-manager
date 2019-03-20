/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Agent;
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
public class AgentManagerTest {
    
    public AgentManagerTest() {
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
     * Test of createAgent method, of class AgentManager.
     */
    @Test
    public void testCreateAgent() {
        System.out.println("createAgent");
        Agent agent = null;
        AgentManager instance = new AgentManagerImpl();
        instance.createAgent(agent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateAgent method, of class AgentManager.
     */
    @Test
    public void testUpdateAgent() {
        System.out.println("updateAgent");
        Agent agent = null;
        AgentManager instance = new AgentManagerImpl();
        instance.updateAgent(agent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteAgent method, of class AgentManager.
     */
    @Test
    public void testDeleteAgent() {
        System.out.println("deleteAgent");
        Agent agent = null;
        AgentManager instance = new AgentManagerImpl();
        instance.deleteAgent(agent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAgentById method, of class AgentManager.
     */
    @Test
    public void testFindAgentById() {
        System.out.println("findAgentById");
        Long id = null;
        AgentManager instance = new AgentManagerImpl();
        Agent expResult = null;
        Agent result = instance.findAgentById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAllAgents method, of class AgentManager.
     */
    @Test
    public void testFindAllAgents() {
        System.out.println("findAllAgents");
        AgentManager instance = new AgentManagerImpl();
        List<Agent> expResult = null;
        List<Agent> result = instance.findAllAgents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class AgentManagerImpl implements AgentManager {

        public void createAgent(Agent agent) {
        }

        public void updateAgent(Agent agent) {
        }

        public void deleteAgent(Agent agent) {
        }

        public Agent findAgentById(Long id) {
            return null;
        }

        public List<Agent> findAllAgents() {
            return null;
        }
    }
    
}
