package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Project;
import org.unhcr.osmosys.webServices.model.ProjectResumeWeb;

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

    @SuppressWarnings("JpaQlInspection")
    private String projectResumeWebQuery = "SELECT pr.id, " +
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

    public Project getByName(String name) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Project o " +
                "WHERE lower(o.name) = lower(:name)";
        Query q = getEntityManager().createQuery(jpql, Project.class);
        q.setParameter("name", name);
        try {
            return (Project) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el mismo nombre " + name, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ProjectResumeWeb> getProjectResumenWebByPeriodId(Long periodId) throws GeneralAppException {

        String sql = this.projectResumeWebQuery + " WHERE pe.id =:periodId";
        Query q = getEntityManager().createNativeQuery(sql, "ProjectResumeWebMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }


}
