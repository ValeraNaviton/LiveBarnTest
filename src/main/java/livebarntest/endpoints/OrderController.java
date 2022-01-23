package livebarntest.endpoints;

import livebarntest.endpoints.exception.IllegalOrderStatusChangeException;
import livebarntest.endpoints.exception.OrderNotFoundException;
import livebarntest.endpoints.exception.SushiTypeNotFoundException;
import livebarntest.entity.OrderStatusEntity;
import livebarntest.entity.SushiType;
import livebarntest.entity.SushiOrderEntity;
import livebarntest.services.StatusService;
import livebarntest.services.SushiOrderService;
import livebarntest.services.SushiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

import static livebarntest.entity.OrderStatusEntity.OrderStatus.*;

@RestController
public class OrderController {

  @Autowired
  private SushiService sushiService;

  @Autowired
  private SushiOrderService orderService;

  @Autowired
  private StatusService orderStatusService;

  @PostMapping(value = "/api/orders", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public OrderCreateOutputDTO orderSubmit(@RequestBody OrderCreateInputDTO createOrder) throws SushiTypeNotFoundException {
    try {
      SushiType newOrderType = sushiService.findByName(createOrder.getSushiName());
      SushiOrderEntity newOrder = orderService.newOrder(newOrderType);
      return new OrderCreateOutputDTO(newOrder);
    } catch (NoResultException e) {
      throw new SushiTypeNotFoundException("No sushi found by: '" + createOrder.getSushiName() + "'");
    }
  }

  @DeleteMapping(value = "/api/orders/{orderId}", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public OrderStatusChangedOutputDTO deleteOrder(@PathVariable String orderId) throws OrderNotFoundException, IllegalOrderStatusChangeException {
    return setOrderStatus(orderId, CANCELLED);
  }

  @PutMapping(value = "/api/orders/{orderId}/pause", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public OrderStatusChangedOutputDTO pauseOrder(@PathVariable String orderId) throws OrderNotFoundException, IllegalOrderStatusChangeException {
    // if some chef was working on it already would be good to let it start working on another order...
    return setOrderStatus(orderId, PAUSED);
  }

  @PutMapping(value = "/api/orders/{orderId}/resume", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public OrderStatusChangedOutputDTO resumeOrder(@PathVariable String orderId) throws OrderNotFoundException, IllegalOrderStatusChangeException {
    return setOrderStatus(orderId, IN_PROGRESS);
  }

  private OrderStatusChangedOutputDTO setOrderStatus(@PathVariable String orderId,
    OrderStatusEntity.OrderStatus newStatus) throws OrderNotFoundException, IllegalOrderStatusChangeException {

    try {
      orderService.setStatus(Integer.parseInt(orderId), newStatus);
      return OrderStatusChangedOutputDTO.buildResponse(newStatus);
    } catch (NumberFormatException | EntityNotFoundException e) {
      throw new OrderNotFoundException("No order found by: '" + orderId + "'");
    } catch (SushiOrderService.StatusChangeException e) {
      throw new IllegalOrderStatusChangeException(orderId, e.getCurrentStatus(), newStatus);
    }
  }

  @GetMapping(value = "/api/sushilist", produces ="application/json")
  @ResponseStatus(HttpStatus.OK)
  List<SushiOrderEntity> orderList() {
    return orderService.findAll();
  }


}
