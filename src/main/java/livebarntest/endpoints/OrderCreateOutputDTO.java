package livebarntest.endpoints;

import livebarntest.entity.SushiOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateOutputDTO {

  private int code;
  private String msg;
  private OrderDTO order;

  public OrderCreateOutputDTO(SushiOrderEntity order) {
    this.code = 0;
    this.msg = "Order created";
    this.order = new OrderDTO(
      order.getId(), order.getStatus().getId(), order.getSushi().getId(), order.getCreatedAt().getTime()
    );
  }

  @AllArgsConstructor
  @Getter
  @Setter
  public static class OrderDTO {
    private long id;
    private int statusId;
    private int sushiId;
    private long createdAt;
  }
}
