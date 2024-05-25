USE NextStep;

-- Prueba de inserción de usuarios
INSERT INTO Usuario (nombre, correo, contraseña, rol) VALUES 
('Carlos Sanchez', 'carlos.sanchez@example.com', 'hashed_password4', 'normal'),
('Ana Gutierrez', 'ana.gutierrez@example.com', 'hashed_password5', 'normal'),
('SuperAdmin', 'superadmin@example.com', 'hashed_password6', 'admin');

-- Prueba de inserción de pagos
INSERT INTO Pago (usuario_id, nombre, monto, fecha, recurrente, frecuencia) VALUES 
(3, 'Luz', 60.00, '2024-06-07', TRUE, 'mensual'),
(4, 'Agua', 30.00, '2024-06-10', TRUE, 'mensual');

-- Prueba de inserción de categorías
INSERT INTO Categoria (nombre, descripcion) VALUES 
('Educación', 'Gastos relacionados con educación');

-- Prueba de inserción de gastos
INSERT INTO Gasto (usuario_id, nombre, monto, fecha) VALUES 
(3, 'Libros', 200.00, '2024-05-25');

-- Prueba de inserción de relaciones entre gastos y categorías
INSERT INTO Gasto_Categoria (gasto_id, categoria_id) VALUES 
(4, 4); -- Libros -> Educación

-- Prueba de inserción de informes
INSERT INTO Informe (usuario_id, tipo, contenido) VALUES 
(3, 'mensual', 'Informe mensual de gastos de Carlos Sanchez.');

-- Consultar todos los usuarios
SELECT * FROM Usuario;

-- Consultar todos los pagos
SELECT * FROM Pago;

-- Consultar todas las categorías
SELECT * FROM Categoria;

-- Consultar todos los gastos
SELECT * FROM Gasto;

-- Consultar todas las relaciones entre gastos y categorías
SELECT * FROM Gasto_Categoria;

-- Consultar todos los informes
SELECT * FROM Informe;

-- Prueba de actualización de un usuario
UPDATE Usuario SET nombre = 'Carlos S. Sanchez' WHERE id = 3;

-- Prueba de eliminación de un gasto
DELETE FROM Gasto WHERE id = 4;

-- Consultar todas las tablas después de las operaciones
SELECT * FROM Usuario;
SELECT * FROM Pago;
SELECT * FROM Categoria;
SELECT * FROM Gasto;
SELECT * FROM Gasto_Categoria;
SELECT * FROM Informe;