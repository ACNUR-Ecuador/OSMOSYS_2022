package com.sagatechs.generics.security.dao;


import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.model.User;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
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
                " WHERE (o.organization is null or lower(o.organization.acronym)='unhcr'  or lower(o.organization.acronym)='acnur' ) and o.state=:state ";
        Query query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("state", state);
        return query.getResultList();
    }
}
