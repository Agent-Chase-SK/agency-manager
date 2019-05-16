/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.agencymanagergui;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Jakub
 */
public final class ToStringListCellRenderer implements ListCellRenderer {
    private final ListCellRenderer originalRenderer;
    private final ToString toString;

    public ToStringListCellRenderer(final ListCellRenderer originalRenderer,
            final ToString toString) {
        this.originalRenderer = originalRenderer;
        this.toString = toString;
    }

    @Override
    public Component getListCellRendererComponent(final JList list,
            final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus) {
        return originalRenderer.getListCellRendererComponent(list,
            toString.toString(value), index, isSelected, cellHasFocus);
    }

}
