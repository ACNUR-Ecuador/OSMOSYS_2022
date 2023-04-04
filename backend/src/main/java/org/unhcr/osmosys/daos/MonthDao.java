package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.CustomDissagregationAssignationToIndicatorExecution;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicatorExecution;
import org.unhcr.osmosys.model.Month;
import org.unhcr.osmosys.model.enums.Frecuency;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.webServices.model.MonthStateWeb;
import org.unhcr.osmosys.webServices.model.YearMonthDTO;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class MonthDao extends GenericDaoJpa<Month, Long> {
    public MonthDao() {
        super(Month.class, Long.class);
    }

    public List<Month> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Month o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<Month> getMonthsIndicatorExecutionId(Long indicatorExecutionId, State state) {
        String jpql = "SELECT DISTINCT o FROM Month o " +
                " left outer join fetch o.sources " +
                "WHERE o.state = :state" +
                " and o.quarter.indicatorExecution.id =: indicatorExecutionId";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("state", state);
        q.setParameter("indicatorExecutionId", indicatorExecutionId);
        return q.getResultList();
    }

    public List<Month> getMonthsIndicatorExecutionId(Long indicatorExecutionId) {
        String jpql = "SELECT DISTINCT o FROM Month o " +
                " left outer join fetch o.sources " +
                "WHERE o.quarter.indicatorExecution.id =: indicatorExecutionId";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("indicatorExecutionId", indicatorExecutionId);
        return q.getResultList();
    }

    @Override
    public Month find(Long monthId) {
        String jpql = "SELECT DISTINCT o FROM Month o " +
                " left outer join fetch o.sources " +
                "WHERE o.id =: monthId";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("monthId", monthId);
        try {
            return (Month) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<DissagregationAssignationToIndicatorExecution> getDissagregationsByMonthId(Long monthId) {

        String jpql = "SELECT DISTINCT o " +
                " FROM DissagregationAssignationToIndicatorExecution o " +
                " inner join fetch o.indicatorExecution ie " +
                " inner join fetch ie.quarters q " +
                " inner join fetch q.months mon " +
                " WHERE mon.id = :monthId " +
                " and o.state =:state ";
        Query q = getEntityManager().createQuery(jpql, DissagregationAssignationToIndicatorExecution.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("monthId", monthId);
        return q.getResultList();
    }

    public List<CustomDissagregationAssignationToIndicatorExecution> getCustomDissagregationsByMonthId(Long monthId) {

        String jpql = "SELECT DISTINCT o " +
                " FROM CustomDissagregationAssignationToIndicatorExecution o " +
                " inner join fetch o.indicatorExecution ie " +
                " inner join fetch o.customDissagregation " +
                " inner join fetch ie.quarters q " +
                " inner join fetch q.months mon " +
                " WHERE mon.id = :monthId " +
                " and o.state =:state ";
        Query q = getEntityManager().createQuery(jpql, CustomDissagregationAssignationToIndicatorExecution.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("monthId", monthId);
        return q.getResultList();
    }

    public List<Month> getActiveMonthsByProjectIdAndMonthAndYear(Long projectId, MonthEnum month, int year) {
        String jpql = "SELECT DISTINCT m " +
                " FROM Month m" +
                " inner join m.quarter q " +
                " inner join q.indicatorExecution ie " +
                " where ie.project.id = :projectId " +
                " and m.month = :monthE " +
                " and m.year = :yearI " +
                " and ie.state = :state " +
                " and q.state = :state " +
                " and m.state = :state ";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("monthE", month);
        q.setParameter("yearI", year);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<Month> getActiveMonthsAndMonthAndYearAndBlockingStatusAndFrecuency(MonthEnum month, int year, boolean blocked, Frecuency frecuency) {
        String jpql = "SELECT DISTINCT m " +
                " FROM Month m" +
                " inner join m.quarter q " +
                " inner join q.indicatorExecution ie " +
                " inner join ie.indicator i" +
                " where " +
                " m.month = :monthE " +
                " and m.blockUpdate =:blocked" +
                " and m.year = :yearI " +
                " and ie.state = :state " +
                " and q.state = :state " +
                " and m.state = :state" +
                " and i.frecuency=:frecuency ";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("monthE", month);
        q.setParameter("yearI", year);
        q.setParameter("blocked", blocked);
        q.setParameter("frecuency", frecuency);
        return q.getResultList();
    }

    public List<Month> getActiveMonthsAndMonthAndYearAndBlockingStatusGeneralIndicators(MonthEnum month, int year, boolean blocked) {
        String jpql = "SELECT DISTINCT m " +
                " FROM Month m" +
                " inner join m.quarter q " +
                " inner join q.indicatorExecution ie " +
                " where " +
                " m.month = :monthE " +
                " and m.blockUpdate =:blocked" +
                " and m.year = :yearI " +
                " and ie.state = :state " +
                " and q.state = :state " +
                " and m.state = :state" +
                " and ie.indicator  is null ";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("monthE", month);
        q.setParameter("yearI", year);
        q.setParameter("blocked", blocked);
        return q.getResultList();
    }

    public List<Month> getActiveGeneralIndicatorMonthsAndMonthAndYearAndBlockingStatusAndFrecuency(MonthEnum month, int year, boolean blocked) {
        String jpql = "SELECT DISTINCT m " +
                " FROM Month m" +
                " inner join m.quarter q " +
                " inner join q.indicatorExecution ie " +
                " inner join ie.period p " +
                " where " +
                " m.month = :monthE " +
                " and m.blockUpdate =:blocked" +
                " and ie.indicator is null " +
                " and m.year = :yearI " +
                " and ie.state = :state " +
                " and q.state = :state " +
                " and m.state = :state";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("monthE", month);
        q.setParameter("yearI", year);
        q.setParameter("blocked", blocked);
        return q.getResultList();
    }

    public List<YearMonthDTO> getYearMonthDTOSByPeriodId(Long periodId) {

        String sql = "SELECT " +
                "distinct m.year, m.month, m.month_year_order as monthYearOrder " +
                "FROM " +
                "osmosys.indicator_executions ie  " +
                "INNER JOIN osmosys.quarters q on ie.id=q.indicator_execution_id and ie.state='ACTIVO' AND q.state='ACTIVO' " +
                "INNER JOIN osmosys.months m on q.id=m.quarter_id and m.state='ACTIVO' " +
                "WHERE ie.period_id=:periodId  " +
                " ORDER BY m.year, m.month_year_order";
        Query q = getEntityManager().createNativeQuery(sql, "YearMonthMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }
}
