/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

import cz.muni.fi.pv168.agencymanager.entity.Mission;
import java.util.List;

/**
 *
 * @author Jakub Suslik
 */
public interface MissionManager {
    public void createMission(Mission mission);
    public void updateMission(Mission mission);
    public void deleteMission(Mission mission);
    public Mission findMissionById(Long id);
    public List<Mission> findAllMissions();
}
