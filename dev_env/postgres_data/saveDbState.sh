#!/bin/bash

DOCKER_CONTAINER_NAME="routenav_testenv_postgres_container"
POSTGRES_USER="ivan"
POSTGRES_DB="maindb"
BACKUP_FILE="initial_postgres_data.sql"

echo "Dumping database data in the init script for the docker-compose.yml..."
echo "Container: $DOCKER_CONTAINER_NAME"
echo "User: $POSTGRES_USER"
echo "Database: $POSTGRES_DB"
echo "Backup file: $BACKUP_FILE"

docker exec -it "$DOCKER_CONTAINER_NAME" pg_dump -U "$POSTGRES_USER" -d "$POSTGRES_DB" > "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "Backup completed successfully!"
else
    echo "Backup failed with error code $?."
fi
