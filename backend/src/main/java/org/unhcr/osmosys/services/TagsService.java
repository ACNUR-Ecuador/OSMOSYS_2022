package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.omnifaces.util.Messages;
import org.unhcr.osmosys.daos.TagsDao;

import org.unhcr.osmosys.model.IndicatorTagAssignation;
import org.unhcr.osmosys.model.Tags;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TagsService {
    @Inject
    TagsDao tagsDao;
    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;
    private final static Logger LOGGER = Logger.getLogger(AreaService.class);

    public Tags getById(Long id) {
        return this.tagsDao.find(id);
    }
    public Tags saveOrUpdate(Tags tag) throws GeneralAppException{
        boolean hasIndicators = !(tag.getIndicatorTagAssignations().stream().filter(tagd -> tagd.getState().equals(State.ACTIVO)).map(IndicatorTagAssignation::getIndicator).count() == 0);
        if(!hasIndicators){
            throw new GeneralAppException("No se puede guardar un tag ingrese al menos un indicador", Response.Status.BAD_REQUEST);

        }
        if (tag.getId() == null) {
            this.tagsDao.save(tag);
        } else {
            this.tagsDao.update(tag);
        }
        return tag;
    }

    //Implementar save TagsWeb

    public Long save(TagsWeb tagWeb) throws GeneralAppException {
        if (tagWeb == null) {
            throw new GeneralAppException("No se puede guardar un tag null", Response.Status.BAD_REQUEST);
        }
        if (tagWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un tag con id", Response.Status.BAD_REQUEST);
        }
        this.validate(tagWeb);
        Tags tag = this.saveOrUpdate(this.modelWebTransformationService.tagWebToTag(tagWeb));
        return tag.getId();
    }

    public List<TagsWeb> getAll() {
        List<Tags> all = this.tagsDao.findAll();
        return this.modelWebTransformationService.tagsToTagsWeb(all);
    }

    public List<TagsWeb> getByState(State state) {
        return this.modelWebTransformationService.tagsToTagsWeb(this.tagsDao.getByState(state));
    }

    public Long update(TagsWeb tagWeb) throws GeneralAppException {
        if (tagWeb == null) {
            throw new GeneralAppException("No se puede actualizar un tag null", Response.Status.BAD_REQUEST);
        }
        if (tagWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un tag sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(tagWeb);
        Tags tag = this.saveOrUpdate(this.modelWebTransformationService.tagWebToTag(tagWeb));
        return tag.getId();
    }

    public void validate(TagsWeb tagWeb) throws GeneralAppException {
        if (tagWeb == null) {
            throw new GeneralAppException("Tag es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(tagWeb.getName())) {
            throw new GeneralAppException("Nombre no válido", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(tagWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }


        if (tagWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }

        for (PeriodTagAsignationWeb periodTagAsignationWeb : tagWeb.getPeriodTagAsignations()) {
            Tags itemRecovered = this.tagsDao.getByName(tagWeb.getName());
            if (itemRecovered != null) {
                if (tagWeb.getId() == null || !tagWeb.getId().equals(itemRecovered.getId())) {
                    throw new GeneralAppException("Ya existe un tag con este nombre " + tagWeb.getName(), Response.Status.BAD_REQUEST);
                }
            }
        }


        if (CollectionUtils.isEmpty(tagWeb.getPeriodTagAsignations())) {
            throw new GeneralAppException("El tag debe tener al menos un periodo asignado", Response.Status.BAD_REQUEST);
        }
        for (PeriodTagAsignationWeb periodTagAsignationWeb : tagWeb.getPeriodTagAsignations()) {
            if (periodTagAsignationWeb.getPeriod() == null || periodTagAsignationWeb.getPeriod().getId() == null) {
                throw new GeneralAppException("El periodo no tiene un id " + periodTagAsignationWeb.getPeriod().toString(), Response.Status.BAD_REQUEST);
            }
        }

    }

    public List<TagsWeb> getActiveByPeriodId(Long periodId, State state) {

        List<Tags> r = this.tagsDao.getByPeriodIdAndState(periodId, state);

        return this.modelWebTransformationService.tagsToTagsWeb(r);

    }

    public  List<Tags> getTagsByIndicatorIdAndPeriodId(Long indicatorId, Long periodId) throws GeneralAppException {
        return this.tagsDao.getTagsByIndicatorIdAndPeriodId(indicatorId, periodId);
    }











}
