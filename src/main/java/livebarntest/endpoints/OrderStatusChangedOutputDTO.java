package livebarntest.endpoints;

import livebarntest.entity.OrderStatusEntity;
import lombok.Getter;

@Getter
public class OrderStatusChangedOutputDTO {
  private int code = 0;
  private String msg;

  private OrderStatusChangedOutputDTO(String msg) {
    this.msg = msg;
  }

  public static OrderStatusChangedOutputDTO buildResponse(OrderStatusEntity.OrderStatus status) {
    return new OrderStatusChangedOutputDTO("Order " + status.getName());
  }

  public static OrderStatusChangedOutputDTO buildResponse(String status) {
    return new OrderStatusChangedOutputDTO("Order " + status);
  }
}
