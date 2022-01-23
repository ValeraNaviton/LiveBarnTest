package livebarntest.services;

import livebarntest.entity.OrderStatusEntity;
import livebarntest.entity.SushiType;
import livebarntest.entity.SushiOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public interface SushiOrderService {

  SushiOrderEntity newOrder(SushiType sushiType);

  SushiOrderEntity setStatus(int orderId, OrderStatusEntity.OrderStatus newStatus) throws StatusChangeException;

  List<SushiOrderEntity> findAll();
  SushiOrderEntity getById(int orderId);

  List<SushiOrderEntity> findCreated();

  @AllArgsConstructor
  @Getter
  public static class StatusChangeException extends Exception {
    private OrderStatusEntity.OrderStatus currentStatus;
  }
}
