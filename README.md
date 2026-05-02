# Jeanalex Dynamics


## Guia principal

El documento define el alcance funcional del sistema. Para evitar inconsistencias, el codigo debe mantenerse orientado a estos modulos:

1. Autenticacion y gestion de roles.
2. Inventario de equipos electronicos.
3. Asignaciones y control de responsables.
4. Mesa de ayuda.
5. Coordinacion y seguimiento de solicitudes.
6. Planificacion y control de mantenimientos.
7. Reportes, indicadores y ANS.

## Estructura MVC + BD + POO

El segundo avance contiene codigo fuente organizado por responsabilidades:

- `model`: clases POO que representan el diagrama de clases.
- `dao`: operaciones CRUD y acceso a base de datos mediante JDBC.
- `database`: configuracion de conexion a la base de datos.
- `controller`: reglas de flujo entre la vista y los datos.
- `view`: interfaz grafica con Swing/JFrame y menu de consola auxiliar.
- `src/main/resources/database/schema.sql`: script SQL de creacion de la base de datos.

## Modelos actuales

- `Usuario`: usuarios con correo, contrasena cifrada, estado activo y rol asociado.
- `Rol`: roles definidos por el documento: Administrador, Coordinador, Soporte Tecnico y Agente.
- `Agente`: informacion del agente responsable de dispositivos.
- `Dispositivo`: inventario de equipos electronicos.
- `Asignacion`: entregas, devoluciones, responsables y observaciones de equipos.
- `Ticket`: solicitudes de mesa de ayuda asignadas por coordinador a tecnico.
- `Mensaje`: mensajes informativos enviados a agentes o tecnicos.
- `Mantenimiento`: mantenimientos preventivos y correctivos.
- `Ans`: acuerdos de nivel de servicio por tipo de solicitud y prioridad.
- `ReporteCumplimiento`: metricas de cumplimiento de ANS por tecnico, tipo y periodo.

## Requisitos

- Java 17
- Maven 3.x
- MySQL o MariaDB

## Comandos utiles

Desde `JENALEX-DINAMIC/Jeanalex`:

```bash
mvn clean compile
mvn test
mvn package
```

## Base de datos

Crear la base de datos ejecutando el script:

```bash
mysql -u root -p < src/main/resources/database/schema.sql
```

La conexion usa estos valores por defecto:

- URL: `jdbc:mysql://localhost:3306/jeanalex_dynamics`
- Usuario: `root`
- Contrasena: vacia

Tambien se puede configurar con variables de entorno:

- `JEANALEX_DB_URL`
- `JEANALEX_DB_USER`
- `JEANALEX_DB_PASSWORD`

## Ejecucion

Despues de crear la base de datos:

```bash
mvn exec:java -Dexec.mainClass="com.example.Main"
```

La aplicacion abre una ventana Swing/JFrame con pestanas para:

- Registrar y listar usuarios.
- Registrar y listar dispositivos.
- Registrar, listar, asignar y cerrar tickets.

Estas acciones usan controladores y DAOs conectados a la base de datos, manteniendo la estructura MVC solicitada.
