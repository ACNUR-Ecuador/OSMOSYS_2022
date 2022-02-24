package org.unhcr.osmosys.model.geometries;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.vividsolutions.jts.geom.Polygon;
import org.unhcr.osmosys.model.Canton;

import javax.persistence.*;

@Entity
@Table(schema = "geometries", name = "cantones_polygons")
public class CantonPolygon extends BaseEntity<Long> {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Canton canton;

    @Column(name = "geometry", columnDefinition = "geometry(Multipolygon,4326)")
    private Polygon geometry;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public Polygon getGeometry() {
        return geometry;
    }

    public void setGeometry(Polygon geometry) {
        this.geometry = geometry;
    }
}
