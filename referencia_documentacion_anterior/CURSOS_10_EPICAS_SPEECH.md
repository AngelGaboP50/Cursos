# Guion de exposición — 10 épicas, 5 integrantes

Distribución propuesta para la defensa final: dos épicas completas por integrante,
incluyendo sus historias de usuario. Esta es una asignación de presentación y
responsabilidad técnica; no pretende reescribir la autoría histórica de commits.

## Damian Anwar Mata Nevarez — EPIC01 y EPIC02

### EPIC01: autenticación y seguridad — HU01, HU02 y HU03

> “Yo presento la base de seguridad. En HU01 un visitante registra nombre, correo y
> contraseña. Spring valida los campos, normaliza el correo, impide duplicados y
> guarda únicamente un hash BCrypt; la respuesta nunca incluye la contraseña. En
> HU02 el login valida credenciales y entrega un JWT con expiración. Angular guarda
> temporalmente la sesión y un interceptor añade el Bearer sólo a nuestra API. El
> logout responde 204 y limpia la sesión local. En HU03 aplicamos defensa en dos
> capas: guards de Angular para experiencia de usuario y Spring Security como
> autoridad real. Lo comprobamos con USER recibiendo 403 y ADMIN recibiendo 200.”

Demostración: registro, login, Perfil, intento USER a `/api/admin/security-check` y
logout. Pregunta probable: “¿por qué un guard no basta?” Respuesta: porque el cliente
puede alterarse; la autorización decisiva siempre está en el servidor.

### EPIC02: catálogo de cursos — HU04, HU05 y HU06

> “Mi segunda épica convierte el sistema en una plataforma útil. HU04 presenta sólo
> cursos publicados y permite buscar por título o descripción. HU05 filtra por una
> categoría obtenida de PostgreSQL, sin una lista duplicada en Angular. HU06 abre el
> detalle con nivel, precio, fechas, imagen y reseñas. El catálogo es público para
> que alguien explore antes de crear cuenta, mientras las acciones personales sí
> exigen JWT. Flyway aporta seis cursos seed idempotentes para que la demo tenga datos
> desde el primer arranque.”

Demostración: buscar “Java”, cambiar categoría y abrir detalle.

## Diego Tristan Limón Hernández — EPIC03 y EPIC04

### EPIC03: inscripciones — HU07, HU08 y HU09

> “En HU07 una persona autenticada se inscribe en un curso publicado. La base tiene
> una restricción única usuario–curso, y el servicio también detecta duplicados con
> un 409 comprensible. Si una inscripción estaba cancelada se reactiva sin crear una
> relación repetida. HU08 muestra Mis cursos consultando sólo al usuario del JWT; el
> id nunca se confía al navegador. HU09 permite cancelar de forma lógica, conservando
> el historial. Así protegemos integridad y trazabilidad.”

Demostración: Inscribirme, Mis cursos y Cancelar inscripción.

### EPIC04: notificaciones — HU10 y HU11

> “HU10 crea notificaciones reales cuando ocurre algo relevante, por ejemplo una
> inscripción o completar un curso. No usamos mensajes fijos en el frontend: Angular
> consulta la tabla `notifications` del usuario actual. En HU11 cada aviso puede
> marcarse leído y también existe la operación leer todos. El repositorio siempre
> filtra por `user_id`, por lo que conocer el id de otra notificación no permite
> verla o modificarla.”

Demostración: generar una inscripción, abrir Avisos y marcar como leído.

## Luis Felipe Montes Velázquez — EPIC05 y EPIC06

### EPIC05: administración de cursos — HU12, HU13 y HU14

> “Esta épica está reservada a ADMIN. HU12 crea cursos con validaciones de título,
> descripción, categoría, nivel, precio, estado y fechas. HU13 reutiliza el mismo
> contrato para editar o publicar y rechaza una fecha final anterior a la inicial.
> HU14 no borra físicamente: cambia el estado a INACTIVE. El curso deja de aparecer
> en el catálogo público, pero conserva inscripciones, reseñas y auditoría. Cada
> mutación registra actor, acción, entidad y detalle.”

