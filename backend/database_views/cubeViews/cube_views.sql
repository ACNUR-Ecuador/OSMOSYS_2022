-- year
DROP VIEW IF EXISTS  cube.month_quarter_year;
CREATE OR REPLACE VIEW cube.month_quarter_year
    AS
SELECT
    distinct mo."year"||'-'||mo.month_year_order month_year_id, mo."year",q.quarter,q.quarter_year_order, mo."month", mo.month_year_order
FROM osmosys.months mo
         INNER JOIN osmosys.quarters q on mo.quarter_id=q.id
ORDER BY mo."year",q.quarter_year_order, mo.month_year_order
;

-- dissagregations
DROP VIEW IF EXISTS  cube.dissagregation_type;
CREATE OR REPLACE VIEW cube.dissagregation_type
    AS
SELECT
    distinct iv.dissagregation_type
FROM osmosys.indicator_values iv
ORDER BY 1 ;

DROP VIEW IF EXISTS  cube.diversity_type;
CREATE OR REPLACE VIEW cube.diversity_type
    AS
SELECT
    distinct iv.diversity_type
FROM osmosys.indicator_values iv
WHERE iv.diversity_type is not null
UNION
SELECT 'NO APLICA' diversity_type
ORDER BY 1 ;

DROP VIEW IF EXISTS  cube.age_type;
CREATE OR REPLACE VIEW cube.age_type
    AS
SELECT
    distinct iv.age_type
FROM osmosys.indicator_values iv
WHERE iv.age_type is not null
UNION
SELECT 'NO APLICA' age_type
ORDER BY 1 ;

DROP VIEW IF EXISTS  cube.gender_type;
CREATE OR REPLACE VIEW cube.gender_type
    AS
SELECT
    distinct iv.gender_type
FROM osmosys.indicator_values iv
WHERE iv.gender_type  is not null
UNION
SELECT 'NO APLICA' gender_type
ORDER BY 1 ;

DROP VIEW IF EXISTS  cube.country_of_origin;
CREATE OR REPLACE VIEW cube.country_of_origin
    AS
SELECT
    distinct iv.country_of_origin
FROM osmosys.indicator_values iv
WHERE iv.country_of_origin is not null
UNION
SELECT 'NO APLICA' country_of_origin
ORDER BY 1 ;

DROP VIEW IF EXISTS  cube.age_primary_education_type;
CREATE OR REPLACE VIEW cube.age_primary_education_type
    AS
SELECT
    distinct iv.age_primary_education_type
FROM osmosys.indicator_values iv
WHERE iv.age_primary_education_type is not null
UNION
SELECT 'NO APLICA' age_primary_education_type
ORDER BY 1 ;


DROP VIEW IF EXISTS  cube.age_tertiary_education_type;
CREATE OR REPLACE VIEW cube.age_tertiary_education_type
    AS
SELECT
    distinct iv.age_tertiary_education_type
FROM osmosys.indicator_values iv
WHERE iv.age_tertiary_education_type is not null
UNION
SELECT 'NO APLICA' age_tertiary_education_type
ORDER BY 1 ;

DROP VIEW IF EXISTS  cube.population_type;
CREATE OR REPLACE VIEW cube.population_type
    AS
SELECT
    distinct iv.population_type
FROM osmosys.indicator_values iv
WHERE iv.population_type is not null
UNION
SELECT 'NO APLICA' population_type
ORDER BY 1 ;

DROP VIEW IF EXISTS  cube.cantones_provincias;
CREATE OR REPLACE VIEW cube.cantones_provincias
    AS
SELECT
    can.id canton_id,
    can.code canton_code,
    can.description canton,
    prov.code provincia_code,
    prov.description provincia

FROM osmosys.cantones can
         INNER JOIN osmosys.provincias prov on can.provincia_id=prov."id"

UNION
SELECT
    0 canton_id,
    'NO APLICA' canton_code,
    'NO APLICA' canton,
    'NO APLICA' provincia_code,
    'NO APLICA' provincia
ORDER BY canton_code
;


-- indicator_type
DROP VIEW IF EXISTS  cube.indicator_type;
CREATE OR REPLACE VIEW cube.indicator_type
    AS
SELECT
    distinct ie.indicator_type
FROM osmosys.indicator_executions ie
ORDER BY 1
;

