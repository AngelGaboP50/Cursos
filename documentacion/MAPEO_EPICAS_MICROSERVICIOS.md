# Mapeo de épicas a microservicios

| Microservicio | Puerto | Base | Épicas | Responsabilidad |
|---|---:|---|---|---|
| auth-user-service | 8081 | auth_user_db | EPIC01 + EPIC09 | Autenticación, JWT, perfil y usuarios |
| course-service | 8082 | course_db | EPIC02 + EPIC05 | Catálogo y gestión administrativa de cursos |
| enrollment-service | 8083 | enrollment_db | EPIC03 + EPIC07 | Inscripción, mis cursos y progreso |
| engagement-service | 8084 | engagement_db | EPIC04 + EPIC08 | Notificaciones, favoritos y reseñas |
| admin-operations-service | 8085 | operations_db | EPIC06 + EPIC10 | Reportes, auditoría y salud operacional |

Infraestructura adicional:

- `eureka-server` :8761 — registro y descubrimiento de servicios.
- `api-gateway` :8080 — único punto de entrada del frontend.
- Angular :4200 — cliente web.
- PostgreSQL :5432 — una sola instancia física con cinco bases lógicas.
