package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.common.ServiceException;
import cz.muni.fi.pv168.agencymanager.common.ValidationException;
import cz.muni.fi.pv168.agencymanager.entity.Mission;
import cz.muni.fi.pv168.agencymanager.status.MissionStatus;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MissionManagerImpl implements MissionManager {

    private final DataSource dataSource;
    private final Clock clock;

    public MissionManagerImpl(DataSource dataSource, Clock clock) {
        this.clock = clock;
        this.dataSource = dataSource;
    }

    @Override
    public void createMission(Mission mission) {
        validate(mission);
        if (mission.getId() != null) throw new ValidationException("mission id is already set");
        if (mission.getAgentId() == null) throw new ValidationException("agent is not set");

        try(Connection conn = dataSource.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO Mission (codeName, status, date, location, agentId) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)){

            st.setString(1, mission.getCodeName());
            st.setString(2,toString(mission.getStatus()));
            st.setDate(3, toSqlDate(mission.getDate()));
            st.setString(4, mission.getLocation());
            st.setLong(5, mission.getAgentId());

            st.executeUpdate();
            try(ResultSet keys = st.getGeneratedKeys()) {
                if(keys.next()) {
                    Long id = keys.getLong(1);
                    mission.setId(id);
                }
            }

        } catch (SQLException e) {
            throw new ServiceException("Error when inserting mission into DB",e);
        }
    }

    @Override
    public void updateMission(Mission mission) {
        validate(mission);
        if (mission.getAgentId() == null) throw new ValidationException("agentId is null");
        if (mission.getId() == null) throw new ValidationException("mission id is null");
        try(Connection connection = dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(
                    "UPDATE Mission SET codeName = ?, status = ?, date = ?, location = ?, agentID = ?" +
                            " WHERE id = ?")){
            st.setString(1, mission.getCodeName());
            st.setString(2, toString(mission.getStatus()));
            st.setDate(3, toSqlDate(mission.getDate()));
            st.setString(4, mission.getLocation());
            st.setLong(5, mission.getAgentId());
            st.setLong(6, mission.getId());
            int result = st.executeUpdate();
            if(result != 1) throw new ServiceException("updated " + result + " instead of 1 mission");
        } catch (SQLException e) {
            throw new ServiceException("Error when updating mission in the DB",e);
        }

    }

    @Override
    public Mission findMissionById(Long id) {
        if(id == null){
            throw new ValidationException("Id is null");
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(
                    "SELECT id, codeName, status, date, location, agentId FROM Mission WHERE id = ?")){
            st.setLong(1,id);
            try(ResultSet result = st.executeQuery()){
                if(result.next()){
                    return dataToMission(result);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new ServiceException("Error when getting mission with id = " + id, e);
        }
    }

    @Override
    public List<Mission> findAllMissions() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(
                    "SELECT id, codeName, status, date, location, agentId FROM Mission")){
            try (ResultSet rs = st.executeQuery()) {
                List<Mission> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(dataToMission(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new ServiceException("Error when getting all missions from DB", e);
        }

    }

    public static Mission dataToMission(ResultSet resultSet) throws SQLException {
        Mission mission = new Mission();
        mission.setId(resultSet.getLong("id"));
        mission.setCodeName(resultSet.getString("codeName"));
        mission.setLocation(resultSet.getString("location"));
        mission.setStatus(toMissionStatus(resultSet.getString("status")));
        mission.setDate(toLocalDate(resultSet.getDate("date")));
        Long agentId = resultSet.getLong("agentId");
        if (agentId == 0L) {
            mission.setAgentId(null);
        } else {
            mission.setAgentId(agentId);
        }
        return mission;
    }

    private void validate(Mission mission){
        if(mission == null){
            throw new ValidationException("mission is null");
        }
        if(mission.getCodeName() == null){
            throw new ValidationException("Code name is null");
        }
        if(mission.getLocation() == null){
            throw new ValidationException("Location is null");
        }
        if(mission.getStatus() == null){
            throw new ValidationException("Status is null");
        }
        if(mission.getDate() == null){
            throw new ValidationException("Date is null");
        }
        LocalDate today = LocalDate.now(clock);
        if (mission.getStatus() == MissionStatus.SCHEDULED && mission.getDate().isBefore(today)) {
            throw new ValidationException("mission is scheduled in past");
        }
        if (mission.getStatus() != MissionStatus.SCHEDULED && mission.getDate().isAfter(today)) {
            throw new ValidationException("mission is not scheduled in future");
        }
    }

    public static String toString(MissionStatus missionStatus) {
        return missionStatus == null ? null : missionStatus.name();
    }

    public static Date toSqlDate(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }

    private static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }

    private static MissionStatus toMissionStatus(String missionStatus){
        return missionStatus == null ? null : MissionStatus.valueOf(missionStatus);
    }
}
