package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.services.standardDissagregations.StandardDissagregationOptionService;
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
    StandardDissagregationOptionService standardDissagregationOptionService;


    @Path("/options/active/age")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActiveAgeOptions() {

        return this.standardDissagregationOptionService.getAgeDissagregationOptionWebByState(State.ACTIVO);
    }

    @Path("/options/active/gender")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActiveGenderOptions() {
        return this.standardDissagregationOptionService.getGenderDissagregationOptionWebByState(State.ACTIVO);
    }

    @Path("/options/active/populationType")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActivePopulationTypeOptions() {
        return this.standardDissagregationOptionService.getPopulationTypeOptionsWebByState(State.ACTIVO);
    }
    @Path("/options/active/countryOfOrigin")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActiveCountryOfOriginOptions() {
        return this.standardDissagregationOptionService.getCountryOfOriginDissagregationWebOptionByState(State.ACTIVO);
    }

    @Path("/options/active/diversity")
    @GET
    // @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StandardDissagregationOptionWeb> getActiveDiversityOptions() {
        return this.standardDissagregationOptionService.getDiversityeOptionsWebByState(State.ACTIVO);
    }


}
