# CursosFront — 10 épicas

Cliente Angular 21 para la plataforma Cursos. Consume la API real en
`http://localhost:8080/api` a través de Spring Cloud Gateway; no usa catálogos ni
sesiones simuladas. El Gateway descubre `cursos-api` mediante Eureka.

## Ejecutar

```powershell
npm ci
npm start
```

Abre `http://localhost:4200`. Antes deben estar activos Eureka (`8761`), el servicio
de dominio (`8081`) y el API Gateway (`8080`).

| Ruta | Función | Acceso |
|---|---|---|
| `/` y `/courses/:id` | catálogo, detalle y reseñas | público |
| `/login`, `/register` | autenticación | público |
| `/my-courses` | inscripciones y progreso | autenticado |
| `/notifications`, `/favorites`, `/account` | avisos, favoritos y perfil | autenticado |
| `/admin` | indicadores | ADMIN |
| `/admin/courses` | gestión de cursos | ADMIN |
| `/admin/reports` | reportes CSV | ADMIN |
| `/admin/users`, `/admin/audit` | usuarios y operación | ADMIN |

El interceptor adjunta JWT sólo a llamadas de la API. Los guards protegen la
navegación y Spring Security vuelve a validar la autorización en el servidor.

## Verificar

```powershell
npm test -- --watch=false
npm run build
```

La sesión local se elimina al cerrar sesión. La contraseña nunca se persiste.
