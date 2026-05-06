
/*
package com.medinova.ui.panels;

import com.medinova.dao.PatientDAO;
import com.medinova.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientsPanel extends JPanel {

    private final PatientDAO dao = new PatientDAO();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","First Name","Last Name","Age","Gender","Phone","Email","Diagnosis","DoctorId"}, 0){
        public boolean isCellEditable(int r, int c){ return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField tfFirst = new JTextField();
    private final JTextField tfLast = new JTextField();
    private final JTextField tfAge = new JTextField();
    private final JComboBox<String> cbGender = new JComboBox<>(new String[]{"Male","Female"});
    private final JTextField tfPhone = new JTextField();
    private final JTextField tfEmail = new JTextField();
    private final JTextArea tfDiag = new JTextArea();
    private final JTextField tfDoctorId = new JTextField();

    public PatientsPanel(){
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
        p.setBorder(BorderFactory.createTitledBorder("Add / Edit Patient"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,8,6,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        Dimension fieldSize = new Dimension(170, 26);
        tfFirst.setPreferredSize(fieldSize);
        tfLast.setPreferredSize(fieldSize);
        tfAge.setPreferredSize(new Dimension(30,26));
        cbGender.setPreferredSize(new Dimension(120,26));
        tfPhone.setPreferredSize(fieldSize);
        tfEmail.setPreferredSize(new Dimension(220,26));
        tfDiag.setPreferredSize(new Dimension(220,60));
        tfDoctorId.setPreferredSize(new Dimension(30,26));

        int y=0;
        c.gridx=0; c.gridy=y; p.add(new JLabel("First Name"), c);
        c.gridx=1; p.add(tfFirst, c);
        c.gridx=2; p.add(new JLabel("Last Name"), c);
        c.gridx=3; p.add(tfLast, c);
        y++;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Age"), c);
        c.gridx=1; p.add(tfAge, c);
        c.gridx=2; p.add(new JLabel("Gender"), c);
        c.gridx=3; p.add(cbGender, c);
        y++;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Phone"), c);
        c.gridx=1; p.add(tfPhone, c);
        c.gridx=2; p.add(new JLabel("Email"), c);
        c.gridx=3; p.add(tfEmail, c);
        y++;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Diagnosis"), c);
        c.gridx=1; p.add(tfDiag, c);
        c.gridx=2; p.add(new JLabel("Doctor ID"), c);
        c.gridx=3; p.add(tfDoctorId, c);
        y++;

        JButton add = new JButton("Add");
        add.setPreferredSize(new Dimension(220, 28));
        add.addActionListener(e -> addPatient());
        add.setBackground(new Color(52,152,219)); //background of add btn


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

        p.add(ref); p.add(upd); p.add(del);
        return p;
    }

    private void styleTable(){
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(52,152,219));
        table.setSelectionForeground(Color.WHITE);
    }

    private boolean validateForm(){
        if(tfFirst.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "First name is required!");
            return false;
        }
        if(tfLast.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Last name is required!");
            return false;
        }
        if(tfAge.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Age is required!");
            return false;
        }
        if(cbGender.getSelectedItem()==null || cbGender.getSelectedItem().toString().isEmpty()){
            JOptionPane.showMessageDialog(this, "Gender is required!");
            return false;
        }
        return true;
    }

    private void clearForm(){
        tfFirst.setText("");
        tfLast.setText("");
        tfAge.setText("");
        cbGender.setSelectedIndex(0);
        tfPhone.setText("");
        tfEmail.setText("");
        tfDiag.setText("");
        tfDoctorId.setText("");
    }

    private void addPatient(){
        if(!validateForm()) return;
        try {
            int age = Integer.parseInt(tfAge.getText().trim());
            int doctorId = 0;
            if(!tfDoctorId.getText().trim().isEmpty()){
                doctorId = Integer.parseInt(tfDoctorId.getText().trim());
            }
            Patient p = new Patient(
                    tfFirst.getText().trim(),
                    tfLast.getText().trim(),
                    age,
                    cbGender.getSelectedItem().toString(),
                    tfPhone.getText().trim(),
                    tfEmail.getText().trim(),
                    tfDiag.getText().trim(),
                    doctorId
            );
            dao.add(p);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Patient added!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select a Patient!");
            return;
        }

        int id = (int) model.getValueAt(r,0);
        //int doctor_id = Integer.parseInt(String.valueOf(tfDoctorId));

        if (tfFirst.getText().trim().isEmpty() || tfLast.getText().trim().isEmpty() || tfAge.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill required fields!");
        }

        Patient p = new Patient(
                tfFirst.getText().trim(),
                tfLast.getText().trim(),
                Integer.parseInt(tfAge.getText().trim()),
                cbGender.getSelectedItem().toString(),
                tfPhone.getText().trim(),
                tfEmail.getText().trim(),
                tfDiag.getText().trim(),
                Integer.parseInt(tfDoctorId.getText().trim())
        );
        p.setPatientId(id);

        dao.update(p);
        reload();
        clearForm();
        JOptionPane.showMessageDialog(this,"Patient Updatet!");


    }

    private void deleteSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select a Patient!");
            return;
        }
        int id = (Integer) model.getValueAt(r,0);
        dao.delete(id);
        reload();
    }

    private void reload(){
        model.setRowCount(0);
        dao.getAll().forEach(p -> model.addRow(new Object[]{
                p.getPatientId(),
                p.getFirstName(),
                p.getLastName(),
                p.getAge(),
                p.getGender(),
                p.getPhone(),
                p.getEmail(),
                p.getDiagnosis(),
                p.getDoctorId()
        }));

        clearForm();
    }

    private void fillForm() {
        int row = table.getSelectedRow();

        if (row < 0) return;

        tfFirst.setText(String.valueOf(model.getValueAt(row,1)));
        tfLast.setText(String.valueOf(model.getValueAt(row,2)));
        tfAge.setText(String.valueOf(model.getValueAt(row,3)));
        cbGender.setSelectedItem(String.valueOf(model.getValueAt(row,4)));
        tfPhone.setText(String.valueOf(model.getValueAt(row,5)));
        tfEmail.setText(String.valueOf(model.getValueAt(row,6)));
        tfDiag.setText(String.valueOf(model.getValueAt(row,7)));
        tfDoctorId.setText(String.valueOf(model.getValueAt(row,8)));
    }
}


 */

