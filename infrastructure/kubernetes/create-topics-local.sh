#local-confluent-kafka-cp-zookeeper-headless
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic warehouse-created --delete --if-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic warehouse-updated --delete --if-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic warehouse-stock-updated --delete --if-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic order-response --delete --delete --if-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic user-registered --delete --delete --if-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic product-created --delete --delete --if-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic stock-created --delete --delete --if-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic stock-updated --delete --delete --if-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic payment-response --delete --delete --if-exists

kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic warehouse-created --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic warehouse-updated --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic warehouse-stock-updated --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic order-response --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic user-registered --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic product-created --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic stock-created --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic stock-updated --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper my-confluent-cp-zookeeper-headless:2181 --topic payment-response --create --partitions 3 --replication-factor 3 --if-not-exists