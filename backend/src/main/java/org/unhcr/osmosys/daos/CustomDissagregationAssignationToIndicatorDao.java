package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.osmosys.model.CustomDissagregationAssignationToIndicator;

import javax.ejb.Stateless;

@Stateless
public class CustomDissagregationAssignationToIndicatorDao extends GenericDaoJpa<CustomDissagregationAssignationToIndicator, Long> {
    public CustomDissagregationAssignationToIndicatorDao() {
        super(CustomDissagregationAssignationToIndicator.class, Long.class);
    }
}
