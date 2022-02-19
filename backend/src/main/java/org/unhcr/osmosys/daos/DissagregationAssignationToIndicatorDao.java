package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicator;

import javax.ejb.Stateless;

@Stateless
public class DissagregationAssignationToIndicatorDao extends GenericDaoJpa<DissagregationAssignationToIndicator, Long> {
    public DissagregationAssignationToIndicatorDao() {
        super(DissagregationAssignationToIndicator.class, Long.class);
    }
}
