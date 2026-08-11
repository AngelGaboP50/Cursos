$ErrorActionPreference='Stop'
. "$PSScriptRoot/_load-env.ps1"
$env:SERVER_PORT='8081'
Set-Location "backend/auth-user-service"
.\mvnw.cmd spring-boot:run