-- users
DROP VIEW IF EXISTS  cube.users;
CREATE OR REPLACE VIEW cube.users
    AS
SELECT
    distinct u.id, u.name
FROM "security"."user" u
UNION
SELECT 0 as id, 'No asignado' as name
ORDER BY 1 DESC
;

-- periods
DROP VIEW IF EXISTS  cube.periods;
CREATE OR REPLACE VIEW cube.periods
    AS
SELECT
    distinct u.id period_id, u."year"
FROM osmosys.periods u
ORDER BY 1
;

-- projects
DROP VIEW IF EXISTS  cube.projects;
CREATE OR REPLACE VIEW cube.projects
    AS
SELECT
    distinct u.id project_id, u."name"
FROM osmosys.projects u
UNION
SELECT
    0 AS project_id, 'NO APLICA' AS name
FROM osmosys.projects u

ORDER BY 1
;

-- organization
DROP VIEW IF EXISTS  cube.organizations;
CREATE OR REPLACE VIEW cube.organizations
    AS
SELECT
    distinct u."id" organization_id, u.acronym, u.description
FROM osmosys.organizations u
UNION
SELECT
    0 organization_id, 'NO APLICA' acronym, 'NO APLICA' description
FROM osmosys.organizations u
ORDER BY 1
;

-- office
DROP VIEW IF EXISTS  cube.offices;
CREATE OR REPLACE VIEW cube.offices
    AS
SELECT
    distinct u."id" office_id, u.acronym, u.description, u."id" parent_office_id, u.acronym parent_acronym , u.description parent_description
FROM osmosys.offices u
         LEFT JOIN osmosys.offices up on u.parent_office=up."id"
UNION
SELECT
    0 office_id, 'NO APLICA' acronym, 'NO APLICA' AS description, 0 parent_office_id, 'NO APLICA' parent_acronym , 'NO APLICA' parent_description
ORDER BY 1
;

DROP VIEW IF EXISTS  cube.report_state;
CREATE OR REPLACE VIEW cube.report_state
    AS
SELECT
    ie.id ie_id,
    osmosys.months_late(ie.id),
    CASE
        WHEN osmosys.months_late(ie.id) is null THEN
            false
        ELSE
            true
        END late
FROM osmosys.indicator_executions ie
WHERE ie."state"='ACTIVO';


DROP VIEW IF EXISTS  cube.statements;
CREATE OR REPLACE VIEW cube.statements
    AS
SELECT
    0 area_impact_id, 'NO ASIGNADO' area_impact_code,'NO ASIGNADO' area_impact,
    0 statement_impact_id, 'NO ASIGNADO' statement_impact_code,'NO ASIGNADO' statement_impact,
    0 area_outcome_id, 'NO ASIGNADO' area_outcome_code,'NO ASIGNADO' area_outcome,
    0 statement_outcome_id, 'NO ASIGNADO' statement_outcome_code,'NO ASIGNADO' statement_outcome,
    0 statement_output_id, 'NO ASIGNADO' statement_output_code,'NO ASIGNADO' statement_output_product_code,'NO ASIGNADO' statement_output
UNION
SELECT
    ai."id" area_impact_id, ai.code area_impact_code,ai.short_description area_impact,
    sti.id statement_impact_id, sti.code statement_impact_code,sti.description statement_impact,
    ar."id" area_outcome_id, ar.code area_outcome_code,ar.short_description area_outcome,
    str.id statement_outcome_id, str.code statement_outcome_code,str.description statement_outcome,
    stp.id statement_output_id, stp.code statement_output_code,stp.product_code statement_output_product_code,stp.description statement_output
FROM
    osmosys.statements sti
        INNER JOIN osmosys.areas ai on sti.area_id =ai.id
        INNER JOIN osmosys.statements str on sti.id =str.parent_statement_id and sti.area_type='IMPACTO'
        INNER JOIN osmosys.areas ar on str.area_id =ar.id
        INNER JOIN osmosys.statements stp on str.id =stp.parent_statement_id and str.area_type IN ('RESULTADO','APOYO')
order by area_impact_id, statement_impact_id, area_outcome_id, statement_outcome_id, statement_output_id;




DROP VIEW IF EXISTS  cube.month_source;
CREATE OR REPLACE VIEW cube.month_source
    AS
SELECT
    DISTINCT
    mo.id month_id,
    s.source_type,
    mo.source_other,
    per.year
