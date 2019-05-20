/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.common.ServiceException;
import cz.muni.fi.pv168.agencymanager.common.ValidationException;
import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.entity.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author Jakub Suslik
 */
public class AgencyManagerImpl implements AgencyManager {
    private final DataSource ds;

    private final static Logger LOG = LoggerFactory.getLogger(AgencyManagerImpl.class);

    public AgencyManagerImpl(DataSource ds) {
        if (ds == null) {
            LOG.error("Agency Manager Impl -> data source is null");
            throw new IllegalArgumentException("data source is null");
        }
        this.ds = ds;
    }

    @Override
    public List<Mission> findMissionsWithAgent(Agent agent) {
        LOG.debug("Starting findMissionsWithAgent");
        if (agent == null) throw new ValidationException("agent is null");
        if (agent.getId() == null) throw new ValidationException("agent id is null");

        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                     "SELECT id, codeName, status, date, location, agentId FROM Mission WHERE agentId = ?")) {
            st.setLong(1, agent.getId());
            
            try (ResultSet rs = st.executeQuery()) {
                List<Mission> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(MissionManagerImpl.dataToMission(rs));
                }
                LOG.debug("findMissionsWithAgent ended successful");
                return result;
            }
            
        } catch (SQLException ex) {
            LOG.debug("findMissionsWithAgent not ended successfully");
            throw new ServiceException("Error when trying to find missions with agent " + agent, ex);
        }
    }

    @Override
    public Agent findAgentOnMission(Mission mission) {
        LOG.debug("Starting findAgentOnMission");
        if (mission == null){
            LOG.error("findAgentOnMission not ended successfully");
            throw new ValidationException("mission is null");
        }
        if (mission.getId() == null){
            LOG.error("findAgentOnMission not ended successfully");
            throw new ValidationException("mission id is null");
        }

        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                     "SELECT Agent.id, Agent.codeName, Agent.status " +
                             "FROM Agent JOIN Mission ON Agent.id = Mission.agentId " +
                             "WHERE Mission.id = ?")) {
            st.setLong(1, mission.getId());
            
            try (ResultSet rs = st.executeQuery()) {
                if(rs.next()){
                    LOG.debug("findAgentOnMission ended successful with not null result ");
                    return AgentManagerImpl.dataToAgent(rs);
                } else {
                    LOG.debug("findAgentOnMission ended successful with null result ");
                    return null;
                }
            }
            
        } catch (SQLException ex) {
            LOG.error("findAgentOnMission not ended successfully", ex);
            throw new ServiceException("Error when trying to find agent on mission " + mission, ex);
        }
    }

    @Override
    public void assignAgentToMission(Agent agent, Mission mission) {
        LOG.debug("Starting assignAgentToMission");
        if (mission == null){
            LOG.error("mission is null");
            throw new ValidationException("mission is null");
        }
        if (mission.getId() == null){
            LOG.error("mission id is null");
            throw new ValidationException("mission id is null");
        }
        if (agent == null){
            LOG.error("agent is null");
            throw new ValidationException("agent is null");
        }
        if (agent.getId() == null){
            LOG.error("agent id is null");
            throw new ValidationException("agent id is null");
        }

        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                     "UPDATE Mission SET codeName = ?, status = ?, date = ?, location = ?, agentID = ?" +
                            " WHERE id = ?")) {
            
            st.setString(1, mission.getCodeName());
            st.setString(2, MissionManagerImpl.toString(mission.getStatus()));
            st.setDate(3, MissionManagerImpl.toSqlDate(mission.getDate()));
            st.setString(4, mission.getLocation());
            st.setLong(5, agent.getId());
            st.setLong(6, mission.getId());
            
            int result = st.executeUpdate();
            if(result != 1) throw new ServiceException("updated " + result + " instead of 1 mission when asigning agent " + agent);

            mission.setAgentId(agent.getId());
            LOG.debug("assignAgentToMission ended successfully");
            
        } catch (SQLException ex) {
            LOG.error("assignAgentToMission not ended successfully", ex);
            throw new ServiceException("Error when trying to assign agent " + agent + " to mission " + mission, ex);
        }
    }
    
}
