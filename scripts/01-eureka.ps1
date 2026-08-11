$ErrorActionPreference='Stop'
. "$PSScriptRoot/_load-env.ps1"

Set-Location "backend/eureka-server"
.\mvnw.cmd spring-boot:run
