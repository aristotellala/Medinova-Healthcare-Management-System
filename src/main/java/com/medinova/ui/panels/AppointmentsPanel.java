

/*
package com.medinova.ui.panels;

import com.medinova.dao.AppointmentDAO;
import com.medinova.model.Appointment;
import com.medinova.model.enums.AppointmentStatus;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;

public class AppointmentsPanel extends JPanel {

    private final AppointmentDAO dao = new AppointmentDAO();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "PatientId", "DoctorId", "Date", "Time", "Reason", "Status"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfPid = new JTextField();
    private final JTextField tfDid = new JTextField();
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> cbTime = new JComboBox<>(timeOptions());
    private final JTextField tfReason = new JTextField();
    private final JComboBox<String> cbStatus = new JComboBox<>(new String[]{
            AppointmentStatus.Scheduled.name(),
            AppointmentStatus.Completed.name(),
            AppointmentStatus.Cancelled.name()
    });

    public AppointmentsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildForm(), BorderLayout.NORTH);
        styleTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(buildBottomActions(), BorderLayout.SOUTH);
        reload();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Appointments"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfPid.setPreferredSize(new Dimension(110, 26));
        tfDid.setPreferredSize(new Dimension(110, 26));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        JTextField dateEditor = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateEditor.setBackground(new Color(45, 45, 45));
        dateEditor.setForeground(Color.WHITE);
        dateEditor.setCaretColor(Color.WHITE);


        cbTime.setPreferredSize(new Dimension(100, 26));
        tfReason.setPreferredSize(new Dimension(200, 26));

        int y = 0;
        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Patient ID"), c);
        c.gridx = 1;
        p.add(tfPid, c);
        c.gridx = 2;
        p.add(new JLabel("Doctor ID"), c);
        c.gridx = 3;
        p.add(tfDid, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Date"), c);
        c.gridx = 1;
        p.add(dateChooser, c);
        c.gridx = 2;
        p.add(new JLabel("Time"), c);
        c.gridx = 3;
        p.add(cbTime, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Reason"), c);
        c.gridx = 1;
        c.gridwidth = 3;
        p.add(tfReason, c);
        c.gridwidth = 1;
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Status"), c);
        c.gridx = 1;
        p.add(cbStatus, c);
        JButton add = new JButton("Add");
        add.addActionListener(e -> addAppointment());
        add.setBackground(new Color(52, 152, 219)); //background of add btn

        c.gridx = 2;
        c.gridwidth = 2;
        p.add(add, c);
        return p;
    }

    private JPanel buildBottomActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> reload());
        JButton upd = new JButton("Update");
        upd.addActionListener(e -> updateSelected());
        JButton del = new JButton("Delete");
        del.addActionListener(e -> deleteSelected());
        del.setBackground(new Color(231, 76, 60)); //background of delete btn

        p.add(ref);
        p.add(upd);
        p.add(del);
        return p;
    }

    private void styleTable() {
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
    }

    private String[] timeOptions() {
        java.util.List<String> list = new java.util.ArrayList<>();
        for (int h = 6; h <= 22; h++) {
            list.add(String.format("%02d:00", h));
            list.add(String.format("%02d:30", h));
        }
        return list.toArray(new String[0]);
    }

    private boolean validateForm() {
        if (tfPid.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID required!");
            return false;
        }
        if (tfDid.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Doctor ID required!");
            return false;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Date required!");
            return false;
        }
        if (cbTime.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Time required!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        tfPid.setText("");
        tfDid.setText("");
        dateChooser.setDate(null);
        cbTime.setSelectedIndex(0);
        tfReason.setText("");
        cbStatus.setSelectedIndex(0);
    }

    private void addAppointment() {
        if (!validateForm()) return;
        try {
            Appointment a = new Appointment(
                    Integer.parseInt(tfPid.getText().trim()),
                    Integer.parseInt(tfDid.getText().trim()),
                    new Date(dateChooser.getDate().getTime()),
                    Time.valueOf(cbTime.getSelectedItem().toString()),
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
            );
            dao.add(a);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment added!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select Appointment!");
            return;
        }
        try {
            int id = (int) model.getValueAt(r, 0);
            Appointment a = new Appointment(
                    Integer.parseInt(tfPid.getText().trim()),
                    Integer.parseInt(tfDid.getText().trim()),
                    new Date(dateChooser.getDate().getTime()),
                    Time.valueOf(cbTime.getSelectedItem().toString()),
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
            );
            a.setAppointmentId(id);
            dao.update(a);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment updated!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select Appointment!");
            return;
        }
        int id = (Integer) model.getValueAt(r, 0);
        dao.delete(id);
        reload();
    }

    private void reload() {
        model.setRowCount(0);
        dao.getAll().forEach(a -> model.addRow(new Object[]{
                a.getAppointmentId(),
                a.getPatientId(),
                a.getDoctorId(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getReason(),
                a.getStatus()
        }));
        clearForm();
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        tfPid.setText(String.valueOf(model.getValueAt(row, 1)));
        tfDid.setText(String.valueOf(model.getValueAt(row, 2)));
        dateChooser.setDate(Date.valueOf(String.valueOf(model.getValueAt(row, 3))));
        cbTime.setSelectedItem(String.valueOf(model.getValueAt(row, 4)));
        tfReason.setText(String.valueOf(model.getValueAt(row, 5)));
        cbStatus.setSelectedItem(String.valueOf(model.getValueAt(row, 6)));
    }
}


 */

