package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.appConfig.MenuItemService;
import org.unhcr.osmosys.webServices.model.appConfig.MenuItemWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/menuItem")
@RequestScoped
public class MenuItemEndpoint {

    @Inject
    MenuItemService menuItemService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(MenuItemWeb menuItemWeb) throws GeneralAppException {
        return this.menuItemService.save(menuItemWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(MenuItemWeb menuItemWeb) throws GeneralAppException {
        return this.menuItemService.update(menuItemWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MenuItemWeb> getAll() {
        return this.menuItemService.getAll(false);
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MenuItemWeb> getByState(@PathParam("state") State state) {
        return this.menuItemService.getByState(state, false);
    }
    @Path("/{id}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public MenuItemWeb getByState(@PathParam("id") Long id) {
        return this.menuItemService.getByIdWeb(id);
    }
    @Path("/getMenuStructure")
    @GET
    //@Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MenuItemWeb> getMenuStructure() {
        return this.menuItemService.getMenuStructure();
    }

}
