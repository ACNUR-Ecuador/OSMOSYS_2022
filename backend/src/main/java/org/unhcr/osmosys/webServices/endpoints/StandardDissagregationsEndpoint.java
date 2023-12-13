package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.services.standardDissagregations.*;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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

    @Path("/options/active/age")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActiveAgeOptions() throws GeneralAppException {

        return this.ageDissagregationOptionService.getWebByState(State.ACTIVO);
    }

    @Path("/options/active/gender")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActiveGenderOptions() {
        return this.genderDissagregationOptionService.getWebByState(State.ACTIVO);
    }

    @Path("/options/active/populationType")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActivePopulationTypeOptions() {
        List<StandardDissagregationOptionWeb> result = this.populationTypeDissagregationOptionService.getWebByState(State.ACTIVO);
        return result;
    }
    @Path("/options/active/countryOfOrigin")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActiveCountryOfOriginOptions() {
        return this.countryOfOriginDissagregationOptionService.getWebByState(State.ACTIVO);
    }

    @Path("/options/active/diversity")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActiveDiversityOptions() {
        return this.diversityDissagregationOptionService.getWebByState(State.ACTIVO);
    }


}
