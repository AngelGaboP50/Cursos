$root = Split-Path -Parent $PSScriptRoot
$envFile = Join-Path $PSScriptRoot 'local-env.ps1'
if (-not (Test-Path $envFile)) {
  Write-Error "Falta scripts/local-env.ps1. Copia local-env.example.ps1 y edita tus secretos."
  exit 1
}
. $envFile
Set-Location $root
