package org.unhcr.osmosys.model.appConfig;


import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.security.model.RoleType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.unhcr.osmosys.model.Organization;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "menu_items_organization_assigments", schema = "app_config",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_menu_item", columnNames = {"menu_item_id", "organization_id"})
        }
)
public class MenuItemsOrganizationAssigment extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", foreignKey = @ForeignKey(name = "menu_item_roles"))
    private MenuItem menuItem;


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("organization", organization)
                .append("menuItem", menuItem)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MenuItemsOrganizationAssigment)) return false;

        MenuItemsOrganizationAssigment that = (MenuItemsOrganizationAssigment) o;

        return new EqualsBuilder().append(id, that.id).append(organization, that.organization).append(menuItem, that.menuItem).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(organization).append(menuItem).toHashCode();
    }
}
