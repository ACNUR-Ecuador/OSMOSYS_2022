package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.osmosys.model.Office;

import javax.ejb.Stateless;

@Stateless
public class OfficeDao extends GenericDaoJpa<Office, Long> {
    public OfficeDao() {
        super(Office.class, Long.class);
    }


}
