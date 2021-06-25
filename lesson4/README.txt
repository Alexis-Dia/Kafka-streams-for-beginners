1. To build the TicketSale Avro object, you need to run mvn generate-sources, and then you should be able to build the project.
2. run schema-registry server - ./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties

./bin/kafka-console-producer.sh  --broker-list localhost:9092 --topic movie-ticket-sales --property value.schema="/home/alex/IntelijIdeaProjects/hoWorldProjects/kafkastreamsforbeginners/lesson4/src/main/resources/avro/ticket-sale.avsc"

Useful links about Kafka-streams:
 * Example from https://dzone.com/articles/kafka-stream-kstream-vs-apache-flink
 * Usefull article about stateful operations - https://dev.to/itnext/how-to-use-stateful-operations-in-kafka-streams-4ia1
 * Official doc - https://kafka-tutorials.confluent.io/create-stateful-aggregation-count/kstreams.html

