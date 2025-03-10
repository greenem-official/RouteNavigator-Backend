#!/bin/bash

echo "Creating the environment dev container..."
docker compose -p routenav_dev down --volumes
docker compose -p routenav_dev up -d
