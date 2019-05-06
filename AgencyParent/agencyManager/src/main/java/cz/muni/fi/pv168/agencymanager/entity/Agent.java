/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.entity;

import cz.muni.fi.pv168.agencymanager.status.AgentStatus;
import java.util.Objects;

/**
 *
 * @author Jakub Suslik
 */
public class Agent {
    private Long id;
    private String codeName;
    private AgentStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Agent{" + "id=" + id + ", codeName=" + codeName + ", status=" + status + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Agent other = (Agent) obj;
        return Objects.equals(this.id, other.id);
    }
}
