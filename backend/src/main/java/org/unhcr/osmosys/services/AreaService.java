package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.AreaDao;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.webServices.model.AreaWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AreaService {

    @Inject
    AreaDao areaDao;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(AreaService.class);

    public Area getById(Long id) {
        return this.areaDao.find(id);
    }

    public Area saveOrUpdate(Area area){
        if(area.getId()==null){
            this.areaDao.save(area);
        }else {
            this.areaDao.update(area);
        }
        return area;
    }

    public Long save(AreaWeb areaWeb) throws GeneralAppException {
        if(areaWeb==null){
            throw new GeneralAppException("No se puede guardar un area null", Response.Status.BAD_REQUEST);
        }
        if(areaWeb.getId()!=null){
            throw new GeneralAppException("No se puede crear un area con id", Response.Status.BAD_REQUEST);
        }
        this.validate(areaWeb);
        Area area=this.saveOrUpdate(this.areaToAreaWeb(areaWeb));
        return area.getId();
    }

    public List<AreaWeb> getAll(){
        List<AreaWeb> r = new ArrayList<>();
        return this.areasToAreasWeb(this.areaDao.findAll());
    }

    public List<AreaWeb> getByState(State state){
        List<AreaWeb> r = new ArrayList<>();
        return this.areasToAreasWeb(this.areaDao.getByState(state));
    }

    public Long update(AreaWeb areaWeb) throws GeneralAppException {
        if(areaWeb==null){
            throw new GeneralAppException("No se puede actualizar un area null", Response.Status.BAD_REQUEST);
        }
        if(areaWeb.getId()==null){
            throw new GeneralAppException("No se puede crear un area sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(areaWeb);
        Area area=this.saveOrUpdate(this.areaToAreaWeb(areaWeb));
        return area.getId();
    }

    public List<AreaWeb> areasToAreasWeb(List<Area> areas) {
        List<AreaWeb> r = new ArrayList<>();
        for (Area area : areas) {
            r.add(this.areaToAreaWeb(area));
        }
        return r;
    }

    public AreaWeb areaToAreaWeb(Area area) {
        if (area == null) {
            return null;
        }
        AreaWeb areaWeb = new AreaWeb();
        areaWeb.setId(area.getId());
        areaWeb.setAreaType(area.getAreaType());
        areaWeb.setCode(area.getCode());
        areaWeb.setDefinition(area.getDefinition());
        areaWeb.setDescription(area.getDescription());
        areaWeb.setShortDescription(area.getShortDescription());
        areaWeb.setState(area.getState());

        return areaWeb;
    }
    public List<Area> areasWebToAreas(List<AreaWeb> areasWebs) {
        List<Area> r = new ArrayList<>();
        for (AreaWeb areaWeb : areasWebs) {
            r.add(this.areaToAreaWeb(areaWeb));
        }
        return r;
    }
    public Area areaToAreaWeb(AreaWeb areaWeb) {
        if (areaWeb == null) {
            return null;
        }
        Area area = new Area();
        area.setId(areaWeb.getId());
        area.setAreaType(areaWeb.getAreaType());
        area.setState(areaWeb.getState());
        area.setDescription(areaWeb.getDescription());
        area.setCode(areaWeb.getCode());
        area.setDefinition(areaWeb.getDefinition());
        area.setShortDescription(areaWeb.getShortDescription());
        return area;
    }

    public void validate(AreaWeb areaWeb) throws GeneralAppException {
        if (areaWeb == null) {
            throw new GeneralAppException("Oficina es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(areaWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(areaWeb.getShortDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(areaWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (areaWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (areaWeb.getAreaType() == null) {
            throw new GeneralAppException("Tipo no válida", Response.Status.BAD_REQUEST);
        }
    }
}
