package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.Frecuency;
import org.unhcr.osmosys.model.enums.MonthEnum;
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

    public List<Month> getbyCustomDissagregationId(Long customDissagregationId) {
        String jpql = "SELECT DISTINCT m " +
                " FROM Month m" +
                " inner join m.quarter q " +
                " inner join fetch m.indicatorValuesIndicatorValueCustomDissagregations ivc " +
                " inner join fetch ivc.customDissagregationOption cdo" +
                " inner join fetch cdo.customDissagregation cd " +
                " where " +
                " cd.id=:customDissagregationId ";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("customDissagregationId", customDissagregationId);
        return q.getResultList();
    }

    public IndicatorExecution getIndicatorExecutionByMonthId(Long monhId) {
        String jpql = "SELECT DISTINCT o " +
                " FROM IndicatorExecution o" +
                " inner join o.quarters q " +
                " inner join q.months m " +
                " where " +
                " m.id=:monhId ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("monhId", monhId);
        return (IndicatorExecution) q.getSingleResult();
    }

    public Object findMonthRelatedProject(Long idValue) {
        // JPQL de la consulta
        String jpql = "SELECT new map( "
                + "p.code AS Código_de_Proyecto, "
                + "p.name AS Nombre_Proyecto, "
                + "i.code AS Código_de_Indicador, "
                + "i.description AS Descripción_de_Indicador, "
                + "o.acronym AS Organización_Acrónimo, "
                + "o.description AS Organización_Descr, "
                + "q.year AS Periodo, "
                + "q.quarter AS Trimestre, "
                + "m.month AS Mes, "
                + "m.blockUpdate AS Bloqueo_de_mes "
                + ") "
                + "FROM Project p "
                + "JOIN p.indicatorExecutions ie "
                + "JOIN ie.indicator i "
                + "JOIN p.organization o "
                + "JOIN ie.quarters q "
                + "JOIN q.months m "
                + "WHERE m.id = :idValue "
                + "AND ie.project.id = p.id "
                + "AND ie.indicator.id = i.id "
                + "AND o.id = p.organization.id "
                + "AND q.indicatorExecution.id = ie.id "
                + "AND q.id = m.quarter.id ";

        // Ejecutar la consulta con el parámetro `idValue`
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("idValue", idValue);
        // Obtener los resultados como una lista de Mapas
        List<?> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public List<?> findIndicatorsRelatedProjectByMonth(Long idProject, MonthEnum monthEnum, int year) {
        // JPQL de la consulta
        String jpql = "SELECT new map( "
                + "p.code AS Código_de_Proyecto, "
                + "p.name AS Nombre_Proyecto, "
                + "i.code AS Código_de_Indicador, "
                + "i.description AS Descripción_de_Indicador, "
                + "o.acronym AS Organización_Acrónimo, "
                + "o.description AS Organización_Descr, "
                + "q.year AS Periodo, "
                + "q.quarter AS Trimestre, "
                + "m.month AS Mes, "
                + "m.blockUpdate AS Bloqueo_de_mes "
                + ") "
                + "FROM Project p "
                + "JOIN p.indicatorExecutions ie "
                + "JOIN ie.indicator i "
                + "JOIN p.organization o "
                + "JOIN ie.quarters q "
                + "JOIN q.months m "
                + "WHERE m.month = :monthEnum "
                + "AND p.id = :idProject "
                + "AND q.year = :year "
                + "AND ie.project.id = p.id "
                + "AND ie.indicator.id = i.id "
                + "AND o.id = p.organization.id "
                + "AND q.indicatorExecution.id = ie.id "
                + "AND q.id = m.quarter.id ";

        // Ejecutar la consulta con el parámetro `idValue`
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("monthEnum", monthEnum);
        query.setParameter("idProject", idProject);
        query.setParameter("year", year);
        // Obtener los resultados como una lista de Mapas
        List<?> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        }
        return results;
    }


}
