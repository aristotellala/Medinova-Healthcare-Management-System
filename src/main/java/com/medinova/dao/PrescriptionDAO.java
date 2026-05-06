package com.medinova.dao;

import com.medinova.database.DatabaseConnection;
import com.medinova.model.Prescription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO implements BaseDAO<Prescription> {

    @Override
    public void add(Prescription p) {
        String sql = "INSERT INTO Prescription (appointment_id, patient_id, doctor_id, item_id, quantity, instructions, date_issued) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getAppointmentId());
            ps.setInt(2, p.getPatientId());
            ps.setInt(3, p.getDoctorId());
            ps.setInt(4, p.getItemId());
            ps.setInt(5, p.getQuantity());
            ps.setString(6, p.getInstructions());
            ps.setDate(7, p.getDateIssued());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add Prescription: " + e.getMessage());
        }
    }

    @Override
    public List<Prescription> getAll() {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM Prescription ORDER BY prescription_id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionId(rs.getInt("prescription_id"));
                p.setAppointmentId(rs.getInt("appointment_id"));
                p.setPatientId(rs.getInt("patient_id"));
                p.setDoctorId(rs.getInt("doctor_id"));
                p.setItemId(rs.getInt("item_id"));
                p.setQuantity(rs.getInt("quantity"));
                p.setInstructions(rs.getString("instructions"));
                p.setDateIssued(rs.getDate("date_issued"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println("getAll Prescription: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Prescription p) {
        String sql = "UPDATE Prescription SET " +
                "appointment_id=?, patient_id=?, doctor_id=?, item_id=?, quantity=?, instructions=?, date_issued=? " +
                "WHERE prescription_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getAppointmentId());
            ps.setInt(2, p.getPatientId());
            ps.setInt(3, p.getDoctorId());
            ps.setInt(4, p.getItemId());
            ps.setInt(5, p.getQuantity());
            ps.setString(6, p.getInstructions());
            ps.setDate(7, p.getDateIssued());
            ps.setInt(8, p.getPrescriptionId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("update Prescription: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Prescription WHERE prescription_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("delete Prescription: " + e.getMessage());
        }
    }

    public Prescription getById(int id) {
        String sql = "SELECT * FROM Prescription WHERE prescription_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionId(rs.getInt("prescription_id"));
                p.setAppointmentId(rs.getInt("appointment_id"));
                p.setPatientId(rs.getInt("patient_id"));
                p.setDoctorId(rs.getInt("doctor_id"));
                p.setItemId(rs.getInt("item_id"));
                p.setQuantity(rs.getInt("quantity"));
                p.setInstructions(rs.getString("instructions"));
                p.setDateIssued(rs.getDate("date_issued"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("getById Prescription: " + e.getMessage());
        }
        return null;
    }
}
