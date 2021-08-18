1. To build the BusPosition Avro object, you need to run mvn generate-sources, and then you should be able to build the project.
2. run schema-registry server - ./bin/schema-registry-start ./etc/schema-registry/schema-registry.properties

Принцпи работы.
1. rtd-feed скачивает каждые 30 сек обновленные данные с расположением существующих координат одних и тех же курсирующих автобусов и ложит эти данные в типик кафки.
2. rtd-stream берет из топика кафки данные и высчитывает параметры с учетом прошлой точки и полученную аналитику сохраняет в новый топик.