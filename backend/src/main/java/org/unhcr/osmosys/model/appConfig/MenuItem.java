package org.unhcr.osmosys.model.appConfig;


import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu_items", schema = "app_config")
public class MenuItem extends BaseEntityIdState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Column(name = "label", nullable = true)
    private String label;


    @Column(name = "icon", nullable = true)
    private String icon;

    @OneToMany(mappedBy = "menuItem",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuItemsRolesAssigment> assignedRoles= new HashSet<>();


    @Column(name = "isPowerBi", nullable = false)
    private boolean isPowerBi;

    @Column(name = "isRectricted", nullable = false)
    private boolean isRectricted;

    @Column(name = "order_", nullable = false)
    private Integer order;

    @OneToMany(mappedBy = "menuItem",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MenuItemsOrganizationAssigment> menuItemsOrganizationAssigments= new HashSet<>();


    @Column(name = "url",  columnDefinition = "text")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_item", foreignKey = @ForeignKey(name = "menu_item_parent"))
    private MenuItem parentItem;

    @OneToMany(mappedBy = "parentItem", fetch = FetchType.LAZY)
    private Set<MenuItem> childrenItems = new HashSet<>();

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

    public Set<MenuItemsRolesAssigment> getAssignedRoles() {
        return assignedRoles;
    }

    public void setAssignedRoles(Set<MenuItemsRolesAssigment> assignedRoles) {
        this.assignedRoles.clear();
        this.assignedRoles = assignedRoles;
    }

    public boolean isPowerBi() {
        return isPowerBi;
    }

    public void setPowerBi(boolean powerBi) {
        isPowerBi = powerBi;
    }

    public boolean isRectricted() {
        return isRectricted;
    }

    public void setRectricted(boolean rectricted) {
        isRectricted = rectricted;
    }

    public Set<MenuItemsOrganizationAssigment> getMenuItemsOrganizationAssigments() {
        return menuItemsOrganizationAssigments;
    }

    public void setMenuItemsOrganizationAssigments(Set<MenuItemsOrganizationAssigment> menuItemsOrganizationAssigments) {
        this.menuItemsOrganizationAssigments = menuItemsOrganizationAssigments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MenuItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(MenuItem parentItem) {
        this.parentItem = parentItem;
    }

    public Set<MenuItem> getChildrenItems() {
        return childrenItems;
    }

    public void setChildrenItems(Set<MenuItem> childrenItems) {
        this.childrenItems = childrenItems;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("state", state)
                .append("label", label)
                .append("icon", icon)
                .append("assignedRoles", assignedRoles)
                .append("isPowerBi", isPowerBi)
                .append("isRectricted", isRectricted)
                .append("menuItemsOrganizationAssigments", menuItemsOrganizationAssigments)
                .append("url", url)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MenuItem)) return false;

        MenuItem menuItem = (MenuItem) o;

        return new EqualsBuilder().append(id, menuItem.id).append(label, menuItem.label).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(label).toHashCode();
    }
}
