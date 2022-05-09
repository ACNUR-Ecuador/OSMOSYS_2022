DROP VIEW IF EXISTS osmosys.ie_detailed;
CREATE VIEW osmosys.ie_detailed AS
SELECT
    s.ie_id,
    s.period_id,
    s.project_id,
    s.reporting_office_id,
    s.organization_id,
    s.performance_indicator_id,
    s.implementation_type,
    s.area,
    s.statement,
    s.statement_project,
    s.indicator_type,
    s.indicator,
    s.category,
    s.frecuency,
    s.project,
    s.implementers,
    s.total_execution,
    s.target,
    s.execution_percentage,
    s.quarter_order,
    s.quarter,
    s.quarter_execution,
    s.quarter_target,
    s.quarter_percentage,
    s.month_order,
    s.month,
    s.month_execution,
    s.iv_id,
    s.ivc_id,
    s.dissagregation_type,
    s.lugar_canton,
    s.lugar_provincia,
    s.population_type,
    s.gender_type,
    s.age_type,
    s.country_of_origin,
    s.diversity_type,
    s.age_primary_education_type,
    s.age_tertiary_education_type,
    s.custom_dissagregacion,
    s.value
FROM
    (
        (
            SELECT
                ie.id as ie_id,
                ie.period_id,
                ie.project_id,
                ie.reporting_office_id,
                org.id organization_id,
                ie.performance_indicator_id,
                CASE
                    WHEN ie.project_id is null THEN  'Implementación Directa'
                    WHEN ie.project_id is not null THEN  'Implementación Socios'
                    END AS implementation_type,
                ar.code || ' - '||  ar.short_description as area,
                st.code || ' - '|| st.description as statement,
                stb.code || ' - '|| stb.description as statement_project,
                CASE
                    WHEN i.id is null THEN 'General'
                    WHEN i.id is not null THEN 'Producto'
                    END as indicator_type,
                CASE
                    WHEN i.id is null THEN gi.description
                    WHEN i.id is not null
                        and i.category is null THEN i.code || ' - '|| i.description
                    WHEN i.id is not null
                        and i.category is not null THEN i.code || ' - '|| i.description || ' (Categoría: '||i.category||')'
                    END as indicator,
       i.category,
			 i.frecuency,
       pr.code ||'-'||pr. name project,
		COALESCE (offf.acronym,org.acronym) implementers,
       ie.total_execution,
       ie.target,
       ie.execution_percentage,
       q.order_ quarter_order,
       q.quarter,
       q.total_execution quarter_execution,
       q.target quarter_target,
       q.execution_percentage quarter_percentage,
       mo.order_ month_order,
       mo.month,
       mo.total_execution month_execution,
			 iv.id iv_id,
			 null as ivc_id,
			 iv.dissagregation_type,
			 can.code||'-'||can.description as lugar_canton,
			 prov.code||'-'||prov.description as lugar_provincia,
			 iv.population_type,
			 iv.gender_type,
			 iv.age_type,
			 iv.country_of_origin,
			 iv.diversity_type,
			 iv.age_primary_education_type,
			 iv.age_tertiary_education_type,
			 null as custom_dissagregacion,
			 iv.value
            FROM
                osmosys.indicator_executions ie
                LEFT JOIN
                osmosys.indicators i
            on ie.performance_indicator_id=i.id
                LEFT JOIN
                osmosys.statements stb
                on ie.project_statement_id= stb.id
                LEFT JOIN
                osmosys.statements st
                on i.statement_id = st.id
                LEFT JOIN
                osmosys.areas ar
                on st.area_id=ar.id
                LEFT JOIN
                osmosys.offices offf
                on ie.reporting_office_id=offf.id
                LEFT JOIN
                osmosys.projects pr
                on ie.project_id=pr.id
                LEFT JOIN
                osmosys.organizations org
                on pr.organization_id=org.id
                LEFT JOIN
                osmosys.periods per
                on ie.period_id=per.id
                LEFT JOIN
                osmosys.general_indicators gi
                on per.id=gi.periodo_id
                LEFT JOIN
                osmosys.quarters q
                on ie.id=q.indicator_execution_id
                LEFT JOIN
                osmosys.months mo
                on q.id=mo.quarter_id
                LEFT JOIN
                osmosys.indicator_values iv
                on mo.id=iv.month_id
                LEFT JOIN
                osmosys.cantones can
                on iv.canton_id =can.id
                LEFT JOIN
                osmosys.provincias prov
                on can.provincia_id =prov.id
            WHERE ie.state='ACTIVO'
              AND q.state='ACTIVO'
              AND mo.state='ACTIVO'
              AND iv.state='ACTIVO'

        )

        UNION
        (
            SELECT
                ie.id as ie_id,
                ie.period_id,
                ie.project_id,
                ie.reporting_office_id,
                org.id organization_id,
                ie.performance_indicator_id,
                CASE
                    WHEN ie.project_id is null THEN  'Implementación Directa'
                    WHEN ie.project_id is not null THEN  'Implementación Socios'
                    END AS implementation_type,
                ar.code || ' - '||  ar.short_description as area,
                st.code || ' - '|| st.description as statement,
                stb.code || ' - '|| stb.description as statement_project,
                CASE
                    WHEN i.id is null THEN 'General'
                    WHEN i.id is not null THEN 'Producto'
                    END as indicator_type,
                CASE
                    WHEN i.id is null THEN gi.description
                    WHEN i.id is not null
                        and i.category is null THEN i.code || ' - '|| i.description
                    WHEN i.id is not null
                        and i.category is not null THEN i.code || ' - '|| i.description || ' (Categoría: '||i.category||')'
                    END as indicator,
       i.category,
       i.frecuency,
	pr.code ||'-'||pr. name project,
       COALESCE (offf.acronym, org.acronym) implementers,
       ie.total_execution,
       ie.target,
       ie.execution_percentage,
       q.order_ quarter_order,
       q.quarter,
       q.total_execution quarter_execution,
       q.target quarter_target,
       q.execution_percentage quarter_percentage,
       mo.order_ month_order,
       mo.month,
       mo.total_execution month_execution,
			 null as iv_id,
			 ivc.id as ivc_id,
			 cd.description as dissagregation_type,
			 null as lugar_canton,
			 null as lugar_provincia,
			 null as population_type,
			 null as gender_type,
			 null as age_type,
			 null as country_of_origin,
			 null as diversity_type,
			 null as age_primary_education_type,
			 null as age_tertiary_education_type,
			 cdo.name as custom_dissagregacion,
			 ivc.value
            FROM
                osmosys.indicator_executions ie
                LEFT JOIN
                osmosys.indicators i
            on ie.performance_indicator_id=i.id
                LEFT JOIN
                osmosys.statements stb
                on ie.project_statement_id= stb.id
                LEFT JOIN
                osmosys.statements st
                on i.statement_id = st.id
                LEFT JOIN
                osmosys.areas ar
                on st.area_id=ar.id
                LEFT JOIN
                osmosys.offices offf
                on ie.reporting_office_id=offf.id
                LEFT JOIN
                osmosys.projects pr
                on ie.project_id=pr.id
                LEFT JOIN
                osmosys.organizations org
                on pr.organization_id=org.id
                LEFT JOIN
                osmosys.periods per
                on ie.period_id=per.id
                LEFT JOIN
                osmosys.general_indicators gi
                on per.id=gi.periodo_id
                LEFT JOIN
                osmosys.quarters q
                on ie.id=q.indicator_execution_id
                LEFT JOIN
                osmosys.months mo
                on q.id=mo.quarter_id
                INNER JOIN osmosys.indicator_values_custom_dissagregation ivc on mo.id=ivc.month_id
                INNER JOIN osmosys.custom_dissagregation_options cdo on ivc.custom_dissagregation_option=cdo.id
                INNER JOIN osmosys.custom_dissagregations cd on cdo.custom_dissagregation_id=cd.id

            WHERE ie.state='ACTIVO'
              AND q.state='ACTIVO'
              AND mo.state='ACTIVO'
              AND ivc.state='ACTIVO'
        )
    ) as s
;