1. To build the TicketSale Avro object, you need to run mvn generate-sources, and then you should be able to build the project.
2. Run schema-registry server - ./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties
3. For getting schema-registry I used to download confluent-6.1.0
4. Kafka was used kafka_2.13-2.6.0

./bin/kafka-console-producer.sh  --broker-list localhost:9092 --topic movie-ticket-sales --property value.schema="/home/alex/IntelijIdeaProjects/hoWorldProjects/kafkastreamsforbeginners/lesson4/src/main/resources/avro/ticket-sale.avsc"

Useful links about Kafka-streams:
 * Example from https://dzone.com/articles/kafka-stream-kstream-vs-apache-flink
 * Useful article about stateful operations - https://dev.to/itnext/how-to-use-stateful-operations-in-kafka-streams-4ia1
 * Official doc - https://kafka-tutorials.confluent.io/create-stateful-aggregation-count/kstreams.html

From kafka_2.13-2.6.0:
    bin/zookeeper-server-start config/zookeeper.properties
    bin/kafka-server-start.sh config/server.properties
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic movie-tickets-sold
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic movie-ticket-sales
From confluent-6.1.0:
    ./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties