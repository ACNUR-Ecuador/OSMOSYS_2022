package org.unhcr.osmosys.services.standardDissagregations;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.daos.standardDissagregations.StandardDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.StandardDissagregationOption;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.inject.Inject;
import java.util.List;


public abstract class StandardDissagregationOptionService<T extends StandardDissagregationOptionDao, E extends StandardDissagregationOption> {

    @Inject
    private ModelWebTransformationService modelWebTransformationService;

    protected abstract T getDao();




    public E getById(Long id) {
        return (E) this.getDao().find(id);
    }

    public E saveOrUpdate(E dissagregationOption) {
        if (dissagregationOption.getId() == null) {
            this.getDao().save(dissagregationOption);
        } else {
            this.getDao().update(dissagregationOption);
        }
        return dissagregationOption;
    }

    public List<E> getByState(State state) {
        return this.getDao().getByState(state);
    }

    public List<StandardDissagregationOptionWeb> getWebByState(State state){

        return this.modelWebTransformationService
                .standardDissagregationOptionsToStandardDissagregationOptionWebs(
                        this.getDao().getByState(state)
                );
    }


}
