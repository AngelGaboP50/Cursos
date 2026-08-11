$ErrorActionPreference='Stop'
. "$PSScriptRoot/_load-env.ps1"
$env:SERVER_PORT='8082'
Set-Location "backend/course-service"
.\mvnw.cmd spring-boot:run
