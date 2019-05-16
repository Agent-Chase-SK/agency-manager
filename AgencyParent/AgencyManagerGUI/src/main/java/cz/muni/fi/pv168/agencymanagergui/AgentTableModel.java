/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.agencymanagergui;

import cz.muni.fi.pv168.agencymanager.common.Main;
import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.manager.AgentManager;
import cz.muni.fi.pv168.agencymanager.manager.AgentManagerImpl;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jakub
 */
public class AgentTableModel extends AbstractTableModel {
    private final AgentManager manager;
    private final ResourceBundle bundle = java.util.ResourceBundle.getBundle("AgencyManager");

    public AgentTableModel() {
        try {
            this.manager = new AgentManagerImpl(Main.getDataSource());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private enum Column {
        
        CODENAME("codeName" , String.class),
        STATUS("status" , String.class);
        
        private final String label;
        private final Class<?> type;
        

        private <T> Column(String label, Class<T> type) {
            this.label = label;
            this.type = type;            
        }
    }

    @Override
    public int getRowCount() {
        return manager.findAllAgents().size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agent currentAgent = manager.findAgentById(new Long(rowIndex+1));
        switch (columnIndex) {
            case 0:
                return currentAgent.getCodeName();
            case 1:
                return bundle.getString(currentAgent.getStatus().toString());
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
