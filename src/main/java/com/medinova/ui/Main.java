package com.medinova.ui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLaf.setup(new FlatMacDarkLaf());
            UIManager.put("Component.arc", 18);
            UIManager.put("Button.arc", 18);
            UIManager.put("TextComponent.arc", 16);
            new LoginFrame().setVisible(true);
        });
    }
}
