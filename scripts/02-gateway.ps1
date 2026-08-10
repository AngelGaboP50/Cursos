$ErrorActionPreference='Stop'
. "$PSScriptRoot/_load-env.ps1"

Set-Location "backend/api-gateway"
.\mvnw.cmd spring-boot:run
