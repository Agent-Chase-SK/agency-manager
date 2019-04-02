/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.common.DBUtils;
import cz.muni.fi.pv168.agencymanager.common.ServiceException;
import cz.muni.fi.pv168.agencymanager.common.ValidationException;
import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.status.AgentStatus;
import java.io.IOException;
import java.sql.SQLException;
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
public class AgentManagerTest {
    private AgentManager manager;
    private DataSource ds;
    
    @Before
    public void setUp() throws SQLException, IOException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,AgentManager.class.getResourceAsStream("createTables.sql"));
        manager = new AgentManagerImpl(ds);
    }
    
    @After
    public void tearDown() throws SQLException, IOException {
        DBUtils.executeSqlScript(ds,AgentManager.class.getResourceAsStream("dropTables.sql"));
        manager = null;
    }
    
    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:agencymgr-test");
        ds.setCreateDatabase("create");
        return ds;
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
    @Test(expected = ValidationException.class)
    public void testCreateAgentNullAgent() {
        manager.createAgent(null);
    }
    
    @Test
    public void testCreateAgentNullName() {
        Agent agent = createAgentBond();
        agent.setCodeName(null);
        assertThatThrownBy(() -> manager.createAgent(agent))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateAgentNullStatus() {
        Agent agent = createAgentBond();
        agent.setStatus(null);
        assertThatThrownBy(() -> manager.createAgent(agent))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testCreateAgentGotId() {
        Agent agent = createAgentBond();
        manager.createAgent(agent);
        assertThat(agent.getId()).isNotNull();
    }
    
    @Test
    public void testCreateAgentWithId() {
        Agent agent = createAgentBond();
        agent.setId(Long.valueOf(0));
        assertThatThrownBy(() -> manager.createAgent(agent))
                .isInstanceOf(ValidationException.class);
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
    @Test(expected = ValidationException.class)
    public void testUpdateAgentNullAgent() {
        manager.updateAgent(null);
    }
    
    @Test
    public void testUpdateAgentNotCreated() {
        Agent agent = createAgentBond();
        assertThatThrownBy(() -> manager.updateAgent(agent))
                .isInstanceOf(ServiceException.class);
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
    
    @Test
    public void testUpdateAgentNullName() {
        Agent agent = createAgentBond();
        manager.createAgent(agent);
        agent.setCodeName(null);
        assertThatThrownBy(() -> manager.updateAgent(agent))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void testUpdateAgentNullStatus() {
        Agent agent = createAgentBond();
        manager.createAgent(agent);
        agent.setStatus(null);
        assertThatThrownBy(() -> manager.updateAgent(agent))
                .isInstanceOf(ValidationException.class);
    }

    /**
     * Tests of deleteAgent method, of class AgentManager.
     */
    @Test(expected = ValidationException.class)
    public void testDeleteAgentNullAgent() {
        manager.deleteAgent(null);
    }
    
    @Test
    public void testDeleteAgentNotCreated() {
        Agent agent = createAgentBond();
        assertThatThrownBy(() -> manager.deleteAgent(agent))
                .isInstanceOf(ServiceException.class);
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
                .isInstanceOf(ServiceException.class);
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
}
