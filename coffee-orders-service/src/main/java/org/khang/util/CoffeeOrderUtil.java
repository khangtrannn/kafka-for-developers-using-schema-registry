package org.khang.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import org.khang.domain.generated.*;


public class CoffeeOrderUtil {
  public static CoffeeOrder buildNewCoffeeOrder() {
    return CoffeeOrder.newBuilder()
      .setId(randomId())
      .setName("Khang Tran")
      .setStore(generateStore())
      .setOrderedTime(Instant.now())
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
}
