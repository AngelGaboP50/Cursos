# Validación realizada sobre la cirugía

Se realizaron comprobaciones estáticas antes de empaquetar la versión distribuida:

- 5 microservicios de negocio independientes con `pom.xml`, Maven Wrapper, aplicación Spring Boot, puerto y base propios.
- 5/5 clientes Eureka configurados y 5/5 rutas lógicas presentes en API Gateway.
- API Gateway ya no enruta a `lb://cursos-api`.
- 5/5 bases sin `FOREIGN KEY` hacia tablas de otro microservicio.
- Los endpoints que consume Angular conservaron sus URLs públicas.
- Los 5 servicios validan JWT localmente.
- Enrollment y Favorites guardan snapshot del curso para reducir dependencia en tiempo de lectura.
- Auditoría y envío de notificaciones desde Enrollment son best-effort para que una caída secundaria no revierta la operación principal.
- Se ejecutó una revisión sintáctica con `javac` sobre todos los archivos Java generados y no se detectaron errores de sintaxis.
- Se ejecutó una batería propia de 138 verificaciones estructurales finales; 138/138 pasaron.

## Limitación del entorno de construcción usado para esta entrega

No fue posible ejecutar `mvn test` dentro del entorno donde se generó el ZIP porque dicho entorno no puede resolver/descargar dependencias Maven externas y no trae Maven ni el repositorio local de Spring precargado. Por esa razón, el proyecto incluye `scripts/test-all.ps1` para ejecutar la compilación/pruebas reales en la máquina de desarrollo con Internet/dependencias disponibles.

Antes de la presentación, ejecuta `scripts/test-all.ps1`. Después levanta todos los procesos y ejecuta `scripts/smoke-test.ps1`; ese script recorre automáticamente registro, login, catálogo, inscripción, progreso, favoritos, reseña, notificaciones, health y, si está configurado, reportes/auditoría. Finalmente realiza la prueba manual de aislamiento indicada en `START_HERE.md`.
