1. To build the BusPosition Avro object, you need to run mvn generate-sources, and then you should be able to build the project.
2. run schema-registry server - ./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties

Принцпи работы.
1. rtd-feed скачивает каждые 30 сек обновленные данные с расположением существующих координат одних и тех же курсирующих автобусов и ложит эти данные в типик кафки.
2. rtd-stream берет из топика кафки данные и высчитывает параметры с учетом прошлой точки и полученную аналитику сохраняет в новый топик.
	Например высчитывается скорость исходя из прошлой и текущей точки и отображается разным цветом в зависимости от скорости.

Source code:
    https://www.youtube.com/watch?v=yIFOCYy7Wmc&t=486s&ab_channel=AlexWoolford
    https://github.com/alexwoolford/rtd-kafka

1. Установка Kafka Connect Elasticsearch - https://sematext.com/blog/kafka-connect-elasticsearch-how-to/
2. Установка Elasticsearch 7 - https://computingforgeeks.com/install-elasticsearch-on-ubuntu/
3. Установить Java 11
4. bin/connect-standalone.sh config/connect-standalone.properties config/elasticsearch-connect.properties
