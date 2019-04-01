/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.exception.DatabaseException;
import cz.muni.fi.pv168.agencymanager.exception.IllegalEntityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
            throw new IllegalEntityException("agent is null");
        }
        if (agent.getCodeName() == null) {
            throw new IllegalEntityException("agent codename is null");
        }
        if (agent.getStatus() == null) {
            throw new IllegalEntityException("agent status is null");
        }
    }
    
    @Override
    public void createAgent(Agent agent) {
        validate(agent);
        if (agent.getId() != null) {
            throw new IllegalEntityException("agent has assigned id");
        }
        
        try(Connection con = ds.getConnection()) {
            PreparedStatement sta = con.prepareStatement("INSERT INTO Agents (codeName,status) VALUES (?,?)",
                     Statement.RETURN_GENERATED_KEYS);
            
            //WIP
            
        } catch(SQLException ex) {
            throw new DatabaseException("create agent failiure", ex);
        }
    }

    @Override
    public void updateAgent(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
