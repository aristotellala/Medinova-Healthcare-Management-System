package com.medinova.dao;

import com.medinova.database.DatabaseConnection;
import com.medinova.model.Doctor;
import com.medinova.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO implements BaseDAO<Doctor> {

    @Override
    public void add(Doctor d) {
        String sql = "INSERT INTO Doctors (first_name,last_name,specialization,email,phone,working_hours) VALUES (?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getFirstName());
            ps.setString(2, d.getLastName());
            ps.setString(3, d.getSpecialization());
            ps.setString(4, d.getEmail());
            ps.setString(5, d.getPhone());
            ps.setString(6, d.getWorkingHours());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add Doctor: " + e.getMessage());
        }
    }

    @Override
    public List<Doctor> getAll() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM Doctors ORDER BY last_name, first_name";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setFirstName(rs.getString("first_name"));
                d.setLastName(rs.getString("last_name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setEmail(rs.getString("email"));
                d.setPhone(rs.getString("phone"));
                d.setWorkingHours(rs.getString("working_hours"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println("getAll Doctor: " + e.getMessage());
        }
        return list;
    }

    public Doctor getById(int id) {
        Doctor doctor = null;
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                doctor = new Doctor();
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctor.setFirstName(rs.getString("first_name"));
                doctor.setLastName(rs.getString("last_name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setWorkingHours(rs.getString("working_hours"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setEmail(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctor;
    }

    public List<Doctor> searchByName(String name) {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE first_name LIKE ? OR last_name LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public void update(Doctor d) {
        String sql = "UPDATE Doctors SET first_name=?,last_name=?,specialization=?,email=?,phone=?,working_hours=? WHERE doctor_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getFirstName());
            ps.setString(2, d.getLastName());
            ps.setString(3, d.getSpecialization());
            ps.setString(4, d.getEmail());
            ps.setString(5, d.getPhone());
            ps.setString(6, d.getWorkingHours());
            ps.setInt(7, d.getDoctorId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("update Doctor: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Doctors WHERE doctor_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("delete Doctor: " + e.getMessage());
        }
    }

    private Doctor map(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId(rs.getInt("doctor_id"));
        d.setFirstName(rs.getString("first_name"));
        d.setLastName(rs.getString("last_name"));
        d.setSpecialization(rs.getString("specialization"));
        d.setEmail(rs.getString("email"));
        d.setPhone(rs.getString("phone"));
        d.setWorkingHours(rs.getString("working_hours"));

        return d;
    }
}
