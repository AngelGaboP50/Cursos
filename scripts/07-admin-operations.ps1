$ErrorActionPreference='Stop'
. "$PSScriptRoot/_load-env.ps1"
$env:SERVER_PORT='8085'
Set-Location "backend/admin-operations-service"
.\mvnw.cmd spring-boot:run
