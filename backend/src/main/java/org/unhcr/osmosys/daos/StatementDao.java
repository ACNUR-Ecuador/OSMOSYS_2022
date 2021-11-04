package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Statement;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class StatementDao extends GenericDaoJpa<Statement, Long> {
    public StatementDao() {
        super(Statement.class, Long.class);
    }

    public List<Statement> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Statement o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<Statement> getByAreaType(AreaType areaType) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Statement o " +
                "WHERE o.areaType = :areaType ";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("areaType", areaType);

        return q.getResultList();

    }

    public Statement getByShortDescription(String shortDescription) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Statement o " +
                "WHERE lower(o.shortDescription) = lower(:shortDescription)";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("shortDescription", shortDescription);
        try {
            return (Statement) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la descripción corta " + shortDescription, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Statement getByDescription(String description) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Statement o " +
                "WHERE lower(o.description) = lower(:description)";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("description", description);
        try {
            return (Statement) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la descripción  " + description, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    public Statement getByCode(String code) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o FROM Statement o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("code", code);
        try {
            return (Statement) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código  " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
