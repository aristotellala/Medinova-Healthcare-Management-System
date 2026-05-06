package com.medinova.ui;

import com.medinova.dao.UserDAO;
import com.medinova.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField username = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JLabel error = new JLabel(" ", SwingConstants.CENTER);
    private final UserDAO dao = new UserDAO();

    public LoginFrame(){
        setTitle("Medinova - Login");
        setSize(480, 540);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(10,12,20));

        JPanel head = new JPanel(new GridLayout(2,1));
        head.setOpaque(false);
        JLabel brand = new JLabel("Medinova", SwingConstants.CENTER);
        brand.setFont(brand.getFont().deriveFont(Font.BOLD, 28f));
        brand.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Healthcare Management", SwingConstants.CENTER);
        sub.setForeground(new Color(180,190,210));
        head.add(brand);
        head.add(sub);
        head.setBorder(BorderFactory.createEmptyBorder(30,16,10,16));
        add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(20,30,20,30));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,4,10,4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;

        JLabel uLbl = new JLabel("Username");
        uLbl.setForeground(Color.WHITE);
        form.add(uLbl, c);
        c.gridy++;
        username.setPreferredSize(new Dimension(280, 36));
        form.add(username, c);

        c.gridy++;
        JLabel pLbl = new JLabel("Password");
        pLbl.setForeground(Color.WHITE);
        form.add(pLbl, c);
        c.gridy++;
        password.setPreferredSize(new Dimension(280, 36));
        form.add(password, c);

        c.gridy++;
        JButton login = new JButton("Login");
        login.setPreferredSize(new Dimension(140, 38));
        login.addActionListener(e -> doLogin());
        form.add(login, c);

        add(form, BorderLayout.CENTER);

        error.setForeground(new Color(230,80,80));
        error.setBorder(BorderFactory.createEmptyBorder(10,10,16,10));
        add(error, BorderLayout.SOUTH);
    }

    private void doLogin(){
        User u = dao.login(username.getText().trim(), new String(password.getPassword()));
        if(u == null){
            error.setText("Invalid username or password!");
            return;
        }
        dispose();
        new MainFrame(u).setVisible(true);
    }
}
