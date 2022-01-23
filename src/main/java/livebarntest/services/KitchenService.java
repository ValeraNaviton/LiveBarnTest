package livebarntest.services;

import livebarntest.entity.OrderStatusEntity;
import livebarntest.entity.SushiOrderEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class KitchenService {

  private static final int CHEF_COUNT = 3;
  private static final Log LOG = LogFactory.getLog(KitchenService.class);

  private SushiOrderService orderService;
  private ScheduledExecutorService chefThreads = Executors.newScheduledThreadPool(CHEF_COUNT);
  private List<MeilleurChef> chefList = new ArrayList<>(CHEF_COUNT);

  public KitchenService(@Autowired SushiOrderService orderService) {
    this.orderService = orderService;

    for (int k = 0; k < CHEF_COUNT; k++) {
      MeilleurChef newWorker = new MeilleurChef(orderService, this);
      this.chefList.add(newWorker);
      chefThreads.schedule(newWorker, 0, TimeUnit.SECONDS);
    }
  }

  public synchronized Optional<SushiOrderEntity> pickNextOrder() {
    LOG.debug("\t... asked for new order");
    for (SushiOrderEntity order : orderService.findAll()) {
      if (order.getStatus().getStatus() == OrderStatusEntity.OrderStatus.CREATED) {
        try {
          order = orderService.setStatus(order.getId(), OrderStatusEntity.OrderStatus.IN_PROGRESS);
          LOG.debug("\t... got [" + order + "]");
          return Optional.of(order);
        } catch (SushiOrderService.StatusChangeException e) {
          LOG.error("couldnt start work on [" + order + "]", e);
        }
      }
    }
    return Optional.empty();
  }

}
