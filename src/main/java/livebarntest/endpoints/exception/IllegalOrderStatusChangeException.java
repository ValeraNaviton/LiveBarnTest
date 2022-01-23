package livebarntest.endpoints.exception;

import livebarntest.entity.OrderStatusEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class IllegalOrderStatusChangeException extends Exception {

  private String orderId;
  private OrderStatusEntity.OrderStatus oldStatus;
  private OrderStatusEntity.OrderStatus newStatus;

}
