package org.unhcr.osmosys.model.geometries;

import com.sagatechs.generics.persistence.model.BaseEntity;
import org.unhcr.osmosys.model.Provincia;

import javax.persistence.*;
import java.awt.*;

@Entity
@Table(schema = "geometries", name = "provincias_centroid")
public class ProvinciaCentroid extends BaseEntity<Long> {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Provincia provincia;

    @SuppressWarnings("UnsupportedTypeWithoutConverterInspection")
    @Column(name = "geometry", columnDefinition = "geometry(Point,4326)")
    private Point geometry;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Point getGeometry() {
        return geometry;
    }

    public void setGeometry(Point geometry) {
        this.geometry = geometry;
    }
}