package com.medinova.ui.panels;

import com.medinova.dao.PatientDAO;
import com.medinova.dao.DoctorDAO;
import com.medinova.model.Patient;
import com.medinova.model.Doctor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientsPanel extends JPanel {

    private final PatientDAO dao = new PatientDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","First Name","Last Name","Age","Gender","Phone","Email","Diagnosis","DoctorId"}, 0){
        public boolean isCellEditable(int r, int c){ return false; }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfSearch = new JTextField(10);
    private final JTextField tfFirst = new JTextField();
    private final JTextField tfLast = new JTextField();
    private final JTextField tfAge = new JTextField();
    private final JComboBox<String> cbGender = new JComboBox<>(new String[]{"Male","Female"});
    private final JTextField tfPhone = new JTextField();
    private final JTextField tfEmail = new JTextField();
    private final JTextArea tfDiag = new JTextArea();
    private final JTextField tfDoctorId = new JTextField();

    public PatientsPanel(){
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
        p.setBorder(BorderFactory.createTitledBorder("Add / Edit Patient"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,8,6,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        Dimension fieldSize = new Dimension(170, 26);
        tfFirst.setPreferredSize(fieldSize);
        tfLast.setPreferredSize(fieldSize);
        tfAge.setPreferredSize(new Dimension(40,26));
        cbGender.setPreferredSize(new Dimension(120,26));
        tfPhone.setPreferredSize(fieldSize);
        tfEmail.setPreferredSize(new Dimension(220,26));
        tfDiag.setPreferredSize(new Dimension(220,60));
        tfDoctorId.setPreferredSize(new Dimension(40,26));

        // Border blu kur klikojm Diagnosis
        Border normalBorder = BorderFactory.createLineBorder(Color.GRAY);
        Border focusBorder = BorderFactory.createLineBorder(new Color(52,152,219), 2);
        tfDiag.setBorder(normalBorder);
        tfDiag.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e){ tfDiag.setBorder(focusBorder); }
            public void focusLost(java.awt.event.FocusEvent e){ tfDiag.setBorder(normalBorder); }
        });

        // Kontroll doktori
        tfDoctorId.addFocusListener(new java.awt.event.FocusAdapter(){
            public void focusLost(java.awt.event.FocusEvent e){
                if(!tfDoctorId.getText().trim().isEmpty()){
                    try{
                        int id = Integer.parseInt(tfDoctorId.getText().trim());
                        Doctor d = doctorDAO.getById(id);
                        if(d == null){
                            JOptionPane.showMessageDialog(PatientsPanel.this, "Doctor not found with ID: " + id);
                            tfDoctorId.setText("");
                        }
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(PatientsPanel.this, "Invalid Doctor ID!");
                        tfDoctorId.setText("");
                    }
                }
            }
        });

        // Butoni per kerkim
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> findPatient());

        int y=0;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Search (ID or Name):"), c);
        c.gridx=1; p.add(tfSearch, c);
        c.gridx=2; p.add(btnSearch, c);
        y++;

        c.gridx=0; c.gridy=y; p.add(new JLabel("First Name"), c);
        c.gridx=1; p.add(tfFirst, c);
        c.gridx=2; p.add(new JLabel("Last Name"), c);
        c.gridx=3; p.add(tfLast, c);
        y++;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Age"), c);
        c.gridx=1; p.add(tfAge, c);
        c.gridx=2; p.add(new JLabel("Gender"), c);
        c.gridx=3; p.add(cbGender, c);
        y++;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Phone"), c);
        c.gridx=1; p.add(tfPhone, c);
        c.gridx=2; p.add(new JLabel("Email"), c);
        c.gridx=3; p.add(tfEmail, c);
        y++;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Diagnosis"), c);
        c.gridx=1; p.add(new JScrollPane(tfDiag), c);
        c.gridx=2; p.add(new JLabel("Doctor ID"), c);
        c.gridx=3; p.add(tfDoctorId, c);
        y++;

        JButton add = new JButton("Add");
        add.setPreferredSize(new Dimension(220, 28));
        add.addActionListener(e -> addPatient());
        add.setBackground(new Color(52,152,219));
        add.setForeground(Color.WHITE);

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
        del.setBackground(new Color(231,76,60));
        del.setForeground(Color.WHITE);

        p.add(ref); p.add(upd); p.add(del);
        return p;
    }

    private void styleTable(){
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(52,152,219));
        table.setSelectionForeground(Color.WHITE);
    }

    private boolean validateForm(){
        if(tfFirst.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "First name is required!");
            return false;
        }
        if(tfLast.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Last name is required!");
            return false;
        }
        if(tfAge.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Age is required!");
            return false;
        }
        return true;
    }

    private void clearForm(){
        tfFirst.setText("");
        tfLast.setText("");
        tfAge.setText("");
        cbGender.setSelectedIndex(0);
        tfPhone.setText("");
        tfEmail.setText("");
        tfDiag.setText("");
        tfDoctorId.setText("");
    }

    private void addPatient(){
        if(!validateForm()) return;
        try {
            int age = Integer.parseInt(tfAge.getText().trim());
            int doctorId = 0;
            if(!tfDoctorId.getText().trim().isEmpty()){
                doctorId = Integer.parseInt(tfDoctorId.getText().trim());
                if(doctorDAO.getById(doctorId) == null){
                    JOptionPane.showMessageDialog(this, "Doctor not found!");
                    return;
                }
            }

            Patient p = new Patient(
                    tfFirst.getText().trim(),
                    tfLast.getText().trim(),
                    age,
                    cbGender.getSelectedItem().toString(),
                    tfPhone.getText().trim(),
                    tfEmail.getText().trim(),
                    tfDiag.getText().trim(),
                    doctorId
            );
            dao.add(p);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Patient added!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select a Patient!");
            return;
        }
        try {
            int id = (int) model.getValueAt(r,0);
            int doctorId = 0;
            if(!tfDoctorId.getText().trim().isEmpty()){
                doctorId = Integer.parseInt(tfDoctorId.getText().trim());
                if(doctorDAO.getById(doctorId) == null){
                    JOptionPane.showMessageDialog(this, "Doctor not found!");
                    return;
                }
            }

            Patient p = new Patient(
                    tfFirst.getText().trim(),
                    tfLast.getText().trim(),
                    Integer.parseInt(tfAge.getText().trim()),
                    cbGender.getSelectedItem().toString(),
                    tfPhone.getText().trim(),
                    tfEmail.getText().trim(),
                    tfDiag.getText().trim(),
                    doctorId
            );
            p.setPatientId(id);
            dao.update(p);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Patient updated!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select a Patient!");
            return;
        }
        int id = (Integer) model.getValueAt(r,0);
        dao.delete(id);
        reload();
    }

    private void reload(){
        model.setRowCount(0);
        dao.getAll().forEach(p -> model.addRow(new Object[]{
                p.getPatientId(),
                p.getFirstName(),
                p.getLastName(),
                p.getAge(),
                p.getGender(),
                p.getPhone(),
                p.getEmail(),
                p.getDiagnosis(),
                p.getDoctorId()
        }));
        clearForm();
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        tfFirst.setText(String.valueOf(model.getValueAt(row,1)));
        tfLast.setText(String.valueOf(model.getValueAt(row,2)));
        tfAge.setText(String.valueOf(model.getValueAt(row,3)));
        cbGender.setSelectedItem(String.valueOf(model.getValueAt(row,4)));
        tfPhone.setText(String.valueOf(model.getValueAt(row,5)));
        tfEmail.setText(String.valueOf(model.getValueAt(row,6)));
        tfDiag.setText(String.valueOf(model.getValueAt(row,7)));
        tfDoctorId.setText(String.valueOf(model.getValueAt(row,8)));
    }

    private void findPatient(){
        try{
            String query = tfSearch.getText().trim();
            if(query.isEmpty()){
                JOptionPane.showMessageDialog(this, "Enter patient ID or name to search!");
                return;
            }

            if(query.matches("\\d+")){ // Kerkim me ID
                int id = Integer.parseInt(query);
                Patient p = dao.getById(id);
                if(p == null){
                    JOptionPane.showMessageDialog(this, "No patient found with ID: " + id);
                    return;
                }
                loadPatient(p);
            } else {   // kerkim me emer
                List<Patient> results = dao.searchByName(query);
                if(results.isEmpty()){
                    JOptionPane.showMessageDialog(this, "No patient found with name: " + query);
                    return;
                } else if(results.size() == 1){
                    loadPatient(results.get(0));
                } else {
                    String[] names = results.stream()
                            .map(p -> p.getPatientId() + " - " + p.getFirstName() + " " + p.getLastName())
                            .toArray(String[]::new);
                    String choice = (String) JOptionPane.showInputDialog(
                            this,
                            "Multiple patients found:",
                            "Select Patient",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            names,
                            names[0]
                    );
                    if(choice != null){
                        int id = Integer.parseInt(choice.split(" - ")[0]);
                        Patient selected = results.stream().filter(p -> p.getPatientId() == id).findFirst().orElse(null);
                        if(selected != null) loadPatient(selected);
                    }
                }
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadPatient(Patient p){
        tfFirst.setText(p.getFirstName());
        tfLast.setText(p.getLastName());
        tfAge.setText(String.valueOf(p.getAge()));
        cbGender.setSelectedItem(p.getGender());
        tfPhone.setText(p.getPhone());
        tfEmail.setText(p.getEmail());
        tfDiag.setText(p.getDiagnosis());
        tfDoctorId.setText(String.valueOf(p.getDoctorId()));
    }
}



