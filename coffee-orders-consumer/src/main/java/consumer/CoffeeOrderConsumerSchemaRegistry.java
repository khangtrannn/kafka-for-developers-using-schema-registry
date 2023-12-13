package consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jboss.logging.Logger;
import org.khang.domain.generated.CoffeeOrder;
import org.khang.domain.generated.OrderId;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

public class CoffeeOrderConsumerSchemaRegistry {
  private static final Logger logger = Logger.getLogger(CoffeeOrderConsumerSchemaRegistry.class);
  private static final String COFFEE_ORDERS = "coffee-orders-sr";

  public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "coffee.consumer.sr");
    properties.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

    KafkaConsumer<String, CoffeeOrder> consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Collections.singletonList(COFFEE_ORDERS));
    logger.info("Consumer started");

    while (true) {
      ConsumerRecords<String, CoffeeOrder> records = consumer.poll(Duration.ofMillis(1000));

      for (ConsumerRecord<String, CoffeeOrder> record : records) {
        try {
          logger.infof("Consumed message, key: %s, value: %s", record.key(), record.value().toString());
        } catch (Exception e) {
          logger.errorf("Exception is: %s", e.getMessage(), e);
        }
      }
    }
  }
}
