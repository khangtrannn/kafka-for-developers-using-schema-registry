package org.khang.producer;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jboss.logging.Logger;
import org.khang.Greeting;

public class GreetingProducer {
  private static final String GREETING_TOPIC = "greeting";
  private static final Logger logger = Logger.getLogger(GreetingProducer.class);

  public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

    KafkaProducer<String, byte[]> producer = new KafkaProducer<>(properties);

    Greeting greeting = buildGreeting("Hello, Schema Registry");

    byte[] value = greeting.toByteBuffer().array();

    ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>(GREETING_TOPIC, value);

    var recordMetaData = producer.send(producerRecord).get();
    logger.infof("recordMetaData: %s", recordMetaData);
  }

  private static Greeting buildGreeting(String greeting) {
    return Greeting.newBuilder().setGreeting(greeting).build();
  }
}
