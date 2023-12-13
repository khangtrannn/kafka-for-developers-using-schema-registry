package org.khang.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;
import org.khang.domain.generated.CoffeeOrder;
import org.khang.domain.generated.CoffeeUpdateEvent;
import org.khang.domain.generated.OrderId;
import org.khang.util.CoffeeOrderUtil;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;

public class CoffeeOrderUpdateProducerSchemaRegistry {
  private static final Logger logger = Logger.getLogger(CoffeeOrderProducerSchemaRegistry.class.getName());
  private static final String COFFEE_ORDERS = "coffee-orders-sr";

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    properties.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class.getName());

    KafkaProducer<String, CoffeeUpdateEvent> producer = new KafkaProducer<>(properties);
    CoffeeUpdateEvent coffeeUpdateEvent = CoffeeOrderUtil.buildCoffeeOrderUpdateEvent();

    ProducerRecord<String, CoffeeUpdateEvent> producerRecord = new ProducerRecord<>(COFFEE_ORDERS, coffeeUpdateEvent.getId().toString(), coffeeUpdateEvent);
    var recordMetaData = producer.send(producerRecord).get();
    logger.infof("recordMetaData: %s", recordMetaData);
    logger.infof("Published the producerRecord: %s", producerRecord);
  }
}
