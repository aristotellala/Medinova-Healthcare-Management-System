package com.medinova.ui.panels;

import com.medinova.dao.DoctorDAO;
import com.medinova.model.Doctor;
import com.medinova.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorsPanel extends JPanel {

    private final DoctorDAO dao = new DoctorDAO();
    private final boolean readOnly;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","First Name","Last Name","Specialization","Email","Phone","Hours"}, 0){
        public boolean isCellEditable(int r, int c){ return false; }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfSearch = new JTextField(10);
    private final JTextField tfFirst = new JTextField();
    private final JTextField tfLast = new JTextField();
    private final JTextField tfSpec = new JTextField();
    private final JTextField tfEmail = new JTextField();
    private final JTextField tfPhone = new JTextField();
    private final JComboBox<String> cbStart = new JComboBox<>(timeOptions());
    private final JComboBox<String> cbEnd = new JComboBox<>(timeOptions());

    public DoctorsPanel(boolean readOnly){
        this.readOnly = readOnly;
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(buildTopForm(), BorderLayout.NORTH);
        styleTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(buildBottomActions(), BorderLayout.SOUTH);
        reload();
    }

    private JPanel buildTopForm(){
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder(readOnly ? "Doctors (read-only)" : "Doctors"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,8,6,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        Dimension fieldSize = new Dimension(160,26);
        tfFirst.setPreferredSize(fieldSize);
        tfLast.setPreferredSize(fieldSize);
        tfSpec.setPreferredSize(new Dimension(200,26));
        tfEmail.setPreferredSize(new Dimension(240,26));
        tfPhone.setPreferredSize(new Dimension(140,26));

        // Butoni per kerkim
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> findDoctor());

        int y=0;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Search (ID or Name):"), c);
        c.gridx=1; p.add(tfSearch, c);
        c.gridx=2; p.add(btnSearch, c);
        y++;

        //int y=0;
        c.gridx=0; c.gridy=y; p.add(new JLabel("First Name"), c);
        c.gridx=1; p.add(tfFirst, c);
        c.gridx=2; p.add(new JLabel("Last Name"), c);
        c.gridx=3; p.add(tfLast, c);
        y++;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Specialization"), c);
        c.gridx=1; p.add(tfSpec, c);
        c.gridx=2; p.add(new JLabel("Email"), c);
        c.gridx=3; p.add(tfEmail, c);
        y++;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Phone"), c);
        c.gridx=1; p.add(tfPhone, c);
        c.gridx=2; p.add(new JLabel("Working Hours"), c);
        JPanel hours = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        hours.add(cbStart);
        hours.add(new JLabel(" - "));
        hours.add(cbEnd);
        c.gridx=3; p.add(hours, c);
        y++;

        JButton add = new JButton("Add");
        add.setPreferredSize(new Dimension(220, 28));
        add.addActionListener(e -> addDoctor());
        add.setBackground(new Color(52,152,219)); //background of add btn


        /*
        JPanel addBtnCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        addBtnCenter.setPreferredSize(new Dimension(200,28));
        addBtnCenter.add(add);

         */

        if(readOnly) add.setEnabled(false);
        c.gridx=3; c.gridy=y; c.gridwidth=4;
        p.add(add, c);

        return p;
    }

    private JPanel buildBottomActions(){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> reload());
        JButton upd = new JButton("Update");
        upd.addActionListener(e -> updateSelected());
        JButton del = new JButton("Delete");
        del.addActionListener(e -> deleteSelected());
        del.setBackground(new Color(231,76,60)); //background of delete btn

        if(readOnly){
            upd.setEnabled(false);
            del.setEnabled(false);
        }
        p.add(ref); p.add(upd); p.add(del);
        return p;
    }

    private void styleTable(){
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(46,204,113));
        table.setSelectionForeground(Color.WHITE);
    }

    private String[] timeOptions(){
        java.util.List<String> list = new java.util.ArrayList<>();
        for(int h=6; h<=22; h++){
            list.add(String.format("%02d:00", h));
            list.add(String.format("%02d:30", h));
        }
        return list.toArray(new String[0]);
    }

    private boolean validateForm(){
        if(tfFirst.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "First name required!");
            return false;
        }
        if(tfLast.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Last name required!");
            return false;
        }
        return true;
    }

    private void clearForm(){
        tfFirst.setText("");
        tfLast.setText("");
        tfSpec.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        cbStart.setSelectedIndex(0);
        cbEnd.setSelectedIndex(1);
    }

    private void addDoctor(){
        if(!validateForm()) return;
        try {
            String hours = cbStart.getSelectedItem() + "-" + cbEnd.getSelectedItem();
            Doctor d = new Doctor(
                    tfFirst.getText().trim(),
                    tfLast.getText().trim(),
                    tfSpec.getText().trim(),
                    tfEmail.getText().trim(),
                    tfPhone.getText().trim(),
                    hours
            );
            dao.add(d);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Doctor added!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select a Doctor!");
            return;
        }

        try {
        int id = (int) model.getValueAt(r,0);
        String hours = cbStart.getSelectedItem() + "-" + cbEnd.getSelectedItem();


        Doctor d = new Doctor(
                tfFirst.getText().trim(),
                tfLast.getText().trim(),
                tfSpec.getText().trim(),
                tfEmail.getText().trim(),
                tfPhone.getText().trim(),
                hours
        );

        d.setDoctorId(id);
            dao.update(d);
            reload();
            clearForm();

            JOptionPane.showMessageDialog(this, "Updated!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select a Doctor!");
            return;
        }
        int id = (Integer) model.getValueAt(r,0);
        dao.delete(id);
        reload();
    }

    private void reload(){
        model.setRowCount(0);
        dao.getAll().forEach(d -> model.addRow(new Object[]{
                d.getDoctorId(),
                d.getFirstName(),
                d.getLastName(),
                d.getSpecialization(),
                d.getEmail(),
                d.getPhone(),
                d.getWorkingHours()
        }));
        clearForm();
    }

    private void fillForm() {
        int row = table.getSelectedRow();

        if (row < 0) return;

        tfFirst.setText(String.valueOf(model.getValueAt(row,1)));
        tfLast.setText(String.valueOf(model.getValueAt(row,2)));
        tfSpec.setText(String.valueOf(model.getValueAt(row,3)));
        tfEmail.setText(String.valueOf(model.getValueAt(row,4)));
        tfPhone.setText(String.valueOf(model.getValueAt(row,5)));
        //cbStart.setSelectedItem(String.valueOf(model.getValueAt(row,6)).trim());

        String hours = String.valueOf(model.getValueAt(row,6));
        if (hours != null && hours.contains("-")) {

            String[] parts = hours.split("-");

            if (parts.length == 2) {

                cbStart.setSelectedItem(String.valueOf(parts[0]));
                cbEnd.setSelectedItem(String.valueOf(parts[1]));
            }
        }
    }

    private void findDoctor(){
        try{
            String query = tfSearch.getText().trim();
            if(query.isEmpty()){
                JOptionPane.showMessageDialog(this, "Enter Doctor ID or name to search!");
                return;
            }

            if(query.matches("\\d+")){ // Kerkim me ID
                int id = Integer.parseInt(query);
                Doctor d = dao.getById(id);
                if(d == null){
                    JOptionPane.showMessageDialog(this, "No Doctor found with ID: " + id);
                    return;
                }
                loadDoctor(d);
            } else {   // kerkim me emer
                List<Doctor> results = dao.searchByName(query);
                if(results.isEmpty()){
                    JOptionPane.showMessageDialog(this, "No patient found with name: " + query);
                    return;
                } else if(results.size() == 1){
                    loadDoctor(results.get(0));
                } else {
                    String[] names = results.stream()
                            .map(d -> d.getDoctorId() + " - " + d.getFirstName() + " " + d.getLastName())
                            .toArray(String[]::new);
                    String choice = (String) JOptionPane.showInputDialog(
                            this,
                            "Multiple doctors found:",
                            "Select Doctor",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            names,
                            names[0]
                    );
                    if(choice != null){
                        int id = Integer.parseInt(choice.split(" - ")[0]);
                        Doctor selected = results.stream().filter(d -> d.getDoctorId() == id).findFirst().orElse(null);
                        if(selected != null) loadDoctor(selected);
                    }
                }
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadDoctor(Doctor d){

        if (d == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found");
            return;
        }

        tfFirst.setText(d.getFirstName());
        tfLast.setText(d.getLastName());
        tfSpec.setText(String.valueOf(d.getSpecialization()));
        tfEmail.setText(d.getPhone());
        tfPhone.setText(d.getPhone());

        if (d.getWorkingHours() != null && d.getWorkingHours().contains("-")) {

            String[] parts = d.getWorkingHours().split("-");

            if (parts.length == 2) {

                cbStart.setSelectedItem(String.valueOf(parts[0]));
                cbEnd.setSelectedItem(String.valueOf(parts[1]));
            }
        }
    }
}
