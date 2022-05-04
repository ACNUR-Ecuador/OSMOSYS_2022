package org.unhcr.osmosys.model.geometries;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.vividsolutions.jts.geom.Polygon;
import org.unhcr.osmosys.model.Provincia;

import javax.persistence.*;

@Entity
@Table(schema = "geometries", name = "provincias_polygons")
public class ProvinciaPolygon extends BaseEntity<Long> {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Provincia provincia;

    @SuppressWarnings("UnsupportedTypeWithoutConverterInspection")
    @Column(name = "geometry", columnDefinition = "geometry(Multipolygon,4326)")
    private Polygon geometry;

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

    public Polygon getGeometry() {
        return geometry;
    }

    public void setGeometry(Polygon geometry) {
        this.geometry = geometry;
    }
}
