package com.medinova.ui.panels;

import com.medinova.dao.*;
import com.medinova.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class PrescriptionsPanel extends JPanel {

    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "AppointmentId", "PatientId", "DoctorId", "Item", "Qty", "Date", "Instructions"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfAppointmentId = new JTextField();
    private final JTextField tfPatientId = new JTextField();
    private final JTextField tfDoctorId = new JTextField();
    private final JTextField tfPatientName = new JTextField();
    private final JTextField tfDoctorName = new JTextField();

    private final JComboBox<Inventory> cbItem = new JComboBox<>();
    private final JTextField tfQty = new JTextField();
    private final JTextArea taInstructions = new JTextArea(3, 20);

    // butoni search
    private final JTextField tfSearch = new JTextField();

    public PrescriptionsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTopForm(), BorderLayout.NORTH);
        styleTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottomActions(), BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        loadInventoryItems();
        reload();
    }

    private JPanel buildTopForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Prescriptions"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        Dimension smallField = new Dimension(100, 26);
        Dimension normalField = new Dimension(160, 26);

        tfAppointmentId.setPreferredSize(smallField);
        tfPatientId.setPreferredSize(smallField);
        tfDoctorId.setPreferredSize(smallField);
        tfPatientName.setPreferredSize(normalField);
        tfDoctorName.setPreferredSize(normalField);
        tfDoctorName.setForeground(Color.WHITE);
        tfPatientName.setEditable(false);
        tfDoctorName.setEditable(false);

        taInstructions.setLineWrap(true);
        taInstructions.setWrapStyleWord(true);

        // mbushet automatikisht pasi vendosim id e takimit
        tfAppointmentId.addCaretListener(e -> loadFromAppointment());

        int y = 0;
        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Appointment ID"), c);
        c.gridx = 1;
        p.add(tfAppointmentId, c);
        c.gridx = 2;
        p.add(new JLabel("Patient ID"), c);
        c.gridx = 3;
        p.add(tfPatientId, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Doctor ID"), c);
        c.gridx = 1;
        p.add(tfDoctorId, c);
        c.gridx = 2;
        p.add(new JLabel("Patient Name"), c);
        c.gridx = 3;
        p.add(tfPatientName, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Doctor Name"), c);
        c.gridx = 1;
        p.add(tfDoctorName, c);
        c.gridx = 2;
        p.add(new JLabel("Item"), c);
        c.gridx = 3;
        p.add(cbItem, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Quantity"), c);
        c.gridx = 1;
        p.add(tfQty, c);
        y++;

        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel("Instructions"), c);
        c.gridx = 1;
        c.gridwidth = 3;
        p.add(new JScrollPane(taInstructions), c);
        c.gridwidth = 1;
        y++;

        JButton add = new JButton("Add");
        add.addActionListener(e -> addPrescription());
        add.setBackground(new Color(52, 152, 219));
        add.setForeground(Color.WHITE);

        JButton upd = new JButton("Update");
        upd.addActionListener(e -> updateSelected());

        JButton del = new JButton("Delete");
        del.addActionListener(e -> deleteSelected());
        del.setBackground(new Color(231, 76, 60));
        del.setForeground(Color.WHITE);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.add(add);
        btns.add(upd);
        btns.add(del);

        c.gridx = 0;
        c.gridy = y;
        c.gridwidth = 4;
        p.add(btns, c);

        return p;
    }

    private JPanel buildBottomActions() {
        JPanel p = new JPanel(new BorderLayout());

        /*
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        left.add(new JLabel("Search:"));
        tfSearch.setPreferredSize(new Dimension(200, 24));
        left.add(tfSearch);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> search());
        left.add(btnSearch);

         */

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> reload());

        right.add(ref);

       // p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private void styleTable() {
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
    }

    private void loadInventoryItems() {
        cbItem.removeAllItems();
        List<Inventory> items = inventoryDAO.getAll();
        for (Inventory i : items) {
            cbItem.addItem(i);
        }
        // i bejme render qe te shfaqet emri dhe sasia
        cbItem.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Inventory inv) {
                    l.setText(inv.getItemName() + " (Stock: " + inv.getQuantity() + ")");
                }
                return l;
            }
        });
    }

    private boolean validateForm() {
        if (tfAppointmentId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Appointment ID required!");
            return false;
        }
        if (tfPatientId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID required!");
            return false;
        }
        if (tfDoctorId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Doctor ID required!");
            return false;
        }
        if (cbItem.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Item required!");
            return false;
        }
        if (tfQty.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quantity required!");
            return false;
        }
        try {
            int q = Integer.parseInt(tfQty.getText().trim());
            if (q <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be > 0");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        tfAppointmentId.setText("");
        tfPatientId.setText("");
        tfDoctorId.setText("");
        tfPatientName.setText("");
        tfDoctorName.setText("");
        tfQty.setText("");
        taInstructions.setText("");
        if (cbItem.getItemCount() > 0) cbItem.setSelectedIndex(0);
    }

    private void addPrescription() {
        if (!validateForm()) return;
        try {
            int appointmentId = Integer.parseInt(tfAppointmentId.getText().trim());
            int patientId = Integer.parseInt(tfPatientId.getText().trim());
            int doctorId = Integer.parseInt(tfDoctorId.getText().trim());
            Inventory inv = (Inventory) cbItem.getSelectedItem();
            int itemId = inv.getItemId();
            int qty = Integer.parseInt(tfQty.getText().trim());

            Prescription p = new Prescription(
                    appointmentId,
                    patientId,
                    doctorId,
                    itemId,
                    qty,
                    taInstructions.getText().trim(),
                    Date.valueOf(LocalDate.now())
            );
            prescriptionDAO.add(p);


            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Prescription added!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a prescription!");
            return;
        }
        if (!validateForm()) return;

        try {
            int id = (int) model.getValueAt(r, 0);
            int appointmentId = Integer.parseInt(tfAppointmentId.getText().trim());
            int patientId = Integer.parseInt(tfPatientId.getText().trim());
            int doctorId = Integer.parseInt(tfDoctorId.getText().trim());
            Inventory inv = (Inventory) cbItem.getSelectedItem();
            int itemId = inv.getItemId();
            int qty = Integer.parseInt(tfQty.getText().trim());

            Prescription p = new Prescription(
                    appointmentId,
                    patientId,
                    doctorId,
                    itemId,
                    qty,
                    taInstructions.getText().trim(),
                    Date.valueOf(LocalDate.now())
            );
            p.setPrescriptionId(id);

            prescriptionDAO.update(p);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Prescription updated!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a prescription!");
            return;
        }
        int id = (int) model.getValueAt(r, 0);
        prescriptionDAO.delete(id);
        reload();
    }

    private void reload() {
        model.setRowCount(0);
        prescriptionDAO.getAll().forEach(p -> {

            Inventory inv = inventoryDAO.getById(p.getItemId());
            String itemName = inv != null ? inv.getItemName() : ("Item #" + p.getItemId());

            model.addRow(new Object[]{
                    p.getPrescriptionId(),
                    p.getAppointmentId(),
                    p.getPatientId(),
                    p.getDoctorId(),
                    itemName,
                    p.getQuantity(),
                    p.getDateIssued(),
                    p.getInstructions()
            });
        });
        clearForm();
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        int prescriptionId = (int) model.getValueAt(row, 0);
        int appointmentId = (int) model.getValueAt(row, 1);
        int patientId = (int) model.getValueAt(row, 2);
        int doctorId = (int) model.getValueAt(row, 3);
        String itemName = String.valueOf(model.getValueAt(row, 4));
        int qty = (int) model.getValueAt(row, 5);
        String instr = String.valueOf(model.getValueAt(row, 7));

        tfAppointmentId.setText(String.valueOf(appointmentId));
        tfPatientId.setText(String.valueOf(patientId));
        tfDoctorId.setText(String.valueOf(doctorId));
        tfQty.setText(String.valueOf(qty));
        taInstructions.setText(instr);


        for (int i = 0; i < cbItem.getItemCount(); i++) {
            Inventory inv = cbItem.getItemAt(i);
            if (inv.getItemName().equals(itemName)) {
                cbItem.setSelectedIndex(i);
                break;
            }
        }


        loadNamesFromIds(patientId, doctorId);
    }

    private void loadFromAppointment() {
        try {
            String s = tfAppointmentId.getText().trim();
            if (s.isEmpty()) return;
            int id = Integer.parseInt(s);
            Appointment a = appointmentDAO.getById(id);
            if (a == null) return;

            tfPatientId.setText(String.valueOf(a.getPatientId()));
            tfDoctorId.setText(String.valueOf(a.getDoctorId()));

            loadNamesFromIds(a.getPatientId(), a.getDoctorId());
        } catch (Exception ignored) {
        }
    }

    private void loadNamesFromIds(int patientId, int doctorId) {
        Patient p = patientDAO.getById(patientId);

        if (p != null) {
            tfPatientName.setText(p.getFirstName() + " " + p.getLastName());
        } else {
            tfPatientName.setText("");
        }

        Doctor d = doctorDAO.getById(doctorId);

        if (d != null) {
            tfDoctorName.setText(d.getFirstName() + " " + d.getLastName());
        } else {
            tfDoctorName.setText("");
        }


    }

    private void search() {
        String q = tfSearch.getText().trim();
        if (q.isEmpty()) {
            reload();
            return;
        }

        model.setRowCount(0);
        for (Prescription p : prescriptionDAO.getAll()) {
            if (matchesSearch(p, q)) {
                Inventory inv = inventoryDAO.getById(p.getItemId());
                String itemName = inv != null ? inv.getItemName() : ("Item #" + p.getItemId());
                model.addRow(new Object[]{
                        p.getPrescriptionId(),
                        p.getAppointmentId(),
                        p.getPatientId(),
                        p.getDoctorId(),
                        itemName,
                        p.getQuantity(),
                        p.getDateIssued(),
                        p.getInstructions()
                });
            }
        }
    }

    private boolean matchesSearch(Prescription p, String q) {
        q = q.toLowerCase();
        if (String.valueOf(p.getPrescriptionId()).contains(q)) return true;
        if (String.valueOf(p.getAppointmentId()).contains(q)) return true;
        if (String.valueOf(p.getPatientId()).contains(q)) return true;
        if (String.valueOf(p.getDoctorId()).contains(q)) return true;
        if (p.getInstructions() != null && p.getInstructions().toLowerCase().contains(q)) return true;
        return false;
    }
}
