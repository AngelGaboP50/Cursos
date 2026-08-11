$ErrorActionPreference='Stop'
. "$PSScriptRoot/_load-env.ps1"
$env:SERVER_PORT='8083'
Set-Location "backend/enrollment-service"
.\mvnw.cmd spring-boot:run
