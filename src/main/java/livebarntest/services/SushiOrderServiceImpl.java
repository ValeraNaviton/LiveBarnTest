package livebarntest.services;

import livebarntest.entity.OrderStatusEntity;
import livebarntest.entity.SushiType;
import livebarntest.entity.SushiOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

import static livebarntest.entity.OrderStatusEntity.OrderStatus.*;


@Component
@Transactional
public class SushiOrderServiceImpl implements SushiOrderService {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private StatusService statusService;


  @Override
  public SushiOrderEntity newOrder(SushiType sushiType) {

    SushiOrderEntity newOrder = new SushiOrderEntity();
    newOrder.setSushi(sushiType);
    newOrder.setStatus(statusService.findStatusByName(CREATED.getName()));
    entityManager.persist(newOrder);

    return newOrder;
  }

  @Override
  public List<SushiOrderEntity> findAll() {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SushiOrderEntity> criteriaQuery = criteriaBuilder.createQuery(SushiOrderEntity.class);

    Root<SushiOrderEntity> rootEntry = criteriaQuery.from(SushiOrderEntity.class);
    CriteriaQuery<SushiOrderEntity> all = criteriaQuery.select(rootEntry);

    TypedQuery<SushiOrderEntity> query = entityManager.createQuery(all);
    return query.getResultList();
  }

  @Override
  public List<SushiOrderEntity> findCreated() {
    OrderStatusEntity createdStatus = statusService.findStatusByName(CREATED.getName());

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SushiOrderEntity> criteriaQuery = criteriaBuilder.createQuery(SushiOrderEntity.class);

    Root<SushiOrderEntity> order = criteriaQuery.from(SushiOrderEntity.class);
    Predicate orderStatusPredicate = criteriaBuilder.equal(order.get("status_id"), createdStatus.getId());
    criteriaQuery.where(orderStatusPredicate);

    TypedQuery<SushiOrderEntity> query = entityManager.createQuery(criteriaQuery);
    return query.getResultList();
  }

  @Override
  public SushiOrderEntity setStatus(int orderId, OrderStatusEntity.OrderStatus newStatus)
    throws StatusChangeException {

    SushiOrderEntity byId = getById(orderId);
    switch (newStatus) {
      case PAUSED:
        if (!PAUSABLE_STATUSES.contains(byId.getStatus().getStatus())) {
          throw new StatusChangeException(byId.getStatus().getStatus());
        }
        break;
      case IN_PROGRESS:
        if (!RESUMABLE_STATUSES.contains(byId.getStatus().getStatus())) {
          throw new StatusChangeException(byId.getStatus().getStatus());
        }
        break;
      case CANCELLED:
        if (!CANCELLABLE_STATUSES.contains(byId.getStatus().getStatus())) {
          throw new StatusChangeException(byId.getStatus().getStatus());
        }
        break;
    }
    byId.setStatus(statusService.findStatusByName(newStatus.getName()));
    entityManager.persist(byId);
    return byId;
  }

  @Override
  public SushiOrderEntity getById(int orderId) {
    SushiOrderEntity byId = entityManager.find(SushiOrderEntity.class, orderId);
    if (byId == null) {
      throw new EntityNotFoundException("order by id:[" + orderId + "] not found");
    }
    return byId;
  }

}