FROM
    osmosys.months mo
        INNER JOIN osmosys.month_source s on mo.id=s.month_id and mo.state='ACTIVO'
        INNER JOIN osmosys.quarters q on mo.quarter_id=q.id and q.state='ACTIVO'
        INNER JOIN osmosys.indicator_executions ie on q.indicator_execution_id=ie.id and ie.state='ACTIVO'
        INNER JOIN osmosys.periods per on ie.period_id=per.id
order by 1, 2,3;


DROP VIEW IF EXISTS  cube.month_cualitative_data;
CREATE OR REPLACE VIEW cube.month_cualitative_data
    AS
SELECT
    DISTINCT
    mo.id month_id,
    mo.commentary month_cualitative_data,
    per.year
FROM
    osmosys.months mo
        INNER JOIN osmosys.month_source s on mo.id=s.month_id and mo.state='ACTIVO'
        INNER JOIN osmosys.quarters q on mo.quarter_id=q.id and q.state='ACTIVO'
        INNER JOIN osmosys.indicator_executions ie on q.indicator_execution_id=ie.id and ie.state='ACTIVO'
        INNER JOIN osmosys.periods per on ie.period_id=per.id
order by 1;

DROP VIEW IF EXISTS  cube.indicators;
CREATE OR REPLACE VIEW cube.indicators
    AS
SELECT
    i.id indicator_id,
    i.code,
    i.description,
    i.category,
    i.frecuency,
    i.unit
FROM
    osmosys.indicators AS i
ORDER BY 1;

DROP VIEW IF EXISTS  cube.fact_table;
CREATE OR REPLACE VIEW cube.fact_table
    AS
SELECT
    iv.id,
    ie."id" ie_id,
    ie.period_id,
    per."year" period_year,
    CASE
        WHEN ie.project_id is null THEN 'Implementación Socios'
        ELSE 'Implementación Directa'
        END  as implementation_type,
    COALESCE(ie.assigned_user_id,0) assigned_user_id,
    COALESCE(ie.assigned_user_backup_id,0) assigned_user_backup_id,
    COALESCE(ie.performance_indicator_id,0) as indicator_id,
    COALESCE(i.statement_id,0) statement_id,
    COALESCE(ie.project_statement_id,0) project_statement_id,
    ie.indicator_type,
    COALESCE(pr.organization_id,1) as organization_id,
    COALESCE(pr.focal_point_id,ie.supervisor_user_id) as supervisor_id,
    COALESCE(ie.reporting_office_id,0) office_id,
    COALESCE(ie.project_id,0) project_id,
    q."year" ||'-'|| mo.month_year_order month_year_id,
    iv.dissagregation_type,
    COALESCE(iv.age_type,'NO APLICA') age_type,
    COALESCE(iv.age_primary_education_type,'NO APLICA') age_primary_education_type,
    COALESCE(iv.age_tertiary_education_type,'NO APLICA') age_tertiary_education_type,
    COALESCE(iv.gender_type,'NO APLICA') gender_type,
    COALESCE(iv.country_of_origin,'NO APLICA') country_of_origin,
    COALESCE(iv.population_type,'NO APLICA') population_type,
    COALESCE(iv.diversity_type,'NO APLICA') diversity_type,
    COALESCE(iv.canton_id,0) canton_id,
    ie.total_execution total_execution,
    ie.target total_target,
    ie.execution_percentage total_execution_percentage,
    q.total_execution quarter_execution,
    q.target quarter_target,
    q.execution_percentage quarter_execution_percentage,
    mo.id as month_id,
    mo.total_execution month_execution,
    iv."value"
FROM
    osmosys.indicator_values iv
        INNER JOIN osmosys.months mo on mo."id"=iv.month_id and iv."state"='ACTIVO' and mo."state"='ACTIVO'
        INNER JOIN osmosys.quarters q on mo.quarter_id=q."id" and q."state"='ACTIVO'
        INNER JOIN osmosys.indicator_executions ie on q.indicator_execution_id=ie."id" and ie."state"='ACTIVO'
        INNER JOIN osmosys.periods per on ie.period_id=per.id
        LEFT JOIN osmosys.projects pr on ie.project_id=pr.id and pr."state"='ACTIVO'
        LEFT JOIN osmosys.indicators i on ie.performance_indicator_id=i.id

ORDER BY
    q."year",
    mo.month_year_order

;