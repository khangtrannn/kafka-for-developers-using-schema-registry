# Kafka For Developers - Data Contracts using Schema Registry

This repository has the content to interact with Kafka using AVRO and Schema Registry.


## NOTES
- Evolving the Schema - Consumer Fails to Read the New Schema
The New Schema was published is different from the Schema was cached by the Consumer
```
ERROR: Cannot resolve schema for fingerprint: -4256560753428017869
org.apache.avro.message.MissingSchemaException: Cannot resolve schema for fingerprint: -4256560753428017869
        at org.apache.avro.message.BinaryMessageDecoder.getDecoder(BinaryMessageDecoder.java:142)
        at org.apache.avro.message.BinaryMessageDecoder.decode(BinaryMessageDecoder.java:160)
        at org.apache.avro.message.MessageDecoder$BaseDecoder.decode(MessageDecoder.java:141)
        at org.apache.avro.message.MessageDecoder$BaseDecoder.decode(MessageDecoder.java:129)
        at org.khang.domain.generated.CoffeeOrder.fromByteBuffer(CoffeeOrder.java:79)
        at consumer.CoffeeOrderConsumer.decodeAvroCoffeeOrder(CoffeeOrderConsumer.java:57)
        at consumer.CoffeeOrderConsumer.main(CoffeeOrderConsumer.java:42)
```

- Why Schema Registry?
  Producers and consumers are decoupled and they exchange data using the Kafka Broker
  But the consumer is indirectly coupled to producer to understand the data format
  Anytime business requirements change data may be needed to change

- Handling Data Changes
  Business needed to make change and the producer modifed the data

- Kafka Architecture in Prod
  There is no limit on number of data types that can be exchanged between the apps
  With this kind of architecture the failure is very likely in the consumer side
  Publishing any new changes requires approval from all the consuming apps

- Schema Registry
  Enforcing Data Contracts
  Handling Schema Evolution

- Where does the Schema gets stored?
  _schemas topic

- Different ways to Interact with Schema Registry?
  REST API

- Schema Registry REST API
  Subjects
  Fundamentally a scope in which the schemas evolve

  Schemas
  This resource is used to represent a schema

  Config
  This resource is used to update cluster level config for the Schema Registry

  Compatibility
  This resource is used to check the compatibility between schemas

- Data/Schema Evolution
  Any time the data/schema evolves, the downstream consumers should be able to handle the old and new schema seamlessly

## Set up Kafka Environment using Docker

- This should set up the Zookeeper and Kafka Broker in your local environment

```aidl
docker-compose up
```

### Verify the Local Kafka Environment

- Run this below command

```
docker ps
```

- You should be below containers up and running in local

```
CONTAINER ID   IMAGE                                   COMMAND                  CREATED          STATUS          PORTS                                            NAMES
fd305f78339a   confluentinc/cp-schema-registry:7.1.0   "/etc/confluent/dock…"   49 seconds ago   Up 48 seconds   0.0.0.0:8081->8081/tcp                           schema-registry
fb28f7f91b0e   confluentinc/cp-server:7.1.0            "/etc/confluent/dock…"   50 seconds ago   Up 49 seconds   0.0.0.0:9092->9092/tcp, 0.0.0.0:9101->9101/tcp   broker
d00a0f845a45   confluentinc/cp-zookeeper:7.1.0         "/etc/confluent/dock…"   50 seconds ago   Up 49 seconds   2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp       zookeeper
```

### Interacting with Kafka

#### Produce Messages

- This  command should take care of logging in to the Kafka container.

```
docker exec -it broker bash
```

- Command to produce messages in to the Kafka topic.

```
kafka-console-producer --broker-list localhost:9092 --topic test-topic
```

#### Consume Messages

- This  command should take care of logging in to the Kafka container.

```
docker exec -it broker bash
```
- Command to produce messages in to the Kafka topic.

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic
```

### Interacting with Kafka using AVRO Records

#### Produce AVRO Messages

- This  command should take care of logging in to the Schema Registry container.

```
docker exec -it schema-registry bash
```

- Run the **kafka-avro-console-producer** with the Schema

```
kafka-avro-console-producer --broker-list broker:29092 --topic greetings --property value.schema='{"type": "record","name":"Greeting","fields": [{"name": "greeting","type": "string"}]}'
```

- Publish the **Greeting** message

```
{"greeting": "Good Morning!, AVRO"}
```

```
{"greeting": "Good Evening!, AVRO"}
```

```
{"greeting": "Good Night!, AVRO"}
```

### Consume AVRO Messages

- This  command should take care of logging in to the Schema Registry container.

```
docker exec -it schema-registry bash

```

- Run the kafka-avro-console-consumer

```
kafka-avro-console-consumer --bootstrap-server broker:29092 --topic greetings --from-beginning
```

## Kafka Helpful Commands

- To view the list of topics in the Kafka environment.

```
kafka-topics --bootstrap-server localhost:9092 --describe
```

- **Note :** Schemas are stored in an internal topic named *_schemas**

- Lets view the configuration set up for **_schemas** topics   

```
kafka-topics --bootstrap-server localhost:9092 --describe --topic _schemas
```
