package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.osmosys.model.Organization;

import javax.ejb.Stateless;

@Stateless
public class OrganizactionDao extends GenericDaoJpa<Organization, Long> {
    public OrganizactionDao() {
        super(Organization.class, Long.class);
    }
}
