package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.unhcr.osmosys.model.enums.OfficeType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OfficeWeb extends BaseWebEntity implements Serializable {

    public OfficeWeb() {
        super();
    }

    private String description;

    private String acronym;

    private OfficeType type;


    private OfficeWeb parentOffice;

    private List<OfficeWeb> childOffices = new ArrayList<>();

    private List<UserWeb> administrators = new ArrayList<>();


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

    public OfficeType getType() {
        return type;
    }

    public void setType(OfficeType type) {
        this.type = type;
    }


    public OfficeWeb getParentOffice() {
        return parentOffice;
    }

    public void setParentOffice(OfficeWeb parentOffice) {
        this.parentOffice = parentOffice;
    }

    public List<OfficeWeb> getChildOffices() {
        return childOffices;
    }

    public void setChildOffices(List<OfficeWeb> childOffices) {
        this.childOffices = childOffices;
    }

    public List<UserWeb> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<UserWeb> administrators) {
        this.administrators = administrators;
    }
}
