#!/bin/bash

echo "Running Tests..."
./gradlew test --tests MarketplaceApplicationTests --rerun-tasks
