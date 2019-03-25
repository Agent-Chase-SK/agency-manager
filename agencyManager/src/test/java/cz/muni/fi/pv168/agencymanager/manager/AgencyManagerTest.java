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

    private AgencyManagerImpl instance;

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
        AgencyManagerImpl instance = new AgencyManagerImpl();
    }
    
    @After
    public void tearDown() {
    }

    private Agent createAgentBond() {
        Agent agent = new Agent();
        agent.setId(4574);
        agent.setCodeName("007");
        agent.setStatus(AgentStatus.ACTIVE);
        return agent;
    }

    private Mission createMissionSample() {
        Mission mission = new Mission();
        mission.setId(45250);
        mission.setCodeName("mission");
        return mission;
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFindMissionsWithlAgentNullAgent(){
        Agent agent = null;
        expectedException.expect(IllegalArgumentException.class);
        List<Mission> result = instance.findMissionsWithAgent(agent);
    }

    @Test
    public void testFindAgentOnMissionNullMission(){
        Mission mission = null;
        expectedException.expect(IllegalArgumentException.class);
        Agent agent = instance.findAgentOnMission(mission);
    }

    @Test
    public void assignAgentToMissionNullAgent(){
        Agent agent = null;
        expectedException.expect(IllegalArgumentException.class);
        ((AgencyManagerImpl) instance).assignAgentToMission(agent,this.createMissionSample());
    }

    @Test
    public void assignAgentToMissionNullAgent(){
        Mission mission = null;
        expectedException.expect(IllegalArgumentException.class);
        ((AgencyManagerImpl) instance).assignAgentToMission(this.createAgentBond(), null);
    }

    @Test
    public void findMissionsWithAgentTest(){
        Agent agent = this.createAgentBond();
        List<Mission> missions = instance.findMissionsWithAgent(agent);
        assertThat(missions.isEmpty());

        Mission mission = this.createMissionSample();
        instance.assignAgentToMission(agent, mission);
        assertThat(instance.findMissionsWithAgent(agent).containsOnly(mission));
    }

    @Test
    public void findAgentOnMissionTest(){
        Mission mission = this.createMissionSample();
        expectedException.expect(IllegalArgumentException.class);
        Agent agent = instance.findAgentOnMission(mission)
    }

    @Test
    public void assignAgentToMissionTest(){
        Agent agent = this.createAgentBond();
        Agent secondAgent = new Agent();
        agent.setId(2);
        secondAgent.setCodeName("009");
        secondAgent.setStatus(AgentStatus.ACTIVE);
        Mission mission = this.createMissionSample();

        instance.assignAgentToMission(agent,mission);
        expectedException.expect(IllegalArgumentException.class);
        instance.assignAgentToMission(secondAgent,mission);
    }

}
