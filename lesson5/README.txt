0. Use kafka_2.13-2.6.0
1. bin/zookeeper-server-start.sh config/zookeeper.properties
2. bin/kafka-server-start.sh config/server.properties
3. ./bin/kafka-console-producer.sh --topic sentences --bootstrap-server localhost:9092
4. run FlinkKafkaApp
