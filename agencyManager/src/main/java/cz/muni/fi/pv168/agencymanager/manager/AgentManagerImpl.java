/**
 *
 * @author Jakub Suslik
 */
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
            
            try(ResultSet keys = sta.executeQuery();) {
                if(keys.next()) {
                    agent.setId(keys.getLong(1));
                }
            }
        } catch(SQLException ex) {
            throw new ServiceException("create agent failiure", ex);
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
            throw new ServiceException("update agent failiure", ex);
        }
    }

    @Override
    public void deleteAgent(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Agent findAgentById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Agent> findAllAgents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
