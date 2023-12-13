package org.khang.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.khang.domain.generated.Address;
import org.khang.domain.generated.CoffeeOrder;
import org.khang.domain.generated.CoffeeUpdateEvent;
import org.khang.domain.generated.OrderId;
import org.khang.domain.generated.OrderLineItem;
import org.khang.domain.generated.OrderStatus;
import org.khang.domain.generated.PickUp;
import org.khang.domain.generated.Size;
import org.khang.domain.generated.Store;


public class CoffeeOrderUtil {
  public static CoffeeOrder buildNewCoffeeOrder() {
    var orderId = OrderId.newBuilder()
      .setId(randomId())
      .build();

    return CoffeeOrder.newBuilder()
      .setId(UUID.randomUUID())
      .setName("Khang Tran")
      .setStore(generateStore())
      .setOrderedTime(Instant.now())
      .setOrderedDate(LocalDate.now())
      .setPickUp(PickUp.IN_STORE)
      .setOrderLineItems(generateOrderLineItems())
      .build();
  }

  private static List<OrderLineItem> generateOrderLineItems() {
    var orderLineItem = OrderLineItem.newBuilder()
        .setName("Caffe Latte")
        .setQuantity(1)
        .setSize(Size.MEDIUM)
        .setCost(BigDecimal.valueOf(3.99))
        .build();

    return List.of(orderLineItem);

  }

  private static Store generateStore() {
    return Store.newBuilder()
        .setId(randomId())
        .setAddress(buildAddress())
        .build();
  }

  private static Address buildAddress() {
    return Address.newBuilder()
        .setAddressLine1("1234 Address Line 1")
        .setCity("Chicago")
        .setStateProvince("IL")
        .setZip("12345")
        .build();

  }

  public static int randomId() {
    Random random = new Random();
    return random.nextInt(1000);
  }

  public static CoffeeUpdateEvent buildCoffeeOrderUpdateEvent() {
    return CoffeeUpdateEvent.newBuilder()
      .setId(UUID.randomUUID())
      .setStatus(OrderStatus.PROCESSING)
      .build();
  }
}
