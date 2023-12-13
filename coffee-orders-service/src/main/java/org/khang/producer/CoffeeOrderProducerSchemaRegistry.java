package org.khang.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jboss.logging.Logger;
import org.khang.domain.generated.CoffeeOrder;
import org.khang.util.CoffeeOrderUtil;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

public class CoffeeOrderProducerSchemaRegistry {
  private static final Logger logger = Logger.getLogger(CoffeeOrderProducerSchemaRegistry.class.getName());
  private static final String COFFEE_ORDERS = "coffee-orders-sr";

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

    KafkaProducer<String, CoffeeOrder> producer = new KafkaProducer<>(properties);
    CoffeeOrder coffeeOrder = CoffeeOrderUtil.buildNewCoffeeOrder();

    ProducerRecord<String, CoffeeOrder> producerRecord = new ProducerRecord<>(COFFEE_ORDERS, coffeeOrder);
    var recordMetaData = producer.send(producerRecord).get();
    logger.infof("recordMetaData: %s", recordMetaData);
  }
}
