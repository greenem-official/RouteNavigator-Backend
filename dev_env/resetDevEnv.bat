@echo off

echo Creating the environment dev container...
docker compose -p routenav_dev down
docker compose -p routenav_dev up -d
