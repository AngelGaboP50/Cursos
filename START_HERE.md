# CURSOS - versión microservicios

Esta versión separa el antiguo `cursos-api` en cinco procesos Spring Boot independientes. Angular conserva los mismos endpoints públicos y habla únicamente con el API Gateway.

## Arquitectura

Angular :4200 -> API Gateway :8080 -> Eureka discovery ->
- auth-user-service :8081 (EPIC01 + EPIC09)
- course-service :8082 (EPIC02 + EPIC05)
- enrollment-service :8083 (EPIC03 + EPIC07)
- engagement-service :8084 (EPIC04 + EPIC08)
- admin-operations-service :8085 (EPIC06 + EPIC10)

Cada microservicio tiene su propia base PostgreSQL. Todos validan JWT localmente con la misma firma, así que un JWT ya emitido no requiere consultar al servicio de autenticación en cada petición.

## Preparación
1. Conserva `login_db` como respaldo.
2. Crea las cinco bases indicadas en `database/README.md`.
3. Copia `scripts/local-env.example.ps1` a `scripts/local-env.ps1` y edita DB_PASSWORD/JWT_SECRET/INTERNAL_SECRET.
4. En CADA terminal PowerShell, primero ejecuta: `. .\scripts\local-env.ps1` desde la raíz del proyecto.

## Arranque, una terminal por proceso
Desde la raíz, abre terminales separadas:

```powershell
# T1
. .\scripts\local-env.ps1
cd backend\eureka-server
.\mvnw.cmd spring-boot:run

# T2
. .\scripts\local-env.ps1
cd backend\api-gateway
.\mvnw.cmd spring-boot:run

# T3
. .\scripts\local-env.ps1
cd backend\auth-user-service
.\mvnw.cmd spring-boot:run

# T4
. .\scripts\local-env.ps1
cd backend\course-service
.\mvnw.cmd spring-boot:run

# T5
. .\scripts\local-env.ps1
cd backend\enrollment-service
.\mvnw.cmd spring-boot:run

# T6
. .\scripts\local-env.ps1
cd backend\engagement-service
.\mvnw.cmd spring-boot:run

# T7
. .\scripts\local-env.ps1
cd backend\admin-operations-service
.\mvnw.cmd spring-boot:run

# T8
cd frontend
npm ci
npm start
```

Visita `http://localhost:8761`. Deben verse seis aplicaciones registradas: API-GATEWAY y los cinco servicios.

## Swagger directo por microservicio
- 8081/swagger-ui.html
- 8082/swagger-ui.html
- 8083/swagger-ui.html
- 8084/swagger-ui.html
- 8085/swagger-ui.html


## Smoke test automático
Cuando los ocho procesos estén levantados, desde la raíz puedes ejecutar:

```powershell
. .\scripts\local-env.ps1
.\scripts\smoke-test.ps1
```

El script crea un usuario temporal y recorre registro, login, catálogo, inscripción, progreso, favoritos, reseña, notificaciones, health y, si el administrador demo está configurado, reportes/auditoría.

## Prueba de aislamiento para la exposición
Con todo levantado e iniciada sesión, detén con Ctrl+C `engagement-service`. Login ya emitido, catálogo, inscripciones, progreso y gestión de cursos siguen perteneciendo a otros procesos. Favoritos/reseñas/notificaciones fallarán. Vuelve a levantar engagement y regresan.

Notas de diseño: enrollment y favorites guardan snapshots del curso para poder listar datos ya existentes aunque `course-service` esté temporalmente fuera. Las notificaciones y auditoría se envían con llamadas best-effort: si esos servicios están caídos, la operación principal no se revierte.
