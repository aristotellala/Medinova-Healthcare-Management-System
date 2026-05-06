package com.medinova.dao;

import com.medinova.database.DatabaseConnection;
import com.medinova.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO implements BaseDAO<Patient> {

    @Override
    public void add(Patient p) {
        String sql = "INSERT INTO Patients (first_name,last_name,age,gender,phone,email,diagnosis,doctor_id) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setInt(3, p.getAge());
            ps.setString(4, p.getGender());
            ps.setString(5, p.getPhone());
            ps.setString(6, p.getEmail());
            ps.setString(7, p.getDiagnosis());

            if (p.getDoctorId() > 0) {
                ps.setInt(8, p.getDoctorId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add Patient: " + e.getMessage());
        }
    }

    public Patient getById(int id) {
        Patient p = null;
        String sql = "SELECT * FROM Patients WHERE patient_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Patient(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("diagnosis"),
                        rs.getInt("doctor_id")
                );
                p.setPatientId(rs.getInt("patient_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public List<Patient> searchByName(String name) {
        List<Patient> list = new ArrayList<>();
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
    public List<Patient> getAll() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM Patients ORDER BY last_name, first_name";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setFirstName(rs.getString("first_name"));
                p.setLastName(rs.getString("last_name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setPhone(rs.getString("phone"));
                p.setEmail(rs.getString("email"));
                p.setDiagnosis(rs.getString("diagnosis"));
                p.setDoctorId(rs.getInt("doctor_id"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println("getAll Patient: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Patient p) {
        String sql = "UPDATE Patients SET first_name=?,last_name=?,age=?,gender=?,phone=?,email=?,diagnosis=?,doctor_id=? WHERE patient_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setInt(3, p.getAge());
            ps.setString(4, p.getGender());
            ps.setString(5, p.getPhone());
            ps.setString(6, p.getEmail());
            ps.setString(7, p.getDiagnosis());

            if (p.getDoctorId() > 0) {
                ps.setInt(8, p.getDoctorId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setInt(9, p.getPatientId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("update Patient: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Patients WHERE patient_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("delete Patient: " + e.getMessage());
        }
    }

    private Patient map(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientId(rs.getInt("patient_id"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setAge(rs.getInt("age"));
        p.setGender(rs.getString("gender"));
        p.setPhone(rs.getString("phone"));
        p.setEmail(rs.getString("email"));
        p.setDiagnosis(rs.getString("diagnosis"));
        p.setDoctorId(rs.getInt("doctor_id"));
        return p;
    }


}
