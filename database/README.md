# Bases de datos
La versión de microservicios usa cinco bases lógicas en la MISMA instalación PostgreSQL.
No borres `login_db`; queda como respaldo de la versión anterior.

1. Conéctate en pgAdmin/DBeaver a la base `postgres`.
2. Crea: `auth_user_db`, `course_db`, `enrollment_db`, `engagement_db`, `operations_db`.
3. No crees tablas a mano. Flyway crea las tablas cuando arranca cada servicio.

Cada servicio es dueño de sus tablas y no existen foreign keys entre bases. Las referencias a otros dominios se guardan como IDs y snapshots.
