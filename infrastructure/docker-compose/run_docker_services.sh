#!/bin/bash

# Step 1: Start Zookeeper services
echo "Starting Zookeeper services..."
docker compose -f common.yml -f zookeeper.yml up -d  --remove-orphans
if [ $? -ne 0 ]; then
  echo "Error: Failed to start Zookeeper services."
  exit 1
fi

# Wait for Zookeeper to be fully initialized (adjust sleep time if necessary)
sleep 10

# Step 2: Start Kafka cluster
echo "Starting Kafka cluster..."
docker compose -f common.yml -f kafka_cluster.yml up -d
if [ $? -ne 0 ]; then
  echo "Error: Failed to start Kafka cluster."
  exit 1
fi

# Wait for Kafka to be fully initialized (adjust sleep time if necessary)
sleep 10

# Step 3: Initialize Kafka topics
echo "Initializing Kafka topics..."
docker compose -f common.yml -f init_kafka.yml up -d
if [ $? -ne 0 ]; then
  echo "Error: Failed to initialize Kafka topics."
  exit 1
fi

echo "All services started successfully!"
