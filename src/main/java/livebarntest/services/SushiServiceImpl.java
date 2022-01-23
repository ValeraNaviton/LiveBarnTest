package livebarntest.services;

import livebarntest.entity.SushiType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


@Service
public class SushiServiceImpl implements SushiService{

    @Autowired
    private EntityManager entityManager;

    @Override
    public SushiType findByName(String name) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SushiType> criteriaQuery = criteriaBuilder.createQuery(SushiType.class);

        Root<SushiType> sushi = criteriaQuery.from(SushiType.class);
        Predicate sushiNamePredicate = criteriaBuilder.equal(sushi.get("name"), name);
        criteriaQuery.where(sushiNamePredicate);

        TypedQuery<SushiType> query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();

    }
}
