package org.unhcr.osmosys.daos.standardDissagregations;


import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.StandardDissagregationOption;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.util.List;

/**
 * Dao Genérico
 *
 * @param <T>
 */
@SuppressWarnings({"rawtypes", "FieldMayBeFinal"})
public abstract class StandardDissagregationOptionDao<T extends StandardDissagregationOption> extends GenericDaoJpa<T, Long>{

    public StandardDissagregationOptionDao(Class<T> entityClass) {
        super(entityClass, Long.class);

    }


    public List<T> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM " + this.getEntityClass().getSimpleName() + " o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<StandardDissagregationOption> getAllByState(State state) {

        String jpql = "SELECT DISTINCT o FROM StandardDissagregationOption  o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

}
