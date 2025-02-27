package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.unhcr.osmosys.model.Organization;
import org.unhcr.osmosys.model.Project;
import org.unhcr.osmosys.webServices.model.MonthStateWeb;
import org.unhcr.osmosys.webServices.model.ProjectResumeWeb;
import org.unhcr.osmosys.webServices.model.QuarterStateWeb;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class ProjectDao extends GenericDaoJpa<Project, Long> {
    public ProjectDao() {
        super(Project.class, Long.class);
    }


    private final String projectResumeWebQuery = "SELECT pr.id, " +
            "pr.code, " +
            "pr.name, " +
            "pr.state, " +
            "org.id as organizationId, " +
            "org.description as organizationDescription, " +
            "org.acronym as organizationAcronym, " +
            "pe.id as periodId, " +
            "pe.year as periodYear, " +
            "pr.start_date as startDate, " +
            "pr.end_date as endDate " +
            // "pr.focal_point_id as focalPointId " +
            "FROM  " +
            "osmosys.projects pr " +
            "LEFT JOIN osmosys.organizations org ON pr.organization_id=org.id " +
            "LEFT JOIN osmosys.periods pe ON pr.period_id =pe.id ";

    public List<Project> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Project o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Project.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Project getByCode(String code) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Project o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Project.class);
        q.setParameter("code", code);
        try {
            return (Project) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Project getByNameAndPeriodId(String name, Long periodId) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Project o " +
                "WHERE lower(o.name) = lower(:name) " +
                " and o.period.id=:periodId";
        Query q = getEntityManager().createQuery(jpql, Project.class);
        q.setParameter("name", name);
        q.setParameter("periodId", periodId);
        try {
            return (Project) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el mismo nombre " + name, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ProjectResumeWeb> getProjectResumenWebByPeriodId(Long periodId) {

        String sql = this.projectResumeWebQuery + " WHERE pe.id =:periodId";
        Query q = getEntityManager().createNativeQuery(sql, "ProjectResumeWebMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<ProjectResumeWeb> getProjectResumenWebByPeriodIdAndFocalPointId(Long periodId, Long focalPointId) {

        String sql = this.projectResumeWebQuery
                +"LEFT JOIN osmosys.focal_point_assignation fpa ON pr.id = fpa.project_id "
                + " WHERE pe.id =:periodId and fpa.focal_pointer_id =:focalPointId";
        Query q = getEntityManager().createNativeQuery(sql, "ProjectResumeWebMapping");
        q.setParameter("periodId", periodId);
        q.setParameter("focalPointId", focalPointId);
        return q.getResultList();
    }

    public List<ProjectResumeWeb> getProjectResumenWebByPeriodIdAndOrganizationId(Long periodId, Long organizationId) {

        String sql = this.projectResumeWebQuery + " WHERE pe.id =:periodId and pr.organization_id =:organizationId ";
        Query q = getEntityManager().createNativeQuery(sql, "ProjectResumeWebMapping");
        q.setParameter("periodId", periodId);
        q.setParameter("organizationId", organizationId);
        return q.getResultList();
    }


    public List<Project> getByIds(List<Long> ids) {
        String sql = this.projectResumeWebQuery + " WHERE pe.id in (:ids)";
        Query q = getEntityManager().createNativeQuery(sql, "ProjectResumeWebMapping");
        q.setParameter("ids", ids);
        return q.getResultList();
    }

    public List<Project> getByPeriodId(Long periodId) {
        String jpql = "SELECT DISTINCT o FROM Project o " +
                " WHERE o.period.id =:periodId";
        Query q = getEntityManager().createQuery(jpql, Project.class);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<Project> getByPeriodIdWithDataToUpdateGeneralIndicator(Long periodId) {
        String jpql = "SELECT DISTINCT o FROM Project o " +
                " left join fetch o.indicatorExecutions ie " +
                " left join fetch ie.quarters q " +
                " left join fetch q.months mon " +
                " left join fetch mon.indicatorValues iv " +
                " left join fetch mon.indicatorValuesIndicatorValueCustomDissagregations ivc " +
                "  WHERE o.period.id =:periodId";
        Query q = getEntityManager().createQuery(jpql, Project.class);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public Project findWithData(Long projectId) {
        String jpql = "SELECT DISTINCT " +
                " o FROM Project o " +
                " left outer join fetch o.organization " +
                " left outer join fetch o.focalPointAssignations fpa " +
                " left outer join fetch fpa.focalPointer " +
                " left outer join fetch fpa.focalPointer fp " +
                " left outer join fetch fp.organization " +
                " left outer join fetch fp.office " +
                " left outer join fetch o.period pe " +
                " left outer join fetch pe.periodPopulationTypeDissagregationOptions " +
                " left outer join fetch pe.periodGenderDissagregationOptions " +
                " left outer join fetch pe.periodDiversityDissagregationOptions " +
                " left outer join fetch pe.periodCountryOfOriginDissagregationOptions " +
                " left outer join fetch pe.periodAgeDissagregationOptions " +
                " left outer join fetch pe.generalIndicator " +
                " left outer join fetch o.projectLocationAssigments pla " +
                " left outer join fetch pla.location " +
                " left outer join fetch pla.location can " +
                " left outer join fetch can.provincia " +
                " left outer join fetch can.office " +
                " WHERE o.id =:projectId";
        Query q = getEntityManager().createQuery(jpql, Project.class);
        q.setParameter("projectId", projectId);
        try {
            return (Project) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public List<QuarterStateWeb> getQuartersStateByProjectId(Long projectId) {

        String sql = "SELECT DISTINCT " +
                "	q.quarter, " +
                "	q.year, " +
                "	q.block_update, " +
                "	q.quarter_year_order  " +
                "FROM " +
                "	osmosys.indicator_executions ie " +
                "	INNER JOIN osmosys.quarters q ON ie.id = q.indicator_execution_id  " +
                "WHERE " +
                "	ie.project_id = :projectId  " +
                "	AND q.state = 'ACTIVO'  " +
                "ORDER BY " +
                "	q.quarter_year_order";
        Query q = getEntityManager().createNativeQuery(sql, "QuarterStateWebMapping");
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<MonthStateWeb> getMonthsStateByProjectId(Long projectId) {

        String sql = "SELECT  " +
                "mon.year, mon.month, mon.order_ as order, bool_and(mon.block_update) as blockupdate  " +
                "FROM  " +
                "osmosys.indicator_executions ie   " +
                "INNER JOIN osmosys.quarters q on ie.id = q.indicator_execution_id and ie.state='ACTIVO' AND q.state='ACTIVO'  " +
                "INNER JOIN osmosys.months mon on q.id=mon.quarter_id and mon.state='ACTIVO'  " +
                "WHERE ie.project_id = :projectId  " +
                "GROUP BY mon.year, mon.month, mon.order_  " +
                "ORDER BY mon.order_";
        Query q = getEntityManager().createNativeQuery(sql, "MonthStateWebMapping");
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<User> getFocalPointByPeriodId(Long periodId) {
        String jpql = "SELECT DISTINCT fpa FROM Project pr " +
                " inner join pr.focalPointAssignations o " +
                " inner join o.focalPointer fpa " +
                "  WHERE o.state =:state and pr.period.id=:periodId and pr.state=:state";
        Query q = getEntityManager().createQuery(jpql, User.class);
        q.setParameter("periodId", periodId);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }
    public List<User> getPartnerSupervisorsByPeriodId(Long periodId) {
        String jpql = "SELECT DISTINCT pm FROM Project pr " +
                " inner join pr.partnerManager pm " +
                "  WHERE pm.state =:state and pr.period.id=:periodId and pr.state=:state";
        Query q = getEntityManager().createQuery(jpql, User.class);
        q.setParameter("periodId", periodId);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }

    public List<ProjectResumeWeb> getProjectResumenWebByPeriodIdAndPartnerSupervisorId(Long periodId, Long partnerSupervisorId) {
        String sql = this.projectResumeWebQuery
                + " WHERE pe.id =:periodId and pr.partner_manager =:partnerSupervisorId";
        Query q = getEntityManager().createNativeQuery(sql, "ProjectResumeWebMapping");
        q.setParameter("periodId", periodId);
        q.setParameter("partnerSupervisorId", partnerSupervisorId);
        return q.getResultList();
    }

    public List<Organization> getActiveProjectsPartnersByPeriodId(Long periodId) {
        String jpql = "SELECT DISTINCT o FROM Project pr " +
                " inner join pr.organization o " +
                "  WHERE o.state =:state and pr.period.id=:periodId and pr.state=:state";
        Query q = getEntityManager().createQuery(jpql, Organization.class);
        q.setParameter("periodId", periodId);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }

    public List<Project> getProjectsByPeriodIdAndOrganizationId(Long periodId , Long organizationId) {
        String jpql = "SELECT DISTINCT pr FROM Project pr " +
                " inner join pr.organization o " +
                "  WHERE o.state =:state and pr.period.id=:periodId and o.id =:organizationId and pr.state=:state";
        Query q = getEntityManager().createQuery(jpql, Project.class);
        q.setParameter("periodId", periodId);
        q.setParameter("organizationId", organizationId);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }



}
