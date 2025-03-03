-- Cambia RESULTADO POR EFECTO EN HISTORICOS, Ya está controlado en Pantalla 
-- update osmosys.areas a set area_type = 'RESULTADO' where a.area_type = 'EFECTO'; 
ALTER TABLE osmosys.statements ALTER COLUMN situation_id DROP NOT NULL;

-- Inserta Punto focal dentro de la nueva estructura
-- SOLO IGRACIÔN
-- IMPORTANTE EJECUTAR UNA SOLA VEZ EN LA MIGRACIÔN 
INSERT INTO osmosys.focal_point_assignation (id, main_focal_pointer, state, project_id, focal_pointer_id)
select nextval('osmosys.focal_point_assignation_id_seq'::regclass), false, 'ACTIVO', id, focal_point_id
FROM osmosys.projects
WHERE focal_point_id IS NOT NULL; 

-- Inserta Core Indicators 
delete from osmosys.core_indicators;
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(55, 'OA01', '1.1.1', 'Número de personas registradas de manera individual', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(56, 'OA01', '1.2.1', 'Número de personas apoyadas para obtener documentación de estado civil, identidad o estatus legal', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(60, 'OA04', '4.1.1', 'Número de personas que se beneficiaron de programas especializados en violencia de género', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(61, 'OA05', '5.1.1', 'Número de niños y cuidadores que recibieron servicios de protección infantil', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(62, 'OA06', '6.1.1', 'Número de personas que recibieron asistencia legal', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(63, 'OA07', '7.1.1', 'Número de personas consultadas a través de Diagnósticos Participativos', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(64, 'OA07', '7.2.1 ', 'Número de personas que utilizaron mecanismos de retroalimentación y respuesta apoyados por ACNUR para expresar sus necesidades/preocupaciones/comentarios', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(65, 'OA07', '7.3.1', 'Número de personas que recibieron servicios de protección', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(66, 'OA08', '8.1.1', 'Número de personas que recibieron asistencia en efectivo', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(67, 'OA08', '8.2.1', 'Número de personas que recibieron artículos no alimentarios', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(68, 'OA08', '8.3.1', 'Número de personas apoyadas con artículos de cocina mejorados', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(69, 'OA09', '9.1.1', 'Número de personas que recibieron asistencia para albergue y vivienda', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(70, 'OA10', '10.1.1', 'Número de consultas individuales en servicios de atención médica apoyados por ACNUR', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(71, 'OA10', '10.2.1', 'Número de consultas en servicios de salud mental y apoyo psicosocial apoyados por ACNUR', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(72, 'OA11', '11.1.1', 'Número de personas que se beneficiaron de programas educativos', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(73, 'OA12', '12.1.1', 'Número de personas apoyadas con acceso a servicios de agua y/o saneamiento', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(74, 'OA13', '13.1.1', 'Número de personas que se beneficiaron de intervenciones de medios de vida e inclusión económica', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(75, 'OA14', '14.1.1', 'Número de personas que recibieron asesoramiento y/o información sobre repatriación voluntaria', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');
INSERT INTO osmosys.core_indicators
(id, area_code, code, description, frecuency, guiadance, measure_type, state)
VALUES(78, 'OA16', '16.2.1', 'Número de personas apoyadas por ACNUR para adquirir nacionalidad, estatus de residencia permanente o acceder a procedimientos de naturalización', 'MENSUAL', 'Guidance', 'NUMERO', 'ACTIVO');


-- Roles
UPDATE app_config.menu_items_roles_assigments SET "role"='ADMINISTRADOR_LOCAL' where "role"='ADMINISTRADOR';
--script cambio y agregación de roles
UPDATE "security"."role" SET tipo_rol='ADMINISTRADOR_LOCAL' where tipo_rol='ADMINISTRADOR';
INSERT INTO "security"."role"("id", "tipo_rol", "state") VALUES (10, 'ADMINISTRADOR_REGIONAL', 'ACTIVO');

SELECT setval('app_config.app_config_id_seq', (SELECT MAX(id) FROM app_config.app_config));
-- Cambio de Configuraciones
INSERT INTO app_config.app_config ( clave, descripcion, nombre, valor)
SELECT  'TIME_ZONE', 'Zona horaria de la operación', 'Configuración de Zona horaria para la operación', 'America/Guayaquil'
WHERE NOT EXISTS (
    SELECT 1 FROM app_config.app_config WHERE clave = 'TIME_ZONE'
);

INSERT INTO app_config.app_config ( clave, descripcion, nombre, valor)
SELECT  'POWER_BI_USERNAME',	'Nombre de usuario para la conexión de Power Bi','Configuración para la conexión con PowerBi. Nombre de usuario','osmosys_im'
WHERE NOT EXISTS (
    SELECT 1 FROM app_config.app_config WHERE clave = 'POWER_BI_USERNAME'
);

INSERT INTO app_config.app_config ( clave, descripcion, nombre, valor)
SELECT  'POWER_BI_PASSWORD',	'Contraseña para la conexión de Power Bi',	'Configuración para la conexión con PowerBi. Contraseña',	'A1a2a3a4$'
WHERE NOT EXISTS (
    SELECT 1 FROM app_config.app_config WHERE clave = 'POWER_BI_PASSWORD'
);

UPDATE app_config.app_config
SET valor = 'email-smtp.eu-west-1.amazonaws.com'
WHERE clave = 'EMAIL_SMTP_HOST';

UPDATE app_config.app_config
SET valor = 'AKIAUDY3XMXWSF3ZIPHO'
WHERE clave = 'EMAIL_USERNAME';

UPDATE app_config.app_config
SET valor = 'BFCDouneGkAmnh2iwQtJG+2PVBC6tl6Ax5O69+xD2ba6'
WHERE clave = 'EMAIL_PASSOWRD';


UPDATE app_config.app_config
SET valor = 'no-reply@osmosys.unhcr.org'
WHERE clave = 'EMAIL_ADDRES';
