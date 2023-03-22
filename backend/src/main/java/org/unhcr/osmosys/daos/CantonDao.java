package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Canton;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class CantonDao extends GenericDaoJpa<Canton, Long> {

    private static final Logger LOGGER = Logger.getLogger(CantonDao.class);
    public CantonDao() {
        super(Canton.class, Long.class);
    }

    public List<Canton> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Canton o " +
                " left outer join fetch o.provincia "+
                " left outer join fetch o.office "+
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Canton.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Canton getByCode(String code) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Canton o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Canton.class);
        q.setParameter("code", code);
        try {
            return (Canton) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    public Canton getByCantonDescriptionAndProvinceDescription(String cantonDescription,String provinceDescription) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Canton o " +
                "WHERE " +
                " lower(o.description) = lower(:cantonDescription) "+
                " and lower(o.provincia.description) = lower(:provinceDescription)";
        Query q = getEntityManager().createQuery(jpql, Canton.class);
        q.setParameter("cantonDescription", cantonDescription);
        q.setParameter("provinceDescription", provinceDescription);

        try {
            return (Canton) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con  " + cantonDescription+"-"+provinceDescription, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Canton> getByIds(List<Long> ids) {

        String jpql = "SELECT DISTINCT o FROM Canton o " +
                "WHERE o.id in (:ids)";
        Query q = getEntityManager().createQuery(jpql, Canton.class);
        q.setParameter("ids", ids);
        return q.getResultList();
    }


    public Canton discoverCanton(String codeCanton, String descriptionCanton, String codeProvincia, String descriptionProvincia) throws GeneralAppException {
        LOGGER.info(descriptionCanton+" "+descriptionProvincia+" "+codeCanton);
        String jpql = "SELECT DISTINCT o FROM Canton o " +
                "WHERE " +
                " o.code =:codeCanton" +
                " or (" +
                " lower(o.description) = lower(:descriptionCanton) "+
                " and lower(o.provincia.description) = lower(:descriptionProvincia)" +
                " )";
        Query q = getEntityManager().createQuery(jpql, Canton.class);
        q.setParameter("descriptionCanton", descriptionCanton!=null?descriptionCanton:"");
        q.setParameter("descriptionProvincia", descriptionProvincia!=null?descriptionProvincia:"");
        q.setParameter("codeCanton", codeCanton!=null?codeCanton:"");



        try {
            return (Canton) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con  " + codeCanton+"-"+descriptionCanton, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
