1. To build the TicketSale Avro object, you need to run mvn generate-sources, and then you should be able to build the project.
2. Run schema-registry server - ./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties
3. For getting schema-registry I used to download confluent-6.1.0
4. Kafka was used kafka_2.13-2.6.0

./bin/kafka-console-producer.sh  --broker-list localhost:9092 --topic movie-tickets-sales --property value.schema="/home/alex/IntelijIdeaProjects/hoWorldProjects/kafkastreamsforbeginners/lesson4/src/main/resources/avro/ticket-sale.avsc"

Useful links about Kafka-streams:
 * Example from https://dzone.com/articles/kafka-stream-kstream-vs-apache-flink
 * Useful article about stateful operations - https://dev.to/itnext/how-to-use-stateful-operations-in-kafka-streams-4ia1
 * Official doc - https://kafka-tutorials.confluent.io/create-stateful-aggregation-count/kstreams.html

From kafka_2.13-2.6.0:
    bin/zookeeper-server-start.sh config/zookeeper.properties
    bin/kafka-server-start.sh config/server.properties
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic movie-tickets-sold30
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic movie-tickets-sales30
From confluent-6.1.0:
    ./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties

Все конфиги кафки kafka_2.13-2.6.0 смотри в lesson4 в папке config.

TODO: AvroAggregatingCountReader needs to be fixed because he doesn't upload data at the real time.
Use KafkaConsumerConfigReader for reading data from topics then AvroAggregatingCountReader because AvroAggregatingCountReader needs to be fixed.

1. Установка Kafka Connect Elasticsearch - https://sematext.com/blog/kafka-connect-elasticsearch-how-to/
2. Установка Elasticsearch 7 - https://computingforgeeks.com/install-elasticsearch-on-ubuntu/
        !!!Тут важное замечание, что с репа https://github.com/confluentinc/kafka-connect-elasticsearch/branches/active
    нужно качать нужный бренч. Т е в доке по установке этого плагина, есть команда, которая качает самую первую версию,
    и она не подойдет под твою.
3. Установить Java 11
4. bin/connect-standalone.sh config/connect-standalone.properties config/elasticsearch-connect.properties
5. Установка Elasticsearch 7 + Kibana - https://phoenixnap.com/kb/how-to-install-elk-stack-on-ubuntu