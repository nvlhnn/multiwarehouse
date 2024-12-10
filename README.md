# Multi Warehouse Kafka Example

This application demonstrates how to use Kafka in a microservices architecture with warehouse and order services. It shows how to integrate Kafka for event-driven communication between these services.

## Prerequisites

Before running the application, ensure you have the following:

- Docker installed
- Kafka and Zookeeper up and running
- Database and schema setup for `order` and `warehouse` services

## How to Run

Follow these steps to get the application up and running:

### 1. Create Database and Schema for Order and Warehouse Services

Make sure you have the necessary databases and schemas for both the `order` and `warehouse` services. You can create them manually or use migration tools depending on your stack.

### 2. Update `application.yml` in Order-Service and Warehouse-Service

In both `order-service` and `warehouse-service`, update the `application.yml` configuration file to match your environment settings. This typically involves setting up the Kafka server address, database connection strings, and other configurations specific to your services.

### 3. Run Zookeeper

Zookeeper is required by Kafka for distributed coordination. To start Zookeeper, use Docker Compose with the following command:

```bash
/infrastructure/docker-compose/ docker compose -f common.yml -f zookeeper.yml up
```
This will start the Zookeeper service as part of your Docker containers.

### 4. Run Kafka Cluster
Next, run Kafka, which is the message broker for your services. Use the following command to start Kafka:

```bash
/infrastructure/docker-compose/ docker compose -f common.yml -f kafka.yml up
```
This will bring up the Kafka cluster, ready to send and receive messages.

### 5. Create Kafka Topics
After Kafka is running, you need to create the necessary Kafka topics. This is done using the `init_kafka.yml` configuration file. Run the following command:

```bash
/infrastructure/docker-compose/ docker compose -f common.yml -f init_kafka.yml up
```
This will create the topics required by both the order and warehouse services.

### 6. Run Order and Warehouse Services
Finally, run the warehouse and order services. Ensure both services are up and communicating via Kafka to handle the event-driven workflows.


## Exammple usage
Here’s an example of how to interact with the warehouse-service using a curl request to add a new warehouse.


### Add New Warehouse via API
Use the following `curl` command to create a new warehouse by sending a POST request to the `warehouse-service`:

```bash
curl --location 'http://localhost:8181/warehouses' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiI2MTU5ZTJlYy04ZTYyLTQxMmItYTQ3YS0xNWJkNDNjMjg3MDMiLCJyb2xlIjoiU1VQRVJfQURNSU4iLCJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsImlhdCI6MTczMzc2MTYzMywiZXhwIjoxNzM1MjMyODYxfQ.S6vGYakry6moMeShsMmZKyvuBzK3z8U-8NQiloUTVC9YLjbj48F4QYTVEcmpFxh0qFgpiuuIJaWuUENBIrNpaw' \
--data '{
  "name": "New Warehouse",
  "warehouseAddress": {
    "street": "123 Warehouse St",
    "postalCode": "12345",
    "city": "Warehouse City",
    "latitude": 40.7128,
    "longitude": -74.0060
  }
}'
```
that will create a new warehouse in `warehouse service` and send a message to the Kafka topic. And then the order-service will consume the message and create a new warehouse in the `order service` database.

