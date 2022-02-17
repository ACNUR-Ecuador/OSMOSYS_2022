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

    private static final String jpqlProjectIndicators =
            " SELECT DISTINCT o FROM IndicatorExecution o " +
                    " left join fetch o.indicator i " +
                    " left join fetch i.statement ist " +
                    " left join fetch ist.area " +
                    " left join fetch o.projectStatement pst " +
                    " left join fetch o.indicatorExecutionLocationAssigments iela " +
                    " left join fetch iela.location can " +
                    " left join fetch can.provincia prov " +
                    " left join fetch pst.area psta" +
                    " left join fetch pst.situation pstsit " +
                    " left join fetch pst.pillar  pstpil" +
                    " left join fetch o.quarters q " +
                    " left join fetch q.months m " +
                    " left join fetch m.sources sou " +
                    " left join fetch o.period p " +
                    " left join fetch p.generalIndicator " +
                    " left join fetch o.indicator ";
    private static final String jpqlDirectImplementationIndicators =
            "SELECT DISTINCT o FROM IndicatorExecution o " +
                    " left join fetch o.indicatorExecutionLocationAssigments iela " +
                    " left join fetch iela.location can " +
                    " left join fetch can.provincia prov " +
                    " left join fetch o.markers " +
                    " left join fetch o.indicator i " +
                    " left join fetch i.statement st " +
                    " left join fetch st.area " +
                    " left join fetch o.quarters q " +
                    " left join fetch q.months m " +
                    " left join fetch m.sources " +
                    " left join fetch o.period p " +
                    " left join fetch p.generalIndicator " +
                    " left join fetch o.reportingOffice repOff " +
                    " left join fetch o.supervisorUser su " +
                    " left join fetch su.organization suorg " +
                    " left join fetch su.office suofff " +
                    " left join fetch o.assignedUser au " +
                    " left join fetch au.organization auorg" +
                    " left join fetch au.office auoff" +
                    " left join fetch o.assignedUserBackup aub " +
                    " left join fetch aub.organization auborg " +
                    " left join fetch aub.office aubofff ";

    public List<IndicatorExecution> getGeneralAndPerformanceIndicatorExecutionsByProjectId(Long projectId) {

        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.project.id = :projectId";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        State active = State.ACTIVO;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.project.id = :projectId" +
                " and o.indicatorType = :generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("active", active);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectIdAndState(Long projectId, State state) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.project.id = :projectId" +
                " and o.state = :state " +
                " and o.indicatorType =: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("state", state);

        return q.getResultList();
    }

    public List<IndicatorExecution> getPerformanceIndicatorExecutionsByProjectIdAndState(Long projectId, State state) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +

                " WHERE o.project.id = :projectId" +
                " and o.indicatorType <> :generalType " +
                " and o.state = :state ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<IndicatorExecution> getPerformanceIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.project.id = :projectId" +
                " and o.indicatorType <>: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }

    public IndicatorExecution getPerformanceIndicatorExecutionById(Long id) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.id = :id" +
                " and o.indicatorType <>: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("generalType", indicatorType);
        return (IndicatorExecution) q.getSingleResult();
    }

    // todo
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
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
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

    // todo
    public List<IndicatorExecution> getLateIndicatorExecutionPerformanceByProjectIdMonthly(Long id, Integer yearToControl, List<MonthEnum> monthsToControl) {
        if (CollectionUtils.isEmpty(monthsToControl)) {
            return new ArrayList<>();
        }
        IndicatorType generalType = IndicatorType.GENERAL;
        State stateActive = State.ACTIVO;
        Frecuency frecuency = Frecuency.MENSUAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
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
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
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
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
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
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
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

    // todo
    public List<IndicatorExecution> getActiveProjectIndicatorExecutionsByPeriodId(Long periodId) {
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " left join fetch o.project pr " +
                " left join fetch pr.organization org " +
                " left join fetch pr.focalPoint fpu " +
                " left join fetch fpu.organization fpuorg " +
                " left join fetch pr.projectLocationAssigments pla" +
                " left join fetch pla.location canpl" +
                " left join fetch canpl.provincia " +
                " WHERE o.project.state =:state " +
                " and o.state =:state " +
                " and (pla.state is null or pla.state =:state )" +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " and p.id =:periodId " +
                " order by org.acronym, org.description, " +
                " pr.code, pr.name, o.indicatorType, o.projectStatement.code , i.code  ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }


    /*** direct implementation**********/
    public List<IndicatorExecution> getAllDirectImplementationIndicatorByPeriodId(Long periodId) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        return q.getResultList();
    }

    public IndicatorExecution getByIndicatorIdAndOfficeId(Long indicatorId, Long reportingOfficeId) {

        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
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
        String jpql =
                IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                        " WHERE p.id = :periodId " +
                        " and o.indicatorType <> :generalType " +
                        " and o.project.id is null " +
                        " and o.state =:state " +
                        " and q.state =:state " +
                        " and m.state =:state ";

        if (officeId != null) {
            jpql += " and offf.id =:officeId ";
        }
        if (userId != null) {
            if (responsible && backup && supervisor) {
                jpql += " and (au.id =:userId or aub.id =:userId or su.id =:userId ) ";
            } else if (responsible && backup) {
                jpql += " and (au.id =:userId or aub.id =:userId) ";
            } else if (responsible && supervisor) {
                jpql += " and (au.id =:userId or su.id =:userId ) ";
            } else if (responsible) {
                jpql += " and (au.id =:userId) ";
            } else if (backup && supervisor) {
                jpql += " and (aub.id =:userId or su.id =:userId ) ";
            } else if (backup) {
                jpql += " and (aub.id =:userId) ";
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

    public List<IndicatorExecution> getAllIndicatorDirectImplementation(Long periodId) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project is null " +
                " and o.state =:state ";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }

    public List<IndicatorExecution> getDirectImplementationIndicatorExecutionsByIds(List<Long> indicatorExecutionIds) {

        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE o.id in (:indicatorExecutionIds) " +
                " and o.state=:state " +
                " and o.indicatorType <> :generalType " +
                " and o.project is null ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("generalType", generalType);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("indicatorExecutionIds", indicatorExecutionIds);
        return q.getResultList();
    }

    public IndicatorExecution getDirectImplementationIndicatorExecutionsById(Long indicatorExecutionId) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE o.id =:indicatorExecutionId " +
                " and o.project is null ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("indicatorExecutionId", indicatorExecutionId);
        try {
            return (IndicatorExecution) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
