0. Use kafka_2.13-2.6.0
1. bin/zookeeper-server-start.sh config/zookeeper.properties
2. bin/kafka-server-start.sh config/server.properties
3. ./bin/kafka-console-producer.sh --topic sentences --bootstrap-server localhost:9092
4. bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic word-count --property print.key=true --property key.separator=" : " --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"
5. run WordCountApp

WordCountApp is used to proceeding from one queue to another