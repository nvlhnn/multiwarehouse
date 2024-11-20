#local-confluent-kafka-cp-zookeeper-headless
kafka-topics --zookeeper $1:2181 --topic warehouse-created --delete --if-exists
kafka-topics --zookeeper $1:2181 --topic warehouse-updated --delete --if-exists
kafka-topics --zookeeper $1:2181 --topic warehouse-stock-updated --delete --if-exists
kafka-topics --zookeeper $1:2181 --topic order-response --delete --delete --if-exists

kafka-topics --zookeeper $1:2181 --topic warehouse-created --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper $1:2181 --topic warehouse-updated --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper $1:2181 --topic warehouse-stock-updated --create --partitions 3 --replication-factor 3 --if-not-exists
kafka-topics --zookeeper $1:2181 --topic order-response --create --partitions 3 --replication-factor 3 --if-not-exists
