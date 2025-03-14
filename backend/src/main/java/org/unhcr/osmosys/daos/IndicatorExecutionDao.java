package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.IndicatorExecution;
import org.unhcr.osmosys.model.enums.Frecuency;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unchecked")
@Stateless
public class IndicatorExecutionDao extends GenericDaoJpa<IndicatorExecution, Long> {
    @SuppressWarnings("unused")
    @Inject
    private ModelWebTransformationService modelWebTransformationService;

    private static final Logger LOGGER = Logger.getLogger(IndicatorExecutionDao.class);

    public IndicatorExecutionDao() {
        super(IndicatorExecution.class, Long.class);
    }

    public List<IndicatorExecution> getByIndicatorIdAndPeriodId(Long periodId, Long indicatorId) {
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE i.id = :indicatorId" +
                " and o.period.id =: periodId " ;
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("indicatorId", indicatorId);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }


    public List<IndicatorExecution> getGeneralIndicatorsExecutionsByPeriodId(Long periodId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.period per " +
                // " left join fetch o.quarters q " +
                // " left join fetch  q.months mo " +
                // " left join fetch mo.indicatorValues iv " +
                // " left join fetch mo.indicatorValuesIndicatorValueCustomDissagregations ivc " +
                " WHERE per.id = :periodId and o.indicatorType=:indicatorType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("indicatorType", indicatorType);
        return q.getResultList();
    }

    /**************************************************************************/

    public static final String jpqlProjectIndicators =
            " SELECT DISTINCT o FROM IndicatorExecution o " +
                    " left join fetch o.indicator i " +
                    " left join fetch o.project pr " +
                    " left join fetch pr.focalPointAssignations fpa " +
                    " left join fetch fpa.focalPointer fpu " +
                    " left join fetch pr.organization org " +
                    " left join fetch i.statement ist " +
                    " left join fetch ist.area " +
                    " left join fetch o.projectStatement pst " +
                    " left join fetch o.indicatorExecutionLocationAssigments iela " +
                    " left join fetch iela.location can " +
                    " left join fetch can.provincia prov " +
                    " left join fetch pst.area psta" +
                    " left join fetch pst.situation pstsit " +
                    " left join fetch pst.pillar  pstpil" +
                    " left join fetch o.quarters q " +
                    " left join fetch q.months m " +
                    " left join fetch m.sources sou " +
                    " left join fetch o.period p " +
                    /*" left join fetch p.periodAgeDissagregationOptions " +
                    " left join fetch p.periodCountryOfOriginDissagregationOptions " +
                    " left join fetch p.periodDiversityDissagregationOptions " +
                    " left join fetch p.periodGenderDissagregationOptions " +
                    " left join fetch p.periodPopulationTypeDissagregationOptions " +*/
                    " left join fetch p.generalIndicator " ;
    public static final String jpqlProjectIndicatorsSimplified =
            " SELECT DISTINCT o FROM IndicatorExecution o " +
                    " left join fetch o.indicator i " +
                    " left join fetch o.project pr "
                   ;

    public static final String jpqlProjectIndicatorsAdmin =
            " SELECT DISTINCT o FROM IndicatorExecution o " +
                    " left join fetch o.indicator i " +
                    " left join fetch o.project pr " ;

    public static final String jpqlDirectImplementationIndicators =
            "SELECT DISTINCT o FROM IndicatorExecution o " +
                    " left join fetch o.indicatorExecutionLocationAssigments iela " +
                    " left join fetch iela.location can " +
                    " left join fetch can.provincia prov " +
                    " left join fetch o.indicator i " +
                    " left join fetch i.statement st " +
                    " left join fetch st.area " +
                    " left join fetch o.quarters q " +
                    " left join fetch q.months m " +
                    " left join fetch m.sources " +
                    " left join fetch o.period p " +
                    " left join fetch p.generalIndicator " +
                    " left join fetch o.reportingOffice repOff " +
                    " left join fetch o.supervisorUser su " +
                    " left join fetch su.organization suorg " +
                    " left join fetch su.office suofff " +
                    " left join fetch o.assignedUser au " +
                    " left join fetch au.organization auorg" +
                    " left join fetch au.office auoff" +
                    " left join fetch o.assignedUserBackup aub " +
                    " left join fetch aub.organization auborg " +
                    " left join fetch aub.office aubofff ";

    /**
     * partners
     ***/
    public List<IndicatorExecution> getIndicatorExecutionsByProjectId(Long projectId) {

        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE pr.id = :projectId";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE pr.id = :projectId" +
                " and o.indicatorType = :generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        return q.getResultList();
    }



    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectIdAndState(Long projectId, State state) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE pr.id = :projectId" +
                " and o.indicatorType =: generalType " +
                " and o.state = :state " +
                " and m.state = :state ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("state", state);

