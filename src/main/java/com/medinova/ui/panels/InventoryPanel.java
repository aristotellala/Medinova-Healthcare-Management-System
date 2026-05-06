
/*
package com.medinova.ui.panels;

import com.medinova.dao.InventoryDAO;
import com.medinova.model.Inventory;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;

public class InventoryPanel extends JPanel {

    private final InventoryDAO dao = new InventoryDAO();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","Item Name","Category","Qty","Min Qty","Unit Price","Expiry"}, 0){
        public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfName = new JTextField();
    private final JTextField tfQty = new JTextField();
    private final JTextField tfMin = new JTextField();
    private final JTextField tfPrice = new JTextField();
    //private final JTextField tfExp = new JTextField();
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> cbCat = new JComboBox<>(new String[]{"Medicine","Equipment"});

    public InventoryPanel(){
        setLayout(new BorderLayout(15,15));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(buildForm(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        add(buildBottomActions(), BorderLayout.SOUTH);
        reload();
    }

    private JPanel buildForm(){
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Inventory"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,8,6,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        Dimension fieldSize = new Dimension(180,26);
        tfName.setPreferredSize(fieldSize);
        tfQty.setPreferredSize(new Dimension(100,26));
        tfMin.setPreferredSize(new Dimension(100,26));
        tfPrice.setPreferredSize(new Dimension(120,26));
       // tfExp.setPreferredSize(new Dimension(140,26));

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

        int y=0;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Item Name"), c);
        c.gridx=1; p.add(tfName, c);
        c.gridx=2; p.add(new JLabel("Category"), c);
        c.gridx=3; p.add(cbCat, c);
        y++;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Quantity"), c);
        c.gridx=1; p.add(tfQty, c);
        c.gridx=2; p.add(new JLabel("Min Qty"), c);
        c.gridx=3; p.add(tfMin, c);
        y++;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Unit Price"), c);
        c.gridx=1; p.add(tfPrice, c);
        c.gridx=2; p.add(new JLabel("Expiry (YYYY-MM-DD)"), c);
        c.gridx=3; p.add(dateChooser, c);
        y++;

        JButton add = new JButton("Add");
        add.addActionListener(e -> addItem());
        add.setBackground(new Color(52,152,219)); //background of add btn

        //JButton upd = new JButton("Update");
       // upd.addActionListener(e -> updateSelected());
       // JButton del = new JButton("Delete");
       // del.addActionListener(e -> deleteSelected());

       // JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
      //  btns.add(add); btns.add(upd); btns.add(del);

        c.gridx=3;c.gridy=y;c.gridwidth=4;
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

    private JPanel buildTable(){
        JPanel p = new JPanel(new BorderLayout(10,10));
        table.setRowHeight(24);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private boolean validateForm(){
        if(tfName.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Item name required!");
            return false;
        }
        if(tfQty.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Quantity required!");
            return false;
        }
        if(tfMin.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Min qty required!");
            return false;
        }
        if(tfPrice.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Price required!");
            return false;
        }
        return true;
    }

    private void clearForm(){
        tfName.setText("");
        tfQty.setText("");
        tfMin.setText("");
        tfPrice.setText("");
        dateChooser.setDate(null);
        cbCat.setSelectedIndex(0);
    }

    private void addItem(){
        if(!validateForm()) return;
        try {
            //Date exp = tfExp.getText().trim().isEmpty() ? null : Date.valueOf(tfExp.getText().trim());
            Inventory i = new Inventory(
                    tfName.getText().trim(),
                    cbCat.getSelectedItem().toString(),
                    Integer.parseInt(tfQty.getText().trim()),
                    Integer.parseInt(tfMin.getText().trim()),
                    Double.parseDouble(tfPrice.getText().trim()),
                    new Date(dateChooser.getDate().getTime())
            );
            dao.add(i);
            reload();
            clearForm();
            JOptionPane.showMessageDialog(this, "Item added!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select an Item!");
            return;
        }
        try {
            int id = (Integer) model.getValueAt(r,0);
            //Date exp = tfExp.getText().trim().isEmpty() ? null : Date.valueOf(tfExp.getText().trim());
            Inventory i = new Inventory(
                    tfName.getText().trim(),
                    cbCat.getSelectedItem().toString(),
                    Integer.parseInt(tfQty.getText().trim()),
                    Integer.parseInt(tfMin.getText().trim()),
                    Double.parseDouble(tfPrice.getText().trim()),
                    new Date(dateChooser.getDate().getTime())
            );
            i.setItemId(id);
            dao.update(i);
            reload();
            JOptionPane.showMessageDialog(this, "Updated!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select an Item!");
            return;
        }
        int id = (Integer) model.getValueAt(r,0);
        dao.delete(id);
        reload();
    }

    private void reload(){
        model.setRowCount(0);
        dao.getAll().forEach(i -> model.addRow(new Object[]{
                i.getItemId(),
                i.getItemName(),
                i.getCategory(),
                i.getQuantity(),
                i.getMinQuantity(),
                i.getUnitPrice(),
                i.getExpiryDate()
        }));
    }
}

 */

