SELECT ie.id                                as ie_id,
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
     on ie.state = 'ACTIVO' AND q.state = 'ACTIVO' AND ie.id = q.indicator_execution_id
         INNER JOIN
     osmosys.months mo
     on mo.state = 'ACTIVO' AND q.id = mo.quarter_id
         INNER JOIN
     osmosys.indicator_values iv
     on iv.state = 'ACTIVO' AND mo.id = iv.month_id
         left JOIN
     osmosys.cantones can
     on iv.canton_id = can.id
         left JOIN
     osmosys.provincias prov
     on can.provincia_id = prov.id