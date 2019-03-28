package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Mission;
import cz.muni.fi.pv168.agencymanager.status.MissionStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class MissionManagerImpl implements MissionManager {

    private DataSource dataSource;

    public MissionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createMission(Mission mission) {
        // ---TO DO--- check all attributes of the mission
        if (mission.getId() != null) throw new IllegalArgumentException("mission id is already set");

        try(Connection conn = dataSource.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO Mission (codeName, status, date, location) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)){

            st.setString(1, mission.getCodeName());
            st.setString(2,toString(mission.getStatus()));
            st.setDate(3,toSqlDate(mission.getDate()));
            st.setString(4,mission.getLocation());

            st.executeUpdate();
            //mission.setId((DBUtils.getId(st.getGeneratedKeys()));       // import DBUtils from Grave Manager???

        } catch (SQLException e) {
            e.printStackTrace();                            // space to throw own exception?
        }
    }

    @Override
    public void updateMission(Mission mission) {

    }

    @Override
    public void deleteMission(Mission mission) {

    }

    @Override
    public Mission findMissionById(Long id) {
        return null;
    }

    @Override
    public List<Mission> findAllMissions() {
        return null;
    }

    private void validate(Mission mission){
        if(mission == null){
            throw new IllegalArgumentException("mission is null");
            // --To DO--//
        }

    }

    private static String toString(MissionStatus missionStatus) {
        return missionStatus == null ? null : missionStatus.name();
    }

    private static Date toSqlDate(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }
}
