package com.medinova.ui;

import com.medinova.model.User;
import com.medinova.model.enums.Role;
import com.medinova.ui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private final JPanel content = new JPanel(new CardLayout());
    private final Map<String, SidebarButton> buttons = new LinkedHashMap<>();
    private final User currentUser;

    public MainFrame(User user){
        this.currentUser = user;
        setTitle("Medinova - " + user.getFullName());
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        content.add(new DashboardPanel(), "Dashboard");
        content.add(new PatientsPanel(), "Patients");
        boolean doctorsReadOnly = (user.getRole() == Role.RECEPTIONIST);
        content.add(new DoctorsPanel(doctorsReadOnly), "Doctors");
        content.add(new AppointmentsPanel(), "Appointments");
        content.add(new InventoryPanel(), "Inventory");
        content.add(new UsersPanel(), "Users");
        content.add(new PrescriptionsPanel(), "Prescriptions");

        if(user.getRole() == Role.RECEPTIONIST){
            buttons.get("Inventory").setVisible(false);
            buttons.get("Users").setVisible(false);
        }

        switchTo("Dashboard");

        getContentPane().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                getContentPane().requestFocusInWindow();
            }
        });

    }

    private JPanel buildSidebar(){
        JPanel side = new JPanel(new BorderLayout());
        side.setPreferredSize(new Dimension(240, 800));
        side.setBackground(new Color(26,31,59));

        JPanel header = new JPanel(new GridLayout(2,1));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20,16,10,16));
        JLabel title = new JLabel("Medinova");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Management");
        subtitle.setForeground(new Color(160,170,200));
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 13f));
        header.add(title);
        header.add(subtitle);
        side.add(header, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(10,1,0,6));
        menu.setOpaque(false);
        menu.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        addMenuButton(menu, "Dashboard");
        addMenuButton(menu, "Patients");
        addMenuButton(menu, "Doctors");
        addMenuButton(menu, "Appointments");
        addMenuButton(menu, "Inventory");
        addMenuButton(menu, "Users");
        addMenuButton(menu, "Prescriptions");
        side.add(menu, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10,16,16,16));
        JLabel userLbl = new JLabel("Signed in: " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        userLbl.setForeground(new Color(120,132,168));
        userLbl.setFont(userLbl.getFont().deriveFont(Font.PLAIN, 11f));
        footer.add(userLbl, BorderLayout.NORTH);
        side.add(footer, BorderLayout.SOUTH);

        return side;
    }

    private void addMenuButton(JPanel menu, String name){
        SidebarButton btn = new SidebarButton(name);
        btn.addActionListener(e -> switchTo(name));
        buttons.put(name, btn);
        menu.add(btn);
    }

    private void switchTo(String card){
        buttons.forEach((k,v) -> v.setSelectedState(k.equals(card)));
        ((CardLayout) content.getLayout()).show(content, card);
    }


}