package com.medinova.ui.panels;

import com.medinova.dao.InventoryDAO;
import com.medinova.model.Inventory;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPanel extends JPanel {

    private final InventoryDAO dao = new InventoryDAO();
    private List<Inventory> allItems;

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","Item Name","Category","Qty","Min Qty","Unit Price","Expiry"}, 0){
        public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable table = new JTable(model);

    private final JTextField tfName = new JTextField();
    private final JTextField tfQty = new JTextField();
    private final JTextField tfMin = new JTextField();
    private final JTextField tfPrice = new JTextField();
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> cbCat = new JComboBox<>(new String[]{"Medicine","Equipment"});

    // Search
    private final JTextField tfSearch = new JTextField();
    private final JButton btnSearch = new JButton("Search");
    private final JButton btnClear = new JButton("Clear");

    public InventoryPanel(){
        setLayout(new BorderLayout(15,15));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(buildForm(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        add(buildBottomActions(), BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        reload();
    }

    private JPanel buildForm(){
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Inventory"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,8,6,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        Dimension fieldSize = new Dimension(180,26);
        tfName.setPreferredSize(fieldSize);
        tfQty.setPreferredSize(new Dimension(100,26));
        tfMin.setPreferredSize(new Dimension(100,26));
        tfPrice.setPreferredSize(new Dimension(120,26));

        // Date chooser
        dateChooser.setDateFormatString("yyyy-MM-dd");
        JTextField dateEditor = (JTextField) dateChooser.getDateEditor().getUiComponent();
        Runnable applyDateColors = () -> {
            dateEditor.setBackground(new Color(45,45,45));
            dateEditor.setForeground(Color.WHITE);
            dateEditor.setCaretColor(Color.WHITE);
        };
        applyDateColors.run();
        dateChooser.getDateEditor().addPropertyChangeListener("date", e -> SwingUtilities.invokeLater(applyDateColors));

        int y=0;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Item Name"), c);
        c.gridx=1; p.add(tfName, c);
        c.gridx=2; p.add(new JLabel("Category"), c);
        c.gridx=3; p.add(cbCat, c);
        y++;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Quantity"), c);
        c.gridx=1; p.add(tfQty, c);
        c.gridx=2; p.add(new JLabel("Min Qty"), c);
        c.gridx=3; p.add(tfMin, c);
        y++;
        c.gridx=0;c.gridy=y; p.add(new JLabel("Unit Price"), c);
        c.gridx=1; p.add(tfPrice, c);
        c.gridx=2; p.add(new JLabel("Expiry Date"), c);
        c.gridx=3; p.add(dateChooser, c);
        y++;

        JButton add = new JButton("Add");
        add.addActionListener(e -> addItem());
        add.setBackground(new Color(52,152,219));
        add.setForeground(Color.WHITE);
        c.gridx=3; c.gridy=y;
        p.add(add, c);
        y++;

        // Search row
        c.gridx=0; c.gridy=y; p.add(new JLabel("Search"), c);
        c.gridx=1; c.gridwidth=2; p.add(tfSearch, c);
        c.gridwidth=1;

        JPanel searchBtns = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        btnSearch.addActionListener(e -> applySearch());
        btnClear.addActionListener(e -> {
            tfSearch.setText("");
            reload();
        });
        searchBtns.add(btnSearch);
        searchBtns.add(btnClear);
        c.gridx=3; p.add(searchBtns, c);

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

    private JPanel buildTable(){
        JPanel p = new JPanel(new BorderLayout(10,10));
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(52,152,219));
        table.setSelectionForeground(Color.WHITE);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private boolean validateForm(){
        if(tfName.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Item name required!");
            return false;
        }
        if(tfQty.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Quantity required!");
            return false;
        }
        if(tfMin.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Min qty required!");
            return false;
        }
        if(tfPrice.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Price required!");
            return false;
        }
        if(dateChooser.getDate()==null){
            JOptionPane.showMessageDialog(this, "Expiry date required!");
            return false;
        }
        return true;
    }

    private void clearForm(){
        tfName.setText("");
        tfQty.setText("");
        tfMin.setText("");
        tfPrice.setText("");
        dateChooser.setDate(null);
        cbCat.setSelectedIndex(0);
        tfSearch.setText("");
        table.clearSelection();
    }

    private void addItem(){
        if(!validateForm()) return;
        try {
            Inventory i = new Inventory(
                    tfName.getText().trim(),
                    cbCat.getSelectedItem().toString(),
                    Integer.parseInt(tfQty.getText().trim()),
                    Integer.parseInt(tfMin.getText().trim()),
                    Double.parseDouble(tfPrice.getText().trim()),
                    new Date(dateChooser.getDate().getTime())
            );
            dao.add(i);
            reload();
            JOptionPane.showMessageDialog(this, "Item added!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select an Item!");
            return;
        }
        if(!validateForm()) return;
        try {
            int id = (Integer) model.getValueAt(r,0);
            Inventory i = new Inventory(
                    tfName.getText().trim(),
                    cbCat.getSelectedItem().toString(),
                    Integer.parseInt(tfQty.getText().trim()),
                    Integer.parseInt(tfMin.getText().trim()),
                    Double.parseDouble(tfPrice.getText().trim()),
                    new Date(dateChooser.getDate().getTime())
            );
            i.setItemId(id);
            dao.update(i);
            reload();
            JOptionPane.showMessageDialog(this, "Item updated!");
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelected(){
        int r = table.getSelectedRow();
        if(r < 0){
            JOptionPane.showMessageDialog(this, "Select an Item!");
            return;
        }
        int id = (Integer) model.getValueAt(r,0);
        dao.delete(id);
        reload();
    }

    private void reload(){
        allItems = dao.getAll();
        model.setRowCount(0);
        allItems.forEach(i -> model.addRow(new Object[]{
                i.getItemId(),
                i.getItemName(),
                i.getCategory(),
                i.getQuantity(),
                i.getMinQuantity(),
                i.getUnitPrice(),
                i.getExpiryDate()
        }));
        clearForm(); // pastro gjithçka kur bëhet refresh
    }

    private void applySearch(){
        String q = tfSearch.getText().trim().toLowerCase();
        if(q.isEmpty()){
            reload();
            return;
        }
        List<Inventory> filtered = allItems.stream()
                .filter(i -> String.valueOf(i.getItemId()).toLowerCase().contains(q)
                        || i.getItemName().toLowerCase().contains(q)
                        || i.getCategory().toLowerCase().contains(q))
                .collect(Collectors.toList());
        model.setRowCount(0);
        filtered.forEach(i -> model.addRow(new Object[]{
                i.getItemId(),
                i.getItemName(),
                i.getCategory(),
                i.getQuantity(),
                i.getMinQuantity(),
                i.getUnitPrice(),
                i.getExpiryDate()
        }));
    }

    private void fillForm(){
        int row = table.getSelectedRow();
        if(row < 0) return;

        tfName.setText(String.valueOf(model.getValueAt(row,1)));
        cbCat.setSelectedItem(String.valueOf(model.getValueAt(row,2)));
        tfQty.setText(String.valueOf(model.getValueAt(row,3)));
        tfMin.setText(String.valueOf(model.getValueAt(row,4)));
        tfPrice.setText(String.valueOf(model.getValueAt(row,5)));

        Object expObj = model.getValueAt(row,6);
        if(expObj != null){
            try { dateChooser.setDate(Date.valueOf(expObj.toString())); } catch (Exception ignored){}
        }
    }
}
