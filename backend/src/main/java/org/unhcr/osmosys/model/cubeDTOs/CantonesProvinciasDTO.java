package org.unhcr.osmosys.model.cubeDTOs;

public class CantonesProvinciasDTO {

    public CantonesProvinciasDTO(Long canton_id, String canton_code, String canton, String provincia_code, String provincia) {
        this.canton_id = canton_id;
        this.canton_code = canton_code;
        this.canton = canton;
        this.provincia_code = provincia_code;
        this.provincia = provincia;
    }

    private Long canton_id;
    private String canton_code;
    private String canton;
    private String provincia_code;
    private String provincia;

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
}
