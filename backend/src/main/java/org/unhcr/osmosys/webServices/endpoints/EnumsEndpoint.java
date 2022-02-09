package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleType;
import org.apache.poi.ss.formula.functions.T;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.webServices.model.EnumWeb;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Path("/enums")
@RequestScoped
public class EnumsEndpoint {


    @Path("/{type}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EnumWeb> getTypes(@PathParam("type") String type) throws GeneralAppException {
        switch (type) {
            case "AgeType":
                return this.EnumsToEnumsWeb(AgeType.values());
            case "AreaType":
                return this.EnumsToEnumsWeb(AreaType.values());
            case "CountryOfOrigin":
                return this.EnumsToEnumsWeb(CountryOfOrigin.values());
            case "DissagregationType":
                return this.EnumsToEnumsWeb(DissagregationType.values());
            case "Frecuency":
                return this.EnumsToEnumsWeb(Frecuency.values());
            case "GenderType":
                return this.EnumsToEnumsWeb(GenderType.values());
            case "IndicatorType":
                return this.EnumsToEnumsWeb(IndicatorType.values());
            case "MarkerType":
                return this.EnumsToEnumsWeb(MarkerType.values());
            case "MeasureType":
                return this.EnumsToEnumsWeb(MeasureType.values());
            case "OfficeType":
                return this.EnumsToEnumsWeb(OfficeType.values());
            case "PopulationType":
                return this.EnumsToEnumsWeb(PopulationType.values());
            case "State":
                return this.EnumsToEnumsWeb(State.values());
            case "DiversityType":
                return this.EnumsToEnumsWeb(DiversityType.values());
            case "TotalIndicatorCalculationType":
                return this.EnumsToEnumsWeb(TotalIndicatorCalculationType.values());
            case "RoleType":
                return this.EnumsToEnumsWeb(RoleType.values());
            case "SourceType":
                return this.EnumsToEnumsWeb(SourceType.values());
            case "UnitType":
                return this.EnumsToEnumsWeb(UnitType.values());
        }

        throw new GeneralAppException("Enumerador no soportado " + type, Response.Status.BAD_GATEWAY);
    }

    public List<EnumWeb> EnumsToEnumsWeb(EnumInterface[] enumerator) {
        List<EnumWeb> r = new ArrayList<>();

        for (EnumInterface enumInterface : enumerator) {
            r.add(this.EnumToEnumWeb(enumInterface));
        }
        return r.stream().sorted(Comparator.comparingInt(EnumWeb::getOrder)).collect(Collectors.toList());
    }

    public EnumWeb EnumToEnumWeb(EnumInterface enumerator) {
        EnumWeb ew = new EnumWeb();
        ew.setValue(enumerator.getStringValue());
        ew.setLabel(enumerator.getLabel());
        ew.setOrder(enumerator.getOrder());

        return ew;
    }
}