/*
package com.medinova.ui.panels;

import com.medinova.dao.AppointmentDAO;
import com.medinova.dao.PatientDAO;
import com.medinova.model.Appointment;
import com.medinova.model.Patient;
import com.medinova.model.enums.AppointmentStatus;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class AppointmentsPanel extends JPanel {

    private final AppointmentDAO dao = new AppointmentDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "PatientId", "DoctorId", "Date", "Time", "Reason", "Status"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfPid = new JTextField();
    private final JTextField tfDid = new JTextField();
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> cbTime = new JComboBox<>(timeOptions());
    private final JTextField tfReason = new JTextField();
    private final JComboBox<String> cbStatus = new JComboBox<>(new String[]{
            AppointmentStatus.Scheduled.name(),
            AppointmentStatus.Completed.name(),
            AppointmentStatus.Cancelled.name()
    });

    public AppointmentsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildForm(), BorderLayout.NORTH);
        styleTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(buildBottomActions(), BorderLayout.SOUTH);
        reload();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Appointments"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfPid.setPreferredSize(new Dimension(110, 26));
        tfDid.setPreferredSize(new Dimension(110, 26));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        cbTime.setPreferredSize(new Dimension(120, 26));
        tfReason.setPreferredSize(new Dimension(200, 26));

        // 🔹 Rregullim për dark mode
        JTextField dateEditor = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateEditor.setBackground(new Color(45, 45, 45));
        dateEditor.setForeground(Color.WHITE);
        dateEditor.setCaretColor(Color.WHITE);
        dateChooser.getDateEditor().addPropertyChangeListener("date", e -> {
            SwingUtilities.invokeLater(() -> {
                dateEditor.setForeground(Color.WHITE);
                dateEditor.setBackground(new Color(45, 45, 45));
            });
        });

        // 🔹 Ngjyra e cbTime për dark mode
        cbTime.setBackground(new Color(45, 45, 45));
        cbTime.setForeground(Color.WHITE);

        // 🔹 Kur shkruhet Patient ID, plotësohet automatikisht Doctor ID
        tfPid.addCaretListener(e -> autoFillDoctorId());

        int y = 0;
        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Patient ID"), c);
        c.gridx = 1;
        p.add(tfPid, c);
        c.gridx = 2;
        p.add(new JLabel("Doctor ID"), c);
        c.gridx = 3;
        p.add(tfDid, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Date"), c);
        c.gridx = 1;
        p.add(dateChooser, c);
        c.gridx = 2;
        p.add(new JLabel("Time"), c);
        c.gridx = 3;
        p.add(cbTime, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Reason"), c);
        c.gridx = 1;
        c.gridwidth = 3;
        p.add(tfReason, c);
        c.gridwidth = 1;
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Status"), c);
        c.gridx = 1;
        p.add(cbStatus, c);
        JButton add = new JButton("Add");
        add.addActionListener(e -> addAppointment());
        add.setBackground(new Color(52, 152, 219));
        add.setForeground(Color.WHITE);

        c.gridx = 2;
        c.gridwidth = 2;
        p.add(add, c);
        return p;
    }

    private JPanel buildBottomActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> reload());
        JButton upd = new JButton("Update");
        upd.addActionListener(e -> updateSelected());
        JButton del = new JButton("Delete");
        del.addActionListener(e -> deleteSelected());
        del.setBackground(new Color(231, 76, 60));
        del.setForeground(Color.WHITE);

        p.add(ref);
        p.add(upd);
        p.add(del);
        return p;
    }

    private void styleTable() {
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
    }

    private String[] timeOptions() {
        java.util.List<String> list = new java.util.ArrayList<>();
        for (int h = 6; h <= 22; h++) {
            list.add(String.format("%02d:00", h));
            list.add(String.format("%02d:30", h));
        }
        return list.toArray(new String[0]);
    }

    private boolean validateForm() {
        if (tfPid.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID required!");
            return false;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Date required!");
            return false;
        }
        if (cbTime.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Time required!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        tfPid.setText("");
        tfDid.setText("");
        dateChooser.setDate(null);
        cbTime.setSelectedIndex(0);
        tfReason.setText("");
        cbStatus.setSelectedIndex(0);
    }

    private void addAppointment() {
        if (!validateForm()) return;
        try {
            int patientId = Integer.parseInt(tfPid.getText().trim());
            Patient patient = patientDAO.getById(patientId);

            if (patient == null) {
                JOptionPane.showMessageDialog(this, "Patient not found!");
                return;
            }

            int doctorId = patient.getDoctorId();

            Appointment a = new Appointment(
                    patientId,
                    doctorId,
                    new Date(dateChooser.getDate().getTime()),
                    Time.valueOf(cbTime.getSelectedItem().toString()),
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
            );
            dao.add(a);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment added!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select Appointment!");
            return;
        }
        try {
            int id = (int) model.getValueAt(r, 0);
            Appointment a = new Appointment(
                    Integer.parseInt(tfPid.getText().trim()),
                    Integer.parseInt(tfDid.getText().trim()),
                    new Date(dateChooser.getDate().getTime()),
                    Time.valueOf(cbTime.getSelectedItem().toString()),
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
            );
            a.setAppointmentId(id);
            dao.update(a);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment updated!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select Appointment!");
            return;
        }
        int id = (Integer) model.getValueAt(r, 0);
        dao.delete(id);
        reload();
    }

    private void reload() {
        model.setRowCount(0);
        dao.getAll().forEach(a -> model.addRow(new Object[]{
                a.getAppointmentId(),
                a.getPatientId(),
                a.getDoctorId(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getReason(),
                a.getStatus()
        }));
        clearForm();
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        tfPid.setText(String.valueOf(model.getValueAt(row, 1)));
        tfDid.setText(String.valueOf(model.getValueAt(row, 2)));
        dateChooser.setDate(Date.valueOf(String.valueOf(model.getValueAt(row, 3))));
        cbTime.setSelectedItem(String.valueOf(model.getValueAt(row, 4)));
        tfReason.setText(String.valueOf(model.getValueAt(row, 5)));
        cbStatus.setSelectedItem(String.valueOf(model.getValueAt(row, 6)));
    }

    private void autoFillDoctorId() {
        try {
            if (tfPid.getText().trim().isEmpty()) return;
            int pid = Integer.parseInt(tfPid.getText().trim());
            Patient p = patientDAO.getById(pid);
            if (p != null) {
                tfDid.setText(String.valueOf(p.getDoctorId()));
            } else {
                tfDid.setText("");
            }
        } catch (Exception ignored) {}
    }
}

 */

