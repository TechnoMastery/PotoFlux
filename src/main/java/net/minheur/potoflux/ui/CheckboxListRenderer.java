package net.minheur.potoflux.ui;

import javax.swing.*;
import java.awt.*;

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
