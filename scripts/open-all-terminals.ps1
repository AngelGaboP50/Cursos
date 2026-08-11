# Opcional: abre una ventana de PowerShell por proceso. Para la exposición puedes abrirlas manualmente una a una.
$names=@('01-eureka.ps1','02-gateway.ps1','03-auth-user.ps1','04-course.ps1','05-enrollment.ps1','06-engagement.ps1','07-admin-operations.ps1','08-frontend.ps1')
foreach($name in $names){
  $script=Join-Path $PSScriptRoot $name
  Start-Process powershell -ArgumentList '-NoExit','-ExecutionPolicy','Bypass','-File',"`"$script`""
  Start-Sleep -Seconds 2
}
