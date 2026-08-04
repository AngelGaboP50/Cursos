# Cursos API — EPIC01

Backend de autenticación para Cursos. La solución local conecta el cliente Angular
con una API Spring Boot y una única base PostgreSQL `login_db`.

## Requisitos

- Java 21.
- PostgreSQL accesible en `localhost:5432`.
- Base existente `login_db`.
- Node.js y npm para ejecutar el repositorio frontend.

## Configuración local

Spring Boot lee exclusivamente variables de entorno. `.env.example` documenta los
nombres, pero Spring no carga ese archivo automáticamente: configúralos en el
sistema, la terminal o el IDE. Nunca versiones un `.env` real.

| Variable | Uso | Valor de ejemplo seguro |
|---|---|---|
| `DB_URL` | JDBC de PostgreSQL | `jdbc:postgresql://localhost:5432/login_db` |
| `DB_USERNAME` | Usuario PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña PostgreSQL | marcador local, nunca versionado |
| `JWT_SECRET` | Firma HMAC del JWT | secreto local de al menos 32 caracteres |
| `JWT_EXPIRATION_MS` | Vigencia del token | `3600000` |
| `FRONTEND_URL` | Origen permitido por CORS | `http://localhost:4200` |
| `DEMO_DATA_ENABLED` | Activa el admin local | `false` |
| `DEMO_ADMIN_EMAIL` | Correo del admin local | `admin@demo.com` |
| `DEMO_ADMIN_PASSWORD` | Contraseña del admin local | marcador local, nunca versionado |

Con las variables definidas, inicia la API:

```powershell
.\mvnw.cmd spring-boot:run
```

Flyway ejecuta `V1__epic01_users_security.sql` de forma aditiva y Hibernate no crea
ni elimina tablas (`ddl-auto=none`). Swagger queda disponible localmente en
`http://localhost:8080/swagger-ui.html`.

## Autenticación y autorización

| Método y ruta | Resultado correcto | Acceso |
|---|---:|---|
| `POST /api/auth/register` | 201 | Público; crea `USER` |
| `POST /api/auth/login` | 200 | Público; entrega JWT Bearer |
| `POST /api/auth/logout` | 204 | Autenticado |
| `GET /api/account/me` | 200 | Autenticado |
| `GET /api/admin/security-check` | 200 | Solo `ADMIN` |

Los errores usan JSON sin trazas ni credenciales: `400` para datos o JSON inválidos,
`401` para autenticación ausente/inválida, `403` para rol insuficiente y `409` para
correo duplicado.

Flujo funcional:

1. Registro: el cliente envía nombre, correo y contraseña; la API normaliza el correo,
   valida la solicitud y persiste únicamente el hash BCrypt.
2. Login: la API valida las credenciales y devuelve un JWT junto con el usuario seguro.
3. Sesión: Angular agrega `Authorization: Bearer <token>` a llamadas de la API.
4. Logout: la API responde 204 y Angular elimina token y usuario del almacenamiento local.

## Datos de demostración

No existe una contraseña demo versionada. Para crear localmente `admin@demo.com`,
define `DEMO_DATA_ENABLED=true`, el correo deseado y una contraseña segura en el
entorno antes de iniciar la API. El inicializador es idempotente, usa BCrypt y no
modifica ninguna cuenta que ya exista. Para el flujo `USER`, registra una cuenta
desde Angular.

Desactiva siempre los datos demo y usa credenciales distintas en producción.

## Pruebas y paquete

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

Las pruebas usan H2 en modo PostgreSQL y mantienen desactivado el inicializador demo.
El esquema, las restricciones, el respaldo y la restauración se detallan en
`docs/EPIC01_DATABASE.md`.