        return q.getResultList();
    }

    public List<IndicatorExecution> getPerformanceIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicatorsAdmin +
                " WHERE pr.id = :projectId" +
                " and o.indicatorType <>: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        return q.getResultList();
    }

    public List<IndicatorExecution> getPerformanceIndicatorExecutionsByProjectIdAndState(Long projectId, State state) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicatorsSimplified +

                " WHERE pr.id = :projectId" +
                " and o.indicatorType <> :generalType " +
                " and o.state = :state ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId", projectId);
        q.setParameter("generalType", indicatorType);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<IndicatorExecution> getActivePartnersIndicatorExecutionsByPeriodId(Long periodId) {

        String jpql= " " +
                "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.indicator i " +
                " left join fetch o.project pr " +
                " left join fetch pr.projectLocationAssigments pla" +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch pr.organization org " +
                " left join fetch o.projectStatement pst " +
               /*

                " left join fetch pr.focalPoint fpu " +

                " left join fetch i.statement ist " +
                " left join fetch ist.area " +

                " left join fetch o.indicatorExecutionLocationAssigments iela " +
                " left join fetch iela.location can " +
                " left join fetch can.provincia prov " +
                " left join fetch pst.area psta" +
                " left join fetch pst.situation pstsit " +
                " left join fetch pst.pillar  pstpil" +

                " left join fetch m.sources sou " +
                " left join fetch o.period p "+
                " left join fetch fpu.organization fpuorg " +

                " left join fetch pla.location canpl" +
                " left join fetch canpl.provincia " +*/
                " WHERE " +
                "  o.period.id =:periodId " +
                " and o.state =:state " +
                " and o.project.state =:state " +
                " and (pla.state is null or pla.state =:state )" +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " order by org.acronym, org.description, " +
                " pr.code, pr.name, o.indicatorType, pst.code , i.code  ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getActivePartnersIndicatorExecutionsByProjectId(Long projectId) {
        String jpql =
                " SELECT DISTINCT o FROM IndicatorExecution o " +
                        " left join fetch o.indicator i " +
                        " left join fetch o.project pr " +
                        " left join fetch pr.focalPointAssignations fpa " +
                        " left join fetch fpa.focalPointer fpu " +
                        " left join fetch pr.organization org " +
                        " left join fetch i.statement ist " +
                        " left join fetch ist.area " +
                        " left join fetch o.projectStatement pst " +
                        // " left join fetch o.indicatorExecutionLocationAssigments iela " +
                        // " left join fetch iela.location can " +
                        // " left join fetch can.provincia prov " +
                        " left join fetch pst.area psta" +
                        " left join fetch pst.situation pstsit " +
                        " left join fetch pst.pillar  pstpil" +
                        " left join fetch o.quarters q " +
                        " left join fetch q.months m " +
                        " left join fetch m.sources sou " +
                        " left join fetch o.period p " +
                        " left join fetch p.generalIndicator " +
                        " left join fetch o.indicator " +
                        " left join fetch fpu.organization fpuorg " +
                        // " left join fetch pr.projectLocationAssigments pla" +
                        // " left join fetch pla.location canpl" +
                        // " left join fetch canpl.provincia " +
                        " WHERE " +
                        " pr.id =:projectId " +
                        " and o.project.state =:state " +
                        " and o.state =:state " +
                        // " and (pla.state is null or pla.state =:state )" +
                        " and (q.state is null or q.state =:state )" +
                        " and (m.state is null or m.state =:state )" +
                        " " +
                        " order by org.acronym, org.description, " +
                        " pr.code, pr.name, o.indicatorType, o.projectStatement.code , i.code  ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }


    /*** direct implementation**********/
    public List<IndicatorExecution> getDirectImplementationIndicatorByPeriodId(Long periodId) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        return q.getResultList();
    }

    public List<IndicatorExecution> getDirectImplementationActiveByPeriodId(Long periodId) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null " +
                " and o.state=:state ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }

    public List<IndicatorExecution> getDirectImplementationIndicatorByPeriodIdAndState(Long periodId, State state) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null " +
                " and o.state=:state ";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        q.setParameter("state", state);
        return q.getResultList();
    }

    /**
     * for validation, without related data
     *
     * @param indicatorId
     * @param reportingOfficeId
     * @return
     */
    public IndicatorExecution getByIndicatorIdAndOfficeIdAndPeriodId(Long indicatorId, Long reportingOfficeId, Long periodId) {

        String jpql = "select o from IndicatorExecution o " +
                " WHERE o.indicator.id = :indicatorId and o.reportingOffice.id = :reportingOfficeId" +
                " and o.period.id =:periodId";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("indicatorId", indicatorId);
        q.setParameter("reportingOfficeId", reportingOfficeId);
        q.setParameter("periodId", periodId);

        try {
            return (IndicatorExecution) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<IndicatorExecution> getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
            Long userId,
            Long periodId,
            Long officeId,
            boolean supervisor,
            boolean responsible,
            boolean backup) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql =
                IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                        " WHERE p.id = :periodId " +
                        " and o.indicatorType <> :generalType " +
                        " and o.project.id is null " +
                        " and o.state =:state " +
                        " and q.state =:state " +
                        " and m.state =:state ";

        if (officeId != null) {
            jpql += " and repOff.id =:officeId ";
        }
        if (userId != null) {
            if (responsible && backup && supervisor) {
                jpql += " and (au.id =:userId or aub.id =:userId or su.id =:userId ) ";
            } else if (responsible && backup) {
                jpql += " and (au.id =:userId or aub.id =:userId) ";
            } else if (responsible && supervisor) {
                jpql += " and (au.id =:userId or su.id =:userId ) ";
            } else if (responsible) {
                jpql += " and (au.id =:userId) ";
            } else if (backup && supervisor) {
                jpql += " and (aub.id =:userId or su.id =:userId ) ";
            } else if (backup) {
                jpql += " and (aub.id =:userId) ";
            } else if (supervisor) {
                jpql += " and ( su.id =:userId ) ";
            }
        }
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        q.setParameter("state", State.ACTIVO);
        if (officeId != null) {
            q.setParameter("officeId", officeId);
        }
        if (userId != null) {
            q.setParameter("userId", userId);
        }
        return q.getResultList();
    }

    /**
     * for values quartes and month creation
     *
     * @param periodId
     * @return
     */
    public List<IndicatorExecution> getAllIndicatorDirectImplementationNoValues(Long periodId) {
        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project is null " +
                " and q.id is null";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", generalType);
        return q.getResultList();
    }

    public List<IndicatorExecution> getDirectImplementationIndicatorExecutionsByIdsAndState(List<Long> indicatorExecutionIds, State state) {

        IndicatorType generalType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE o.id in (:indicatorExecutionIds) " +
                " and o.project is null " +
                " and o.state=:state " +
                " and o.indicatorType <> :generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("generalType", generalType);
        q.setParameter("state", state);
        q.setParameter("indicatorExecutionIds", indicatorExecutionIds);
        return q.getResultList();
    }

    public IndicatorExecution getDirectImplementationIndicatorExecutionsById(Long indicatorExecutionId) {
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE o.id =:indicatorExecutionId " +
                " and o.project is null ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("indicatorExecutionId", indicatorExecutionId);
        try {
            return (IndicatorExecution) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**** partners and direct implementation*****/
    public IndicatorExecution getPerformanceIndicatorExecutionById(Long id) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.id = :id" +
                " and o.indicatorType <>: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("generalType", indicatorType);
        return (IndicatorExecution) q.getSingleResult();
    }

    public IndicatorExecution getPartnerIndicatorExecutionById(Long id) {
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.id = :id" +
                " and o.project is not null ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);

        try {
            return (IndicatorExecution) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public IndicatorExecution getByIdWithIndicatorValues(Long id) {

        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.indicator ind " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources " +
                " left join fetch  m.indicatorValues " +
                " left join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE o.id = :id";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        return (IndicatorExecution) q.getSingleResult();
    }

    public List<IndicatorExecution> getByIdsWithIndicatorValues(List<Long> ids) {

        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.indicator ind " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch m.sources " +
                " left join fetch  m.indicatorValues " +
                " left join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE o.id in (:ids)";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("ids", ids);
        return q.getResultList();
    }

    public List<Long> getByQuartersIds(List<Long> quartersIds) {
        String jpql = "SELECT DISTINCT o.id FROM IndicatorExecution o " +
                " inner join o.quarters q " +
                " WHERE q.id in (:quartersIds)";
        Query q = getEntityManager().createQuery(jpql, Long.class);
        q.setParameter("quartersIds", quartersIds);
        return q.getResultList();
    }


    public List<IndicatorExecution> getByPeriodIdAndIndicatorId(Long periodId, Long indicatorId) {
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.period per " +
                " left join fetch o.indicator i " +
                " left join fetch o.quarters q " +
                " left join fetch  q.months mo " +
                " left join fetch mo.indicatorValues iv " +
                " left join fetch mo.indicatorValuesIndicatorValueCustomDissagregations ivc " +
                " WHERE per.id = :periodId and i.id=:indicatorId ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("indicatorId", indicatorId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getLateIndicatorExecutionGeneralByProjectIdMonthly(Long id, Integer yearToControl, List<MonthEnum> monthsToControl) {

        IndicatorType generalType = IndicatorType.GENERAL;
        State stateActive = State.ACTIVO;
        Frecuency frecuency = Frecuency.MENSUAL;
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE " +
                " o.id = :id " +
                " and o.indicator.frecuency = :frecuency " +
                " and o.state = :stateActive " +
                " and m.state = :stateActive " +
                " and o.indicatorType = :generalType " +
                " and m.totalExecution is null " +
                " and ( m.year < :year or (m.year =:year and m.month in (:monthsToControl))) ";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id", id);
        q.setParameter("frecuency", frecuency);
        q.setParameter("stateActive", stateActive);
        q.setParameter("generalType", generalType);
        q.setParameter("monthsToControl", monthsToControl);
        q.setParameter("year", yearToControl);
        return q.getResultList();
    }

    public List<User> getSupervisorstByPeriodId(Long periodId) {
        String jpql = "SELECT DISTINCT u FROM IndicatorExecution o " +
                " inner join o.supervisorUser u " +
                " WHERE u.id is not null " +
                " and o.period.id=:periodId " +
                " and o.state =:state ";
        Query q = getEntityManager().createQuery(jpql, User.class);
        q.setParameter("periodId", periodId);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }

    public List<IndicatorExecution> getDirectImplementationsIndicatorExecutionsBySupervisorId(Long periodId, Long supervisorId) {
        String jpql =
                IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                        " WHERE p.id = :periodId " +
                        " and o.indicatorType <> :generalType " +
                        " and o.project.id is null " +
                        " and su.id =:supervisorId " +
                        " and o.state =:state " +
                        " and q.state =:state " +
                        " and m.state =:state ";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", IndicatorType.GENERAL);
        q.setParameter("supervisorId", supervisorId);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }

    public Object findIndicatorDissagregationValuesById(Long idValue) {
        // JPQL de la consulta con JOIN FETCH
        String jpql = "SELECT new map( "
                + "p.code AS Código_de_Proyecto, "
                + "p.name AS Nombre_Proyecto, "
                + "i.code AS Código_de_Indicador, "
                + "i.description AS Descripción_de_Indicador, "
                + "o.acronym AS Organización_Acrónimo, "
                + "o.description AS Organización_Descr, "
                + "q.year AS Periodo, "
                + "q.quarter AS Trimestre, "
                + "m.month AS Mes, "
                + "iv.dissagregationType AS Tipo_de_Desagregación, "
                + "iv.value AS Valor, "
                + "iv.location.id AS Cantón_id, "
                + "cantonOption.name AS Cantón_nombre, "
                + "iv.populationType.id AS Tipo_de_población_id, "
                + "populationTypeOption.name AS Tipo_de_población, "
                + "iv.ageType.id AS Edad_id, "
                + "ageOption.name AS Edad, "
                + "iv.genderType.id AS Género_id, "
                + "genderOption.name AS Género, "
                + "iv.countryOfOrigin.id AS País_de_Origen_id, "
                + "countryOfOriginOption.name AS País_de_Origen, "
                + "iv.diversityType.id AS Diversidad_id, "
                + "diversityOption.name AS Diversidad "
                + ") "
                + "FROM Project p "
                + "JOIN p.indicatorExecutions ie "
                + "JOIN ie.indicator i "
                + "JOIN p.organization o "
                + "JOIN ie.quarters q "
                + "JOIN q.months m "
                + "JOIN m.indicatorValues iv "
                + "LEFT JOIN iv.location cantonOption "
                + "LEFT JOIN iv.populationType populationTypeOption "
                + "LEFT JOIN iv.ageType ageOption "
                + "LEFT JOIN iv.genderType genderOption "
                + "LEFT JOIN iv.countryOfOrigin countryOfOriginOption "
                + "LEFT JOIN iv.diversityType diversityOption "
                + "WHERE iv.id = :idValue "
                + "AND ie.project.id = p.id "
                + "AND ie.indicator.id = i.id "
                + "AND o.id = p.organization.id "
                + "AND q.indicatorExecution.id = ie.id "
                + "AND q.id = m.quarter.id "
                + "AND m.id = iv.month.id";

        // Ejecutar la consulta con el parámetro `idValue`
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("idValue", idValue);

        // Obtener los resultados como un único Map
        return query.getSingleResult();
    }


    public Object findIndicatorCustomDissagregationValuesById(Long idValue) {
        // JPQL de la consulta
        String jpql = "SELECT new map( "
                + "p.code AS Código_de_Proyecto, "
                + "p.name AS Nombre_Proyecto, "
                + "i.code AS Código_de_Indicador, "
                + "i.description AS Descripción_de_Indicador, "
                + "o.acronym AS Organización_Acrónimo, "
                + "o.description AS Organización_Descr, "
                + "q.year AS Periodo, "
                + "q.quarter AS Trimestre, "
                + "m.month AS Mes, "
                + "cd.name AS Nombre_de_Desagregación, "
                + "iv.value AS Valor, "
                + "iv.customDissagregationOption.id AS Opción_de_Desagregación_id, "
                + "cdo.name AS Opción_de_Desagregación "
                + ") "
                + "FROM Project p "
                + "JOIN p.indicatorExecutions ie "
                + "JOIN ie.indicator i "
                + "JOIN p.organization o "
                + "JOIN ie.quarters q "
                + "JOIN q.months m "
                + "JOIN m.indicatorValuesIndicatorValueCustomDissagregations iv "
                + "JOIN iv.customDissagregationOption cdo "
                + "JOIN cdo.customDissagregation cd "
                + "WHERE iv.id = :idValue "
                + "AND ie.project.id = p.id "
                + "AND ie.indicator.id = i.id "
                + "AND o.id = p.organization.id "
                + "AND q.indicatorExecution.id = ie.id "
                + "AND q.id = m.quarter.id "
                + "AND m.id = iv.month.id "
                + "AND iv.customDissagregationOption.id = cdo.id "
                + "AND cdo.customDissagregation.id = cd.id";

        // Ejecutar la consulta con el parámetro `idValue`
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("idValue", idValue);

        // Obtener los resultados como una lista de Mapas
        List<?> results = query.getResultList();


        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    public Object findIndicatorDirectImplementationValuesById(Long idValue) {
        // JPQL de la consulta con JOIN FETCH
        String jpql = "SELECT new map( "
                + "i.code AS Código_de_Indicador, "
                + "i.description AS Descripción_de_Indicador, "
                + "q.year AS Periodo, "
                + "q.quarter AS Trimestre, "
                + "m.month AS Mes, "
                + "iv.dissagregationType AS Tipo_de_Desagregación, "
                + "iv.value AS Valor, "
                + "CONCAT(ie.reportingOffice.acronym, '-', ie.reportingOffice.description) AS Oficina, "
                + "ie.supervisorUser.name AS Supervisor, "
                + "ie.assignedUser.name AS Responsable ,"
                + "iv.location.id AS Cantón_id, "
                + "cantonOption.name AS Cantón_nombre, "
                + "iv.populationType.id AS Tipo_de_población_id, "
                + "populationTypeOption.name AS Tipo_de_población, "
                + "iv.ageType.id AS Edad_id, "
                + "ageOption.name AS Edad, "
                + "iv.genderType.id AS Género_id, "
                + "genderOption.name AS Género, "
                + "iv.countryOfOrigin.id AS País_de_Origen_id, "
                + "countryOfOriginOption.name AS País_de_Origen, "
                + "iv.diversityType.id AS Diversidad_id, "
                + "diversityOption.name AS Diversidad "
                + ") "
                + "FROM IndicatorExecution ie "
                + "JOIN ie.indicator i "
                + "JOIN ie.quarters q "
                + "JOIN q.months m "
                + "JOIN m.indicatorValues iv "
                + "LEFT JOIN iv.location cantonOption "
                + "LEFT JOIN iv.populationType populationTypeOption "
                + "LEFT JOIN iv.ageType ageOption "
                + "LEFT JOIN iv.genderType genderOption "
                + "LEFT JOIN iv.countryOfOrigin countryOfOriginOption "
                + "LEFT JOIN iv.diversityType diversityOption "
                + "WHERE iv.id = :idValue "
                + "AND ie.indicator.id = i.id "
                + "AND q.indicatorExecution.id = ie.id "
                + "AND q.id = m.quarter.id "
                + "AND m.id = iv.month.id";

        // Ejecutar la consulta con el parámetro `idValue`
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("idValue", idValue);

        // Obtener los resultados como un único Map
        return query.getSingleResult();
    }

    public Object findIndicatorDirectImplementationCustomDissValuesById(Long idValue) {
        // JPQL de la consulta con JOIN FETCH
        String jpql = "SELECT new map( "
                + "i.code AS Código_de_Indicador, "
                + "i.description AS Descripción_de_Indicador, "
                + "q.year AS Periodo, "
                + "q.quarter AS Trimestre, "
                + "m.month AS Mes, "
                + "cd.name AS Nombre_de_Desagregación, "
                + "iv.value AS Valor, "
                + "CONCAT(ie.reportingOffice.acronym, '-', ie.reportingOffice.description) AS Oficina, "
                + "ie.supervisorUser.name AS Supervisor, "
                + "ie.assignedUser.name AS Responsable ,"
                + "iv.customDissagregationOption.id AS Opción_de_Desagregación_id, "
                + "cdo.name AS Opción_de_Desagregación "
                + ") "
                + "FROM IndicatorExecution ie "
                + "JOIN ie.indicator i "
                + "JOIN ie.quarters q "
                + "JOIN q.months m "
                + "JOIN m.indicatorValuesIndicatorValueCustomDissagregations iv "
                + "JOIN iv.customDissagregationOption cdo "
                + "JOIN cdo.customDissagregation cd "
                + "WHERE iv.id = :idValue "
                + "AND ie.indicator.id = i.id "
                + "AND q.indicatorExecution.id = ie.id "
                + "AND q.id = m.quarter.id "
                + "AND m.id = iv.month.id";

        // Ejecutar la consulta con el parámetro `idValue`
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("idValue", idValue);

        // Obtener los resultados como un único Map
        return query.getSingleResult();
    }
    public Object findGeneralIndicatorDissagregationValuesById(Long idValue) {
        // JPQL de la consulta con JOIN FETCH
        String jpql = "SELECT new map( "
                + "p.code AS Código_de_Proyecto, "
                + "p.name AS Nombre_Proyecto, "
                + "i.id AS ID_de_Indicador, "
                + "i.description AS Descripción_de_Indicador, "
                + "o.acronym AS Organización_Acrónimo, "
                + "o.description AS Organización_Descr, "
                + "q.year AS Periodo, "
                + "q.quarter AS Trimestre, "
                + "m.month AS Mes, "
                + "iv.dissagregationType AS Tipo_de_Desagregación, "
                + "iv.value AS Valor, "
                + "iv.location.id AS Cantón_id, "
                + "cantonOption.name AS Cantón_nombre, "
                + "iv.populationType.id AS Tipo_de_población_id, "
                + "populationTypeOption.name AS Tipo_de_población, "
                + "iv.ageType.id AS Edad_id, "
                + "ageOption.name AS Edad, "
                + "iv.genderType.id AS Género_id, "
                + "genderOption.name AS Género, "
                + "iv.countryOfOrigin.id AS País_de_Origen_id, "
                + "countryOfOriginOption.name AS País_de_Origen, "
                + "iv.diversityType.id AS Diversidad_id, "
                + "diversityOption.name AS Diversidad "
                + ") "
                + "FROM Project p "
                + "JOIN p.indicatorExecutions ie "
                + "JOIN p.period pe "
                + "JOIN pe.generalIndicator i "
                + "JOIN p.organization o "
                + "JOIN ie.quarters q "
                + "JOIN q.months m "
                + "JOIN m.indicatorValues iv "
                + "LEFT JOIN iv.location cantonOption "
                + "LEFT JOIN iv.populationType populationTypeOption "
                + "LEFT JOIN iv.ageType ageOption "
                + "LEFT JOIN iv.genderType genderOption "
                + "LEFT JOIN iv.countryOfOrigin countryOfOriginOption "
                + "LEFT JOIN iv.diversityType diversityOption "
                + "WHERE iv.id = :idValue "
                + "AND ie.project.id = p.id "
                + "AND p.period.id = pe.id "
                + "AND pe.generalIndicator.id = i.id "
                + "AND o.id = p.organization.id "
                + "AND q.indicatorExecution.id = ie.id "
                + "AND q.id = m.quarter.id "
                + "AND m.id = iv.month.id";

        // Ejecutar la consulta con el parámetro `idValue`
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("idValue", idValue);

        // Obtener los resultados como un único Map
        return query.getSingleResult();
    }

    public Object findGeneralIndicatorCustomDissagregationValuesById(Long idValue) {
        // JPQL de la consulta
        String jpql = "SELECT new map( "
                + "p.code AS Código_de_Proyecto, "
                + "p.name AS Nombre_Proyecto, "
                + "i.id AS ID_de_Indicador, "
                + "i.description AS Descripción_de_Indicador, "
                + "o.acronym AS Organización_Acrónimo, "
                + "o.description AS Organización_Descr, "
                + "q.year AS Periodo, "
                + "q.quarter AS Trimestre, "
                + "m.month AS Mes, "
                + "cd.name AS Nombre_de_Desagregación, "
                + "iv.value AS Valor, "
                + "iv.customDissagregationOption.id AS Opción_de_Desagregación_id, "
                + "cdo.name AS Opción_de_Desagregación "
                + ") "
                + "FROM Project p "
                + "JOIN p.indicatorExecutions ie "
                + "JOIN p.period pe "
                + "JOIN pe.generalIndicator i "
                + "JOIN p.organization o "
                + "JOIN ie.quarters q "
                + "JOIN q.months m "
                + "JOIN m.indicatorValuesIndicatorValueCustomDissagregations iv "
                + "JOIN iv.customDissagregationOption cdo "
                + "JOIN cdo.customDissagregation cd "
                + "WHERE iv.id = :idValue "
                + "AND ie.project.id = p.id "
                + "AND p.period.id = pe.id "
                + "AND pe.generalIndicator.id = i.id "
                + "AND o.id = p.organization.id "
                + "AND q.indicatorExecution.id = ie.id "
                + "AND q.id = m.quarter.id "
                + "AND m.id = iv.month.id "
                + "AND iv.customDissagregationOption.id = cdo.id "
                + "AND cdo.customDissagregation.id = cd.id";

        // Ejecutar la consulta con el parámetro `idValue`
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("idValue", idValue);

        // Obtener los resultados como una lista de Mapas
        List<?> results = query.getResultList();


        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    public ResultManagerIndicatorWeb getIndicatorAnualTargetandExecutions(Long indicatorId, Long periodId) {
        String jpql = "SELECT SUM(ie.target), SUM(ie.totalExecution) FROM IndicatorExecution ie " +
                "WHERE ie.indicator.id = :indicatorId " +
                "AND ie.indicator.state = :state " +
                "AND ie.state = :state " +
                "AND ie.period.id = :periodId " +
                "GROUP BY ie.indicator.id";

        Query q = getEntityManager().createQuery(jpql);
        q.setParameter("indicatorId", indicatorId);
        q.setParameter("periodId", periodId);
        q.setParameter("state", State.ACTIVO);

        List<Object[]> results = q.getResultList();

        BigDecimal anualTarget;
        BigDecimal totalExecution;
        // Verifica si la consulta devuelve resultados
        if (results.isEmpty()) {
            anualTarget = BigDecimal.ZERO;
            totalExecution = BigDecimal.ZERO;
        }else{
            // Si hay resultados, procesarlos
            Object[] result = results.get(0);
            anualTarget = (result[0] == null) ? BigDecimal.ZERO : (BigDecimal) result[0];
            totalExecution = (result[1] == null) ? BigDecimal.ZERO : (BigDecimal) result[1];

        }

        ResultManagerIndicatorWeb resultManager = new ResultManagerIndicatorWeb();
        resultManager.setAnualExecution(totalExecution);
        resultManager.setAnualTarget(anualTarget);

        return resultManager;
    }


    public List<ResultManagerIndicatorQuarterWeb> getIndicatorQuarterExecutions(Long indicatorId, int year) {

        String jpql = "SELECT q.quarterYearOrder, SUM(q.totalExecution) " +
                "FROM IndicatorExecution ie " +
                "LEFT JOIN ie.quarters q " +
                "WHERE ie.indicator.id = :indicatorId " +
                "AND ie.indicator.state = :state " +
                "AND ie.state = :state " +
                "AND q.year = :year " +
                "GROUP BY q.quarterYearOrder "+
                "ORDER BY q.quarterYearOrder";

        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("indicatorId", indicatorId);
        query.setParameter("year", year);
        query.setParameter("state", State.ACTIVO);

        // Obtener la lista de resultados (List<Object[]>)
        List<Object[]> results = query.getResultList();

        List<ResultManagerIndicatorQuarterWeb> resultList = new ArrayList<>();

        for (Object[] result : results) {
            int quarterYearOrder = (int) result[0];  // El valor de q.quarterYearOrder
            BigDecimal totalQuarterExecution;
            if(result[1]==null){
                totalQuarterExecution=BigDecimal.ZERO;
            }else{
                totalQuarterExecution = (BigDecimal) result[1]; // El valor de SUM(q.totalExecution)

            }

            ResultManagerIndicatorQuarterWeb rmiq = new ResultManagerIndicatorQuarterWeb();
            rmiq.setQuarter(quarterYearOrder);
            rmiq.setQuarterExecution(totalQuarterExecution);

            resultList.add(rmiq);
        }

        return resultList;
    }
    public List<ResultManagerQuarterImplementerWeb> getIndicatorQuarterImplementers(Long indicatorId, Long periodId, int quarterOrder) throws GeneralAppException {
        String jpql = "SELECT ie, q.totalExecution " +
                "FROM IndicatorExecution ie " +
                "LEFT JOIN ie.quarters q " +
                "WHERE ie.indicator.id = :indicatorId " +
                "AND ie.indicator.state = :state " +
                "AND ie.state = :state " +
                "AND ie.period.id = :periodId "+
                "AND q.quarterYearOrder = :quarterOrder ";

        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("indicatorId", indicatorId);
        query.setParameter("periodId", periodId);
        query.setParameter("quarterOrder", quarterOrder);
        query.setParameter("state", State.ACTIVO);

        // Obtener la lista de resultados (List<Object[]>)
        List<Object[]> results = query.getResultList();

        List<ResultManagerQuarterImplementerWeb> resultList = new ArrayList<>();

        // Iterar sobre los resultados para mapearlos a ResultManagerIndicatorQuarterWeb
        for (Object[] result : results) {
            IndicatorExecutionWeb iew = this.modelWebTransformationService.indicatorExecutionToIndicatorExecutionWeb((IndicatorExecution)result[0],true);  // El valor de q.quarterYearOrder
            BigDecimal quarterExecution;
            if(result[1]==null){
                quarterExecution =BigDecimal.ZERO;
            }else{
                quarterExecution = (BigDecimal) result[1];

            }
            ResultManagerQuarterImplementerWeb rmq = new ResultManagerQuarterImplementerWeb();
            rmq.setQuarterImplementerExecution(quarterExecution);
            rmq.setIndicatorExecution(iew);

            resultList.add(rmq);
        }

        return resultList;

    }

    public List<ResultManagerQuarterPopulationTypeWeb> getResultManagerQuarterPopulationType(Long indicatorId, int quarterOrder, int year) {
        String psql = "SELECT  " +
                "SUM(iv.value), sdo " +
                "FROM  " +
                "IndicatorExecution ie   " +
                "LEFT JOIN ie.quarters q " +
                "LEFT JOIN q.months m " +
                "LEFT JOIN m.indicatorValues iv " +
                "LEFT JOIN iv.populationType sdo " +
                "WHERE ie.indicator.id = :indicatorId AND q.quarterYearOrder= :quarterOrder AND q.year= :year AND sdo.state=:state AND ie.state = :state " +
                "GROUP BY sdo.id";

        Query query = getEntityManager().createQuery(psql);
        query.setParameter("indicatorId", indicatorId);
        query.setParameter("year", year);
        query.setParameter("quarterOrder", quarterOrder);
        query.setParameter("state", State.ACTIVO);

        List<Object[]> results = query.getResultList();

        List<ResultManagerQuarterPopulationTypeWeb> resultList = new ArrayList<>();

        // Iterar sobre los resultados para mapearlos a ResultManagerQuarterPopulationTypeWeb
        for (Object[] result : results) {
            BigDecimal quarterExecution = (result[0] == null) ? BigDecimal.ZERO : (BigDecimal) result[0];
            StandardDissagregationOption sdow = (StandardDissagregationOption) result[1];

            StandardDissagregationOptionWeb sdowWeb = this.modelWebTransformationService.standardDissagregationOptionToStandardDissagregationOptionWeb(sdow);

            ResultManagerQuarterPopulationTypeWeb rmqpt = new ResultManagerQuarterPopulationTypeWeb();
            rmqpt.setQuarterPopulationTypeExecution(quarterExecution);
            rmqpt.setPopulationType(sdowWeb);

            resultList.add(rmqpt);
        }

        return resultList;
    }

    public List<IndicatorExecution> getIndicatorExecutionsByResultManagerAndPeriodId(Long userId, Long periodId) {
        String jpql = "SELECT  " +
                "ie " +
                "FROM  " +
                "IndicatorExecution ie   " +
                "LEFT JOIN ie.indicator i " +
                "LEFT JOIN i.resultManager rm " +
                "WHERE ie.period.id = :periodId AND rm.id= :userId AND ie.state= :state  ";

        Query query = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        query.setParameter("userId", userId);
        query.setParameter("periodId", periodId);
        query.setParameter("state", State.ACTIVO);
        return query.getResultList();

    }
    public List<IndicatorExecution> getDirectImplementationsIndicatorExecutionsByReportUserId(Long periodId, Long userId) {
        String jpql =
                IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                        " WHERE p.id = :periodId " +
                        " and o.indicatorType <> :generalType " +
                        " and o.project.id is null " +
                        " and (au.id = :userId or aub.id = :userId) " +
                        " and o.state = :state " +
                        " and q.state = :state " +
                        " and au.state = :state " +
                        " and aub.state = :state " +
                        " and m.state = :state ";

        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("periodId", periodId);
        q.setParameter("generalType", IndicatorType.GENERAL);
        q.setParameter("userId", userId);
        q.setParameter("state", State.ACTIVO);
        return q.getResultList();
    }














}
