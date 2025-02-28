package com.sagatechs.generics.security.dao;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.model.User;
import org.unhcr.osmosys.model.Indicator;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class UserDao extends GenericDaoJpa<User, Long> {

    public UserDao() {
        super(User.class, Long.class);

    }

    /**
     * Busca el usuario por su nombre de usuario y pasword hasheado con roles
     *
     * @param username nombre de usuario
     * @param password contraseña
     * @return usuario
     */
    public User findByUserNameAndPasswordWithRoles(String username, byte[] password, State state) {

        String jpql = "SELECT DISTINCT o FROM User o " +
                " left outer join fetch o.roleAssigments ra " +
                " left outer join fetch ra.role " +
                " WHERE o.username = :username AND o.password = :password AND o.state = :state"
                + " and ra.state = :state and ra.role.state = :state";
        Query q = getEntityManager().createQuery(jpql, User.class);
        q.setParameter("username", username);
        q.setParameter("password", password);
        q.setParameter("state", state);

        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    ////*****************************************olds*****************////////////


    /**
     * Busca el usuario por su nombre de usuario y pasword hasheado
     *
     * @param username nombre de usuario
     * @param password contraseña
     * @return usuario
     */
    public User findByUserNameAndPassword(String username, byte[] password) {

        String jpql = "SELECT DISTINCT o FROM User o left outer join fetch o.roleAssigments ra join fetch ra.role  WHERE o.username = :username AND o.password = :password";
        Query q = getEntityManager().createQuery(jpql, User.class);
        q.setParameter("username", username);
        q.setParameter("password", password);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    /**
     * Busca el usuario por su nombre de usuario y pasword hasheado con roles
     *
     * @param username nombre de usuario
     * @param state    estado
     * @return usuario
     */
    public User findByUserNameWithRoles(String username, State state) {

        String jpql = "SELECT DISTINCT o FROM User o left outer join fetch o.roleAssigments ra " +
                "join fetch ra.role  WHERE o.username = :username AND o.state = :state and ra.state = :state and ra.role.state = :state  ";
        Query q = getEntityManager().createQuery(jpql, User.class);
        q.setParameter("username", username);
        q.setParameter("state", state);

        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Obtiene los roles asignados, de roles y asignaciona que tengan el estado dado
     *
     * @param username nombre de usuario
     * @param state    estado
     * @return los roles asignados, de roles y asignaciona que tengan el estado dado
     */
    @SuppressWarnings("unchecked")
    public List<RoleType> getRoleTypesByUsernameAndState(String username, State state) {
        String jpql = "SELECT  DISTINCT r.roleType FROM User o  JOIN o.roleAssigments ra  JOIN ra.role r WHERE o.username = :username and ra.state = :state AND r.state = :state";
        Query q = getEntityManager().createQuery(jpql);
        q.setParameter("username", username);
        q.setParameter("state", state);
        return q.getResultList();
    }

    /**
     * Recupera un usuario por su nombre de usuario
     *
     * @param username nombre de usuario
     * @return usuario por su nombre de usuario
     */
    public User findByUserName(String username) {

        String jpql = "SELECT DISTINCT o FROM User o WHERE o.username = :username";
        Query q = getEntityManager().createQuery(jpql, User.class);
        q.setParameter("username", username);

        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recupera el usuario con roles
     *
     * @param username nombre de usuario
     * @return usuario con roles
     */
    public User findByUserNameWithRole(String username) {

        String jpql = "SELECT DISTINCT o FROM User o LEFT JOIN FETCH  o.roleAssigments ra LEFT JOIN FETCH  ra.role r WHERE o.username = :username";
        Query q = getEntityManager().createQuery(jpql, User.class);
        q.setParameter("username", username);

        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<User> findAllWithRoles() {

        String jpql = "SELECT DISTINCT o FROM User o LEFT JOIN FETCH  o.roleAssigments ra LEFT JOIN FETCH  ra.role r order by o.username";
        Query q = getEntityManager().createQuery(jpql, User.class);

        return q.getResultList();
    }

    public User getByEmail(String email) {
        String jpql = "SELECT DISTINCT o FROM User o WHERE o.email = :email";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }


    public List<User> getUNHCRUsersByState(State state) {
        String jpql = "SELECT DISTINCT o FROM User o " +
                " left outer join fetch o.roleAssigments ra " +
                " left outer join fetch ra.role ro " +
                " left outer join fetch o.office " +
                " left outer join fetch o.organization " +
                " WHERE (o.organization is null or lower(o.organization.acronym)='unhcr'  or lower(o.organization.acronym)='acnur' ) and o.state=:state ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("state", state);
        return query.getResultList();
    }

    public List<User> getUNHCRUsersByOfficeIdAndState(State state, Long officeId) {
        String jpql = "SELECT DISTINCT o FROM User o " +
                " left outer join fetch o.roleAssigments ra " +
                " left outer join fetch ra.role ro " +
                " left outer join fetch o.office off " +
                " left outer join fetch o.organization " +
                " WHERE off.id=:officeId " +
                " and ( o.organization is null or lower(o.organization.acronym)='unhcr'  or lower(o.organization.acronym)='acnur' ) " +
                " and o.state=:state ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("state", state);
        query.setParameter("officeId", officeId);
        return query.getResultList();
    }

    public User getUNHCRUsersByName(String name) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o FROM User o " +
                " left outer join fetch o.roleAssigments ra " +
                " left outer join fetch ra.role ro " +
                " left outer join fetch o.office off " +
                " left outer join fetch o.organization " +
                " WHERE  ( o.organization is null or lower(o.organization.acronym)='unhcr'  or lower(o.organization.acronym)='acnur' ) " +
                " and  lower(o.name)=lower(:name) ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("name", name);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontrás de un usuario con este nombre:" + name);
        }
    }

    public User getUNHCRUsersByUsername(String username) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o FROM User o " +
                " left outer join fetch o.roleAssigments ra " +
                " left outer join fetch ra.role ro " +
                " left outer join fetch o.office off " +
                " left outer join fetch o.organization " +
                " WHERE  ( o.organization is null or lower(o.organization.acronym)='unhcr'  or lower(o.organization.acronym)='acnur' ) " +
                " and  o.username=:username ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("username", username);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un usuario con este nombre:" + username);
        }
    }

    public List<User> getAllUsers() {
        String jpql = "SELECT DISTINCT o FROM User o " +
                " left outer join fetch o.roleAssigments ra " +
                " left outer join fetch ra.role ro " +
                " left outer join fetch o.organization org " +
                " left outer join fetch o.office offf " +
                " order by org.code, o.name ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        return query.getResultList();
    }

    public User findWithRoles(Long id) {
        String jpql = "SELECT DISTINCT o FROM User o " +
                " left outer join fetch o.roleAssigments ra " +
                " left outer join fetch ra.role ro " +
                " left outer join fetch o.organization org " +
                " left outer join fetch o.office offf " +
                " where o.id =:id ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("id", id);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Long> findProjectsFocalPoints(Long id) {
        String jpql = "SELECT DISTINCT pr.id FROM " +
                "  Project pr inner join pr.focalPointAssignations fpa  " +
                " where fpa.focalPointer.id=:id and pr.state=:state and fpa.state=:state";
        Query query = getEntityManager().createQuery(jpql, Long.class);
        query.setParameter("id", id);
        query.setParameter("state", State.ACTIVO);
        return query.getResultList();
    }

    public List<User> getActivePartnerUsers(Long organizationId) {
        State state = State.ACTIVO;
        String jpql = "SELECT DISTINCT o FROM " +
                "  User o " +
                " where o.organization.id=:organizationId and o.state=:state ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("organizationId", organizationId);
        query.setParameter("state", state);
        return query.getResultList();
    }

    public List<User> getFocalPointsByOrganizationIdAndPeriodId(Long organizationId, Long periodId) {
        State state = State.ACTIVO;
        String jpql = "SELECT DISTINCT fp FROM " +
                "  Project pr " +
                " inner join pr.organization org" +
                " inner join pr.focalPoint fp " +
                " where org.id=:organizationId and pr.state=:state and pr.period.id=:periodId ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("organizationId", organizationId);
        query.setParameter("state", state);
        query.setParameter("periodId", periodId);
        return query.getResultList();
    }

    public List<User> getActiveResponsableDirectImplementationUsers(Long periodId) {
        State state = State.ACTIVO;
        String jpql = "SELECT DISTINCT o FROM " +
                " IndicatorExecution  ie inner join ie.assignedUser o " +
                " where  ie.state=:state and ie.period.id=:periodId ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("periodId", periodId);
        query.setParameter("state", state);
        return query.getResultList();
    }

    public List<User> getActiveSupervisorDirectImplementationUserWebs(Long periodId) {
        State state = State.ACTIVO;
        String jpql = "SELECT DISTINCT o FROM " +
                " IndicatorExecution  ie inner join ie.supervisorUser o " +
                " where  ie.state=:state and ie.period.id=:periodId ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("periodId", periodId);
        query.setParameter("state", state);
        return query.getResultList();
    }

    public List<User> getActiveSupervisorsDirectImplementationUsers(Long periodId) {
        State state = State.ACTIVO;
        String jpql = "SELECT DISTINCT o FROM " +
                " IndicatorExecution  ie inner join ie.supervisorUser o " +
                " where  ie.state=:state and ie.period.id=:periodId ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("periodId", periodId);
        query.setParameter("state", state);
        return query.getResultList();
    }

    public List<Indicator> findIndicatorsByResultManager(Long id) {
        String jpql = "SELECT DISTINCT i FROM " +
                "  Indicator i inner join i.resultManager rm  " +
                " where rm.id=:id AND i.state=:state";
        Query query = getEntityManager().createQuery(jpql, Indicator.class);
        query.setParameter("id", id);
        query.setParameter("state", State.ACTIVO);
        return query.getResultList();
    }

    public List<Long> findProjectsPartnerManager(Long id) {
        String jpql = "SELECT DISTINCT pr.id FROM " +
                "  Project pr inner join pr.partnerManager pm  " +
                " where pm.id=:id AND pr.state=:state";
        Query query = getEntityManager().createQuery(jpql, Long.class);
        query.setParameter("id", id);
        query.setParameter("state", State.ACTIVO);
        return query.getResultList();
    }
    public List<Long> findSupervisorDirectImplementations(Long id) {
        String jpql = "SELECT DISTINCT ie.id FROM " +
                "  IndicatorExecution ie inner join ie.supervisorUser su  " +
                " where su.id=:id AND ie.state=:state";
        Query query = getEntityManager().createQuery(jpql, Long.class);
        query.setParameter("id", id);
        query.setParameter("state", State.ACTIVO);
        return query.getResultList();
    }
    public List<User> getActiveResultManagerUsers() {
        State state = State.ACTIVO;
        String jpql = "SELECT DISTINCT o FROM " +
                " Indicator  i inner join i.resultManager o " +
                " where  i.state=:state ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("state", state);
        return query.getResultList();
    }
}
