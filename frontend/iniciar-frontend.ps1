# Script para iniciar el frontend
# Añade Node.js al PATH si no está disponible

# Añadir Node.js al PATH de esta sesión
$nodePath = "C:\Program Files\nodejs"
if ($env:Path -notlike "*$nodePath*") {
    Write-Host "Añadiendo Node.js al PATH de esta sesión..." -ForegroundColor Yellow
    $env:Path += ";$nodePath"
}

# Verificar Node.js
Write-Host "Verificando Node.js..." -ForegroundColor Cyan
try {
    $nodeVersion = & "$nodePath\node.exe" --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] Node.js encontrado: $nodeVersion" -ForegroundColor Green
    } else {
        throw "Node.js no disponible"
    }
} catch {
    Write-Host "[ERROR] Node.js no está disponible" -ForegroundColor Red
    Write-Host "Por favor, instala Node.js desde: https://nodejs.org/" -ForegroundColor Yellow
    exit 1
}

# Verificar npm
Write-Host "Verificando npm..." -ForegroundColor Cyan
try {
    $npmVersion = & "$nodePath\npm.cmd" --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] npm encontrado: v$npmVersion" -ForegroundColor Green
    } else {
        throw "npm no disponible"
    }
} catch {
    Write-Host "[ERROR] npm no está disponible" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Instalando dependencias..." -ForegroundColor Cyan
& "$nodePath\npm.cmd" install

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "[OK] Dependencias instaladas correctamente" -ForegroundColor Green
    Write-Host ""
    Write-Host "Iniciando servidor de desarrollo..." -ForegroundColor Cyan
    Write-Host ""
    & "$nodePath\npm.cmd" run dev
} else {
    Write-Host ""
    Write-Host "[ERROR] Error al instalar dependencias" -ForegroundColor Red
    exit 1
}
