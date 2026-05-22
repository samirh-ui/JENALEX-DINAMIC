# Jeanalex Dynamics

Aplicacion de gestion de inventario, asignaciones, mesa de ayuda, mantenimientos, mensajes, ANS y reportes para equipos de dotacion policial.

La version principal del proyecto esta en `Jeanalex/`. La carpeta `demo/` corresponde a una version inicial/simple y no representa el alcance descrito en el documento definitivo.

## Relacion con el documento

El PDF `ProyectoIntegrador_JeanalexDynamics_DEFINITIVO.pdf` plantea una solucion para centralizar inventario, asignaciones, soporte tecnico, mantenimientos, reportes y cumplimiento de ANS.

El proyecto actual cubre estos puntos:

| Requerimiento del documento | Estado en el codigo |
| --- | --- |
| R01 Autenticacion de usuarios | Implementado con login y roles |
| R02 Gestion de usuarios | Implementado |
| R03 Registro de agentes | Implementado |
| R04 Registro de dispositivos | Implementado |
| R05 Consulta de inventario | Implementado con listado y filtro |
| R06 Asignacion de dispositivos | Implementado |
| R07 Registro de devolucion | Implementado |
| R08 Estados de dispositivos | Implementado |
| R09 Mensajes internos | Implementado |
| R10 Historial de asignaciones | Implementado |
| R11 Asignacion de solicitudes a tecnicos | Implementado |
| R12 Seguimiento de solicitudes | Implementado |
| R13 Reportes de cumplimiento | Implementado con indicadores y exportacion PDF |
| R14 Gestion de ANS | Implementado |

Aclaraciones frente al documento:

- El documento se interpreta como una aplicacion de gestion; la implementacion actual es Java Swing de escritorio.
- Los reportes se exportan en PDF desde la opcion `Reportes e Indicadores`.
- El dashboard usa datos reales consultados desde PostgreSQL.
- Las pruebas automatizadas no son obligatorias para ejecutar la aplicacion, pero son recomendables si el proyecto seguira creciendo o si el jurado pide evidencia tecnica adicional.

## Base de datos

El proyecto usa PostgreSQL. El esquema compatible con el codigo esta en:

```text
Jeanalex/src/main/resources/database/schema.sql
```

Ejecuta ese archivo conectado a la base de datos `jeanalex_dynamics` o a la base configurada en `Jeanalex/config.properties`.

Usuarios iniciales incluidos por el esquema:

| Rol | Correo | Contrasena |
| --- | --- | --- |
| Administrador | `admin@jeanalex.com` | `admin123` |
| Coordinador | `coordinador@jeanalex.com` | `coord123` |
| Soporte Tecnico | `soporte@jeanalex.com` | `soporte123` |
| Agente | `agente@jeanalex.com` | `agente123` |

## Compilar

Requisitos:

- Java 17
- Maven 3.x
- PostgreSQL accesible con las credenciales de `Jeanalex/config.properties`

Comando:

```bash
cd Jeanalex
mvn -q -DskipTests compile
```

## Ejecutar

Desde la carpeta `Jeanalex`:

```bash
mvn exec:java -Dexec.mainClass=com.example.Main
```

Si tu Maven no tiene configurado el plugin de ejecucion, tambien puedes ejecutar desde tu IDE abriendo `Jeanalex/src/main/java/com/example/Main.java`.
