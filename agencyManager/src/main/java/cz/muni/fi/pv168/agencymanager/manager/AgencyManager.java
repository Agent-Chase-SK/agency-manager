/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.entity.Mission;
import java.util.List;

/**
 *
 * @author Jakub Suslik
 */
public interface AgencyManager {
    public List<Mission> findMissionsWithAgent(Agent agent);
    public Agent findAgentOnMission(Mission mission);
    public void assignAgentToMission(Agent agent, Mission mission);
}
