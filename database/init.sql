-- DROP DATABASE NextStepDB; --

-- Crear la base de datos y las tablas necesarias para el proyecto
CREATE DATABASE NextStepDB;

-- Conectar a la base de datos
USE NextStepDB;

-- Crear las tablas necesarias

-- Tabla Usuario
CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY,
    nombre VARCHAR(50),
    correo VARCHAR(100),
    contraseña VARCHAR(50),
    rol ENUM('user', 'admin')
);

-- Tabla Pago
CREATE TABLE Pago (
    idPago INT PRIMARY KEY,
    idUsuario INT,
    nombre VARCHAR(50),
    cantidad DECIMAL(10, 2),
    fecha DATE,
    recurrencia VARCHAR(50),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- Tabla Categoría, que contiene las categorías de los gastos
CREATE TABLE Categoría (
    idCategoría INT PRIMARY KEY,
    idUsuario INT,
    nombre VARCHAR(50),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);


-- Tabla Gasto
CREATE TABLE Gasto (
    idGasto INT PRIMARY KEY,
    idUsuario INT,
    nombre VARCHAR(50),
    cantidad DECIMAL(10, 2),
    fecha DATE,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);


-- Crear la tabla de unión Gasto_Categoría
CREATE TABLE Gasto_Categoría (
    idGasto INT,
    idCategoría INT,
    PRIMARY KEY (idGasto, idCategoría),
    FOREIGN KEY (idGasto) REFERENCES Gasto(idGasto),
    FOREIGN KEY (idCategoría) REFERENCES Categoría(idCategoría)
);


-- Tabla Informe
CREATE TABLE Informe (
    idInforme INT PRIMARY KEY,
    idUsuario INT,
    fechaInicio DATE,
    fechaFin DATE,
    tipo VARCHAR(50),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);