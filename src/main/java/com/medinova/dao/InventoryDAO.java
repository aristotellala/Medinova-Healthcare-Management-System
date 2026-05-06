package com.medinova.dao;

import com.medinova.database.DatabaseConnection;
import com.medinova.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO implements BaseDAO<Inventory> {

    @Override
    public void add(Inventory i) {
        String sql = "INSERT INTO Inventory (item_name,category,quantity,min_quantity,unit_price,expiry_date) VALUES (?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, i.getItemName());
            ps.setString(2, i.getCategory());
            ps.setInt(3, i.getQuantity());
            ps.setInt(4, i.getMinQuantity());
            ps.setDouble(5, i.getUnitPrice());
            if (i.getExpiryDate() != null) {
                ps.setDate(6, i.getExpiryDate());
            } else {
                ps.setNull(6, Types.DATE);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add Inventory: " + e.getMessage());
        }
    }

    @Override
    public List<Inventory> getAll() {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT * FROM Inventory ORDER BY item_name";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Inventory i = new Inventory();
                i.setItemId(rs.getInt("item_id"));
                i.setItemName(rs.getString("item_name"));
                i.setCategory(rs.getString("category"));
                i.setQuantity(rs.getInt("quantity"));
                i.setMinQuantity(rs.getInt("min_quantity"));
                i.setUnitPrice(rs.getDouble("unit_price"));
                i.setExpiryDate(rs.getDate("expiry_date"));
                list.add(i);
            }
        } catch (SQLException e) {
            System.out.println("getAll Inventory: " + e.getMessage());
        }
        return list;
    }

    public Inventory getById(int id){
        String sql = "SELECT * FROM Inventory WHERE item_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Inventory i = new Inventory(
                        rs.getString("item_name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getInt("min_quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDate("expiry_date")
                );
                i.setItemId(rs.getInt("item_id"));
                return i;
            }
        } catch (Exception e){
            System.out.println("getById Inventory: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Inventory i) {
        String sql = "UPDATE Inventory SET item_name=?,category=?,quantity=?,min_quantity=?,unit_price=?,expiry_date=? WHERE item_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, i.getItemName());
            ps.setString(2, i.getCategory());
            ps.setInt(3, i.getQuantity());
            ps.setInt(4, i.getMinQuantity());
            ps.setDouble(5, i.getUnitPrice());
            if (i.getExpiryDate() != null) {
                ps.setDate(6, i.getExpiryDate());
            } else {
                ps.setNull(6, Types.DATE);
            }
            ps.setInt(7, i.getItemId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("update Inventory: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Inventory WHERE item_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("delete Inventory: " + e.getMessage());
        }
    }

    public int countLowStock() {
        String sql = "SELECT COUNT(*) FROM Inventory WHERE quantity < min_quantity";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("countLowStock: " + e.getMessage());
        }
        return 0;
    }
}
