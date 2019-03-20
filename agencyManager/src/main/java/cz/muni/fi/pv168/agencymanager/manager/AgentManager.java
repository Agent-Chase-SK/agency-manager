/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Agent;
import java.util.List;

/**
 *
 * @author Jakub Suslik
 */
public interface AgentManager {
    public void createAgent(Agent agent);
    public void updateAgent(Agent agent);
    public void deleteAgent(Agent agent);
    public Agent findAgentById(Long id);
    public List<Agent> findAllAgents();
}
