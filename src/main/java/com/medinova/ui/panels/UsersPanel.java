
/*
package com.medinova.ui.panels;

import com.medinova.dao.UserDAO;
import com.medinova.model.User;
import com.medinova.model.enums.Role;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsersPanel extends JPanel {

    private final UserDAO dao = new UserDAO();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","Username","Role","Full Name"}, 0){
        public boolean isCellEditable(int r, int c){ return false; }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfUser = new JTextField();
    private final JTextField tfPass = new JTextField();
    private final JTextField tfFull = new JTextField();
    private final JComboBox<Role> cbRole = new JComboBox<>(Role.values());

    public UsersPanel(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(buildForm(), BorderLayout.NORTH);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottomActions(), BorderLayout.SOUTH);
        reload();
    }

    private JPanel buildForm(){
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Users"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,8,6,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfUser.setPreferredSize(new Dimension(150,26));
        tfPass.setPreferredSize(new Dimension(150,26));
        tfFull.setPreferredSize(new Dimension(240,26));

        int y=0;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Username"), c);
        c.gridx=1; p.add(tfUser, c);
        c.gridx=2; p.add(new JLabel("Password"), c);
        c.gridx=3; p.add(tfPass, c);
        y++;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Role"), c);
        c.gridx=1; p.add(cbRole, c);
        c.gridx=2; p.add(new JLabel("Full Name"), c);
        c.gridx=3; p.add(tfFull, c);
        y++;
        JButton add = new JButton("Add");
        add.addActionListener(e -> addUser());
        add.setBackground(new Color(52,152,219)); //background of add btn

        c.gridx=3;c.gridy=y;c.gridwidth=4;
        p.add(add, c);

        return p;
    }

    private JPanel buildBottomActions(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,8));
        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> reload());
        JButton upd = new JButton("Update");
        upd.addActionListener(e -> updateSelected());
        JButton del = new JButton("Delete");
        del.addActionListener(e -> deleteSelected());
        del.setBackground(new Color(231,76,60)); //background of delete btn

        p.add(ref); p.add(upd); p.add(del);
        return p;
    }

    private boolean validateForm(){
        if(tfUser.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Username required!");
            return false;
        }
        if(tfPass.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Password required!");
            return false;
        }
        if(tfFull.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Full name required!");
            return false;
        }
        return true;
    }

    private void clearForm(){
        tfUser.setText("");
        tfPass.setText("");
        tfFull.setText("");
        cbRole.setSelectedIndex(0);
    }

    private void addUser(){
        if(!validateForm()) return;
        try {
            User u = new User(
                    tfUser.getText().trim(),
                    tfPass.getText().trim(),
                    (Role) cbRole.getSelectedItem(),
                    tfFull.getText().trim()
            );
            dao.add(u);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "User added!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select an User!");
            return;
        }
        try {
            User u = new User();
            u.setUserId((Integer) model.getValueAt(r,0));
            u.setUsername((String) model.getValueAt(r,1));
            u.setRole(Role.valueOf(String.valueOf(model.getValueAt(r,2))));
            u.setFullName((String) model.getValueAt(r,3));
            u.setPassword(tfPass.getText().trim().isEmpty() ? "password" : tfPass.getText().trim());
            dao.update(u);
            reload();
            JOptionPane.showMessageDialog(this, "Updated!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select an User!");
            return;
        }
        int id = (Integer) model.getValueAt(r,0);
        dao.delete(id);
        reload();
    }

    private void reload(){
        model.setRowCount(0);
        dao.getAll().forEach(u -> model.addRow(new Object[]{
                u.getUserId(),
                u.getUsername(),
                u.getRole(),
                u.getFullName()
        }));
    }
}

 */

package com.medinova.ui.panels;

