package com.medinova.dao;

import com.medinova.database.DatabaseConnection;
import com.medinova.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO implements BaseDAO<Appointment> {

    @Override
    public void add(Appointment a) {
        String sql = "INSERT INTO Appointments (patient_id,doctor_id,appointment_date,appointment_time,reason,status) VALUES (?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setDate(3, a.getAppointmentDate());
            ps.setTime(4, a.getAppointmentTime());
            ps.setString(5, a.getReason());
            ps.setString(6, a.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("add Appointment: " + e.getMessage());
        }
    }

    @Override
    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointments ORDER BY appointment_date, appointment_time";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setDoctorId(rs.getInt("doctor_id"));
                a.setAppointmentDate(rs.getDate("appointment_date"));
                a.setAppointmentTime(rs.getTime("appointment_time"));
                a.setReason(rs.getString("reason"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.out.println("getAll Appointment: " + e.getMessage());
        }
        return list;
    }

    public Appointment getById(int id) {
        String sql = "SELECT * FROM Appointments WHERE appointment_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setDoctorId(rs.getInt("doctor_id"));
                a.setAppointmentDate(rs.getDate("appointment_date"));
                a.setAppointmentTime(rs.getTime("appointment_time"));
                a.setReason(rs.getString("reason"));
                a.setStatus(rs.getString("status"));
                return a;
            }

        } catch (SQLException e) {
            System.out.println("getById Appointment: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Appointment a) {
        String sql = "UPDATE Appointments SET patient_id=?,doctor_id=?,appointment_date=?,appointment_time=?,reason=?,status=? WHERE appointment_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setDate(3, a.getAppointmentDate());
            ps.setTime(4, a.getAppointmentTime());
            ps.setString(5, a.getReason());
            ps.setString(6, a.getStatus());
            ps.setInt(7, a.getAppointmentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("update Appointment: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Appointments WHERE appointment_id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("delete Appointment: " + e.getMessage());
        }
    }
}
