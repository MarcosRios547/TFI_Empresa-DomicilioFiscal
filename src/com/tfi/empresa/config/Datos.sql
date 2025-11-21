USE empresas;

INSERT INTO domicilio_fiscal (calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES ('Av. Siempre Viva', 742, 'Springfield', 'Buenos Aires', '1000', 'Argentina');

INSERT INTO empresa (razon_social, cuit, actividad_principal, email, domicilio_id)
VALUES ('Simpsons S.A.', '30-12345678-9', 'Entretenimiento', 'info@simpsons.com', 1);
