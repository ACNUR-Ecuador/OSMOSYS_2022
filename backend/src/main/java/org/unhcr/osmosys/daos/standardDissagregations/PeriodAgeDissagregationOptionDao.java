package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodAgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodAgeDissagregationOptionDao {

    @PersistenceContext(unitName = "main-persistence-unit")
    protected EntityManager entityManager;

    private Class<PeriodAgeDissagregationOption> entityClass;
    private Class<AgeDissagregationOptionPeriodId> entityPKClass;

    public PeriodAgeDissagregationOptionDao() {

        this.entityClass = PeriodAgeDissagregationOption.class;
        this.entityPKClass = AgeDissagregationOptionPeriodId.class;
    }

    @SuppressWarnings("unused")
    private Class<AgeDissagregationOptionPeriodId> getPrimaryKeyClass() {

        return this.entityPKClass;
    }

    protected Class<PeriodAgeDissagregationOption> getEntityClass() {
        return entityClass;
    }
    /*
     * public GenericDaoJpa(Class<T> entityClass, Class<PK> entityPKClass) {
     * this.entityClass = entityClass; this.entityPKClass = entityPKClass; }
     */

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public PeriodAgeDissagregationOption save(PeriodAgeDissagregationOption t) {

        this.getEntityManager().persist(t);
        return t;
    }

    public PeriodAgeDissagregationOption find(AgeDissagregationOptionPeriodId id) {
        return this.entityManager.find(entityClass, id);
    }

    public PeriodAgeDissagregationOption update(PeriodAgeDissagregationOption t) {
        return this.entityManager.merge(t);
    }

    public void remove(PeriodAgeDissagregationOption t) {
        t = this.entityManager.merge(t);
        this.entityManager.remove(t);
    }


    public List<PeriodAgeDissagregationOption> findAll() {
        CriteriaQuery<PeriodAgeDissagregationOption> criteria = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        criteria = criteria.select(criteria.from(getEntityClass()));
        return getEntityManager().createQuery(criteria).getResultList();
    }

    public List<PeriodAgeDissagregationOption> findAll(int start, int limit) {
        CriteriaQuery<PeriodAgeDissagregationOption> criteria = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        criteria = criteria.select(criteria.from(getEntityClass()));
        TypedQuery<PeriodAgeDissagregationOption> typedQuery = entityManager.createQuery(criteria);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(limit);
        return typedQuery.getResultList();
    }

    public long countAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(getEntityClass())));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    public void removeAll() {
        String jpql = String.format("delete from %s", getEntityClass());
        Query query = getEntityManager().createQuery(jpql);
        query.executeUpdate();
    }


    /**
     * Returns the table name for a given entity type in the {@link EntityManager}.
     *
     * @return
     */
    public String getTableName() {
        /*
         * Check if the specified class is present in the metamodel. Throws
         * IllegalArgumentException if not.
         */
        Metamodel meta = getEntityManager().getMetamodel();
        EntityType<PeriodAgeDissagregationOption> entityType = meta.entity(getEntityClass());

        // Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);


        return (t == null) ? entityType.getName().toUpperCase() : t.name();
    }

    /**
     * @return Returns the schema name for a given entity type in the {@link EntityManager}.
     */
    public String getSchemaName() {
        /*
         * Check if the specified class is present in the metamodel. Throws
         * IllegalArgumentException if not.
         */
        //Metamodel meta = getEntityManager().getMetamodel();
        //EntityType<T> entityType = meta.entity(getEntityClass());

        // Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);


        return (t == null) ? null : t.schema();
    }
}
