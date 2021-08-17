package com;

import io.confluent.developer.avro.TicketSale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@EnableScheduling
public class AvroTopicPoller {

    //private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(AvroTopicPoller.class, args);
    }

    static void sendAvroToTopic(String title, String inputTopicName, KafkaTemplate<String, TicketSale> kafkaTemplate, Logger logger) {
        try {
            TicketSale ticketSale = new TicketSale();
            //.nextInt(min, max + 1);
            ticketSale.setTitle(title);
            ticketSale.setSaleTs("2019-07-18T10:00:00Z");
            ticketSale.setTicketTotalValue(12);
            Message<TicketSale> message = MessageBuilder
                    .withPayload(ticketSale)
                    .setHeader(KafkaHeaders.TOPIC, inputTopicName)
                    //.setHeader(KafkaHeaders.MESSAGE_KEY, ThreadLocalRandom.current().nextInt(1000, 100000 + 1))
                    .setHeader(KafkaHeaders.MESSAGE_KEY, title)
                    .build();

            // publish to `movie-tickets-sales` Kafka topic
            kafkaTemplate.send(message);

            logger.info("Published latest data to Kafka.");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}

@Component
@EnableScheduling
class ScheduleFeedPoller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${input.topic.name}")
    private String inputTopicName;

    @Autowired
    KafkaTemplate<String, TicketSale> kafkaTemplate;

    //@Scheduled(cron = "*/10 * * * * *")
    private void pullDataToTopic() {
        try {
            logger.info("Getting latest data.");

            TicketSale ticketSale = new TicketSale();

            //.nextInt(min, max + 1);
            String title = "The Big Lebowski-" + ThreadLocalRandom.current().nextInt(4, 6 + 1);
            ticketSale.setTitle(title);
            ticketSale.setSaleTs("2019-07-18T10:00:00Z");
            ticketSale.setTicketTotalValue(12);
            Message<TicketSale> message = MessageBuilder
                    .withPayload(ticketSale)
                    .setHeader(KafkaHeaders.TOPIC, inputTopicName)
                    //.setHeader(KafkaHeaders.MESSAGE_KEY, ThreadLocalRandom.current().nextInt(1000, 100000 + 1))
                    .setHeader(KafkaHeaders.MESSAGE_KEY, title)
                    .build();

            // publish to `movie-tickets-sales` Kafka topic
            kafkaTemplate.send(message);

            logger.info("Published latest data to Kafka topic " + inputTopicName + ".");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}

@RestController
@RequestMapping(value = "/poolAvro")
class AvroRestTopicPoller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${input.topic.name}")
    private String inputTopicName;

    @Autowired
    KafkaTemplate<String, TicketSale> kafkaTemplate;

    /**
     * http://localhost:8080/poolAvro/send?title=The%20Big%20Lebowski-5
     * @param title
     * @return
     */
    @GetMapping("/send")
    public @ResponseBody String greeting(String title) {
        AvroTopicPoller.sendAvroToTopic(title, inputTopicName, kafkaTemplate, logger);
        return title;
    }

}