/*
package com.medinova.ui.panels;

import com.medinova.dao.AppointmentDAO;
import com.medinova.dao.PatientDAO;
import com.medinova.model.Appointment;
import com.medinova.model.Patient;
import com.medinova.model.enums.AppointmentStatus;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;

public class AppointmentsPanel extends JPanel {

    private final AppointmentDAO dao = new AppointmentDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","PatientId","DoctorId","Date","Time","Reason","Status"}, 0){
        public boolean isCellEditable(int r, int c){ return false; }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfPid = new JTextField();
    private final JTextField tfDid = new JTextField();
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> cbTime = new JComboBox<>(timeOptions());
    private final JTextField tfReason = new JTextField();
    private final JComboBox<String> cbStatus = new JComboBox<>(new String[]{
            AppointmentStatus.Scheduled.name(),
            AppointmentStatus.Completed.name(),
            AppointmentStatus.Cancelled.name()
    });

    public AppointmentsPanel(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(buildForm(), BorderLayout.NORTH);
        styleTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        add(buildBottomActions(), BorderLayout.SOUTH);
        reload();
    }

    private JPanel buildForm(){
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Appointments"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,6,5,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfPid.setPreferredSize(new Dimension(110,26));
        tfDid.setPreferredSize(new Dimension(110,26));
        tfDid.setEditable(false); // nuk lejon ndryshim doktori

        // Date chooser
        dateChooser.setDateFormatString("yyyy-MM-dd");
        JTextField dateEditor = (JTextField) dateChooser.getDateEditor().getUiComponent();
        // ngjyre e bardhe te data
        Runnable applyDateColors = () -> {
            dateEditor.setBackground(new Color(45,45,45));
            dateEditor.setForeground(Color.WHITE);
            dateEditor.setCaretColor(Color.WHITE);
        };
        applyDateColors.run();
        dateChooser.getDateEditor().addPropertyChangeListener("date", e -> SwingUtilities.invokeLater(applyDateColors));

        // ngjyre e bardhe per oren
        cbTime.setRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setOpaque(true);
                l.setBackground(isSelected ? new Color(60,60,60) : new Color(45,45,45));
                l.setForeground(Color.WHITE);
                return l;
            }
        });
        cbTime.setBackground(new Color(45,45,45));
        cbTime.setForeground(Color.WHITE);

        tfReason.setPreferredSize(new Dimension(200,26));

        // vendos automatikisht id e doktorit
        tfPid.addCaretListener(e -> autoFillDoctorId());

        int y=0;
        c.gridx=0; c.gridy=y; p.add(new JLabel("Patient ID"), c);
        c.gridx=1; p.add(tfPid, c);
        c.gridx=2; p.add(new JLabel("Doctor ID"), c);
        c.gridx=3; p.add(tfDid, c);
        y++;

        c.gridx=0; c.gridy=y; p.add(new JLabel("Date"), c);
        c.gridx=1; p.add(dateChooser, c);
        c.gridx=2; p.add(new JLabel("Time"), c);
        c.gridx=3; p.add(cbTime, c);
        y++;

        c.gridx=0; c.gridy=y; p.add(new JLabel("Reason"), c);
        c.gridx=1; c.gridwidth=3; p.add(tfReason, c);
        c.gridwidth=1;
        y++;

        c.gridx=0; c.gridy=y; p.add(new JLabel("Status"), c);
        c.gridx=1; p.add(cbStatus, c);
        JButton add = new JButton("Add");
        add.addActionListener(e -> addAppointment());
        add.setBackground(new Color(52,152,219));
        add.setForeground(Color.WHITE);

        c.gridx=2; c.gridwidth=2;
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

    private String[] timeOptions(){
        java.util.List<String> list = new java.util.ArrayList<>();
        for(int h=6; h<=22; h++){
            list.add(String.format("%02d:00", h));
            list.add(String.format("%02d:30", h));
        }
        return list.toArray(new String[0]);
    }

    private boolean validateForm(){
        if(tfPid.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Patient ID required!");
            return false;
        }
        if(dateChooser.getDate() == null){
            JOptionPane.showMessageDialog(this, "Date required!");
            return false;
        }
        if(cbTime.getSelectedItem() == null){
            JOptionPane.showMessageDialog(this, "Time required!");
            return false;
        }
        return true;
    }

    private void clearForm(){
        tfPid.setText("");
        tfDid.setText("");
        dateChooser.setDate(null);
        cbTime.setSelectedIndex(0);
        tfReason.setText("");
        cbStatus.setSelectedIndex(0);
    }

    private String normalizeTime(Object value){
        if(value == null) return null;
        String t = String.valueOf(value).trim();
        if(t.length() == 5) t = t + ":00";
        return t;
    }

    private void addAppointment(){
        if(!validateForm()) return;
        try {
            int pid = Integer.parseInt(tfPid.getText().trim());
            Patient p = patientDAO.getById(pid);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Patient not found!");
                return;
            }
            int did = p.getDoctorId();
            tfDid.setText(String.valueOf(did)); // shfaqet

            String t = normalizeTime(cbTime.getSelectedItem());
            if(t == null){
                JOptionPane.showMessageDialog(this, "Invalid time!");
                return;
            }

            Appointment a = new Appointment(
                    pid,
                    did,
                    new Date(dateChooser.getDate().getTime()),
                    Time.valueOf(t),
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
            );
            dao.add(a);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment added!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select Appointment!");
            return;
        }
        if(dateChooser.getDate() == null){
            JOptionPane.showMessageDialog(this, "Please select a date!");
            return;
        }
        try {
            int id = (int) model.getValueAt(r,0);

            String t = normalizeTime(cbTime.getSelectedItem());
            if(t == null){
                JOptionPane.showMessageDialog(this, "Invalid time!");
                return;
            }

            int pid = Integer.parseInt(tfPid.getText().trim());
            int did;
            if(tfDid.getText().trim().isEmpty()){
                Patient p = patientDAO.getById(pid);
                if(p == null){
                    JOptionPane.showMessageDialog(this, "Patient not found!");
                    return;
                }
                did = p.getDoctorId();
            } else {
                did = Integer.parseInt(tfDid.getText().trim());
            }

            Appointment a = new Appointment(
                    pid,
                    did,
                    new Date(dateChooser.getDate().getTime()),
                    Time.valueOf(t),
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
            );
            a.setAppointmentId(id);

            dao.update(a);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment updated!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select Appointment!");
            return;
        }
        int id = (Integer) model.getValueAt(r,0);
        dao.delete(id);
        reload();
    }

    private void reload(){
        model.setRowCount(0);
        dao.getAll().forEach(a -> model.addRow(new Object[]{
                a.getAppointmentId(),
                a.getPatientId(),
                a.getDoctorId(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getReason(),
                a.getStatus()
        }));
        clearForm();
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        tfPid.setText(String.valueOf(model.getValueAt(row,1)));
        tfDid.setText(String.valueOf(model.getValueAt(row,2)));
        dateChooser.setDate(Date.valueOf(String.valueOf(model.getValueAt(row,3))));

        // Kthejme ne HH:MM oren pasi ne db eshte HH:MM:ss
        String time = String.valueOf(model.getValueAt(row,4));
        if(time != null && time.length() >= 5){
            cbTime.setSelectedItem(time.substring(0,5));
        }

        tfReason.setText(String.valueOf(model.getValueAt(row,5)));
        cbStatus.setSelectedItem(String.valueOf(model.getValueAt(row,6)));
    }

    private void autoFillDoctorId(){
        try{
            String s = tfPid.getText().trim();
            if(s.isEmpty()) { tfDid.setText(""); return; }
            int pid = Integer.parseInt(s);
            Patient p = patientDAO.getById(pid);
            tfDid.setText(p != null ? String.valueOf(p.getDoctorId()) : "");
        }catch(Exception ignored){}
    }
}


 */

