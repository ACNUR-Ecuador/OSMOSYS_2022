package org.unhcr.osmosys.daos.standardDissagregations;


import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.BaseEntity;
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
 * @param <T>
 *
 */
@SuppressWarnings({"rawtypes", "FieldMayBeFinal"})
public abstract class StandardDissagregationOptionDao<T extends StandardDissagregationOption> extends GenericDaoJpa {

    public StandardDissagregationOptionDao(Class<T> entityClass) {
        super(entityClass, Long.class);

    }

}
