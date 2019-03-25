/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.status.AgentStatus;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Jakub Suslik
 */
public class AgentManagerTest {
    private AgentManager manager;
    
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
        manager = new AgentManagerImpl();
    }
    
    @After
    public void tearDown() {
    }
    
    private Agent createAgentBond() {
        Agent agent = new Agent();
        agent.setCodeName("007");
        agent.setStatus(AgentStatus.ACTIVE);
        return agent;
    }
    
    private Agent createAgentNoname() {
        Agent agent = new Agent();
        agent.setCodeName("null");
        agent.setStatus(AgentStatus.MIA);
        return agent;
    }
    
    /**
     * Tests of createAgent method, of class AgentManager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateAgentNullAgent() {
        manager.createAgent(null);
    }
    
    @Test
    public void testCreateAgentNullName() {
        Agent agent = createAgentBond();
        agent.setCodeName(null);
        assertThatThrownBy(() -> manager.createAgent(agent))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateAgentNullStatus() {
        Agent agent = createAgentBond();
        agent.setStatus(null);
        assertThatThrownBy(() -> manager.createAgent(agent))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateAgentGotId() {
        Agent agent = createAgentBond();
        manager.createAgent(agent);
        Long id = agent.getId();
        assertThat(id).isNotNull();
    }
    
    @Test
    public void testCreateAgentWithId() {
        Agent agent = createAgentBond();
        agent.setId(Long.valueOf(0));
        assertThatThrownBy(() -> manager.createAgent(agent))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testCreateAgentCreatesCopy() {
        Agent agent = createAgentBond();
        manager.createAgent(agent);
        assertThat(manager.findAgentById(agent.getId()))
                .isNotSameAs(agent)
                .isEqualToComparingFieldByField(agent);
    }

    /**
     * Tests of updateAgent method, of class AgentManager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAgentNullAgent() {
        manager.updateAgent(null);
    }
    
    @Test
    public void testUpdateAgentNotCreated() {
        Agent agent = createAgentBond();
        assertThatThrownBy(() -> manager.updateAgent(agent))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    private void testUpdateAgentOperation(Operation<Agent> updateOperation) {
        Agent agent1 = createAgentBond();
        Agent agent2 = createAgentNoname();
        
        manager.createAgent(agent1);
        manager.createAgent(agent2);
        
        updateOperation.callOn(agent2);
        
        manager.updateAgent(agent2);
        
        assertThat(manager.findAgentById(agent1.getId()))
                .isEqualToComparingFieldByField(agent1);
        assertThat(manager.findAgentById(agent2.getId()))
                .isEqualToComparingFieldByField(agent2);
    }
    
    @Test
    public void testUpdateAgentCodeName() {
        testUpdateAgentOperation((agent) -> agent.setCodeName("007+1"));
    }
    
    @Test
    public void testUpdateAgentStatus() {
        testUpdateAgentOperation((agent) -> agent.setStatus(AgentStatus.KIA));
    }

    /**
     * Tests of deleteAgent method, of class AgentManager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAgentNullAgent() {
        manager.deleteAgent(null);
    }
    
    @Test
    public void testDeleteAgentNotCreated() {
        Agent agent = createAgentBond();
        assertThatThrownBy(() -> manager.deleteAgent(agent))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testDeleteAgentOnlyOne() {
        Agent agent1 = createAgentBond();
        Agent agent2 = createAgentNoname();
        
        manager.createAgent(agent1);
        manager.createAgent(agent2);
        
        assertThat(agent1.getId()).isNotNull();
        assertThat(agent2.getId()).isNotNull();
        
        manager.deleteAgent(agent2);
        
        assertThat(agent1.getId()).isNotNull();
        assertThat(agent2.getId()).isNull();
    }

    /**
     * Tests of findAgentById method, of class AgentManager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindAgentByIdNullId() {
        manager.findAgentById(null);
    }
    
    @Test
    public void testFindAgentByIdWrongId() {
        Agent agent = createAgentBond();
        manager.createAgent(agent);
        assertThatThrownBy(() -> manager.findAgentById(agent.getId()+1))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testFindAgentByIdReturnRightOne() {
        Agent agent1 = createAgentBond();
        Agent agent2 = createAgentNoname();
        
        manager.createAgent(agent1);
        manager.createAgent(agent2);
        
        Agent agentF = manager.findAgentById(agent2.getId());
        assertThat(agentF).isEqualToComparingFieldByField(agent2);
    }

    /**
     * Tests of findAllAgents method, of class AgentManager.
     */
    @Test
    public void testFindAllAgentsEmpty() {
        assertThat(manager.findAllAgents()).isEmpty();
    }
    
    @Test
    public void testFindAllAgentsAllAndSame() {
        Agent agent1 = createAgentBond();
        Agent agent2 = createAgentNoname();
        
        manager.createAgent(agent1);
        manager.createAgent(agent2);
        
        assertThat(manager.findAllAgents())
                .usingFieldByFieldElementComparator()
                .containsOnly(agent1,agent2);
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
