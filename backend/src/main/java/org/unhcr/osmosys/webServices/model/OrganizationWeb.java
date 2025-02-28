package org.unhcr.osmosys.webServices.model;

import javax.ejb.Stateless;
import java.io.Serializable;

@Stateless
public class OrganizationWeb extends BaseWebEntity implements Serializable {
    public OrganizationWeb() {super();
    }

    private String code;
    private String description;
    private String acronym;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

}
