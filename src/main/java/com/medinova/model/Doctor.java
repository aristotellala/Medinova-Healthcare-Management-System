package com.medinova.model;

public class Doctor extends Person {
    private int doctorId;
    private String firstName;
    private String lastName;
    private String specialization;
    private String workingHours;

    public Doctor() {
    }

    public Doctor(String first, String last, String spec,
                  String email, String phone, String hours) {
        super(email, phone);
        this.firstName = first;
        this.lastName = last;
        this.specialization = spec;
        this.workingHours = hours;
    }


    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }
}
