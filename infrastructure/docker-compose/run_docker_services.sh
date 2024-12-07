#!/bin/bash

# Step 1: Start Zookeeper and Kafka services using a single docker-compose command
echo "Starting Zookeeper and Kafka services..."
docker-compose -f common.yml -f zookeeper.yml -f kafka_cluster.yml up -d --remove-orphans
if [ $? -ne 0 ]; then
  echo "Error: Failed to start Zookeeper and Kafka services."
  exit 1
fi

# Wait for Zookeeper and Kafka to be fully initialized (adjust sleep time if necessary)
echo "Waiting for Zookeeper and Kafka to initialize..."
sleep 20

# Step 2: Initialize Kafka topics (optional)
echo "Initializing Kafka topics..."
docker-compose -f common.yml -f init_kafka.yml up -d
if [ $? -ne 0 ]; then
  echo "Error: Failed to initialize Kafka topics."
  exit 1
fi

echo "Zookeeper and Kafka services started successfully!"
