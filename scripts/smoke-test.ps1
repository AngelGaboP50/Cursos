$ErrorActionPreference = 'Stop'

$base = 'http://localhost:8080/api'
$stamp = [DateTimeOffset]::UtcNow.ToUnixTimeSeconds()
$email = "smoke+$stamp@example.com"
$password = 'SmokeTest123'

function Step($message) {
    Write-Host "`n== $message ==" -ForegroundColor Cyan
}

function Headers($token) {
    return @{ Authorization = "Bearer $token" }
}

Step 'Registro de usuario'
$user = Invoke-RestMethod -Method Post -Uri "$base/auth/register" -ContentType 'application/json' -Body (@{
    name = 'Smoke Test'
    email = $email
    password = $password
} | ConvertTo-Json)
Write-Host "Usuario creado: $($user.email)"

Step 'Login y JWT'
$login = Invoke-RestMethod -Method Post -Uri "$base/auth/login" -ContentType 'application/json' -Body (@{
    email = $email
    password = $password
} | ConvertTo-Json)
$token = $login.token
if ([string]::IsNullOrWhiteSpace($token)) { throw 'El login no devolvio JWT.' }
$headers = Headers $token
Write-Host 'JWT recibido.'

Step 'Catalogo de cursos'
$courses = @(Invoke-RestMethod -Method Get -Uri "$base/courses")
if ($courses.Count -lt 1) { throw 'No hay cursos publicados para probar.' }
$course = $courses[0]
Write-Host "Curso seleccionado: #$($course.id) $($course.title)"

Step 'Inscripcion'
$enrollment = Invoke-RestMethod -Method Post -Uri "$base/enrollments/courses/$($course.id)" -Headers $headers -ContentType 'application/json' -Body '{}'
Write-Host "Inscripcion: #$($enrollment.id)"

Step 'Actualizar progreso'
$progress = Invoke-RestMethod -Method Patch -Uri "$base/enrollments/$($enrollment.id)/progress" -Headers $headers -ContentType 'application/json' -Body (@{ progressPercent = 50 } | ConvertTo-Json)
if ($progress.progressPercent -ne 50) { throw 'No se pudo actualizar el progreso.' }
Write-Host 'Progreso actualizado a 50%.'

Step 'Favoritos'
$favorite = Invoke-RestMethod -Method Post -Uri "$base/favorites/$($course.id)" -Headers $headers -ContentType 'application/json' -Body '{}'
Write-Host "Favorito agregado: $($favorite.title)"

Step 'Resena'
$review = Invoke-RestMethod -Method Put -Uri "$base/reviews/courses/$($course.id)" -Headers $headers -ContentType 'application/json' -Body (@{
    rating = 5
    comment = 'Prueba automatizada de integracion entre microservicios.'
} | ConvertTo-Json)
Write-Host "Resena guardada: $($review.rating)/5"

Step 'Notificaciones'
$notifications = @(Invoke-RestMethod -Method Get -Uri "$base/notifications" -Headers $headers)
Write-Host "Notificaciones encontradas: $($notifications.Count)"

Step 'Salud publica'
$health = Invoke-RestMethod -Method Get -Uri "$base/public/health"
if ($health.status -ne 'UP') { throw 'Health endpoint no esta UP.' }
Write-Host "Operations/DB: $($health.status)/$($health.database)"

Step 'Prueba administrativa opcional'
if ($env:DEMO_ADMIN_EMAIL -and $env:DEMO_ADMIN_PASSWORD) {
    try {
        $adminLogin = Invoke-RestMethod -Method Post -Uri "$base/auth/login" -ContentType 'application/json' -Body (@{
            email = $env:DEMO_ADMIN_EMAIL
            password = $env:DEMO_ADMIN_PASSWORD
        } | ConvertTo-Json)
        $adminHeaders = Headers $adminLogin.token
        $summary = Invoke-RestMethod -Method Get -Uri "$base/admin/reports/summary" -Headers $adminHeaders
        $audit = @(Invoke-RestMethod -Method Get -Uri "$base/admin/audit" -Headers $adminHeaders)
        Write-Host "Resumen admin: $($summary.courses) cursos, $($summary.enrollments) inscripciones."
        Write-Host "Eventos de auditoria visibles: $($audit.Count)"
    }
    catch {
        Write-Warning "La prueba admin no pudo completarse: $($_.Exception.Message)"
    }
}
else {
    Write-Host 'DEMO_ADMIN_EMAIL/DEMO_ADMIN_PASSWORD no definidos; se omite la prueba admin.'
}

Write-Host "`nSMOKE TEST COMPLETADO CORRECTAMENTE" -ForegroundColor Green
Write-Host "Usuario temporal: $email"
