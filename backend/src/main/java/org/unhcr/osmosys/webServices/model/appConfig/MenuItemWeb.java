package org.unhcr.osmosys.webServices.model.appConfig;


import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.RoleType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MenuItemWeb extends BaseEntityIdState {

    private Long id;


    private State state;


    private String label;



    private String icon;


    private Set<RoleType> assignedRoles= new HashSet<>();



    private boolean powerBi;


    private boolean restricted;


    private Integer order;


    private Set<OrganizationWeb> organizations= new HashSet<>();



    private String url;

    private MenuItemWeb parent;
    private List<MenuItemWeb> children= new ArrayList<>();

    private Boolean openInNewTab;



    @Override
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<RoleType> getAssignedRoles() {
        return assignedRoles;
    }

    public void setAssignedRoles(Set<RoleType> assignedRoles) {
        this.assignedRoles.clear();
        if(assignedRoles!=null) {
            this.assignedRoles = assignedRoles;
        }
    }

    public boolean isPowerBi() {
        return powerBi;
    }

    public void setPowerBi(boolean powerBi) {
        this.powerBi = powerBi;
    }

    public Set<OrganizationWeb> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<OrganizationWeb> organizations) {
        this.organizations = organizations;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MenuItemWeb getParent() {
        return parent;
    }

    public void setParent(MenuItemWeb parent) {
        this.parent = parent;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }


    public Boolean getOpenInNewTab() {
        return openInNewTab;
    }

    public void setOpenInNewTab(Boolean openInNewTab) {
        this.openInNewTab = openInNewTab;
    }

    public List<MenuItemWeb> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItemWeb> children) {
        this.children = children;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("state", state)
                .append("label", label)
                .append("icon", icon)
                .append("assignedRoles", assignedRoles)
                .append("isPowerBi", powerBi)
                .append("isRectricted", restricted)
                .append("url", url)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MenuItemWeb)) return false;

        MenuItemWeb menuItem = (MenuItemWeb) o;

        return new EqualsBuilder().append(id, menuItem.id).append(label, menuItem.label).isEquals();
    }



    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(label).toHashCode();
    }
}
