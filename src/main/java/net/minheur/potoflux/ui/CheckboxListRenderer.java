package net.minheur.potoflux.ui;

import javax.swing.*;
import java.awt.*;

/**
 * This is a checkbox that can be put in a multiple selection list.
 */
public class CheckboxListRenderer extends JCheckBox implements ListCellRenderer<Object> {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
        setSelected(isSelected);
        setBackground(list.getBackground());
        setForeground(list.getForeground());

        return this;
    }
}
