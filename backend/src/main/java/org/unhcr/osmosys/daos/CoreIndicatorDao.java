package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Audit;
import org.unhcr.osmosys.model.CoreIndicator;
import org.unhcr.osmosys.model.Indicator;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class CoreIndicatorDao extends GenericDaoJpa<CoreIndicator, Long> {

    public CoreIndicatorDao() {
        super(CoreIndicator.class, Long.class);
    }



    private static final String indicatorJpqlProjectAdm =
            " SELECT DISTINCT o" +
                    " FROM CoreIndicator o " ;

    public CoreIndicatorDao(Class<CoreIndicator> entityClass, Class<Long> entityPKClass) {
        super(entityClass, entityPKClass);
    }

    public List<CoreIndicator> getByState(State state) {

        String jpql = CoreIndicatorDao.indicatorJpqlProjectAdm +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, CoreIndicator.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


}
