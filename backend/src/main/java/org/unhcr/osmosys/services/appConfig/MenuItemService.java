package org.unhcr.osmosys.services.appConfig;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.RoleType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.OrganizationDao;
import org.unhcr.osmosys.daos.appConfig.MenuItemDao;
import org.unhcr.osmosys.model.Organization;
import org.unhcr.osmosys.model.appConfig.MenuItem;
import org.unhcr.osmosys.model.appConfig.MenuItemsOrganizationAssigment;
import org.unhcr.osmosys.model.appConfig.MenuItemsRolesAssigment;
import org.unhcr.osmosys.services.AreaService;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;
import org.unhcr.osmosys.webServices.model.appConfig.MenuItemWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
@Stateless
public class MenuItemService {

    @Inject
    MenuItemDao menuItemDao;

    @Inject
    OrganizationDao organizationDao;
    @Inject
    ModelWebTransformationService modelWebTransformationService;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(AreaService.class);

    public MenuItem getById(Long id) {
        return this.menuItemDao.find(id);
    }


    public Long save(MenuItemWeb menuItemWeb) throws GeneralAppException {
        if (menuItemWeb == null) {
            throw new GeneralAppException("No se puede guardar un menu null", Response.Status.BAD_REQUEST);

        }
        if (menuItemWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un menu con id", Response.Status.BAD_REQUEST);
        }
        this.menuItemDao.save(this.setValues(menuItemWeb));

        return menuItemWeb.getId();
    }

    public List<MenuItemWeb> getAll(boolean getChildren) {
        return this.itemsToItemWebs(this.menuItemDao.findAll(), getChildren);
    }

    public List<MenuItemWeb> getByState(State state, boolean getChildren) {
        return this.itemsToItemWebs(this.menuItemDao.getByState(state), getChildren);
    }

