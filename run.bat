@echo off

set "PROFILE=local"
set "DIR=%~1"
if "%DIR%"=="" set "DIR=.\docs"
set "REGEX=.*-%PROFILE%.env$"

for /f "delims=" %%f in ('dir /b "%DIR%" ^| findstr /R /C:"%REGEX%"') do (
    set "ENV_FILE=%DIR%\%%f"
    goto :found
)

echo Nenhum arquivo .env encontrado com o padrão '%REGEX%' no diretório '%DIR%'.
exit /b 1

:found
for /f "usebackq delims=" %%a in (%ENV_FILE%) do (
    set "line=%%a"
    setlocal enabledelayedexpansion
    if "!line:~0,1!" neq "#" (
        for /f "tokens=1,* delims==" %%b in ("!line!") do set "%%b=%%c"
    )
    endlocal
)

echo Variáveis de ambiente carregadas a partir do arquivo %ENV_FILE%

mvn clean package -DskipTests
copy /Y ".\target\votacao-*.jar" ".\target\votacao.jar"
docker compose up -d
java -jar .\target\votacao.jar --spring.profiles.active=%PROFILE%
