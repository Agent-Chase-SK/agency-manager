
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.common.ServiceException;
import cz.muni.fi.pv168.agencymanager.common.ValidationException;
import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.status.AgentStatus;
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

    public AgentManagerImpl(DataSource ds) {
        if (ds == null) {
            throw new IllegalArgumentException("data source is null");
        }
        this.ds = ds;
    }
    
    private void validate(Agent agent) {
        if (agent == null) {
            throw new ValidationException("agent is null");
        }
        if (agent.getCodeName() == null) {
            throw new ValidationException("agent codename is null");
        }
        if (agent.getStatus() == null) {
            throw new ValidationException("agent status is null");
        }
    }
    
    private static String toString(AgentStatus agentStatus) {
        return agentStatus == null ? null : agentStatus.name();
    }
    
    private static AgentStatus toAgentStatus(String agentStatus){
        return agentStatus == null ? null : AgentStatus.valueOf(agentStatus);
    }
    
    @Override
    public void createAgent(Agent agent) {
        validate(agent);
        if (agent.getId() != null) {
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
                }
            }
        } catch(SQLException ex) {
            throw new ServiceException("create agent failure", ex);
        }
    }

    @Override
    public void updateAgent(Agent agent) {
        validate(agent);
        if (agent.getId() == null) {
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
                throw new ServiceException("updated " + result + " instead of 1 agent");
            }
        } catch (SQLException ex) {
            throw new ServiceException("update agent failure", ex);
        }
    }

    @Override
    public void deleteAgent(Agent agent) {
        if (agent == null) throw new ValidationException("agent is null");
        if (agent.getId() == null) throw new ServiceException("agent id is null");
        try(Connection connection = ds.getConnection();
            PreparedStatement st = connection.prepareStatement("DELETE FROM Agent WHERE id = ?")){
            st.setLong(1, agent.getId());
            int result = st.executeUpdate();
            if(result != 1) throw new ServiceException("deleted " + result + " instead of 1 agent");
            agent.setId(null);
        } catch (SQLException e) {
            throw new ServiceException("Error during deletion agent from db", e);
        }
    }

    @Override
    public Agent findAgentById(Long id) {
        if(id == null){
            throw new ValidationException("Id is null");
        }

        try(Connection connection = ds.getConnection();
            PreparedStatement st = connection.prepareStatement(
                    "SELECT id, codeName, status FROM Agent WHERE id=?")){
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if(rs.next()){
                    return dataToAgent(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new ServiceException("Error when getting all agents from DB0", e);
        }
    }

    @Override
    public List<Agent> findAllAgents() {
        try(Connection connection = ds.getConnection();
            PreparedStatement st = connection.prepareStatement(
                    "SELECT id, codeName, status FROM Agent")){
            try (ResultSet rs = st.executeQuery()) {
                List<Agent> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(dataToAgent(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new ServiceException("Error when getting all agents from DB0", e);
        }
    }

    private Agent dataToAgent(ResultSet resultSet) throws SQLException {
        Agent agent = new Agent();
        agent.setId(resultSet.getLong("id"));
        agent.setCodeName(resultSet.getString("codeName"));
        agent.setStatus(toAgentStatus(resultSet.getString("status")));
        return agent;
    }

}
