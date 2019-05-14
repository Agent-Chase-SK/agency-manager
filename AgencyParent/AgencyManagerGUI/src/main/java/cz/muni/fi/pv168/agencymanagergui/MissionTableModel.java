/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.agencymanagergui;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jakub
 */
public class MissionTableModel extends DefaultTableModel {
    private static final int NUM_OF_COLUMNS = 4;

    @Override
    public Object getValueAt(int row, int column) {
        return super.getValueAt(row, column); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getColumnName(int column) {
        return super.getColumnName(column); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        return NUM_OF_COLUMNS;
    }

    @Override
    public int getRowCount() {
        return super.getRowCount(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
