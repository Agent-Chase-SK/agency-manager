/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.agencymanagergui;

import cz.muni.fi.pv168.agencymanager.common.Main;
import cz.muni.fi.pv168.agencymanager.entity.Mission;
import cz.muni.fi.pv168.agencymanager.manager.AgentManager;
import cz.muni.fi.pv168.agencymanager.manager.AgentManagerImpl;
import cz.muni.fi.pv168.agencymanager.manager.MissionManager;
import cz.muni.fi.pv168.agencymanager.manager.MissionManagerImpl;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jakub
 */
public class MissionTableModel extends AbstractTableModel {
    private final MissionManager missionManager;
    private final AgentManager agentManager;
    private final ResourceBundle bundle = java.util.ResourceBundle.getBundle("AgencyManager");

    public MissionTableModel() {
        try {
            this.missionManager = new MissionManagerImpl(Main.getDataSource(), Clock.systemDefaultZone());
            this.agentManager = new AgentManagerImpl(Main.getDataSource());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private String getAgentFromMission(Mission mission) {
        return agentManager.findAgentById(mission.getAgentId()).getCodeName();
    }
    
    private enum Column {
        
        CODENAME("codeName", String.class),
        STATUS("status", String.class),
        DATE("date", LocalDate.class),
        LOCATION("location", String.class),
        AGENT("agent", String.class);
        
        private final String label;
        private final Class<?> type;

        private <T> Column(String label, Class<T> type) {
            this.label = label;
            this.type = type;
        }
    }

    @Override
    public int getRowCount() {
        return missionManager.findAllMissions().size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mission currentMission = missionManager.findMissionById(new Long(rowIndex+1));
        switch (columnIndex) {
            case 0:
                return currentMission.getCodeName();
            case 1:
                return bundle.getString(currentMission.getStatus().toString());
            case 2:
                return currentMission.getDate();
            case 3:
                return currentMission.getLocation();
            case 4:
                return getAgentFromMission(currentMission);
            default:
                throw new IndexOutOfBoundsException("Invalid columnIndex: " + columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Column.values()[columnIndex].type;
    }

    @Override
    public String getColumnName(int column) {
        return bundle.getString(Column.values()[column].label);
    }
    
    public void insertedRow(int rowIndex) {
        fireTableRowsInserted(rowIndex, rowIndex);
    }
    
    public void updatedCell(int rowIndex, int columnIndex) {
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
