package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.model.enums.PopulationType;
import org.unhcr.osmosys.services.AreaService;
import org.unhcr.osmosys.services.standardDissagregations.*;
import org.unhcr.osmosys.webServices.model.AreaWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/standardDissagregations")
@RequestScoped
public class StandardDissagregationsEndpoint {

    @Inject
    AgeDissagregationOptionService ageDissagregationOptionService;

    @Inject
    GenderDissagregationOptionService genderDissagregationOptionService;

    @Inject
    PopulationTypeDissagregationOptionService populationTypeDissagregationOptionService;

    @Inject
    DiversityDissagregationOptionService diversityDissagregationOptionService;

    @Inject
    CountryOfOriginDissagregationOptionService countryOfOriginDissagregationOptionService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(AreaWeb areaWeb) throws GeneralAppException {
        // return this.areaService.save(areaWeb);
        return 1l;
    }


}
