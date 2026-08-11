$ErrorActionPreference='Stop'
$services=@('eureka-server','api-gateway','auth-user-service','course-service','enrollment-service','engagement-service','admin-operations-service')
foreach($s in $services){Write-Host "== Testing $s ==";Push-Location "backend/$s"; .\mvnw.cmd test; Pop-Location}
Push-Location frontend; npm ci; npm test -- --watch=false; Pop-Location
