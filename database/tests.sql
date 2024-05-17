-- Insertar datos de prueba en la tabla Pago
INSERT INTO Pago (idPago, idUsuario, nombre, cantidad, fecha, recurrencia)
VALUES (1, 1, 'Alquiler', 1000.00, '2024-04-01', 'mensual'),
       (2, 1, 'Teléfono', 50.00, '2024-04-05', 'mensual'),
       (3, 2, 'Internet', 60.00, '2024-04-10', 'mensual');

-- Insertar datos de prueba en la tabla Categoría
INSERT INTO Categoría (idCategoría, idUsuario, nombre)
VALUES (1, 1, 'Alimentación'),
       (2, 1, 'Transporte'),
       (3, 2, 'Ocio');

-- Insertar datos de prueba en la tabla Gasto
INSERT INTO Gasto (idGasto, idUsuario, idCategoría, nombre, cantidad, fecha)
VALUES (1, 1, 1, 'Compras en el supermercado', 50.00, '2024-04-03'),
       (2, 1, 2, 'Gasolina', 40.00, '2024-04-07'),
       (3, 2, 3, 'Cine', 20.00, '2024-04-12');

-- Insertar datos de prueba en la tabla Informe
INSERT INTO Informe (idInforme, idUsuario, fecha_generacion, contenido)
VALUES (1, 1, '2024-05-01', 'Informe de gastos del usuario 1 generado el 2024-05-01'),
       (2, 2, '2024-05-01', 'Informe de gastos del usuario 2 generado el 2024-05-01');

-- Mostrar los datos insertados en la tabla Usuario
SELECT * FROM Usuario;

-- Mostrar los datos insertados en la tabla Pago
SELECT * FROM Pago;

-- Mostrar los datos insertados en la tabla Categoría
SELECT * FROM Categoría;

-- Mostrar los datos insertados en la tabla Gasto
SELECT * FROM Gasto;

-- Mostrar los datos insertados en la tabla Informe
SELECT * FROM Informe;

-- COMPROBAR LAS RELACIONES
-- Consultar los pagos realizados por un usuario
SELECT *
FROM Pago
WHERE idUsuario = 1;

-- Consultar los gastos de una categoría
SELECT *
FROM Gasto
WHERE idCategoría = 1;

-- Consultar los informes generados por un usuario específico junto con su contenido
SELECT Informe.*, Usuario.nombre AS nombre_usuario
FROM Informe
JOIN Usuario ON Informe.idUsuario = Usuario.idUsuario
WHERE Usuario.idUsuario = 1;