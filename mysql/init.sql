-- Eliminar la base de datos si existe
-- DROP DATABASE IF EXISTS NextStepDB;

CREATE DATABASE IF NOT EXISTS NextStepDB;
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
