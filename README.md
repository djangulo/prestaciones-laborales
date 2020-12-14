# Prestaciones laborales

Aplicación para cálculo de prestaciones laborales.

Requiere una instalación de MySQL o MariaDB.

## Migraciones

Las migraciones son manejadas con [golang-migrate](https://github.com/golang-migrate/migrate). Por supuesto, si no quiere usar la herramienta, sólo debe de correr el código sql en los archivos `*.up.sql`.

Descargue la version para su sistema operativo de [https://github.com/golang-migrate/migrate/releases](https://github.com/golang-migrate/migrate/releases), luego

Linux:

```sh
~$ export MARIADB_URL=mysql://root:abcd1234@tcp(localhost:3306)/db
~$ migrate -path=migraciones -database=$DB_URL up
```

Windows:

```cmd
C:\...\> SET DB_URL=mysql://root:abcd1234@tcp(localhost:3306)/db
C:\...\> migrate -path=migraciones -database=%DB_URL% up
```

