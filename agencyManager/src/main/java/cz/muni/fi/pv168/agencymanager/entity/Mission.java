/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.entity;

import cz.muni.fi.pv168.agencymanager.status.MissionStatus;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Jakub Suslik
 */
public class Mission {
    private Long id;
    private String codeName;
    private MissionStatus status;
    private LocalDate date;
    private String location;
    private Long agentId;

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

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "Mission{" + "id=" + id + ", codeName=" + codeName + ", status=" + status + ", date=" + date + ", location=" + location + ", agentId=" + agentId + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Mission other = (Mission) obj;
        return Objects.equals(this.id, other.id);
    }
}
