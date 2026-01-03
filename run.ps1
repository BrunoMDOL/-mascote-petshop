$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
Set-Location $PSScriptRoot
Write-Host "Iniciando Mascote Pet Shop..." -ForegroundColor Green
Write-Host "JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Yellow
Write-Host ""
& .\mvnw.cmd spring-boot:run
