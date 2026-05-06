package com.medinova.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JButton {

    private final Color base = new Color(26,31,59);
    private final Color hover = new Color(39,47,87);
    private final Color active = new Color(64,78,146);
    private boolean selected = false;

    public SidebarButton(String text){
        super(text);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setBackground(base);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        setHorizontalAlignment(LEFT);
        setFont(getFont().deriveFont(Font.PLAIN, 15f));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(!selected) setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!selected) setBackground(base);
            }
        });
    }

    public void setSelectedState(boolean sel){
        this.selected = sel;
        setBackground(sel ? active : base);
    }
}
