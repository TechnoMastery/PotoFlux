package net.minheur.potoflux.screen.tabs.all;

import net.minheur.potoflux.modGen.GeneratorStageHandler;
import net.minheur.potoflux.screen.tabs.BaseTab;

import javax.swing.*;
import java.awt.*;

public class MinecraftModGenTab extends BaseTab {
    @Override
    protected void setPanel() {
        JLabel infoName = new JLabel("This page is only Minecraft for devs, so english only.");
        infoName.setFont(new Font("Consolas", Font.BOLD, 15));
        infoName.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(infoName);

        PANEL.add(Box.createVerticalStrut(10));

        JButton createButton = new JButton("Create a mod");
        createButton.setFont(new Font("Consolas", Font.BOLD, 15));
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        PANEL.add(createButton);

        createButton.addActionListener(e -> {
            GeneratorStageHandler.createGenerator(PANEL).run();
        });
    }

    @Override
    protected String getTitle() {
        return "Minecraft mod generator";
    }
}
