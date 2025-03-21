package org.unhcr.osmosys.daos.standardDissagregations;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.model.standardDissagregations.options.*;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Dao Genérico
 */
@SuppressWarnings("unchecked")
@Stateless
public class StandardDissagregationOptionDao extends GenericDaoJpa<StandardDissagregationOption, Long> {

    public StandardDissagregationOptionDao() {
        super(StandardDissagregationOption.class, Long.class);

    }


    public List<StandardDissagregationOption> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM " + this.getEntityClass().getSimpleName() + " o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }



    public List<AgeDissagregationOption> getAgeOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM AgeOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<PopulationTypeDissagregationOption> getPopulationTypeOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM PopulationTypeOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<GenderDissagregationOption> getGenderOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM GenderOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<DiversityDissagregationOption> getDiversityOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM DiversityOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<CountryOfOriginDissagregationOption> getCountryOfOriginOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM CountryOfOriginOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<Canton> getCantonOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Canton o " +
                " WHERE o.state = :state" +
                " order by o.name ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }
    public List<Canton> getCantonOptions() {

        String jpql = "SELECT DISTINCT o FROM Canton o " +
                " order by o.name ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        return q.getResultList();
    }

    public List<Canton> getCantonByIds(List<Long> ids) {

        String jpql = "SELECT DISTINCT o FROM Canton o " +
                " left join fetch o.provincia p " +
                "WHERE o.id in (:ids)";
        Query q = getEntityManager().createQuery(jpql, Canton.class);
        q.setParameter("ids", ids);
        return q.getResultList();
    }

    public List<StandardDissagregationOption> getDissagregationOptionsByIds(List<Long> ids) {

        String jpql = "SELECT DISTINCT o FROM StandardDissagregationOption o " +
                "WHERE o.id in (:ids)";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("ids", ids);
        return q.getResultList();
    }

    public Canton getByCantonDescriptionAndProvinceDescription(String cantonDescription,String provinceDescription) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Canton o " +
                "WHERE " +
                " lower(o.name) = lower(:cantonDescription) "+
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

    public Canton discoverCanton(String codeCanton, String descriptionCanton, String codeProvincia, String descriptionProvincia) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o FROM Canton o " +
                "WHERE " +
                " o.code =:codeCanton" +
                " or (" +
                " lower(o.name) = lower(:descriptionCanton) "+
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
