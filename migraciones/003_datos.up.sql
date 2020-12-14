INSERT INTO empresas (
    nombre, rnc
) VALUES 
    ('El Estudiante', '000000001'),
    ('UNICARIBE SA', '000000010'),
    ('APS', '000000011'),
    ('Whiteboard', '000000100'),
    ('Contoso', '000000101'),
    ('Fakesouth', '000000110'),
    ('Macrohard', '000000111'),
    ('Empanadas de Unicaribe', '000001000');

INSERT INTO empleados (
    id_empresa, nombre, apellidos,
    fecha_de_contratacion, salario_mensual
) VALUES
    (1, 'Ana', 'Rodríguez', '2017-03-01', 145000.25),
    (1, 'Denis', 'Angulo', '2018-05-04', 23129.72),
    (2, 'Soriano', 'Pelagio', '2014-01-01', 70000.00),
    (2, 'Jose', 'De Jesus', '2015-01-01', 70000.00),
    (2, 'Richard', 'Polanco', '2016-01-01', 70000.00),
    (2, 'Juan', 'Peguero', '2017-01-01', 70000.00),
    (3, 'Juan', 'Perez', '2011-05-04', 23129.72),
    (3, 'Andres', 'Mendez', '2015-01-03', 23129.72),
    (3, 'Miguel', 'Rodríguez', '2019-05-04', 44000.00),
    (3, 'Antonio', 'Santana', '2012-06-23', 51000.00),
    (3, 'Emilia', 'Carvajal', '2014-05-04', 36000.00),
    (4, 'John', 'Smith', '2004-01-01', 89000.00),
    (4, 'Mike', 'Shoemaker', '2010-12-01', 70000.00),
    (4, 'Jane', 'Thompson', '2018-05-04', 94000.00),
    (4, 'Jose', 'Caraballo', '2013-05-04', 79000.00),
    (4, 'Kelly', 'Clarke', '2009-03-16', 90000.00),
    (5, 'Brett', 'Harmon', '2011-01-01', 80000.00),
    (5, 'Jeff', 'Winger', '2014-05-04', 68000.00),
    (5, 'Troy', 'Barnes', '2011-03-03', 54000.00),
    (5, 'Britta', 'Perry', '2015-02-01', 39000.00),
    (5, 'Pierce', 'Hawthorne', '2004-01-01', 130000.00);
    