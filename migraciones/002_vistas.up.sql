CREATE VIEW empleados_full AS
SELECT
    empleados.`id`,
    empleados.`id_empresa`,
    empleados.`nombre`,
    empleados.`apellidos`,
    empleados.`fecha_de_contratacion`,
    empleados.`salario_mensual`,
    empresas.`nombre` AS nombre_empresa,
    empresas.`rnc` AS rnc_empresa
FROM empleados
LEFT JOIN empresas ON empleados.`id_empresa` = empresas.`id`;
