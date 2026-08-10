# Infraestructura común e integración

Este paquete se sube **una sola vez** a la rama `integracion/microservicios`. No corresponde a un integrante específico.

Incluye:
- Eureka Server
- API Gateway
- Frontend Angular
- scripts de ejecución/pruebas
- SQL para crear las bases
- documentación compartida

Los cinco microservicios de negocio se integran después mediante Pull Requests individuales.

## Orden recomendado
1. Crear/actualizar `integracion/microservicios` con este paquete.
2. Cada integrante crea una rama nueva desde esa rama.
3. Cada integrante copia únicamente su microservicio.
4. Cada integrante hace commit y Pull Request hacia `integracion/microservicios`.
5. Integrar los cinco PR.
6. Ejecutar `scripts/test-all.ps1` y prueba manual.
7. Crear PR final `integracion/microservicios` → `main`.

Nunca subir `scripts/local-env.ps1` ni secretos reales.
