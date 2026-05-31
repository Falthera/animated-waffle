@echo off
setlocal

set APP_DIR=%~dp0
if "%APP_DIR%"=="" set APP_DIR=.

set GRADLE_VERSION=9.5.1
set GRADLE_DIST_NAME=gradle-%GRADLE_VERSION%-bin
set GRADLE_EXTRACTED_NAME=gradle-%GRADLE_VERSION%
set GRADLE_DIST_URL=https://services.gradle.org/distributions/%GRADLE_DIST_NAME%.zip
set GRADLE_CACHE_DIR=%USERPROFILE%\.gradle\pvp-optimizer
set GRADLE_DIST_DIR=%GRADLE_CACHE_DIR%\%GRADLE_EXTRACTED_NAME%
set GRADLE_ZIP=%GRADLE_CACHE_DIR%\%GRADLE_DIST_NAME%.zip

if not exist "%GRADLE_DIST_DIR%\bin\gradle.bat" (
	mkdir "%GRADLE_CACHE_DIR%" >NUL 2>&1
	powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%GRADLE_DIST_URL%' -OutFile '%GRADLE_ZIP%'"
	if exist "%GRADLE_DIST_DIR%" rmdir /s /q "%GRADLE_DIST_DIR%"
	powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%GRADLE_ZIP%' -DestinationPath '%GRADLE_CACHE_DIR%'"
)

call "%GRADLE_DIST_DIR%\bin\gradle.bat" -Dorg.gradle.appname=gradlew -p "%APP_DIR%" %*
