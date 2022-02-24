package org.unhcr.osmosys.model.geometries;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.vividsolutions.jts.geom.Polygon;
import org.unhcr.osmosys.model.Canton;

import javax.persistence.*;
import java.awt.*;

@Entity
@Table(schema = "geometries", name = "cantones_centroid")
public class CantonCentroid extends BaseEntity<Long> {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Canton canton;

    @Column(name = "geometry", columnDefinition = "geometry(Point,4326)")
    private Point geometry;

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

    public Point getGeometry() {
        return geometry;
    }

    public void setGeometry(Point geometry) {
        this.geometry = geometry;
    }
}
