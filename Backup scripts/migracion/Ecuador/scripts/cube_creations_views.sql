
/*---- tag_indicator_values ----*/

CREATE OR REPLACE VIEW cube.tag_indicator_values
 AS
 SELECT DISTINCT iv.id AS iv_id,
    ita.tag_id
   FROM osmosys.indicator_executions ie
     JOIN osmosys.quarters q ON ie.id = q.indicator_execution_id
     JOIN osmosys.months m ON m.quarter_id = q.id
     JOIN osmosys.indicator_values iv ON iv.month_id = m.id
     JOIN osmosys.indicators i ON i.id = ie.performance_indicator_id
     JOIN osmosys.indicator_tag_asignation ita ON ita.indicator_id = i.id
  ORDER BY iv.id;

/*----result_managers----*/

CREATE VIEW "cube"."result_managers" AS  SELECT rmi.id,
    rmi.indicator_id,
    i.result_manager AS user_id,
    rmi.population_diss_option_id AS population_type_id,
    p.year AS period_year,
    p.id AS period_id,
    rmi.quarter_year_order AS quarter,
    rmi.is_confirmed AS confirmed,
    rmi.report_value AS value
   FROM ((osmosys.result_manager_indicators rmi
     JOIN osmosys.periods p ON ((p.id = rmi.period_id)))
     JOIN osmosys.indicators i ON ((i.id = rmi.indicator_id)))
  ORDER BY rmi.indicator_id, p.year, rmi.quarter_year_order;

/*----tags----*/

CREATE VIEW "cube"."tags" AS  SELECT DISTINCT t.id AS tag_id,
    t.name AS tag_name,
    t.description AS tag_description,
    t.operation AS tag_operation,
    p.year AS period_year
   FROM ((osmosys.tags t
     LEFT JOIN osmosys.period_tag_asignations pta ON ((pta.tag_id = t.id)))
     LEFT JOIN osmosys.periods p ON ((p.id = pta.period_id)))
UNION
 SELECT 0 AS tag_id,
    'NO APLICA'::character varying AS tag_name,
    'NO APLICA'::character varying AS tag_description,
    'NO APLICA'::character varying AS tag_operation,
    0 AS period_year
   FROM osmosys.tags t
  ORDER BY 1;


/*----tags_indicators----*/

CREATE VIEW "cube"."tag_indicators" AS  SELECT DISTINCT t.id AS tag_id,
    t.name AS tag_name,
    t.description AS tag_description,
    t.operation AS tag_operation,
    p.year AS period_year,
    ita.indicator_id
   FROM (((osmosys.tags t
     LEFT JOIN osmosys.period_tag_asignations pta ON ((pta.tag_id = t.id)))
     LEFT JOIN osmosys.periods p ON ((p.id = pta.period_id)))
     LEFT JOIN osmosys.indicator_tag_asignation ita ON ((ita.tag_id = t.id)))
  WHERE (((p.state)::text = 'ACTIVO'::text) AND ((pta.state)::text = 'ACTIVO'::text) AND ((ita.state)::text = 'ACTIVO'::text))
UNION
 SELECT 0 AS tag_id,
    'NO APLICA'::character varying AS tag_name,
    'NO APLICA'::character varying AS tag_description,
    'NO APLICA'::character varying AS tag_operation,
    0 AS period_year,
    0 AS indicator_id
   FROM osmosys.tags t
  ORDER BY 1;


/*----project_managers----*/

CREATE VIEW "cube"."project_managers" AS  SELECT fp.id,
    fp.project_id,
    u.id AS user_id,
    u.name AS user_name,
    u.username AS user_username
   FROM (osmosys.focal_point_assignation fp
     LEFT JOIN security."user" u ON ((u.id = fp.focal_pointer_id)))
  WHERE ((fp.state)::text = 'ACTIVO'::text)
UNION
 SELECT 0 AS id,
    0 AS project_id,
    0 AS user_id,
    'NO APLICA'::character varying AS user_name,
    'NO APLICA'::character varying AS user_username
   FROM osmosys.focal_point_assignation fp
  ORDER BY 1;

