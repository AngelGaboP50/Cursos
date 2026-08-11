# Arquitectura y defensa

## Separación por dominio
- Auth/User: identidad, login, JWT, perfil y administración de usuarios.
- Course: catálogo y CRUD administrativo de cursos.
- Enrollment: inscripción y progreso.
- Engagement: notificaciones, favoritos y reseñas.
- Admin Operations: auditoría y reportes agregados.

La separación busca alta cohesión y bajo acoplamiento. El Gateway es el punto único de entrada y Eureka resuelve cada nombre lógico (`lb://...`).

## Seguridad
JWT se firma en Auth/User. Todos los servicios comparten únicamente la clave de validación mediante variable de entorno, por lo que validan firma/expiración/rol localmente. Esto evita que cada petición dependa de Auth/User. Las rutas `/internal/**` requieren además `X-Internal-Secret`.

Trade-off deliberado: si un administrador deshabilita una cuenta, un JWT ya emitido puede seguir siendo válido hasta expirar en servicios que no consultan Auth/User. Para una arquitectura de producción se agregaría revocación distribuida/cache de tokens. Para esta práctica se prioriza aislamiento de fallos y JWT de corta duración.

## Datos
Se usa database-per-service dentro de una misma instancia PostgreSQL. No existen foreign keys entre bases. Se conservan IDs externos y, donde ayuda a resiliencia, snapshots de datos descriptivos.

## Fallos
- Engagement caído: fallan favoritos/reseñas/notificaciones; lo demás continúa.
- Operations caído: auditoría/reportes no disponibles; crear cursos/inscribirse continúa porque auditoría es best-effort.
- Auth caído: no hay nuevos login/registro; sesiones con JWT vigente pueden seguir consumiendo otros servicios.
- Course caído: catálogo/CRUD no disponibles; inscripciones nuevas requieren Course, pero inscripciones ya existentes/progreso pueden consultarse por snapshot.
