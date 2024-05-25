-- Eliminar la base de datos si existe
DROP DATABASE IF EXISTS NextStep;

CREATE DATABASE NextStep;
USE NextStep;

-- Tabla Usuario
CREATE TABLE Usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol ENUM('normal', 'admin') NOT NULL
);

-- Tabla Pago
CREATE TABLE Pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha DATE NOT NULL,
    recurrente BOOLEAN NOT NULL,
    frecuencia ENUM('diaria', 'semanal', 'mensual', 'anual'),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

-- Tabla Categoria
CREATE TABLE Categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- Tabla Gasto
CREATE TABLE Gasto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

-- Tabla Gasto_Categoria (para la relación varios a varios entre Gasto y Categoria)
CREATE TABLE Gasto_Categoria (
    gasto_id INT NOT NULL,
    categoria_id INT NOT NULL,
    PRIMARY KEY (gasto_id, categoria_id),
    FOREIGN KEY (gasto_id) REFERENCES Gasto(id),
    FOREIGN KEY (categoria_id) REFERENCES Categoria(id)
);

-- Tabla Informe
CREATE TABLE Informe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    tipo ENUM('diario', 'semanal', 'mensual', 'anual') NOT NULL,
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    contenido TEXT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

-- Insertar algunos datos de prueba

-- Insertar usuarios
INSERT INTO Usuario (nombre, correo, contraseña, rol) VALUES 
('Juan Perez', 'juan.perez@example.com', 'hashed_password1', 'normal'),
('Maria Lopez', 'maria.lopez@example.com', 'hashed_password2', 'normal'),
('Admin', 'admin@example.com', 'hashed_password3', 'admin');

-- Insertar pagos
INSERT INTO Pago (usuario_id, nombre, monto, fecha, recurrente, frecuencia) VALUES 
(1, 'Renta', 500.00, '2024-06-01', TRUE, 'mensual'),
(1, 'Internet', 50.00, '2024-06-05', TRUE, 'mensual'),
(2, 'Gym', 30.00, '2024-06-01', TRUE, 'mensual');

-- Insertar categorías
INSERT INTO Categoria (nombre, descripcion) VALUES 
('Alimentación', 'Gastos relacionados con la compra de alimentos'),
('Transporte', 'Gastos relacionados con transporte'),
('Ocio', 'Gastos relacionados con actividades recreativas');

-- Insertar gastos
INSERT INTO Gasto (usuario_id, nombre, monto, fecha) VALUES 
(1, 'Supermercado', 150.00, '2024-05-20'),
(1, 'Taxi', 20.00, '2024-05-22'),
(2, 'Cine', 15.00, '2024-05-23');

-- Insertar relaciones entre gastos y categorías
INSERT INTO Gasto_Categoria (gasto_id, categoria_id) VALUES 
(1, 1), -- Supermercado -> Alimentación
(2, 2), -- Taxi -> Transporte
(3, 3); -- Cine -> Ocio

-- Insertar informes
INSERT INTO Informe (usuario_id, tipo, contenido) VALUES 
(1, 'mensual', 'Informe mensual de gastos de Juan Perez.'),
(2, 'mensual', 'Informe mensual de gastos de Maria Lopez.');