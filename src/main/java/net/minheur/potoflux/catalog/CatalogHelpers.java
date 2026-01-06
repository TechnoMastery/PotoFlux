package net.minheur.potoflux.catalog;

import javax.swing.*;
import java.awt.*;

public class CatalogHelpers {
    public static JPanel buildCard(ModCatalog mod) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setOpaque(true);

        card.putClientProperty(
                "FlatLaf.style",
                "background: $Panel.background.lighten(4%); arc: 10"
        );

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    UIManager.getColor("Component.borderColor")
                ),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        // mod name
        JLabel title = new JLabel(mod.modId);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));

        // Statut
        JLabel status = new JLabel(mod.isPublished ? "Published" : "Not Published"); // TODO
        status.putClientProperty(
                "FlatLaf.style",
                mod.isPublished
                        ? "foreground: $Actions.Green"
                        : "foreground: $Actions.Red"
        );

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(status);

        card.add(textPanel, BorderLayout.WEST);
        return card;
    }
}

