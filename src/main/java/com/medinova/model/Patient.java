package com.medinova.model;

public class Patient extends Person {
    private int patientId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String diagnosis;
    private int doctorId;

    public Patient() {
    }

    public Patient(String first, String last, int age, String gender,
                   String phone, String email, String diagnosis, int doctorId) {
        super(email, phone);
        this.firstName = first;
        this.lastName = last;
        this.age = age;
        this.gender = gender;
        this.diagnosis = diagnosis;
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
