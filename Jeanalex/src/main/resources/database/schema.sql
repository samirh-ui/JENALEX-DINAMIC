CREATE DATABASE IF NOT EXISTS jeanalex_dynamics;
USE jeanalex_dynamics;

CREATE TABLE IF NOT EXISTS rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES rol(id)
);

CREATE TABLE IF NOT EXISTS agente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    placa VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS dispositivo (
    id INT AUTO_INCREMENT PRIMARY KEY,
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
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_agente INT NOT NULL,
    id_dispositivo INT NOT NULL,
    id_responsable_entrega INT NOT NULL,
    fecha_asignacion DATETIME NOT NULL,
    fecha_devolucion DATETIME,
    observaciones TEXT,
    estado_prestamo VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_agente) REFERENCES agente(id),
    FOREIGN KEY (id_dispositivo) REFERENCES dispositivo(id),
    FOREIGN KEY (id_responsable_entrega) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS ans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_solicitud VARCHAR(100) NOT NULL,
    prioridad VARCHAR(50) NOT NULL,
    tiempo_respuesta_horas INT NOT NULL,
    tiempo_solucion_horas INT NOT NULL,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (tipo_solicitud, prioridad)
);

CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    asunto VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    tipo_solicitud VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    prioridad VARCHAR(50) NOT NULL,
    id_agente INT NOT NULL,
    id_coordinador INT,
    id_tecnico INT,
    id_ans INT NOT NULL,
    evidencia TEXT,
    fecha_creacion DATETIME NOT NULL,
    fecha_asignacion DATETIME,
    fecha_cierre DATETIME,
    FOREIGN KEY (id_agente) REFERENCES agente(id),
    FOREIGN KEY (id_coordinador) REFERENCES usuario(id),
    FOREIGN KEY (id_tecnico) REFERENCES usuario(id),
    FOREIGN KEY (id_ans) REFERENCES ans(id)
);

CREATE TABLE IF NOT EXISTS mensaje (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_remitente INT NOT NULL,
    id_destinatario INT NOT NULL,
    asunto VARCHAR(150) NOT NULL,
    contenido TEXT NOT NULL,
    fecha_envio DATETIME NOT NULL,
    leido BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (id_remitente) REFERENCES usuario(id),
    FOREIGN KEY (id_destinatario) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS mantenimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_dispositivo INT NOT NULL,
    id_tecnico INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_programada DATETIME NOT NULL,
    fecha_ejecucion DATETIME,
    observaciones TEXT,
    FOREIGN KEY (id_dispositivo) REFERENCES dispositivo(id),
    FOREIGN KEY (id_tecnico) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS historial_dispositivo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_dispositivo INT NOT NULL,
    id_usuario INT NOT NULL,
    accion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha DATETIME NOT NULL,
    FOREIGN KEY (id_dispositivo) REFERENCES dispositivo(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

INSERT IGNORE INTO rol (id, nombre, descripcion) VALUES
    (1, 'Administrador', 'Acceso total al sistema'),
    (2, 'Coordinador', 'Asigna solicitudes, hace seguimiento y genera reportes de ANS'),
    (3, 'Soporte Tecnico', 'Gestiona soporte, mantenimientos y estados de dispositivos'),
    (4, 'Agente', 'Consulta equipos asignados y registra solicitudes');

INSERT IGNORE INTO ans (id, tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas, descripcion) VALUES
    (1, 'Incidente', 'Alta', 2, 8, 'Atencion prioritaria para incidentes criticos'),
    (2, 'Incidente', 'Media', 4, 24, 'Atencion estandar para incidentes'),
    (3, 'Requerimiento', 'Baja', 8, 48, 'Solicitudes no criticas');
