package net.minheur.potoflux.catalog;

import javax.swing.*;
import java.awt.*;

public class CatalogHelpers {
    public static JPanel buildCard(ModCatalog mod) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setBackground(new Color(45, 45, 45));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        // Nom du mod
        JLabel title = new JLabel(mod.modId);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));

        // Statut
        JLabel status = new JLabel(mod.isPublished ? "Published" : "Not Published"); // TODO
        status.setForeground(mod.isPublished ? new Color(120, 220, 120) : new Color(220, 120, 120));
        status.setFont(status.getFont().deriveFont(12f));

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

