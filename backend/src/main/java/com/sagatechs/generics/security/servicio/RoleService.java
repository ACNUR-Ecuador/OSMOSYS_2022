package com.sagatechs.generics.security.servicio;

import com.sagatechs.generics.security.dao.RoleDao;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.webservice.webModel.RoleWeb;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RoleService {


    @Inject
    RoleDao roleDao;

    public List<Role> findAll() {
        return this.roleDao.findAll();
    }

    public Role findByRoleType(RoleType roleType) {
        return this.roleDao.findByRoleType(roleType);
    }

    public RoleWeb roleToRoleWeb(Role role) {
        if (role == null) {
            return null;
        }
        RoleWeb roleWeb = new RoleWeb();
        roleWeb.setId(role.getId());
        roleWeb.setState(role.getState());
        roleWeb.setName(role.getRoleType().name());
        return roleWeb;
    }
    public List<RoleWeb> roleToRoleWeb(List<Role> roles) {
        List<RoleWeb> roleWebs= new ArrayList<>();
        for(Role role: roles){
            roleWebs.add(this.roleToRoleWeb(role));
        }
        return roleWebs;
    }

    public List<RoleWeb> getAllRolesWeb() {
        return this.roleToRoleWeb(this.roleDao.findAll());
    }

    public Role findById(Long id) {
        return this.roleDao.find(id);
    }
}
