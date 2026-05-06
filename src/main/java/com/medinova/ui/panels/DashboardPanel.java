package com.medinova.ui.panels;

import com.medinova.dao.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(){
        //setLayout(new BorderLayout(10,10));
        setLayout(new FlowLayout(FlowLayout.LEFT,10, 10));
        setBackground(new Color(242,245,250));
        setBorder(new EmptyBorder(16,16,16,16));

        JLabel title = new JLabel("Dashboard");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(new Color(32,40,73));
        add(title, BorderLayout.NORTH);

        PatientDAO pDAO = new PatientDAO();
        DoctorDAO dDAO = new DoctorDAO();
        UserDAO uDAO = new UserDAO();
        AppointmentDAO aDAO = new AppointmentDAO();
        InventoryDAO iDAO = new InventoryDAO();

        JPanel grid = new JPanel(new GridLayout(1,5,14,14));
        grid.setOpaque(false);
        grid.add(tile("Patients", pDAO.getAll().size(), new Color(52,152,219)));
        grid.add(tile("Doctors", dDAO.getAll().size(), new Color(46,204,113)));
        grid.add(tile("Appointments", aDAO.getAll().size(), new Color(241,196,15)));
        grid.add(tile("Low Stock", iDAO.countLowStock(), new Color(231,76,60)));
        grid.add(tile("Users", uDAO.countUsers(), new Color(155,89,182)));

        add(grid, BorderLayout.CENTER);
    }

    private JPanel tile(String label, int value, Color bg){
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        p.setPreferredSize(new Dimension(183, 140));

        JLabel t = new JLabel(label);
        t.setForeground(Color.WHITE);
        t.setFont(t.getFont().deriveFont(Font.PLAIN, 15f));

        JLabel v = new JLabel(String.valueOf(value), SwingConstants.RIGHT);
        v.setForeground(Color.WHITE);
        v.setFont(v.getFont().deriveFont(Font.BOLD, 28f));

        p.add(t, BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
        return p;
    }
}
