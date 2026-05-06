
-- Medinova Healthcare Management System Database Schema (SQL Server)

CREATE TABLE Users(
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(100) NOT NULL,
    role NVARCHAR(20) NOT NULL,
    full_name NVARCHAR(100) NOT NULL
);

CREATE TABLE Doctors(
    doctor_id INT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(60),
    last_name NVARCHAR(60),
    specialization NVARCHAR(100),
    email NVARCHAR(120),
    phone NVARCHAR(40),
    working_hours NVARCHAR(60)
);

CREATE TABLE Patients(
    patient_id INT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(60),
    last_name NVARCHAR(60),
    age INT,
    gender NVARCHAR(20),
    phone NVARCHAR(40),
    email NVARCHAR(120),
    diagnosis NVARCHAR(200),
    doctor_id INT NULL
);

CREATE TABLE Appointments(
    appointment_id INT IDENTITY(1,1) PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    appointment_date DATE,
    appointment_time TIME,
    reason NVARCHAR(200),
    status NVARCHAR(20)
);

CREATE TABLE Inventory(
    item_id INT IDENTITY(1,1) PRIMARY KEY,
    item_name NVARCHAR(120),
    category NVARCHAR(50),
    quantity INT,
    min_quantity INT,
    unit_price DECIMAL(10,2),
    expiry_date DATE NULL
);

INSERT INTO Users (username, password, role, full_name)
VALUES
('admin', 'admin123', 'ADMIN', 'System Administrator'),
('reception', 'rec123', 'RECEPTIONIST', 'Receptionist');
