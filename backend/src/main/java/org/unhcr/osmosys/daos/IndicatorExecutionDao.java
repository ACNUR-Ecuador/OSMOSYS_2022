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
import javax.persistence.NoResultException;
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
                " left join fetch o.projectStatement pst " +
                " left join fetch pst.area " +
                " left join fetch pst.situation " +
                " left join fetch pst.pillar " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources sou " +
                " left join fetch m.indicatorValues iv " +
                " left join fetch iv.location can " +
                " left join fetch can.provincia " +
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " left join fetch o.indicator " +
                " WHERE o.project.id = :projectId";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        State active = State.ACTIVO;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources sou " +
                " left join fetch m.indicatorValues iv " +
                " left join fetch iv.location can " +
                " left join fetch can.provincia " +
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " WHERE o.project.id = :projectId" +
                " and o.indicatorType = :generalType " +
                " and iv.state = :active ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("active", active);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectIdAndState(Long projectId, State state) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources sou " +
                " left join fetch m.indicatorValues iv " +
                " left join fetch iv.location can " +
                " left join fetch can.provincia " +
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " WHERE o.project.id = :projectId" +
                " and o.state = :state " +
                " and iv.state = :state " +
                " and o.indicatorType =: generalType ";
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
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " left join fetch o.indicator " +

                " WHERE o.project.id = :projectId" +
                " and o.indicatorType <> :generalType " +
                " and o.state = :state " +
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
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " left join fetch o.indicator " +
                " WHERE o.project.id = :projectId" +
                " and o.indicatorType <>: generalType " +
                " and iv.state = :state ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }

    public IndicatorExecution getPerformanceIndicatorExecutionById(Long id) {
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
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
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
                " left join fetch m.sources " +
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
                " left join fetch m.sources " +
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
        if (CollectionUtils.isEmpty(monthsToControl)) {
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
                " left join fetch m.sources " +
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
                " left join fetch m.sources " +
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
        } else {
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
                " left join fetch m.sources " +
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
            q.setParameter("quartersToControl", quartersToControl);
        }
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
                " left join fetch m.sources " +
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
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.indicator i " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources " +
                " left join fetch o.period p " +
                " left join fetch o.reportingOffice offf " +
                " left join fetch o.supervisorUser su " +
                " left join fetch o.assignedUser u " +
                " left join fetch o.assignedUser ub " +
                " left join fetch  m.indicatorValues " +
                " left join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        return q.getResultList();
    }

    public IndicatorExecution getByIndicatorIdAndOfficeId(Long indicatorId, Long reportingOfficeId) {
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " WHERE o.indicator.id = :indicatorId and o.reportingOffice.id = :reportingOfficeId";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("indicatorId", indicatorId);
        q.setParameter("reportingOfficeId", reportingOfficeId);

        try {
            return (IndicatorExecution) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<IndicatorExecution> getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
            Long userId,
            Long periodId,
            Long officeId,
            boolean supervisor,
            boolean responsible,
            boolean backup) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.markers " +
                " left join fetch o.indicator i " +
                " left join fetch i.statement " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources " +
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " left join fetch o.reportingOffice offf " +
                " left join fetch o.supervisorUser su " +
                " left join fetch su.organization " +
                " left join fetch su.office " +
                " left join fetch o.assignedUser u " +
                " left join fetch u.organization " +
                " left join fetch u.office " +
                " left join fetch o.assignedUserBackup ub " +
                " left join fetch ub.organization " +
                " left join fetch ub.office " +
                " left join fetch  m.indicatorValues iv " +
                " left join fetch  m.indicatorValuesIndicatorValueCustomDissagregations ivc " +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null " +
                " and o.state =:state " +
                " and q.state =:state " +
                " and m.state =:state " +
                " and (iv.state is null  or iv.state =:state) " +
                " and (ivc.state is null  or ivc.state =:state) ";

        if (officeId != null) {
            jpql += " and offf.id =:officeId ";
        }
        if (userId != null) {
            if (responsible && backup && supervisor) {
                jpql += " and (u.id =:userId or ub.id =:userId or su.id =:userId ) ";
            } else if (responsible && backup) {
                jpql += " and (u.id =:userId or ub.id =:userId) ";
            } else if (responsible && supervisor) {
                jpql += " and (u.id =:userId or su.id =:userId ) ";
            } else if (responsible) {
                jpql += " and (u.id =:userId) ";
            } else if (backup && supervisor) {
                jpql += " and (ub.id =:userId or su.id =:userId ) ";
            } else if (backup) {
                jpql += " and (ub.id =:userId) ";
            } else if (supervisor) {
                jpql += " and ( su.id =:userId ) ";
            }
        }
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        q.setParameter("state", State.ACTIVO);
        if (officeId != null) {
            q.setParameter("officeId", officeId);
        }
        if (userId != null) {
            q.setParameter("userId", userId);
        }
        return q.getResultList();
    }
}
