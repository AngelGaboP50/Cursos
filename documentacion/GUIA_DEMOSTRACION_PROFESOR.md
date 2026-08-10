# Guía de demostración frente al profesor

## Antes de iniciar
1. Levanta PostgreSQL y verifica que existan las cinco bases.
2. Inicia Eureka y abre `http://localhost:8761`.
3. Inicia Gateway y los cinco microservicios, cada uno en una terminal distinta.
4. Comprueba en Eureka que todos aparezcan `UP`.
5. Inicia Angular.

## Flujo funcional sugerido
1. Registrar un usuario.
2. Iniciar sesión y explicar que Auth/User emite un JWT.
3. Abrir catálogo y detalle de un curso (Course Service).
4. Inscribirse y modificar progreso (Enrollment Service).
5. Guardar favorito, consultar notificaciones y publicar reseña (Engagement Service).
6. Iniciar sesión como administrador.
7. Crear/editar/desactivar un curso (Course Service).
8. Consultar usuarios (Auth/User).
9. Consultar reportes y auditoría (Admin Operations).

## Demostración de aislamiento de fallos
La prueba más clara es detener `engagement-service` con `Ctrl+C`.

Mientras está detenido:
- catálogo: debe continuar;
- sesión JWT ya iniciada: debe continuar;
- inscripciones existentes y progreso: deben continuar;
- gestión de cursos: debe continuar;
- favoritos, notificaciones y reseñas: no estarán disponibles.

Después vuelve a ejecutar `engagement-service`; Eureka lo registra de nuevo y esas funcionalidades regresan.

### Otra prueba útil
Detén `admin-operations-service` y crea/edita un curso o actualiza progreso. La operación principal debe continuar porque la auditoría está implementada como best-effort. Los reportes/auditoría sí quedan temporalmente fuera.

## Respuesta corta a “¿por qué esto sí son microservicios?”
Cada dominio se ejecuta en un proceso Spring Boot independiente, tiene puerto y base de datos propios, se registra en Eureka y se consume mediante el API Gateway. Los servicios no comparten entidades JPA ni foreign keys entre sus bases; cuando un dominio necesita información de otro, se comunica mediante HTTP usando el nombre lógico registrado en Eureka.
