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
               and i.category is not null THEN i.code || ' - ' || i.description || ' (Categoría: ' || i.category || ')'
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
WHERE ie.state='ACTIVO'