Demostración: crear un borrador, editarlo, publicarlo y desactivarlo.

### EPIC06: reportes — HU15 y HU16

> “HU15 resume usuarios, cursos, publicados, inscripciones activas y completadas,
> avisos y reseñas. Las cifras se calculan en el backend directamente sobre
> PostgreSQL. HU16 exporta cursos e inscripciones en CSV UTF-8 con BOM para abrirlo
> correctamente en Excel. Nunca exportamos contraseñas ni hashes. Spring exige ADMIN
> para el resumen y los archivos, y Angular descarga el `Blob` con un nombre claro.”

Demostración: panel de métricas y descarga de ambos CSV.

## Angel Gabriel Paredes Aviles — EPIC07 y EPIC08

### EPIC07: progreso — HU17 y HU18

> “HU17 permite registrar progreso entre cero y cien por ciento en una inscripción
> propia. La validación existe en Angular para interacción y en Bean Validation para
> seguridad. No se permite avanzar una inscripción cancelada. HU18 cambia
> automáticamente el estado a COMPLETED cuando llega a cien y crea una notificación
> una sola vez. El tablero Mis cursos combina curso, estado y porcentaje para que la
> persona entienda su ruta de aprendizaje.”

Demostración: mover el progreso al 100 % y mostrar COMPLETED y el nuevo aviso.

### EPIC08: favoritos y reseñas — HU19 y HU20

> “HU19 crea una lista de favoritos con relación única usuario–curso; se puede agregar
> y quitar sin afectar el catálogo. HU20 permite una reseña por usuario y curso. Para
> evitar opiniones no verificadas, el servicio exige una inscripción activa o
> completada. Repetir la operación actualiza la reseña existente. El detalle público
> muestra promedio, cantidad, autor, puntuación y comentario, pero no datos sensibles.”

Demostración: Guardar favorito, abrir Favoritos y publicar una reseña de cinco puntos.

## Joshua Emmanuel Salinas Vázquez — EPIC09 y EPIC10

### EPIC09: perfil y administración de usuarios — HU21 y HU22

> “HU21 permite que la persona actualice su nombre, pero el endpoint no acepta rol,
> contraseña ni estado; así evitamos escalación de privilegios por mass assignment.
> HU22 da al ADMIN una lista segura de cuentas y permite habilitar o deshabilitar sin
> borrar datos. Una regla adicional impide que el administrador desactive su propia
> cuenta. Las respuestas usan `UserResponse`, que deliberadamente no contiene el
> hash.”

Demostración: editar Perfil y cambiar el estado de una cuenta de prueba.

### EPIC10: auditoría y operación — HU23 y HU24

> “HU23 registra las operaciones de negocio importantes: cursos, inscripciones,
> progreso y estados de usuario. El ADMIN consulta los cien eventos más recientes con
> actor y fecha. HU24 expone una salud pública mínima: la aplicación ejecuta `select
> 1` y responde si PostgreSQL está UP, sin revelar credenciales o detalles internos.
> Flyway deja el esquema en V2 y un respaldo previo permite recuperación. Para la
> operación distribuida agregamos Eureka Server: `CURSOS-API` y `API-GATEWAY` se
> registran como instancias `UP`. Angular sólo conoce el Gateway; éste resuelve
> `lb://cursos-api` mediante Eureka y conserva JWT y CORS. La prueba final confirmó
> Angular, Gateway, Eureka, Spring Boot y PostgreSQL como un solo recorrido.”

Demostración: abrir Auditoría y salud, señalar DB `UP` y un evento creado en vivo.

## Cierre conjunto

> “El resultado no son diez pantallas aisladas. Es un flujo conectado: seguridad
> identifica al actor; catálogo ofrece el contenido; inscripción, progreso,
> notificaciones, favoritos y reseñas construyen la experiencia; administración,
> reportes, usuarios y auditoría permiten operar el sistema. Las pruebas automáticas
> quedaron en 21 para el servicio de dominio, una para Eureka, una para el Gateway y
> 4 para frontend: 27 en total, además de compilación Angular y una prueba manual
> completa por el Gateway sobre `login_db`.”
