CREATE TABLE empresas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE NOT NULL,
    rnc VARCHAR(9) UNIQUE NOT NULL
);

CREATE TABLE empleados (
    id SERIAL PRIMARY KEY,
    id_empresa BIGINT UNSIGNED NOT NULL,
    nombre VARCHAR(63) NOT NULL,
    apellidos VARCHAR(127) NOT NULL,
    fecha_de_contratacion TIMESTAMP NOT NULL,
    salario_mensual DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_empresa) REFERENCES empresas(id)
);
