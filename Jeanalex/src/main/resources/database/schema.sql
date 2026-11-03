-- Esquema PostgreSQL para Jeanalex Dynamics.
-- Ejecutar conectado a la base de datos configurada en config.properties.

CREATE TABLE IF NOT EXISTS rol (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL REFERENCES rol(id),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS agente (
    id SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL REFERENCES usuario(id),
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    placa VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dispositivo (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    serial VARCHAR(100) NOT NULL UNIQUE,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    ubicacion VARCHAR(120),
    fecha_ingreso DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS asignacion (
    id SERIAL PRIMARY KEY,
    id_agente INT NOT NULL REFERENCES agente(id),
    id_dispositivo INT NOT NULL REFERENCES dispositivo(id),
    id_responsable_entrega INT NOT NULL REFERENCES usuario(id),
    fecha_asignacion TIMESTAMP NOT NULL,
    fecha_devolucion TIMESTAMP,
    observaciones TEXT,
    estado_prestamo VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS ans (
    id SERIAL PRIMARY KEY,
    tipo_solicitud VARCHAR(100) NOT NULL,
    prioridad VARCHAR(50) NOT NULL,
    tiempo_respuesta_horas INT NOT NULL,
    tiempo_solucion_horas INT NOT NULL,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (tipo_solicitud, prioridad)
);

CREATE TABLE IF NOT EXISTS ticket (
    id SERIAL PRIMARY KEY,
    asunto VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    tipo_solicitud VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    prioridad VARCHAR(50) NOT NULL,
    id_agente INT NOT NULL REFERENCES agente(id),
    id_coordinador INT REFERENCES usuario(id),
    id_tecnico INT REFERENCES usuario(id),
    id_ans INT NOT NULL REFERENCES ans(id),
    evidencia TEXT,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_asignacion TIMESTAMP,
    fecha_cierre TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mensaje (
    id SERIAL PRIMARY KEY,
    id_remitente INT NOT NULL REFERENCES usuario(id),
    id_destinatario INT NOT NULL REFERENCES usuario(id),
    asunto VARCHAR(150) NOT NULL,
    contenido TEXT NOT NULL,
    fecha_envio TIMESTAMP NOT NULL,
    leido BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS mantenimiento (
    id SERIAL PRIMARY KEY,
    id_dispositivo INT NOT NULL REFERENCES dispositivo(id),
    id_tecnico INT NOT NULL REFERENCES usuario(id),
    tipo VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_programada TIMESTAMP NOT NULL,
    fecha_ejecucion TIMESTAMP,
    observaciones TEXT
);

CREATE TABLE IF NOT EXISTS historial_dispositivo (
    id SERIAL PRIMARY KEY,
    id_dispositivo INT NOT NULL REFERENCES dispositivo(id),
    id_usuario INT NOT NULL REFERENCES usuario(id),
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha TIMESTAMP NOT NULL
);

INSERT INTO rol (id, nombre, descripcion) VALUES
    (1, 'Administrador', 'Acceso total al sistema'),
    (2, 'Coordinador', 'Asigna solicitudes, hace seguimiento y genera reportes de ANS'),
    (3, 'Soporte Tecnico', 'Gestiona soporte, mantenimientos y estados de dispositivos'),
    (4, 'Agente', 'Consulta equipos asignados y registra solicitudes')
ON CONFLICT (id) DO UPDATE SET
    nombre = EXCLUDED.nombre,
    descripcion = EXCLUDED.descripcion;

INSERT INTO usuario (id, nombre, correo, contrasena_hash, id_rol, activo) VALUES
    (1, 'Administrador General', 'admin@jeanalex.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 1, TRUE),
    (2, 'Coordinador Mesa de Ayuda', 'coordinador@jeanalex.com', '8c63a2fc2b14d8ae6f9d0bf2e2c4227ac2dc4bd84768e1259226b0c3d84f1c65', 2, TRUE),
    (3, 'Tecnico de Soporte', 'soporte@jeanalex.com', '8f51a8428ee0230c0521b3279c574a774ae0064a902f6deecc396315eec96577', 3, TRUE),
    (4, 'Agente Operativo', 'agente@jeanalex.com', 'ddabaa1ea73966696802178a1a075effa58c73b71c1a44a5de551f90ff76b988', 4, TRUE)
ON CONFLICT (id) DO UPDATE SET
    nombre = EXCLUDED.nombre,
    correo = EXCLUDED.correo,
    contrasena_hash = EXCLUDED.contrasena_hash,
    id_rol = EXCLUDED.id_rol,
    activo = EXCLUDED.activo;

INSERT INTO agente (id, id_usuario, nombre, correo, cargo, placa) VALUES
    (1, 4, 'Agente Operativo', 'agente@jeanalex.com', 'Usuario final', 'AG-001')
ON CONFLICT (id) DO UPDATE SET
    id_usuario = EXCLUDED.id_usuario,
    nombre = EXCLUDED.nombre,
    correo = EXCLUDED.correo,
    cargo = EXCLUDED.cargo,
    placa = EXCLUDED.placa;

INSERT INTO dispositivo (id, nombre, serial, marca, modelo, categoria, estado, ubicacion, fecha_ingreso) VALUES
    (1, 'Radio portatil digital', 'RAD-001', 'Motorola', 'APX 6000', 'Radio comunicaciones', 'Disponible', 'Bodega central', CURRENT_DATE - 90),
    (2, 'Radio portatil digital', 'RAD-002', 'Hytera', 'PD785', 'Radio comunicaciones', 'Asignado', 'Estacion norte', CURRENT_DATE - 75),
    (3, 'Camara corporal', 'CAM-001', 'Axon', 'Body 4', 'Camara corporal', 'Mantenimiento', 'Soporte tecnico', CURRENT_DATE - 60),
    (4, 'Camara corporal', 'CAM-002', 'Axon', 'Body 3', 'Camara corporal', 'Disponible', 'Bodega central', CURRENT_DATE - 58),
    (5, 'Tablet robusta para patrulla', 'TAB-001', 'Panasonic', 'Toughbook G2', 'Tablet robusta', 'Asignado', 'Patrulla vial 02', CURRENT_DATE - 45),
    (6, 'Tablet operativa', 'TAB-002', 'Samsung', 'Galaxy Tab Active4 Pro', 'Tablet robusta', 'Disponible', 'Bodega central', CURRENT_DATE - 40),
    (7, 'GPS vehicular', 'GPS-001', 'Garmin', 'fleet 790', 'GPS vehicular', 'Disponible', 'Bodega central', CURRENT_DATE - 35),
    (8, 'Alcoholimetro digital', 'ALC-001', 'Drager', 'Alcotest 6820', 'Alcoholimetro', 'Calibracion', 'Laboratorio tecnico', CURRENT_DATE - 30),
    (9, 'Dron de reconocimiento', 'DRN-001', 'DJI', 'Mavic 3 Enterprise', 'Dron', 'Disponible', 'Bodega central', CURRENT_DATE - 20),
    (10, 'Camara termica', 'TER-001', 'FLIR', 'E8 Pro', 'Camara termica', 'Disponible', 'Unidad de rescate', CURRENT_DATE - 15)
ON CONFLICT (id) DO UPDATE SET
    nombre = EXCLUDED.nombre,
    serial = EXCLUDED.serial,
    marca = EXCLUDED.marca,
    modelo = EXCLUDED.modelo,
    categoria = EXCLUDED.categoria,
    estado = EXCLUDED.estado,
    ubicacion = EXCLUDED.ubicacion,
    fecha_ingreso = EXCLUDED.fecha_ingreso;

INSERT INTO ans (id, tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas, descripcion, activo) VALUES
    (1, 'Incidente', 'Alta', 2, 8, 'Atencion prioritaria para incidentes criticos', TRUE),
    (2, 'Incidente', 'Media', 4, 24, 'Atencion estandar para incidentes', TRUE),
    (3, 'Requerimiento', 'Baja', 8, 48, 'Solicitudes no criticas', TRUE),
    (4, 'Requerimiento', 'Media', 6, 36, 'Requerimientos operativos con prioridad media', TRUE)
ON CONFLICT (id) DO UPDATE SET
    tipo_solicitud = EXCLUDED.tipo_solicitud,
    prioridad = EXCLUDED.prioridad,
    tiempo_respuesta_horas = EXCLUDED.tiempo_respuesta_horas,
    tiempo_solucion_horas = EXCLUDED.tiempo_solucion_horas,
    descripcion = EXCLUDED.descripcion,
    activo = EXCLUDED.activo;

INSERT INTO ticket (id, asunto, descripcion, tipo_solicitud, estado, prioridad, id_agente,
                    id_coordinador, id_tecnico, id_ans, evidencia, fecha_creacion, fecha_asignacion, fecha_cierre)
VALUES
    (1, 'Revision de radio digital', 'El equipo presenta intermitencia en comunicacion.', 'Incidente',
     'Abierto', 'Alta', 1, 2, 3, 1, 'Reporte inicial del usuario', CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP, NULL),
    (2, 'Calibracion de alcoholimetro ALC-001', 'El alcoholimetro requiere calibracion antes del operativo de fin de semana.', 'Requerimiento',
     'En proceso', 'Media', 1, 2, 3, 4, 'Orden de calibracion registrada', CURRENT_TIMESTAMP - INTERVAL '5 hours', CURRENT_TIMESTAMP - INTERVAL '4 hours', NULL),
    (3, 'Falla en camara corporal CAM-001', 'La camara no sincroniza evidencia al finalizar turno.', 'Incidente',
     'Cerrado', 'Alta', 1, 2, 3, 1, 'Firmware actualizado y prueba de carga correcta', CURRENT_TIMESTAMP - INTERVAL '6 hours', CURRENT_TIMESTAMP - INTERVAL '5 hours', CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    (4, 'Asignacion de tablet a patrulla vial', 'Solicitud de tablet robusta para consulta de comparendos y reportes en ruta.', 'Requerimiento',
     'Cerrado', 'Baja', 1, 2, 3, 3, 'Equipo entregado con acta digital', CURRENT_TIMESTAMP - INTERVAL '36 hours', CURRENT_TIMESTAMP - INTERVAL '30 hours', CURRENT_TIMESTAMP - INTERVAL '20 hours'),
    (5, 'Revision preventiva de dron DRN-001', 'Inspeccion de baterias, helices y camara antes de jornada de busqueda.', 'Requerimiento',
     'Pendiente', 'Baja', 1, 2, NULL, 3, 'Solicitud generada por coordinacion', CURRENT_TIMESTAMP - INTERVAL '2 hours', NULL, NULL)
ON CONFLICT (id) DO UPDATE SET
    asunto = EXCLUDED.asunto,
    descripcion = EXCLUDED.descripcion,
    tipo_solicitud = EXCLUDED.tipo_solicitud,
    estado = EXCLUDED.estado,
    prioridad = EXCLUDED.prioridad,
    id_agente = EXCLUDED.id_agente,
    id_coordinador = EXCLUDED.id_coordinador,
    id_tecnico = EXCLUDED.id_tecnico,
    id_ans = EXCLUDED.id_ans,
    evidencia = EXCLUDED.evidencia,
    fecha_creacion = EXCLUDED.fecha_creacion,
    fecha_asignacion = EXCLUDED.fecha_asignacion,
    fecha_cierre = EXCLUDED.fecha_cierre;

SELECT setval(pg_get_serial_sequence('rol', 'id'), COALESCE((SELECT MAX(id) FROM rol), 1), TRUE);
SELECT setval(pg_get_serial_sequence('usuario', 'id'), COALESCE((SELECT MAX(id) FROM usuario), 1), TRUE);
SELECT setval(pg_get_serial_sequence('agente', 'id'), COALESCE((SELECT MAX(id) FROM agente), 1), TRUE);
SELECT setval(pg_get_serial_sequence('dispositivo', 'id'), COALESCE((SELECT MAX(id) FROM dispositivo), 1), TRUE);
SELECT setval(pg_get_serial_sequence('ans', 'id'), COALESCE((SELECT MAX(id) FROM ans), 1), TRUE);
SELECT setval(pg_get_serial_sequence('ticket', 'id'), COALESCE((SELECT MAX(id) FROM ticket), 1), TRUE);
