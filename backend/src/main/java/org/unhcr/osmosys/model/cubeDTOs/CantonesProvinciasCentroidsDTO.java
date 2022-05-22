package org.unhcr.osmosys.model.cubeDTOs;

public class CantonesProvinciasCentroidsDTO {

    public CantonesProvinciasCentroidsDTO(Long canton_id, String canton_code, String canton, Float canton_long, Float canton_lat, String provincia_code, String provincia, Float provincia_long, Float provincia_lat) {
        this.canton_id = canton_id;
        this.canton_code = canton_code;
        this.canton = canton;
        this.canton_long = canton_long;
        this.canton_lat = canton_lat;
        this.provincia_code = provincia_code;
        this.provincia = provincia;
        this.provincia_long = provincia_long;
        this.provincia_lat = provincia_lat;
    }

    private Long canton_id;
    private String canton_code;
    private String canton;
    private Float canton_long;
    private Float canton_lat;

    private String provincia_code;
    private String provincia;
    private Float provincia_long;
    private Float provincia_lat;

    public Long getCanton_id() {
        return canton_id;
    }

    public void setCanton_id(Long canton_id) {
        this.canton_id = canton_id;
    }

    public String getCanton_code() {
        return canton_code;
    }

    public void setCanton_code(String canton_code) {
        this.canton_code = canton_code;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getProvincia_code() {
        return provincia_code;
    }

    public void setProvincia_code(String provincia_code) {
        this.provincia_code = provincia_code;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Float getCanton_long() {
        return canton_long;
    }

    public void setCanton_long(Float canton_long) {
        this.canton_long = canton_long;
    }

    public Float getCanton_lat() {
        return canton_lat;
    }

    public void setCanton_lat(Float canton_lat) {
        this.canton_lat = canton_lat;
    }

    public Float getProvincia_long() {
        return provincia_long;
    }

    public void setProvincia_long(Float provincia_long) {
        this.provincia_long = provincia_long;
    }

    public Float getProvincia_lat() {
        return provincia_lat;
    }

    public void setProvincia_lat(Float provincia_lat) {
        this.provincia_lat = provincia_lat;
    }
}
