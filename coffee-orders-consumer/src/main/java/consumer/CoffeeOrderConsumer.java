package consumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jboss.logging.Logger;
import org.khang.domain.generated.CoffeeOrder;

public class CoffeeOrderConsumer {
  private static final String COFFEE_ORDERS_TOPIC = "coffee-orders";
  private static final Logger logger = Logger.getLogger(CoffeeOrderConsumer.class);

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "greeting.consumer2");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

    KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList(COFFEE_ORDERS_TOPIC));

    logger.info("Consumer started");

    while (true) {
      ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
      
      for (ConsumerRecord<String, byte[]> record : records) {
        try {
          CoffeeOrder coffeeOrder = decodeAvroCoffeeOrder(record.value());
          logger.infof("Consumed message, key: %s, value: %s", record.key(), coffeeOrder.toString());
        } catch (Exception e) {
          logger.error("Exception is: ", e.getMessage(), e);
        }
      }
    }
  }

  private static CoffeeOrder decodeAvroCoffeeOrder(byte[] array) throws IOException {
    return CoffeeOrder.fromByteBuffer(ByteBuffer.wrap(array));
  }
}
