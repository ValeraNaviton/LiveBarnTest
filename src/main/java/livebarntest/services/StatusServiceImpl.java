package livebarntest.services;

import livebarntest.entity.OrderStatusEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class StatusServiceImpl implements StatusService{

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<OrderStatusEntity> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderStatusEntity> criteriaQuery = criteriaBuilder.createQuery(OrderStatusEntity.class);

        Root<OrderStatusEntity> rootEntry = criteriaQuery.from(OrderStatusEntity.class);
        CriteriaQuery<OrderStatusEntity> all = criteriaQuery.select(rootEntry);


        TypedQuery<OrderStatusEntity> query = entityManager.createQuery(all);
        return query.getResultList();
    }

    @Override
    public OrderStatusEntity findStatusByName(String name) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderStatusEntity> criteriaQuery = criteriaBuilder.createQuery(OrderStatusEntity.class);

        Root<OrderStatusEntity> status = criteriaQuery.from(OrderStatusEntity.class);
        Predicate statusNamePredicate = criteriaBuilder.equal(status.get("name"), name);
        criteriaQuery.where(statusNamePredicate);

        TypedQuery<OrderStatusEntity> query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
