package org.unhcr.osmosys.daos.appConfig;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.appConfig.MenuItem;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class MenuItemDao extends GenericDaoJpa<MenuItem, Long> {
    public MenuItemDao() {
        super(MenuItem.class, Long.class);
    }


    private static final String jpqlMenuItems = "SELECT DISTINCT o " +
            " FROM MenuItem o " +
            " left join o.childrenItems o1 " +
            " left join o1.childrenItems o2 " +
            " left join o.assignedRoles ar " +
            " left join o.menuItemsOrganizationAssigments ao " +
            " left join ao.organization org "
            ;
    public List<MenuItem> getByState(State state) {

        String jpql = MenuItemDao.jpqlMenuItems +
                " where o.state= :state" ;
        Query q = getEntityManager().createQuery(jpql, MenuItem.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<MenuItem> getByState(Long organizationId, State state) {

        String jpql = MenuItemDao.jpqlMenuItems +

                " where o.state= :state " +
                "  and (ao.organization.id=:organizationId or o.isRectricted is true )" ;
        Query q = getEntityManager().createQuery(jpql, MenuItem.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


    public List<MenuItem> getMainMenus() {
        State state = State.ACTIVO;
        String jpql = MenuItemDao.jpqlMenuItems +
                " where o.state= :state and o.parentItem is null " ;

        Query q = getEntityManager().createQuery(jpql, MenuItem.class);
        q.setParameter("state", state);
        return q.getResultList();
    }
}