    public Long update(MenuItemWeb menuItemWeb) throws GeneralAppException {
        if (menuItemWeb == null) {
            throw new GeneralAppException("No se puede actualizar un area null", Response.Status.BAD_REQUEST);
        }
        if (menuItemWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un area sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(menuItemWeb);

        MenuItem item = this.setValues(menuItemWeb);
        this.menuItemDao.update(item);


        return item.getId();
    }

    private MenuItem setValues(MenuItemWeb itemWeb) {
        MenuItem item;
        if (itemWeb.getId() != null) {
            item = this.menuItemDao.find(itemWeb.getId());
        } else {
            item = new MenuItem();
        }
        item.setState(itemWeb.getState());
        item.setLabel(itemWeb.getLabel());
        item.setIcon(itemWeb.getIcon());
        item.setPowerBi(itemWeb.isPowerBi());
        item.setRectricted(itemWeb.isRestricted());
        item.setOrder(itemWeb.getOrder());
        item.setUrl(itemWeb.getUrl());

        if (item.isRectricted()) {
            List<OrganizationWeb> tocreateOrganization = itemWeb.getOrganizations().stream().filter(organizationWeb ->
                    item.getMenuItemsOrganizationAssigments().stream().noneMatch(menuItemsOrganizationAssigment -> menuItemsOrganizationAssigment.getOrganization().getId().equals(organizationWeb.getId()))
            ).collect(Collectors.toList());
            for (OrganizationWeb organizationWeb : tocreateOrganization) {
                MenuItemsOrganizationAssigment menuItemsOrganizationAssigment = new MenuItemsOrganizationAssigment();
                Organization organization = this.organizationDao.find(organizationWeb.getId());
                menuItemsOrganizationAssigment.setOrganization(organization);
                menuItemsOrganizationAssigment.setMenuItem(item);
                item.getMenuItemsOrganizationAssigments().add(menuItemsOrganizationAssigment);
            }
            List<MenuItemsOrganizationAssigment> toRemoveOrg = item.getMenuItemsOrganizationAssigments().stream().filter(menuItemsOrganizationAssigment -> itemWeb.getOrganizations().stream().noneMatch(organizationWeb -> menuItemsOrganizationAssigment.getOrganization().getId().equals(organizationWeb.getId()))).collect(Collectors.toList());
            toRemoveOrg.forEach(item.getMenuItemsOrganizationAssigments()::remove);
        } else {
            item.getMenuItemsOrganizationAssigments().clear();
        }

        List<RoleType> tocreateRole = itemWeb.getAssignedRoles().stream().filter(roleType ->
                item.getAssignedRoles().stream().noneMatch(menuItemsRolesAssigment -> menuItemsRolesAssigment.getRole().equals(roleType))
        ).collect(Collectors.toList());
        for (RoleType assignedRole : tocreateRole) {
            MenuItemsRolesAssigment menuItemsRolesAssigment = new MenuItemsRolesAssigment();
            menuItemsRolesAssigment.setRole(assignedRole);
            menuItemsRolesAssigment.setMenuItem(item);
            item.getAssignedRoles().add(menuItemsRolesAssigment);
        }
        List<MenuItemsRolesAssigment> toRemoveRole = item.getAssignedRoles().stream().filter(menuItemsRolesAssigment -> itemWeb.getAssignedRoles().stream().noneMatch(roleType -> menuItemsRolesAssigment.getRole().equals(roleType))).collect(Collectors.toList());
        toRemoveRole.forEach(item.getAssignedRoles()::remove);


        if (itemWeb.getParent() != null) {
            MenuItem parent = this.menuItemDao.find(itemWeb.getParent().getId());
            item.setParentItem(parent);
        } else {
            item.setParentItem(null);
        }
        item.setOpenInNewTab(itemWeb.getOpenInNewTab());

        return item;

    }


    public void validate(MenuItemWeb item) throws GeneralAppException {
        if (item == null) {
            throw new GeneralAppException("Item es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(item.getLabel())) {
            throw new GeneralAppException("etiqueta no válido", Response.Status.BAD_REQUEST);
        }

    }


    public List<MenuItemWeb> itemsToItemWebs(List<MenuItem> items, boolean getChildren) {
        List<MenuItemWeb> r = new ArrayList<>();
        if (items != null) {

            for (MenuItem item : items) {
                if (getChildren) {
                    if (item.getState().equals(State.ACTIVO)) {
                        r.add(this.itemToItemWeb(item, getChildren));
                    }
                } else {
                    r.add(this.itemToItemWeb(item, getChildren));
                }

            }
        }
        return r;
    }

    public MenuItemWeb itemToItemWeb(MenuItem item, boolean getChildren) {
        if (item == null) {
            return null;
        }

        MenuItemWeb web = new MenuItemWeb();
        web.setId(item.getId());
        web.setState(item.getState());
        web.setLabel(item.getLabel());
        web.setIcon(item.getIcon());
        web.setPowerBi(item.isPowerBi());
        web.setRestricted(item.isRectricted());
        web.setOrder(item.getOrder());
        web.setUrl(item.getUrl());

        if (item.isRectricted()) {
            web.setOrganizations(new HashSet<>());
            for (MenuItemsOrganizationAssigment organizationAssignation : item.getMenuItemsOrganizationAssigments()) {
                web.getOrganizations().add(this.modelWebTransformationService.organizationToOrganizationWeb(organizationAssignation.getOrganization()));
            }
        }

        web.setAssignedRoles(new HashSet<>());
        for (MenuItemsRolesAssigment assignedRole : item.getAssignedRoles()) {
            web.getAssignedRoles().add(assignedRole.getRole());

        }
        if (!getChildren) {
            if (item.getParentItem() != null) {
                MenuItemWeb parent = this.itemToItemWeb(item.getParentItem(), getChildren);
                web.setParent(parent);
            } else {
                item.setParentItem(null);
            }
        } else {
            if (CollectionUtils.isNotEmpty(item.getChildrenItems())) {
                List<MenuItemWeb> r = this.itemsToItemWebs(new ArrayList<>(item.getChildrenItems()), getChildren);
                web.setChildren(r);
            }
        }
        web.setOpenInNewTab(item.getOpenInNewTab());

        return web;

    }

    public List<MenuItemWeb> getMenuStructure() {
        List<MenuItem> menuItem = this.menuItemDao.getMainMenus();

        List<MenuItemWeb> menuWebs = this.itemsToItemWebs(menuItem, true);
        return menuWebs;
    }
}
