package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.*;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodPopulationTypeDissagregationOption;
import org.unhcr.osmosys.services.PeriodService;
import org.unhcr.osmosys.services.standardDissagregations.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
@Path("test3")
public class Test3Endpoint {

    private static final Logger LOGGER = Logger.getLogger(TestEndpoint.class);

    @Inject
    PopulationTypeDissagregationOptionService populationTypeDissagregationOptionService;

    @Inject
    AgeDissagregationOptionService ageDissagregationOptionService;

    @Inject
    GenderDissagregationOptionService genderDissagregationOptionService;

    @Inject
    DiversityDissagregationOptionService diversityDissagregationOptionService;

    @Inject
    CountryOfOriginDissagregationOptionService countryOfOriginDissagregationOptionService;

    @Inject
    PeriodService periodService;

    @Path("test")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public String test() throws InterruptedException {
        return "test3";
    }

    @Path("fillOptions")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public String fillOptions() throws InterruptedException {
        List<PopulationTypeDissagregationOption> populationTypeDissagregationOptions = new ArrayList<>();
        PopulationTypeDissagregationOption populationTypeDissagregationOption1 = new PopulationTypeDissagregationOption("Refugiados y Solicitantes de Asilo", "Compass", 1, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption1);
        PopulationTypeDissagregationOption populationTypeDissagregationOption2 = new PopulationTypeDissagregationOption("Refugiados", "Estadísticas Poblacionales", 2, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption2);
        PopulationTypeDissagregationOption populationTypeDissagregationOption3 = new PopulationTypeDissagregationOption("Refugiados Retornados", "Estadísticas Poblacionales", 3, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption3);
        PopulationTypeDissagregationOption populationTypeDissagregationOption4 = new PopulationTypeDissagregationOption("Solicitantes de Asilo", "Estadísticas Poblacionales", 4, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption4);
        PopulationTypeDissagregationOption populationTypeDissagregationOption5 = new PopulationTypeDissagregationOption("Personas en Situación Similar a la de un Refugiado", "Estadísticas Poblacionales", 5, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption5);
        PopulationTypeDissagregationOption populationTypeDissagregationOption6 = new PopulationTypeDissagregationOption("Desplazados Internos", "Compass", 6, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption6);
        PopulationTypeDissagregationOption populationTypeDissagregationOption7 = new PopulationTypeDissagregationOption("Personas en Situación Similar a la de un Desplazado Interno", "El Salvador", 7, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption7);
        PopulationTypeDissagregationOption populationTypeDissagregationOption8 = new PopulationTypeDissagregationOption("Desplazados Internos Retornados", "Estadísticas Poblacionales", 8, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption8);
        PopulationTypeDissagregationOption populationTypeDissagregationOption9 = new PopulationTypeDissagregationOption("Apátridas", "Compass", 9, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption9);
        PopulationTypeDissagregationOption populationTypeDissagregationOption10 = new PopulationTypeDissagregationOption("Retornados", "Compass", 10, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption10);
        PopulationTypeDissagregationOption populationTypeDissagregationOption11 = new PopulationTypeDissagregationOption("Retornados con Necesidades de Protección", "El Salvador", 11, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption11);
        PopulationTypeDissagregationOption populationTypeDissagregationOption12 = new PopulationTypeDissagregationOption("Otras Personas en Necesidad de Protección Internacional", "Estadísticas Poblacionales", 12, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption12);
        PopulationTypeDissagregationOption populationTypeDissagregationOption13 = new PopulationTypeDissagregationOption("Otros de Interés", "Compass", 13, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption13);
        PopulationTypeDissagregationOption populationTypeDissagregationOption14 = new PopulationTypeDissagregationOption("Personas en Proceso de Reasentamiento", "El Salvador", 14, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption14);
        PopulationTypeDissagregationOption populationTypeDissagregationOption15 = new PopulationTypeDissagregationOption("Comunidad de Acogida", "Compass", 15, State.ACTIVO);
        populationTypeDissagregationOptions.add(populationTypeDissagregationOption15);

        for (PopulationTypeDissagregationOption populationTypeDissagregationOption : populationTypeDissagregationOptions) {
            this.populationTypeDissagregationOptionService.saveOrUpdate(populationTypeDissagregationOption);
        }
        return "done";
    }

