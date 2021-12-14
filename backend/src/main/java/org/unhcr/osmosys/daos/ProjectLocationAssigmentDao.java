package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.ProjectLocationAssigment;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class ProjectLocationAssigmentDao extends GenericDaoJpa<ProjectLocationAssigment, Long> {
    public ProjectLocationAssigmentDao() {
        super(ProjectLocationAssigment.class, Long.class);
    }


    public List<ProjectLocationAssigment> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM ProjectLocationAssigment o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, ProjectLocationAssigment.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<ProjectLocationAssigment> getByProjectId(Long projectId) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM ProjectLocationAssigment o " +
                "WHERE o.project.id = :projectId";
        Query q = getEntityManager().createQuery(jpql, ProjectLocationAssigment.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }



}
