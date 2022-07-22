package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import org.unhcr.osmosys.model.cubeDTOs.*;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(schema = "public", name = "cube_helper")
@SqlResultSetMapping(
        name = "FactDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = FactDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "ie_id", type = Long.class),
                                @ColumnResult(name = "period_id", type = Long.class),
                                @ColumnResult(name = "period_year", type = Integer.class),
                                @ColumnResult(name = "implementation_type", type = String.class),
                                @ColumnResult(name = "assigned_user_id", type = Long.class),
                                @ColumnResult(name = "assigned_user_backup_id", type = Long.class),
                                @ColumnResult(name = "indicator_id", type = Long.class),
                                @ColumnResult(name = "statement_id", type = Long.class),
                                @ColumnResult(name = "project_statement_id", type = Long.class),
                                @ColumnResult(name = "indicator_type", type = String.class),
                                @ColumnResult(name = "organization_id", type = Long.class),
                                @ColumnResult(name = "supervisor_id", type = Long.class),
                                @ColumnResult(name = "office_id", type = Long.class),
                                @ColumnResult(name = "project_id", type = Long.class),
                                @ColumnResult(name = "month_year_id", type = String.class),
                                @ColumnResult(name = "month_id", type = Long.class),
                                @ColumnResult(name = "dissagregation_type", type = String.class),
                                @ColumnResult(name = "age_type", type = String.class),
                                @ColumnResult(name = "age_primary_education_type", type = String.class),
                                @ColumnResult(name = "age_tertiary_education_type", type = String.class),
                                @ColumnResult(name = "gender_type", type = String.class),
                                @ColumnResult(name = "country_of_origin", type = String.class),
                                @ColumnResult(name = "population_type", type = String.class),
                                @ColumnResult(name = "diversity_type", type = String.class),
                                @ColumnResult(name = "canton_id", type = Long.class),
                                @ColumnResult(name = "responsable_office_id", type = Long.class),
                                @ColumnResult(name = "implementer_office_id", type = Long.class),
                                @ColumnResult(name = "implementer_id", type = String.class),
                                @ColumnResult(name = "total_execution", type = BigDecimal.class),
                                @ColumnResult(name = "total_target", type = BigDecimal.class),
                                @ColumnResult(name = "total_execution_percentage", type = BigDecimal.class),
                                @ColumnResult(name = "quarter_execution", type = BigDecimal.class),
                                @ColumnResult(name = "quarter_target", type = BigDecimal.class),
                                @ColumnResult(name = "quarter_execution_percentage", type = BigDecimal.class),
                                @ColumnResult(name = "month_execution", type = BigDecimal.class),
                                @ColumnResult(name = "value", type = BigDecimal.class)
                        }
                )
        }
)
@SqlResultSetMapping(
        name = "MonthQuarterYearDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = MonthQuarterYearDTO.class,
                        columns = {
                                @ColumnResult(name = "month_year_id", type = String.class),
                                @ColumnResult(name = "year", type = Integer.class),
                                @ColumnResult(name = "quarter", type = String.class),
                                @ColumnResult(name = "quarter_year_order", type = Integer.class),
                                @ColumnResult(name = "month", type = String.class),
                                @ColumnResult(name = "month_year_order", type = Integer.class),
                        })})
@SqlResultSetMapping(
        name = "DissagregationTypeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = DissagregationTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "dissagregation_type", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "DiversityTypeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = DiversityTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "diversity_type", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "AgeTypeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = AgeTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "age_type", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "AgePrimaryEducationTypeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = AgePrimaryEducationTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "age_primary_education_type", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "AgeTertiaryEducationTypeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = AgeTertiaryEducationTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "age_tertiary_education_type", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "GenderTypeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = GenderTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "gender_type", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "CountryOfOriginDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = CountryOfOriginTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "country_of_origin", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "PopulationTypeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = PopulationTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "population_type", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "CantonesProvinciasDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = CantonesProvinciasDTO.class,
                        columns = {
                                @ColumnResult(name = "canton_id", type = Long.class),
                                @ColumnResult(name = "canton_code", type = String.class),
                                @ColumnResult(name = "canton", type = String.class),
                                @ColumnResult(name = "provincia_code", type = String.class),
                                @ColumnResult(name = "provincia", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "CantonesProvinciasCentroidsDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = CantonesProvinciasCentroidsDTO.class,
                        columns = {
                                @ColumnResult(name = "canton_id", type = Long.class),
                                @ColumnResult(name = "canton_code", type = String.class),
                                @ColumnResult(name = "canton", type = String.class),
                                @ColumnResult(name = "canton_long", type = Float.class),
                                @ColumnResult(name = "canton_lat", type = Float.class),
                                @ColumnResult(name = "provincia_code", type = String.class),
                                @ColumnResult(name = "provincia", type = String.class),
                                @ColumnResult(name = "provincia_long", type = Float.class),
                                @ColumnResult(name = "provincia_lat", type = Float.class),
                        })})