    @Path("fillOptions2")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public String fillOptions2() {

        List<GenderDissagregationOption> genders = new ArrayList<>();
        GenderDissagregationOption a1 = new GenderDissagregationOption("Masculino", "Compass", 1, State.ACTIVO);
        genders.add(a1);
        GenderDissagregationOption a2 = new GenderDissagregationOption("Femenino", "Compass", 2, State.ACTIVO);
        genders.add(a2);
        GenderDissagregationOption a3 = new GenderDissagregationOption("No Binario", "Compass", 3, State.ACTIVO);
        genders.add(a3);

        for (GenderDissagregationOption gender : genders) {
            this.genderDissagregationOptionService.saveOrUpdate(gender);
        }

        List<AgeDissagregationOption> ages = new ArrayList<>();
        AgeDissagregationOption ag1 = new AgeDissagregationOption("Niños, Niñas y Adolescentes", "(0-18 años)", "Compass", 1, State.ACTIVO);
        ages.add(ag1);
        AgeDissagregationOption ag2 = new AgeDissagregationOption("Adultos", "(18 años o más )", "Compass", 2, State.ACTIVO);
        ages.add(ag2);
        AgeDissagregationOption ag3 = new AgeDissagregationOption("Infantes", "(0-4 años)", "Detalle 1", 3, State.ACTIVO);
        ages.add(ag3);
        AgeDissagregationOption ag4 = new AgeDissagregationOption("Niños, Niñas y Adolescentes", "(5-18 años)", "Detalle 1", 4, State.ACTIVO);
        ages.add(ag4);
        AgeDissagregationOption ag5 = new AgeDissagregationOption("Adultos", "(19 a 59 años)", "Detalle 1", 5, State.ACTIVO);
        ages.add(ag5);
        AgeDissagregationOption ag6 = new AgeDissagregationOption("Adultos Mayores", "(59 años o más )", "Detalle 1", 6, State.ACTIVO);
        ages.add(ag6);
        AgeDissagregationOption ag7 = new AgeDissagregationOption("Infantes", "(0-4 años)", "Detalle 2", 7, State.ACTIVO);
        ages.add(ag7);
        AgeDissagregationOption ag8 = new AgeDissagregationOption("Niños, Niñas y Adolescentes", "(5-18 años)", "Detalle 2", 8, State.ACTIVO);
        ages.add(ag8);
        AgeDissagregationOption ag9 = new AgeDissagregationOption("Adultos Jovenes", "(19 a 35 años)", "Detalle 2", 9, State.ACTIVO);
        ages.add(ag9);
        AgeDissagregationOption ag10 = new AgeDissagregationOption("Adultos", "(36 a 59 años)", "Detalle 2", 10, State.ACTIVO);
        ages.add(ag10);
        AgeDissagregationOption ag11 = new AgeDissagregationOption("Adultos Mayores", "(59 años o más )", "Detalle 2", 11, State.ACTIVO);
        ages.add(ag11);

        for (AgeDissagregationOption age : ages) {
            this.ageDissagregationOptionService.saveOrUpdate(age);
        }

        List<DiversityDissagregationOption> ds = new ArrayList<>();
        DiversityDissagregationOption d1 = new DiversityDissagregationOption("Personas con Discapacidad", "General", 1, State.ACTIVO);
        ds.add(d1);
        DiversityDissagregationOption d2 = new DiversityDissagregationOption("Indígenas", "General", 1, State.ACTIVO);
        ds.add(d2);
        DiversityDissagregationOption d3 = new DiversityDissagregationOption("Afrodescendientes", "General", 1, State.ACTIVO);
        ds.add(d3);
        DiversityDissagregationOption d4 = new DiversityDissagregationOption("LGBTI+", "General", 1, State.ACTIVO);
        ds.add(d4);

        for (DiversityDissagregationOption d : ds) {
            this.diversityDissagregationOptionService.saveOrUpdate(d);
        }


        List<CountryOfOriginDissagregationOption> cs = new ArrayList<>();
        //CountryOfOriginDissagregationOption c1 = new CountryOfOriginDissagregationOption("Venezuela","General",1,State.ACTIVO); cs.add(c1);
        CountryOfOriginDissagregationOption c2 = new CountryOfOriginDissagregationOption("Colombia", "General", 2, State.ACTIVO);
        cs.add(c2);
        CountryOfOriginDissagregationOption c3 = new CountryOfOriginDissagregationOption("Ecuador", "General", 3, State.ACTIVO);
        cs.add(c3);
        CountryOfOriginDissagregationOption c4 = new CountryOfOriginDissagregationOption("Otros Países", "General", 4, State.ACTIVO);
        cs.add(c4);

        for (CountryOfOriginDissagregationOption c : cs) {
            this.countryOfOriginDissagregationOptionService.saveOrUpdate(c);
        }


        return "done";
    }


    @Path("setPeriod1")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public String setPeriod() {
        Period period2024 = this.periodService.getWithDissagregationOptionsById(3l);

        List<PopulationTypeDissagregationOption> ptos = new ArrayList<>();
        ptos.add(this.populationTypeDissagregationOptionService.getById(1l));
        ptos.add(this.populationTypeDissagregationOptionService.getById(6l));
        ptos.add(this.populationTypeDissagregationOptionService.getById(7l));
        ptos.add(this.populationTypeDissagregationOptionService.getById(11l));
        ptos.add(this.populationTypeDissagregationOptionService.getById(14l));
        ptos.add(this.populationTypeDissagregationOptionService.getById(15l));

        List<PeriodPopulationTypeDissagregationOption> pptos = new ArrayList<>();

        for (PopulationTypeDissagregationOption pto : ptos) {
            this.periodService.addPopulationTypeDissagregationOption(3l, 1l);

            /*
            PeriodPopulationTypeDissagregationOption periodPopulationTypeDissagregationOption = new PeriodPopulationTypeDissagregationOption(period2024, pto);
            period2024.addPeriodPopulationTypeDissagregationOption(periodPopulationTypeDissagregationOption);
            pptos.add(periodPopulationTypeDissagregationOption);
            */
        }

        this.periodService.saveOrUpdate(period2024);

        return "done";
    }



    @Path("getdata")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgeDissagregationOption> getData() {

        return this.ageDissagregationOptionService.getByState(State.ACTIVO);
    }
}

