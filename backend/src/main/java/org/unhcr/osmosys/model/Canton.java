package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Canton")
@DiscriminatorValue("location")
public class Canton extends StandardDissagregationOption {



    @Column(name = "code", unique = true)
    private String code;


    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "provincia_id", foreignKey = @ForeignKey(name = "fk_cantones_provincias"))
    private Provincia provincia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id", foreignKey = @ForeignKey(name = "fk_cantones_offices"))
    private Office office;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    @SuppressWarnings("unused")
    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Canton canton = (Canton) o;

        if (id != null && canton.getId() != null) {
            return id.equals(canton.getId());
        }else {
            return new EqualsBuilder()
                    .append(code, canton.code)
                    .isEquals();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .append(code)
                .append(name)
                .append(state)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Canton{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + name + '\'' +
                ", state=" + state +
                ", provincia=" + provincia +
                ", office=" + office +
                '}';
    }
}
