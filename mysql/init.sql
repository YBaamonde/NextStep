-- Eliminar la base de datos si existe
-- DROP DATABASE IF EXISTS NextStepDB;

CREATE DATABASE NextStepDB;
USE NextStepDB;

-- Tabla Usuario
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('normal', 'admin') NOT NULL
);

-- Tabla Pago
CREATE TABLE pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha DATE NOT NULL,
    recurrente BOOLEAN NOT NULL,
    frecuencia ENUM('diaria', 'semanal', 'mensual', 'anual'),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Tabla Categoria
CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Tabla Gasto
CREATE TABLE gasto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    categoria_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- Tabla Gasto_Categoria (para la relación varios a varios entre Gasto y Categoria)
CREATE TABLE gasto_categoria (
    gasto_id INT NOT NULL,
    categoria_id INT NOT NULL,
    PRIMARY KEY (gasto_id, categoria_id),
    FOREIGN KEY (gasto_id) REFERENCES gasto(id),
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- Tabla Informe
CREATE TABLE informe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    tipo ENUM('diario', 'semanal', 'mensual', 'anual') NOT NULL,
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    contenido TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Tabla Notificacion In-App
CREATE TABLE inapp_notif (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  pago_id INT NOT NULL,
  titulo VARCHAR(100) NOT NULL,
  mensaje VARCHAR(500) NOT NULL,
  leido BOOLEAN NOT NULL DEFAULT FALSE,
  fecha_creacion DATETIME NOT NULL,
  fecha_leido DATETIME DEFAULT NULL,
  CONSTRAINT fk_usuario_notificacion FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE,
  CONSTRAINT fk_pago_notificacion FOREIGN KEY (pago_id) REFERENCES pago (id) ON DELETE CASCADE
);


-- Tabla Notificación email
CREATE TABLE email_notif (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    pago_id INT NOT NULL,
    asunto VARCHAR(255) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio DATETIME NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (pago_id) REFERENCES pago(id) ON DELETE CASCADE
);



-- Configuración de las notificaciones
CREATE TABLE notificacion_config (
    id INT AUTO_INCREMENT PRIMARY KEY, -- ID único para la configuración
    usuario_id INT NOT NULL UNIQUE,    -- ID del usuario asociado (único)
    email_activas BOOLEAN NOT NULL DEFAULT TRUE, -- Configuración de notificaciones por email (activas o no)
    email_dias_antes INT NOT NULL DEFAULT 1,     -- Días antes para las notificaciones por email
    in_app_activas BOOLEAN NOT NULL DEFAULT TRUE, -- Configuración de notificaciones In-App (activas o no)
    in_app_dias_antes INT NOT NULL DEFAULT 1,     -- Días antes para las notificaciones In-App
    CONSTRAINT fk_usuario_config FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);


/*
-- Insertar algunos datos de prueba

-- Insertar usuarios
INSERT INTO usuario (nombre, username, password, rol) VALUES 
('Juan Perez', 'juan.perez@example.com', 'hashed_password1', 'normal'),
('Maria Lopez', 'maria.lopez@example.com', 'hashed_password2', 'normal'),
('Admin', 'admin@example.com', 'hashed_password3', 'admin');

-- Insertar pagos
INSERT INTO pago (usuario_id, nombre, monto, fecha, recurrente, frecuencia) VALUES 
(1, 'Renta', 500.00, '2024-06-01', TRUE, 'mensual'),
(1, 'Internet', 50.00, '2024-06-05', TRUE, 'mensual'),
(2, 'Gym', 30.00, '2024-06-01', TRUE, 'mensual');

-- Insertar categorías
INSERT INTO categoria (nombre, descripcion) VALUES 
('Alimentación', 'Gastos relacionados con la compra de alimentos'),
('Transporte', 'Gastos relacionados con transporte'),
('Ocio', 'Gastos relacionados con actividades recreativas');

-- Insertar gastos
INSERT INTO gasto (usuario_id, nombre, monto, fecha) VALUES 
(1, 'Supermercado', 150.00, '2024-05-20'),
(1, 'Taxi', 20.00, '2024-05-22'),
(2, 'Cine', 15.00, '2024-05-23');

-- Insertar relaciones entre gastos y categorías
INSERT INTO gasto_categoria (gasto_id, categoria_id) VALUES 
(1, 1), -- Supermercado -> Alimentación
(2, 2), -- Taxi -> Transporte
(3, 3); -- Cine -> Ocio

-- Insertar informes
INSERT INTO informe (usuario_id, tipo, contenido) VALUES 
(1, 'mensual', 'Informe mensual de gastos de Juan Perez.'),
(2, 'mensual', 'Informe mensual de gastos de Maria Lopez.');

*/