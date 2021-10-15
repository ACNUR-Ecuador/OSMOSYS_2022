package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.model.enums.*;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.EnumSet;

@Path("/enums")
@RequestScoped
public class EnumsEndpoint {


    @Path("/{type}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Enum[] getTypes(@PathParam("type") String type) throws GeneralAppException {
        switch (type) {
            case "AgeType":
                return AgeType.values();
            case "AreaType":
                return AreaType.values();
            case "CountyOfOrigin":
                return CountryOfOrigin.values();
            case "DissagregationType":
                return DissagregationType.values();
            case "Frecuency":
                return Frecuency.values();
            case "GenderType":
                return GenderType.values();
            case "IndicatorType":
                return IndicatorType.values();
            case "MarkerType":
                return MarkerType.values();
            case "MeasureType":
                return MeasureType.values();
            case "OfficeType":
                return OfficeType.values();
            case "PopulationType":
                return PopulationType.values();
            case "State":
                return State.values();
        }

        throw new GeneralAppException("Enumerador no soportado", Response.Status.BAD_GATEWAY);
    }
}
