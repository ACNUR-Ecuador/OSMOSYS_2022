DROP VIEW IF EXISTS osmosys.ie_detailed;

CREATE VIEW osmosys.ie_detailed AS
SELECT
    ie_data.ie_id,
    ie_data.period_id,
    ie_data.project_id,
    ie_data.reporting_office_id,
    ie_data.organization_id,
    ie_data.performance_indicator_id,
    ie_data.implementation_type,
    ie_data.area,
    ie_data.statement,
    ie_data.statement_project,
    ie_data.indicator_type,
    ie_data.indicator,
    ie_data.category,
    ie_data.frecuency,
    ie_data.project,
    ie_data.implementers,
    ie_data.total_execution,
    ie_data.target,
    ie_data.execution_percentage,
    ie_values.quarter_order,
    ie_values.quarter,
    ie_values.quarter_execution,
    ie_values.quarter_target,
    ie_values.quarter_percentage,
    ie_values.month_order,
    ie_values.month,
    ie_values.month_execution,
    ie_values.iv_id,
    ie_values.ivc_id,
    ie_values.dissagregation_type,
    ie_values.lugar_canton,
    ie_values.lugar_provincia,
    ie_values.population_type,
    ie_values.gender_type,
    ie_values.age_type,
    ie_values.country_of_origin,
    ie_values.diversity_type,
    ie_values.age_primary_education_type,
    ie_values.age_tertiary_education_type,
    ie_values.custom_dissagregacion,
    ie_values.value
FROM
    (
        SELECT ie.id                                    as ie_id,
               ie.period_id,
               ie.project_id,
               ie.reporting_office_id,
               org.id                                      organization_id,
               ie.performance_indicator_id,
               CASE
                   WHEN ie.project_id is null THEN 'Implementación Directa'
                   WHEN ie.project_id is not null THEN 'Implementación Socios'
                   END                                  AS implementation_type,
               ar.code || ' - ' || ar.short_description as area,
               st.code || ' - ' || st.description       as statement,
               stb.code || ' - ' || stb.description     as statement_project,
               CASE
                   WHEN i.id is null THEN 'General'
                   WHEN i.id is not null THEN 'Producto'
                   END                                  as indicator_type,
               CASE
                   WHEN i.id is null THEN gi.description
                   WHEN i.id is not null
                       and i.category is null THEN i.code || ' - ' || i.description
                   WHEN i.id is not null
                       and i.category is not null
                       THEN i.code || ' - ' || i.description || ' (Categoría: ' || i.category || ')'
                   END                                  as indicator,
       i.category,
       i.frecuency,
       pr.code || '-' || pr.name                   project,
       COALESCE(offf.acronym, org.acronym)         implementers,
       ie.total_execution,
       ie.target,
       ie.execution_percentage
        FROM osmosys.indicator_executions ie
            LEFT JOIN
            osmosys.indicators i on ie.performance_indicator_id = i.id
            LEFT JOIN
            osmosys.statements stb on ie.project_statement_id = stb.id
            LEFT JOIN
            osmosys.statements st
            on i.statement_id = st.id
            LEFT JOIN
            osmosys.areas ar
            on st.area_id = ar.id
            LEFT JOIN
            osmosys.offices offf
            on ie.reporting_office_id = offf.id
            LEFT JOIN
            osmosys.projects pr
            on ie.project_id = pr.id
            LEFT JOIN
            osmosys.organizations org
            on pr.organization_id = org.id
            LEFT JOIN
            osmosys.periods per
            on ie.period_id = per.id
            LEFT JOIN
            osmosys.general_indicators gi
            on per.id = gi.periodo_id
        WHERE ie.state = 'ACTIVO'
    ) as ie_data
        INNER JOIN
    (SELECT
         ie.id                                as ie_id,
         q.order_                                quarter_order,
         q.quarter,
         q.total_execution                       quarter_execution,
         q.target                                quarter_target,
         q.execution_percentage                  quarter_percentage,
         mo.order_                               month_order,
         mo.month,
         mo.total_execution                      month_execution,
         iv.id                                   iv_id,
         null                                 as ivc_id,
         iv.dissagregation_type,
         can.code || '-' || can.description   as lugar_canton,
         prov.code || '-' || prov.description as lugar_provincia,
         iv.population_type,
         iv.gender_type,
         iv.age_type,
         iv.country_of_origin,
         iv.diversity_type,
         iv.age_primary_education_type,
         iv.age_tertiary_education_type,
         null                                 as custom_dissagregacion,
         iv.value
     FROM osmosys.indicator_executions ie

              INNER JOIN
          osmosys.quarters q
          on ie.state = 'ACTIVO' and q.state = 'ACTIVO' and ie.id = q.indicator_execution_id
              INNER JOIN
          osmosys.months mo
          on mo.state = 'ACTIVO' and q.id = mo.quarter_id
              INNER JOIN
          osmosys.indicator_values iv
          on iv.state = 'ACTIVO' and mo.id = iv.month_id
              LEFT JOIN
          osmosys.cantones can
          on iv.canton_id = can.id
              LEFT JOIN
          osmosys.provincias prov
          on can.provincia_id = prov.id
     UNION
     SELECT
         ie.id                                as ie_id,
         q.order_                                quarter_order,
         q.quarter,
         q.total_execution                       quarter_execution,
         q.target                                quarter_target,
         q.execution_percentage                  quarter_percentage,
         mo.order_                               month_order,
         mo.month,
         mo.total_execution                      month_execution,
         null                                   iv_id,
         ivc.id                                 as ivc_id,
         cd.description 											as dissagregation_type,
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
     FROM osmosys.indicator_executions ie
              INNER JOIN
          osmosys.quarters q
          on ie.state = 'ACTIVO' and q.state = 'ACTIVO' and ie.id = q.indicator_execution_id
              INNER JOIN
          osmosys.months mo
          on mo.state = 'ACTIVO' and q.id = mo.quarter_id
              INNER JOIN osmosys.indicator_values_custom_dissagregation ivc
                         on ivc.state = 'ACTIVO' and mo.id = ivc.month_id
              INNER JOIN osmosys.custom_dissagregation_options cdo
                         on ivc.custom_dissagregation_option = cdo.id
              INNER JOIN osmosys.custom_dissagregations cd
                         on cdo.custom_dissagregation_id = cd.id
    ) as ie_values
    ON ie_data.ie_id=ie_values.ie_id
;