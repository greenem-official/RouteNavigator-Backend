@echo off

set VERSION=1.1.0

echo Building the project...
call ./gradlew build -x test --stacktrace

echo Deploying the project
docker build -t d4ylight/route-navigation-backend:%VERSION% -t d4ylight/route-navigation-backend:latest . --target makeImage --no-cache
