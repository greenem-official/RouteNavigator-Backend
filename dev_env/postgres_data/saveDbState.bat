@echo off

SET DOCKER_CONTAINER_NAME=routenav_testenv_postgres_container
SET POSTGRES_USER=ivan
SET POSTGRES_DB=maindb
SET BACKUP_FILE=initial_postgres_data.sql

echo Dumping database data in the init script for the docker-compose.yml...
echo Container: %DOCKER_CONTAINER_NAME%
echo User: %POSTGRES_USER%
echo Database: %POSTGRES_DB%
echo Backup file: %BACKUP_FILE%

docker exec -it %DOCKER_CONTAINER_NAME% pg_dump -U %POSTGRES_USER% -d %POSTGRES_DB% > %BACKUP_FILE%

IF %ERRORLEVEL% EQU 0 (
    echo Backup completed successfully!
) ELSE (
    echo Backup failed with error code %ERRORLEVEL%.
)
