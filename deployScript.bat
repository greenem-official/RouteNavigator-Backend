@echo off

echo Building the project...
call ./gradlew build -x test --stacktrace

echo Deploying the project
docker build -t d4ylight/route-navigation-backend . --target makeImage --no-cache