import com.medinova.dao.UserDAO;
import com.medinova.model.User;
import com.medinova.model.enums.Role;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class UsersPanel extends JPanel {

    private final UserDAO dao = new UserDAO();
    private List<User> allUsers;

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","Username","Role","Full Name"}, 0){
        public boolean isCellEditable(int r, int c){ return false; }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfUser = new JTextField();
    private final JTextField tfPass = new JTextField();
    private final JTextField tfFull = new JTextField();
    private final JComboBox<Role> cbRole = new JComboBox<>(Role.values());


    private final JTextField tfSearch = new JTextField();
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnClear = new JButton("Clear");

    public UsersPanel(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(buildForm(), BorderLayout.NORTH);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottomActions(), BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> fillForm());

        reload();
    }

    private JPanel buildForm(){
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Users"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,8,6,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfUser.setPreferredSize(new Dimension(150,26));
        tfPass.setPreferredSize(new Dimension(150,26));
        tfFull.setPreferredSize(new Dimension(240,26));

        int y=0;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Username"), c);
        c.gridx=1; p.add(tfUser, c);
        c.gridx=2; p.add(new JLabel("Password"), c);
        c.gridx=3; p.add(tfPass, c);
        y++;

        c.gridx=0;c.gridy=y; p.add(new JLabel("Role"), c);
        c.gridx=1; p.add(cbRole, c);
        c.gridx=2; p.add(new JLabel("Full Name"), c);
        c.gridx=3; p.add(tfFull, c);
        y++;

        JButton add = new JButton("Add");
        add.setBackground(new Color(52,152,219));
        add.setForeground(Color.WHITE);
        add.addActionListener(e -> addUser());

        c.gridx=3;c.gridy=y;c.gridwidth=1;
        p.add(add, c);
        y++;


        c.gridx=0;c.gridy=y; p.add(new JLabel("Search"), c);
        c.gridx=1;c.gridwidth=2; p.add(tfSearch, c);
        c.gridwidth=1;
        JPanel searchBtns = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        btnSearch.addActionListener(e -> applySearch());
        btnClear.addActionListener(e -> { tfSearch.setText(""); reload(); });
        searchBtns.add(btnSearch);
        searchBtns.add(btnClear);
        c.gridx=3; p.add(searchBtns, c);

        return p;
    }

    private JPanel buildBottomActions(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,8));
        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> reload());

        JButton upd = new JButton("Update");
        upd.addActionListener(e -> updateSelected());

        JButton del = new JButton("Delete");
        del.addActionListener(e -> deleteSelected());
        del.setBackground(new Color(231,76,60));
        del.setForeground(Color.WHITE);

        p.add(ref); p.add(upd); p.add(del);
        return p;
    }

    private boolean validateForm(){
        if(tfUser.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Username required!");
            return false;
        }
        if(tfPass.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Password required!");
            return false;
        }
        if(tfFull.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Full name required!");
            return false;
        }
        return true;
    }

    private void clearForm(){
        tfUser.setText("");
        tfPass.setText("");
        tfFull.setText("");
        cbRole.setSelectedIndex(0);
        tfSearch.setText("");
        table.clearSelection();
    }

    private void addUser(){
        if(!validateForm()) return;
        try {
            User u = new User(
                    tfUser.getText().trim(),
                    tfPass.getText().trim(),
                    (Role) cbRole.getSelectedItem(),
                    tfFull.getText().trim()
            );
            dao.add(u);
            reload();
            JOptionPane.showMessageDialog(this, "User added!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select a user!");
            return;
        }
        if(!validateForm()) return;
        try {
            int id = (Integer) model.getValueAt(r,0);
            User u = new User(
                    tfUser.getText().trim(),
                    tfPass.getText().trim(),
                    (Role) cbRole.getSelectedItem(),
                    tfFull.getText().trim()
            );
            u.setUserId(id);
            dao.update(u);
            reload();
            JOptionPane.showMessageDialog(this, "User updated!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select a user!");
            return;
        }
        int id = (Integer) model.getValueAt(r,0);
        dao.delete(id);
        reload();
    }

    private void reload(){
        allUsers = dao.getAll();
        model.setRowCount(0);
        allUsers.forEach(u -> model.addRow(new Object[]{
                u.getUserId(),
                u.getUsername(),
                u.getRole(),
                u.getFullName()
        }));
        clearForm();
    }

    private void applySearch(){
        String q = tfSearch.getText().trim().toLowerCase();
        if(q.isEmpty()){
            reload();
            return;
        }
        List<User> filtered = allUsers.stream()
                .filter(u -> String.valueOf(u.getUserId()).toLowerCase().contains(q)
                        || u.getUsername().toLowerCase().contains(q)
                        || u.getFullName().toLowerCase().contains(q))
                .collect(Collectors.toList());

        model.setRowCount(0);
        filtered.forEach(u -> model.addRow(new Object[]{
                u.getUserId(),
                u.getUsername(),
                u.getRole(),
                u.getFullName()
        }));
    }

    private void fillForm(){
        int row = table.getSelectedRow();
        if(row < 0) return;

        tfUser.setText(String.valueOf(model.getValueAt(row,1)));
        cbRole.setSelectedItem(Role.valueOf(String.valueOf(model.getValueAt(row,2))));
        tfFull.setText(String.valueOf(model.getValueAt(row,3)));
        tfPass.setText("");
    }
}
