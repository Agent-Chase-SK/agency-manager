
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.common.ServiceException;
import cz.muni.fi.pv168.agencymanager.common.ValidationException;
import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.status.AgentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author Jakub Suslik
 */
public class AgentManagerImpl implements AgentManager {
    private DataSource ds;

    private final static Logger LOG = LoggerFactory.getLogger(AgentManagerImpl.class);

    public AgentManagerImpl(DataSource ds) {
        if (ds == null) {
            LOG.error("AgentManagerImpl -> data source is null");
            throw new IllegalArgumentException("data source is null");
        }
        this.ds = ds;
    }
    
    private void validate(Agent agent) {
        if (agent == null) {
            LOG.error("Validate -> agent is null");
            throw new ValidationException("agent is null");
        }
        if (agent.getCodeName() == null) {
            LOG.error("Validate -> agent codename is null");
            throw new ValidationException("agent codename is null");
        }
        if (agent.getStatus() == null) {
            LOG.error("Validate -> agent status is null");
            throw new ValidationException("agent status is null");
        }
    }
    
    public static String toString(AgentStatus agentStatus) {
        return agentStatus == null ? null : agentStatus.name();
    }
    
    public static AgentStatus toAgentStatus(String agentStatus){
        return agentStatus == null ? null : AgentStatus.valueOf(agentStatus);
    }
    
    @Override
    public void createAgent(Agent agent) {
        LOG.debug("Starting createAgent");
        validate(agent);
        if (agent.getId() != null) {
            LOG.error("agent has assigned id");
            throw new ValidationException("agent has assigned id");
        }
        
        try(Connection con = ds.getConnection()) {
            PreparedStatement sta = con.prepareStatement(
                    "INSERT INTO Agent (codeName,status) VALUES (?,?)",
                     Statement.RETURN_GENERATED_KEYS);
            
            sta.setString(1, agent.getCodeName());
            sta.setString(2, toString(agent.getStatus()));
            
            sta.executeUpdate();
            try(ResultSet keys = sta.getGeneratedKeys()) {
                if(keys.next()) {
                    Long id = keys.getLong(1);
                    agent.setId(id);
                    LOG.debug("createAgent ended successfully");
                }
            }
        } catch(SQLException ex) {
            LOG.error("create agent failure", ex);
            throw new ServiceException("create agent failure", ex);
        }
    }

    @Override
    public void updateAgent(Agent agent) {
        LOG.debug("Starting updateAgent");
        validate(agent);
        if (agent.getId() == null) {
            LOG.error("agent has null id");
            throw new ValidationException("agent has null id");
        }
        
        try(Connection con = ds.getConnection()) {
            PreparedStatement sta = con.prepareStatement(
                    "UPDATE Agent SET codeName = ?, status = ? WHERE id = ?");
                
            sta.setString(1, agent.getCodeName());
            sta.setString(2, toString(agent.getStatus()));
            sta.setLong(3, agent.getId());

            int result = sta.executeUpdate();
            if(result != 1) {
                LOG.error("updated " + result + " instead of 1 agent");
                throw new ServiceException("updated " + result + " instead of 1 agent");
            }
            LOG.debug("updateAgent ended successfully");
        } catch (SQLException ex) {
            LOG.error("update agent failure", ex);
            throw new ServiceException("update agent failure", ex);
        }
    }

    @Override
    public Agent findAgentById(Long id) {
        LOG.debug("Starting findAgentById");
        if(id == null){
            LOG.error("Id is null");
            throw new ValidationException("Id is null");
        }

        try(Connection connection = ds.getConnection();
            PreparedStatement st = connection.prepareStatement(
                    "SELECT id, codeName, status FROM Agent WHERE id=?")){
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if(rs.next()){
                    LOG.debug("findAgentById ended successfully");
                    return dataToAgent(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOG.error("Error when getting all agents from DB", e);
            throw new ServiceException("Error when getting all agents from DB", e);
        }
    }

    @Override
    public List<Agent> findAllAgents() {
        LOG.debug("Starting findAllAgents");
        try(Connection connection = ds.getConnection();
            PreparedStatement st = connection.prepareStatement(
                    "SELECT id, codeName, status FROM Agent")){
            try (ResultSet rs = st.executeQuery()) {
                List<Agent> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(dataToAgent(rs));
                }
                LOG.debug("findAllAgents ended successfully");
                return result;
            }
        } catch (SQLException e) {
            LOG.error("Error when getting all agents from DB", e);
            throw new ServiceException("Error when getting all agents from DB", e);
        }
    }

    public static Agent dataToAgent(ResultSet resultSet) throws SQLException {
        Agent agent = new Agent();
        agent.setId(resultSet.getLong("id"));
        agent.setCodeName(resultSet.getString("codeName"));
        agent.setStatus(toAgentStatus(resultSet.getString("status")));
        return agent;
    }

}
