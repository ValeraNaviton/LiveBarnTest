package livebarntest.services;

import livebarntest.entity.OrderStatusEntity;

import java.util.List;

public interface StatusService {
    List<OrderStatusEntity> findAll();
    OrderStatusEntity findStatusByName(String name);
}
