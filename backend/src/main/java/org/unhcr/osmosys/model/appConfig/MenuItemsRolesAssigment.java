package org.unhcr.osmosys.model.appConfig;


import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.RoleType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu_items_roles_assigments", schema = "app_config",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_menu_item", columnNames = {"menu_item_id", "role"})
        }
)
public class MenuItemsRolesAssigment extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @NotNull(message = "El rol es un dato obligatorio")
    @Column(name = "role", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private RoleType role;

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


    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
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
                .append("role", role)
                .append("menuItem", menuItem)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MenuItemsRolesAssigment)) return false;

        MenuItemsRolesAssigment that = (MenuItemsRolesAssigment) o;

        return new EqualsBuilder().append(role, that.role).append(menuItem, that.menuItem).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(role).append(menuItem).toHashCode();
    }
}