@SqlResultSetMapping(
        name = "IndicatorTypeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = IndicatorTypeDTO.class,
                        columns = {
                                @ColumnResult(name = "indicator_type", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "UserDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = UserDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "organization", type = String.class),
                                @ColumnResult(name = "office", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "PeriodDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = PeriodDTO.class,
                        columns = {
                                @ColumnResult(name = "period_id", type = Long.class),
                                @ColumnResult(name = "year", type = Integer.class),
                        })})
@SqlResultSetMapping(
        name = "ProjectDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ProjectDTO.class,
                        columns = {
                                @ColumnResult(name = "project_id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "OrganizationDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = OrganizationDTO.class,
                        columns = {
                                @ColumnResult(name = "organization_id", type = Long.class),
                                @ColumnResult(name = "acronym", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "OfficeDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = OfficeDTO.class,
                        columns = {
                                @ColumnResult(name = "office_id", type = Long.class),
                                @ColumnResult(name = "acronym", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "parent_office_id", type = Long.class),
                                @ColumnResult(name = "parent_acronym", type = String.class),
                                @ColumnResult(name = "parent_description", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "ReportStateDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ReportStateDTO.class,
                        columns = {
                                @ColumnResult(name = "ie_id", type = Long.class),
                                @ColumnResult(name = "months_late", type = String.class),
                                @ColumnResult(name = "late", type = Boolean.class),
                        })})
@SqlResultSetMapping(
        name = "StatementDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = StatementDTO.class,
                        columns = {
                                @ColumnResult(name = "area_impact_id", type = Long.class),
                                @ColumnResult(name = "area_impact_code", type = String.class),
                                @ColumnResult(name = "area_impact", type = String.class),
                                @ColumnResult(name = "statement_impact_id", type = Long.class),
                                @ColumnResult(name = "statement_impact_code", type = String.class),
                                @ColumnResult(name = "statement_impact", type = String.class),
                                @ColumnResult(name = "area_outcome_id", type = Long.class),
                                @ColumnResult(name = "area_outcome_code", type = String.class),
                                @ColumnResult(name = "area_outcome", type = String.class),
                                @ColumnResult(name = "statement_outcome_id", type = Long.class),
                                @ColumnResult(name = "statement_outcome_code", type = String.class),
                                @ColumnResult(name = "statement_outcome", type = String.class),
                                @ColumnResult(name = "statement_output_id", type = Long.class),
                                @ColumnResult(name = "statement_output_code", type = String.class),
                                @ColumnResult(name = "statement_output_product_code", type = String.class),
                                @ColumnResult(name = "statement_output", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "MonthSourceDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = MonthSourceDTO.class,
                        columns = {
                                @ColumnResult(name = "month_id", type = Long.class),
                                @ColumnResult(name = "source_type", type = String.class),
                                @ColumnResult(name = "source_other", type = String.class),
                                @ColumnResult(name = "year", type = Integer.class),
                        })})
@SqlResultSetMapping(
        name = "MonthCualitativeDataDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = MonthCualitativeDataDTO.class,
                        columns = {
                                @ColumnResult(name = "month_id", type = Long.class),
                                @ColumnResult(name = "month_cualitative_data", type = String.class),
                                @ColumnResult(name = "year", type = Integer.class),
                        })})
@SqlResultSetMapping(
        name = "IndicatorDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = IndicatorDTO.class,
                        columns = {
                                @ColumnResult(name = "indicator_id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "category", type = String.class),
                                @ColumnResult(name = "frecuency", type = String.class),
                                @ColumnResult(name = "unit", type = String.class),
                        })})
@SqlResultSetMapping(
        name = "IndicatorExecutionsDissagregationSimpleDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = IndicatorExecutionDissagregationSimpleDTO.class,
                        columns = {
                                @ColumnResult(name = "ie_id", type = Long.class),
                                @ColumnResult(name = "year", type = Integer.class),
                                @ColumnResult(name = "dissagregation_simple", type = String.class)
                        })})
@SqlResultSetMapping(
        name = "ImplementerDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ImplementerDTO.class,
                        columns = {
                                @ColumnResult(name = "implementer_id", type = String.class),
                                @ColumnResult(name = "acronym", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "parent_acronym", type = String.class),
                                @ColumnResult(name = "parent_name", type = String.class),
                                @ColumnResult(name = "implementation_type", type = String.class)
                        })})
public class HelperCube extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