package com.medinova.ui.panels;

import com.medinova.dao.AppointmentDAO;
import com.medinova.dao.PatientDAO;
import com.medinova.model.Appointment;
import com.medinova.model.Patient;
import com.medinova.model.enums.AppointmentStatus;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentsPanel extends JPanel {

    // DAOs
    private final AppointmentDAO dao = new AppointmentDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    // --- MODELS & TABLES (Active / History) ---
    private final DefaultTableModel modelActive = new DefaultTableModel(
            new String[]{"ID", "PatientId", "DoctorId", "Date", "Time", "Reason", "Status"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final DefaultTableModel modelHistory = new DefaultTableModel(
            new String[]{"ID", "PatientId", "DoctorId", "Date", "Time", "Reason", "Status"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tableActive = new JTable(modelActive);
    private final JTable tableHistory = new JTable(modelHistory);
    private final JTabbedPane tabs = new JTabbedPane();

    // --- FORM FIELDS ---
    private final JTextField tfPid = new JTextField();
    private final JTextField tfDid = new JTextField();
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> cbTime = new JComboBox<>(timeOptions());
    private final JTextField tfReason = new JTextField();
    private final JComboBox<String> cbStatus = new JComboBox<>(new String[]{
            AppointmentStatus.Scheduled.name(),
            AppointmentStatus.Completed.name(),
            AppointmentStatus.Cancelled.name()
    });

    // --- SEARCH ---
    private final JTextField tfSearch = new JTextField();
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnClear = new JButton("Clear");

    // cache e plote e takimeve (përdoret për search/filter)
    private List<Appointment> allAppointments = new ArrayList<>();

    public AppointmentsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildForm(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottomActions(), BorderLayout.SOUTH);

        styleTable(tableActive);
        styleTable(tableHistory);

        // Selection listeners (që të mbushin formën nga cilido tab)
        tableActive.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabs.getSelectedIndex() == 0) fillFormFrom(tableActive, modelActive);
        });
        tableHistory.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabs.getSelectedIndex() == 1) fillFormFrom(tableHistory, modelHistory);
        });

        // Auto doctor id kur shkruhet PatientId
        tfPid.addCaretListener(e -> autoFillDoctorId());

        // Ngjyrë e bardhë për tekstin e datës në dark mode
        setupDateEditorColors();

        // Renderer dark për orët
        setupTimeComboDark();

        // Load fillestar
        reloadAll();
    }

    // ------------------------ UI BUILDERS ------------------------

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Appointments"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfPid.setPreferredSize(new Dimension(110, 26));
        tfDid.setPreferredSize(new Dimension(110, 26));
        tfDid.setEditable(false); // doctor id vendoset automatikisht nga pacienti

        dateChooser.setDateFormatString("yyyy-MM-dd");
        cbTime.setPreferredSize(new Dimension(110, 26));
        tfReason.setPreferredSize(new Dimension(220, 26));

        int y = 0;
        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Patient ID"), c);
        c.gridx = 1;
        p.add(tfPid, c);
        c.gridx = 2;
        p.add(new JLabel("Doctor ID"), c);
        c.gridx = 3;
        p.add(tfDid, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Date"), c);
        c.gridx = 1;
        p.add(dateChooser, c);
        c.gridx = 2;
        p.add(new JLabel("Time"), c);
        c.gridx = 3;
        p.add(cbTime, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Reason"), c);
        c.gridx = 1;
        c.gridwidth = 3;
        p.add(tfReason, c);
        c.gridwidth = 1;
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Status"), c);
        c.gridx = 1;
        p.add(cbStatus, c);

        JButton add = new JButton("Add");
        add.setBackground(new Color(52, 152, 219));
        add.setForeground(Color.WHITE);
        add.addActionListener(e -> addAppointment());

        c.gridx = 2;
        c.gridwidth = 2;
        p.add(add, c);

        // Row tjetër për Search
        y++;
        c.gridx = 0;
        c.gridy = y;
        c.gridwidth = 1;
        p.add(new JLabel("Search"), c);
        c.gridx = 1;
        c.gridwidth = 2;
        p.add(tfSearch, c);
        c.gridwidth = 1;
        JPanel searchBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnSearch.addActionListener(e -> applySearch());
        btnClear.addActionListener(e -> {
            tfSearch.setText("");
            reloadAll();
        });
        searchBtns.add(btnSearch);
        searchBtns.add(btnClear);
        c.gridx = 3;
        p.add(searchBtns, c);

        return p;
    }

    private JComponent buildCenter() {
        tabs.addTab("Active", new JScrollPane(tableActive));
        tabs.addTab("History", new JScrollPane(tableHistory));
        return tabs;
    }

    private JPanel buildBottomActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> reloadAll());
        JButton upd = new JButton("Update");
        upd.addActionListener(e -> updateSelected());
        JButton del = new JButton("Delete");
        del.setBackground(new Color(231, 76, 60));
        del.setForeground(Color.WHITE);
        del.addActionListener(e -> deleteSelected());

        p.add(ref);
        p.add(upd);
        p.add(del);
        return p;
    }

    // ------------------------ STYLING ------------------------

    private void styleTable(JTable t) {
        t.setRowHeight(24);
        t.setSelectionBackground(new Color(52, 152, 219));
        t.setSelectionForeground(Color.WHITE);
        t.setAutoCreateRowSorter(true);
    }

    private void setupDateEditorColors() {
        JTextField dateEditor = (JTextField) dateChooser.getDateEditor().getUiComponent();
        Runnable applyDateColors = () -> {
            dateEditor.setBackground(new Color(45, 45, 45));
            dateEditor.setForeground(Color.WHITE);
            dateEditor.setCaretColor(Color.WHITE);
        };
        applyDateColors.run();
        PropertyChangeListener l = evt -> SwingUtilities.invokeLater(applyDateColors);
        dateChooser.getDateEditor().addPropertyChangeListener("date", l);
        dateChooser.addPropertyChangeListener("foreground", l);
    }

    private void setupTimeComboDark() {
        cbTime.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setOpaque(true);
                l.setBackground(isSelected ? new Color(60, 60, 60) : new Color(45, 45, 45));
                l.setForeground(Color.WHITE);
                return l;
            }
        });
        cbTime.setBackground(new Color(45, 45, 45));
        cbTime.setForeground(Color.WHITE);
    }

    // ------------------------ HELPERS ------------------------

    private String[] timeOptions() {
        java.util.List<String> list = new java.util.ArrayList<>();
        for (int h = 6; h <= 22; h++) {
            list.add(String.format("%02d:00", h));
            list.add(String.format("%02d:30", h));
        }
        return list.toArray(new String[0]);
    }

    private boolean validateForm() {
        if (tfPid.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID required!");
            return false;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Date required!");
            return false;
        }
        if (cbTime.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Time required!");
            return false;
        }
        return true;
    }

    private String normalizeTime(Object value) {
        if (value == null) return null;
        String t = String.valueOf(value).trim();
        if (t.length() == 5) t = t + ":00";
        return t;
    }

    private void clearForm() {
        tfPid.setText("");
        tfDid.setText("");
        dateChooser.setDate(null);
        cbTime.setSelectedIndex(0);
        tfReason.setText("");
        cbStatus.setSelectedIndex(0);
    }

    private void fillFormFrom(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int modelRow = table.convertRowIndexToModel(row);

        tfPid.setText(String.valueOf(model.getValueAt(modelRow, 1)));
        tfDid.setText(String.valueOf(model.getValueAt(modelRow, 2)));
        dateChooser.setDate(Date.valueOf(String.valueOf(model.getValueAt(modelRow, 3))));

        // nga HH:MM:SS → HH:MM
        String time = String.valueOf(model.getValueAt(modelRow, 4));
        if (time != null && time.length() >= 5) {
            cbTime.setSelectedItem(time.substring(0, 5));
        }

        tfReason.setText(String.valueOf(model.getValueAt(modelRow, 5)));
        cbStatus.setSelectedItem(String.valueOf(model.getValueAt(modelRow, 6)));
    }

    private void autoFillDoctorId() {
        try {
            String s = tfPid.getText().trim();
            if (s.isEmpty()) {
                tfDid.setText("");
                return;
            }
            int pid = Integer.parseInt(s);
            Patient p = patientDAO.getById(pid);
            tfDid.setText(p != null ? String.valueOf(p.getDoctorId()) : "");
        } catch (Exception ignored) {
        }
    }

    // ------------------------ CRUD ------------------------

    private void addAppointment() {
        if (!validateForm()) return;
        try {
            int pid = Integer.parseInt(tfPid.getText().trim());
            Patient p = patientDAO.getById(pid);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Patient not found!");
                return;
            }
            int did = p.getDoctorId(); // vendosim automatikisht
            tfDid.setText(String.valueOf(did));

            String t = normalizeTime(cbTime.getSelectedItem());
            if (t == null) {
                JOptionPane.showMessageDialog(this, "Invalid time!");
                return;
            }

            Appointment a = new Appointment(
                    pid,
                    did,
                    new Date(dateChooser.getDate().getTime()),
                    Time.valueOf(t),
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
            );

            dao.add(a);
            reloadAll();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment added!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected() {
        JTable currentTable = (tabs.getSelectedIndex() == 0) ? tableActive : tableHistory;
        DefaultTableModel currentModel = (tabs.getSelectedIndex() == 0) ? modelActive : modelHistory;

        int r = currentTable.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select Appointment!");
            return;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a date!");
            return;
        }
        try {
            int modelRow = currentTable.convertRowIndexToModel(r);
            int id = (int) currentModel.getValueAt(modelRow, 0);

            String t = normalizeTime(cbTime.getSelectedItem());
            if (t == null) {
                JOptionPane.showMessageDialog(this, "Invalid time!");
                return;
            }

            int pid = Integer.parseInt(tfPid.getText().trim());
            int did;
            if (tfDid.getText().trim().isEmpty()) {
                Patient p = patientDAO.getById(pid);
                if (p == null) {
                    JOptionPane.showMessageDialog(this, "Patient not found!");
                    return;
                }
                did = p.getDoctorId();
            } else {
                did = Integer.parseInt(tfDid.getText().trim());
            }

            Appointment a = new Appointment(
                    pid,
                    did,
                    new Date(dateChooser.getDate().getTime()),
                    Time.valueOf(t),
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
            );
            a.setAppointmentId(id);

            dao.update(a);
            reloadAll();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment updated!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        JTable currentTable = (tabs.getSelectedIndex() == 0) ? tableActive : tableHistory;
        DefaultTableModel currentModel = (tabs.getSelectedIndex() == 0) ? modelActive : modelHistory;

        int r = currentTable.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select Appointment!");
            return;
        }
        int modelRow = currentTable.convertRowIndexToModel(r);
        int id = (Integer) currentModel.getValueAt(modelRow, 0);
        dao.delete(id);
        reloadAll();
    }

    // ------------------------ RELOAD + SEARCH ------------------------

    private void reloadAll() {
        // lexon nga DB dhe mban cache
        allAppointments = dao.getAll();

        // aplikon filterin e search nëse ka diçka të shkruar
        String q = tfSearch.getText().trim().toLowerCase();
        List<Appointment> list = allAppointments;
        if (!q.isEmpty()) {
            list = allAppointments.stream()
                    .filter(a -> matches(a, q))
                    .collect(Collectors.toList());
        }

        // Ndarje aktive/histori
        List<Appointment> act = list.stream()
                .filter(a -> AppointmentStatus.Scheduled.name().equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
        List<Appointment> hist = list.stream()
                .filter(a -> !AppointmentStatus.Scheduled.name().equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());

        // mbush tabelat
        fillModel(modelActive, act);
        fillModel(modelHistory, hist);
    }

    private void applySearch() {
        reloadAll(); // sepse reloadAll zbaton q tekstin e tfSearch
    }

    private boolean matches(Appointment a, String q) {
        // Kërkim global në të gjitha fushat kryesore
        return (String.valueOf(a.getAppointmentId()).toLowerCase().contains(q)) ||
                (String.valueOf(a.getPatientId()).toLowerCase().contains(q)) ||
                (String.valueOf(a.getDoctorId()).toLowerCase().contains(q)) ||
                (a.getAppointmentDate() != null && a.getAppointmentDate().toString().toLowerCase().contains(q)) ||
                (a.getAppointmentTime() != null && a.getAppointmentTime().toString().toLowerCase().contains(q)) ||
                (a.getReason() != null && a.getReason().toLowerCase().contains(q)) ||
                (a.getStatus() != null && a.getStatus().toLowerCase().contains(q));
    }

    private void fillModel(DefaultTableModel model, List<Appointment> data) {
        model.setRowCount(0);
        for (Appointment a : data) {
            model.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getDoctorId(),
                    a.getAppointmentDate(),
                    a.getAppointmentTime(),
                    a.getReason(),
                    a.getStatus()
            });
        }
        clearForm(); // pas çdo reload e pastrojmë formën që të shmangim gjendje të vjetër
    }
}
