$ErrorActionPreference='Stop'
. "$PSScriptRoot/_load-env.ps1"
$env:SERVER_PORT='8084'
Set-Location "backend/engagement-service"
.\mvnw.cmd spring-boot:run
