package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.unhcr.osmosys.model.IndicatorExecution;
import org.unhcr.osmosys.model.enums.Frecuency;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.QuarterEnum;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class IndicatorExecutionDao extends GenericDaoJpa<IndicatorExecution, Long> {
    public IndicatorExecutionDao() {
        super(IndicatorExecution.class, Long.class);
    }

    public List<IndicatorExecution> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralAndPerformanceIndicatorExecutionsByProjectId(Long projectId) {

        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters q " +
                " left join fetch o.period p " +
                " WHERE o.project.id = :projectId";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters q " +
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " WHERE o.project.id = :projectId" +
                " and o.indicatorType =: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectIdAndState(Long projectId, State state) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources sou " +
                " left join fetch m.indicatorValues iv " +
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " WHERE o.project.id = :projectId" +
                " and o.indicatorType =: generalType " +
                " and o.state =: state ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<IndicatorExecution> getPerformanceIndicatorExecutionsByProjectIdAndState(Long projectId, State state) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.projectStatement pst " +
                " left join fetch pst.area " +
                " left join fetch pst.situation " +
                " left join fetch pst.pillar " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources  " +
                " left join fetch m.indicatorValues iv " +
                " left join fetch iv.location can " +
                " left join fetch can.provincia " +
                // " left join fetch iv.location " +
                // " left join fetch m.indicatorValuesIndicatorValueCustomDissagregations ivc " +
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " left join fetch o.indicator " +

                " WHERE o.project.id = :projectId" +
                " and o.indicatorType <> :generalType " +
                " and o.state = :state "+
                " and iv.state = :state ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<IndicatorExecution> getPerformanceIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters " +
                " left join fetch o.period p " +
                " left join fetch o.indicator " +
                " WHERE o.project.id = :projectId" +
                " and o.indicatorType <>: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        return q.getResultList();
    }

    public IndicatorExecution getPerformanceIndicatorExecutionById(Long id) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters " +
                " left join fetch o.period p " +
                " left join fetch o.indicator " +
                " WHERE o.id = :id" +
                " and o.indicatorType <>: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("generalType", indicatorType);
        return (IndicatorExecution) q.getSingleResult();
    }

    public IndicatorExecution getByIdWithValues(Long id) {
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.indicator ind " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch  m.indicatorValues " +
                " left join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE o.id = :id";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        return (IndicatorExecution) q.getSingleResult();
    }

    public List<IndicatorExecution> getLateIndicatorExecutionGeneralByProjectIdMonthly(Long id, Integer yearToControl, List<MonthEnum> monthsToControl) {
        IndicatorType generalType = IndicatorType.GENERAL;
        State stateActive = State.ACTIVO;
        Frecuency frecuency = Frecuency.MENSUAL;
        String jpql = "SELECT " +
                " DISTINCT o FROM IndicatorExecution o " +
                " join fetch o.quarters q " +
                " join fetch q.months m " +
                " join fetch  m.indicatorValues " +
                " join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE " +
                " o.id = :id " +
                " and o.indicator.frecuency = :frecuency " +
                " and o.state = :stateActive " +
                " and m.state = :stateActive " +
                " and o.indicatorType = :generalType " +
                " and m.totalExecution is null " +
                " and ( m.year < :year or (m.year =:year and m.month in (:monthsToControl))) ";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("frecuency", frecuency);
        q.setParameter("stateActive", stateActive);
        q.setParameter("generalType", generalType);
        q.setParameter("monthsToControl", monthsToControl);
        q.setParameter("year", yearToControl);
        return q.getResultList();
    }

    public List<IndicatorExecution> getLateIndicatorExecutionPerformanceByProjectIdMonthly(Long id, Integer yearToControl, List<MonthEnum> monthsToControl) {
        if(CollectionUtils.isEmpty(monthsToControl)){
            return new ArrayList<>();
        }
        IndicatorType generalType = IndicatorType.GENERAL;
        State stateActive = State.ACTIVO;
        Frecuency frecuency = Frecuency.MENSUAL;
        String jpql = "SELECT " +
                " DISTINCT o FROM IndicatorExecution o " +
                " join fetch o.indicator ind " +
                " join fetch o.quarters q " +
                " join fetch q.months m " +
                " join fetch  m.indicatorValues " +
                " join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE " +
                " o.id = :id " +
                " and o.indicator.frecuency = :frecuency " +
                " and o.state = :stateActive " +
                " and m.state = :stateActive " +
                " and o.indicatorType <> :generalType " +
                " and m.totalExecution is null " +
                " and ( m.year < :year or (m.year =:year and m.month in (:monthsToControl))) ";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("frecuency", frecuency);
        q.setParameter("stateActive", stateActive);
        q.setParameter("generalType", generalType);
        q.setParameter("monthsToControl", monthsToControl);
        q.setParameter("year", yearToControl);
        return q.getResultList();
    }

    public List<IndicatorExecution> getLateIndicatorExecutionPerformanceByProjectIdQuarterly(Long id, Integer yearToControl, List<QuarterEnum> quartersToControl) {
        IndicatorType generalType = IndicatorType.GENERAL;
        State stateActive = State.ACTIVO;
        Frecuency frecuency = Frecuency.TRIMESTRAL;
        String jpql = "SELECT " +
                " DISTINCT o FROM IndicatorExecution o " +
                " join fetch o.indicator ind " +
                " join fetch o.quarters q " +
                " join fetch q.months m " +
                " join fetch  m.indicatorValues " +
                " join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE " +
                " o.id = :id " +
                " and o.indicator.frecuency = :frecuency " +
                " and o.state = :stateActive " +
                " and m.state = :stateActive " +
                " and o.indicatorType <> :generalType " +
                " and m.totalExecution is null ";

        if (CollectionUtils.isNotEmpty(quartersToControl)) {
            jpql += " and ( m.year < :year or (m.year =:year and q.quarter in (:quartersToControl))) ";
        }else {
            jpql += " and ( m.year < :year )) ";
        }

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("frecuency", frecuency);
        q.setParameter("stateActive", stateActive);
        q.setParameter("generalType", generalType);

        q.setParameter("year", yearToControl);
        if (CollectionUtils.isNotEmpty(quartersToControl)) {
            q.setParameter("quartersToControl", quartersToControl);
        }
        return q.getResultList();
    }

    public List<IndicatorExecution> getLateIndicatorExecutionPerformanceByProjectIdBiannual(Long id, Integer yearToControl, List<QuarterEnum> quartersToControl) {
        IndicatorType generalType = IndicatorType.GENERAL;
        State stateActive = State.ACTIVO;
        Frecuency frecuency = Frecuency.SEMESTRAL;
        String jpql = "SELECT " +
                " DISTINCT o FROM IndicatorExecution o " +
                " join fetch o.indicator ind " +
                " join fetch o.quarters q " +
                " join fetch q.months m " +
                " join fetch  m.indicatorValues " +
                " join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE " +
                " o.id = :id " +
                " and o.indicator.frecuency = :frecuency " +
                " and o.state = :stateActive " +
                " and m.state = :stateActive " +
                " and o.indicatorType <> :generalType " +
                " and m.totalExecution is null ";
        if (CollectionUtils.isEmpty(quartersToControl)) {
            jpql = jpql + " and m.year < :year  ";
        } else {
            jpql = jpql + " and ( m.year < :year or (m.year =:year and q.quarter in (:quartersToControl))) ";
        }


        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("frecuency", frecuency);
        q.setParameter("stateActive", stateActive);
        q.setParameter("generalType", generalType);

        q.setParameter("year", yearToControl);
        if (CollectionUtils.isEmpty(quartersToControl)) {
            q.setParameter("quartersToControl", quartersToControl);        }
        return q.getResultList();
    }

    public List<IndicatorExecution> getLateIndicatorExecutionPerformanceByProjectIdAnnual(Long id, Integer yearToControl) {
        IndicatorType generalType = IndicatorType.GENERAL;
        State stateActive = State.ACTIVO;
        Frecuency frecuency = Frecuency.ANUAL;
        String jpql = "SELECT " +
                " DISTINCT o FROM IndicatorExecution o " +
                " join fetch o.indicator ind " +
                " join fetch o.quarters q " +
                " join fetch q.months m " +
                " join fetch  m.indicatorValues " +
                " join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE " +
                " o.id = :id " +
                " and o.indicator.frecuency = :frecuency " +
                " and o.state = :stateActive " +
                " and m.state = :stateActive " +
                " and o.indicatorType <> :generalType " +
                " and m.totalExecution is null " +
                " and m.year < :year ";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("frecuency", frecuency);
        q.setParameter("stateActive", stateActive);
        q.setParameter("generalType", generalType);
        q.setParameter("year", yearToControl);
        return q.getResultList();
    }

    public List<IndicatorExecution> getAllDirectImplementationIndicatorByPeriodId(Long periodId) {
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.indicator i " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch o.period p " +
                " left join fetch o.reportingOffice offf " +
                " left join fetch o.assignedUser u " +
                " left join fetch o.assignedUser ub " +
                " WHERE p.id = :periodId";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }
}
