package livebarntest.services;

import livebarntest.entity.OrderStatusEntity;
import livebarntest.entity.SushiOrderEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static livebarntest.entity.OrderStatusEntity.OrderStatus.*;

public class MeilleurChef implements Runnable {

  private static final Log LOG = LogFactory.getLog(MeilleurChef.class);

  private KitchenService kitchenService;
  private SushiOrderService orderService;

  public MeilleurChef(SushiOrderService orderService, KitchenService kitchenService) {
    this.orderService = orderService;
    this.kitchenService = kitchenService;
  }

  @Override
  public void run() {
    Optional<SushiOrderEntity> currentOrder;
    int timeSpentOnOrder;

    while (true) {
      try {
        TimeUnit.SECONDS.sleep(1);
        currentOrder = kitchenService.pickNextOrder();
        if (currentOrder.isPresent()) {
          timeSpentOnOrder = 0;

          while (true) {
            TimeUnit.SECONDS.sleep(1);
            SushiOrderEntity freshSnapshot = orderService.getById(currentOrder.get().getId());
            if (freshSnapshot.getStatus().getStatus() == IN_PROGRESS) {
              timeSpentOnOrder++;
              if (timeSpentOnOrder >= currentOrder.get().getSushi().getTimeToMake()) {
                LOG.warn(Thread.currentThread().getName() + " done with order [" + currentOrder.get().getId() + "]");
                try {
                  orderService.setStatus(currentOrder.get().getId(), OrderStatusEntity.OrderStatus.FINISHED);
                } catch (SushiOrderService.StatusChangeException e) {
                  LOG.error("couldnt finish [" + currentOrder + "]. Maybe someone cancelled in the middle ?..", e);
                }
                break;
              } else {
                LOG.debug(Thread.currentThread().getName() + " is working on order [" + currentOrder.get().getId() + "]");
              }
            } else if (freshSnapshot.getStatus().getStatus() == PAUSED) {
              LOG.debug(Thread.currentThread().getName() + " is NOT working on order [" + currentOrder.get().getId() + "] as its paused. Chef is watching netflix...");
            } else if (freshSnapshot.getStatus().getStatus() == CANCELLED) {
              LOG.debug(Thread.currentThread().getName() + " is pissed off as some tabarnak hipster cancelled his order...");
              break;
            } else if (freshSnapshot.getStatus().getStatus() == FINISHED) {
              LOG.debug(Thread.currentThread().getName() + " is pissed off as some tabarnak at kitchen got no test coverage and gave him again finished order... Next time strike for sure...");
              break;
            }
          }
        } else {
          LOG.warn(Thread.currentThread().getName() + " got no orders from kitchen... Strike or netflix ?..");
        }
      } catch (InterruptedException e) {
        // application stopped ?
        return;
      }
    }


  }
}
