DROP DATABASE IF EXISTS empresas;
CREATE DATABASE IF NOT EXISTS empresas;
USE empresas;

CREATE TABLE domicilio_fiscal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    calle VARCHAR(100) NOT NULL,
    numero INT,
    ciudad VARCHAR(80) NOT NULL,
    provincia VARCHAR(80) NOT NULL,
    codigo_postal VARCHAR(10),
    pais VARCHAR(80) NOT NULL
);

CREATE TABLE empresa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    razon_social VARCHAR(120) NOT NULL,
    cuit VARCHAR(13) NOT NULL UNIQUE,
    actividad_principal VARCHAR(80),
    email VARCHAR(120),
    domicilio_id BIGINT UNIQUE,
    FOREIGN KEY (domicilio_id) REFERENCES domicilio_fiscal(id) ON DELETE CASCADE
);
