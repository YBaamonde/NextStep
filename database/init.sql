-- Eliminar la base de datos si existe
DROP DATABASE IF EXISTS NextStepDB;

-- Crear la base de datos y conectar a ella
CREATE DATABASE NextStepDB;
USE NextStepDB;

-- Crear la tabla Usuario
CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY,
    nombre VARCHAR(50),
    correo VARCHAR(100),
    contraseña VARCHAR(50),
    rol ENUM('user', 'admin')
);

-- Crear la tabla Pago
CREATE TABLE Pago (
    idPago INT PRIMARY KEY,
    idUsuario INT,
    nombre VARCHAR(50),
    cantidad DECIMAL(10, 2),
    fecha DATE,
    recurrencia VARCHAR(50),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- Crear la tabla Categoría
CREATE TABLE Categoría (
    idCategoría INT PRIMARY KEY,
    idUsuario INT,
    nombre VARCHAR(50),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- Crear la tabla Gasto
CREATE TABLE Gasto (
    idGasto INT PRIMARY KEY,
    idUsuario INT,
    idCategoría INT,
    nombre VARCHAR(50),
    cantidad DECIMAL(10, 2),
    fecha DATE,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idCategoría) REFERENCES Categoría(idCategoría)
);

-- Crear la tabla Informe
CREATE TABLE Informe (
    idInforme INT PRIMARY KEY,
    idUsuario INT,
    fecha_generacion DATE,
    contenido TEXT,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- Crear tabla de relación para Gasto-Categoría
CREATE TABLE Gasto_Categoría (
    idGasto INT,
    idCategoría INT,
    PRIMARY KEY (idGasto, idCategoría),
    FOREIGN KEY (idGasto) REFERENCES Gasto(idGasto),
    FOREIGN KEY (idCategoría) REFERENCES Categoría(idCategoría)
);

-- Insertar algunos datos de prueba en la tabla Usuario
INSERT INTO Usuario (idUsuario, nombre, correo, contraseña, rol)
VALUES (1, 'Usuario1', 'usuario1@example.com', 'contraseña1', 'user'),
       (2, 'Usuario2', 'usuario2@example.com', 'contraseña2', 'admin');