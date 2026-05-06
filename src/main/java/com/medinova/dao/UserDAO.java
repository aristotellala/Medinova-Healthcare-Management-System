package com.medinova.dao;

import com.medinova.database.DatabaseConnection;
import com.medinova.model.User;
import com.medinova.model.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements BaseDAO<User> {

    @Override
    public void add(User u) {
        String sql = "INSERT INTO Users (username,password,role,full_name) VALUES (?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole().name());
            ps.setString(4, u.getFullName());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add User: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY user_id";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(Role.valueOf(rs.getString("role")));
                u.setFullName(rs.getString("full_name"));
                list.add(u);
            }
        } catch (SQLException e) {
            System.out.println("getAll User: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(User u) {
        String sql = "UPDATE Users SET username=?,password=?,role=?,full_name=? WHERE user_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole().name());
            ps.setString(4, u.getFullName());
            ps.setInt(5, u.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("update User: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Users WHERE user_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("delete User: " + e.getMessage());
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username=? AND password=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(Role.valueOf(rs.getString("role")));
                u.setFullName(rs.getString("full_name"));
                return u;
            }
        } catch (SQLException e) {
            System.out.println("login: " + e.getMessage());
        }
        return null;
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM Users";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("countUsers: " + e.getMessage());
        }
        return 0;
    }
}
