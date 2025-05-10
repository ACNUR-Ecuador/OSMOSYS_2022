/*-----indicators-----*/

ALTER TABLE "osmosys"."indicators" DROP CONSTRAINT "fk_indicator_result_manager";

ALTER TABLE "osmosys"."indicators" DROP CONSTRAINT "uk_aowtlp29x5c1kfdn8k337bwhy";

ALTER TABLE "osmosys"."indicators" DROP COLUMN "core_indicator";

ALTER TABLE "osmosys"."indicators" DROP COLUMN "regional_code";

ALTER TABLE "osmosys"."indicators" DROP COLUMN "result_manager";

ALTER TABLE "osmosys"."indicators" DROP COLUMN "agg_rule_comment";

ALTER TABLE "osmosys"."indicators" DROP COLUMN "quarter_report_calc";


/*-----projects-----*/

ALTER TABLE "osmosys"."projects" DROP CONSTRAINT "fk_indicator_partner_manager";

ALTER TABLE "osmosys"."projects" DROP CONSTRAINT "fk_project_focal_point_assignation";

ALTER TABLE "osmosys"."projects" DROP COLUMN "focal_point_assignation_id";

ALTER TABLE "osmosys"."projects" DROP COLUMN "partner_manager";

/*-----statements-----*/

ALTER TABLE "osmosys"."statements" ALTER COLUMN "situation_id" SET NOT NULL;

/*-----audits-----*/

DROP TABLE "osmosys"."audits";

/*-----core_indicators-----*/

DROP TABLE "osmosys"."core_indicators";

/*-----focal_point_assignation-----*/

DROP TABLE "osmosys"."focal_point_assignation";


/*-----indicator_tag_asignation-----*/

DROP TABLE "osmosys"."indicator_tag_asignation";


/*-----period_tag_asignations-----*/
DROP TABLE "osmosys"."period_tag_asignations";

/*-----tags-----*/

DROP TABLE "osmosys"."tags";

/*-----result_manager_indicators-----*/

DROP TABLE "osmosys"."result_manager_indicators";

/*-----result_manager_indicator_quarter_report-----*/

DROP TABLE "osmosys"."result_manager_indicator_quarter_report";

/*-----email-----*/

ALTER TABLE "security"."user" ALTER COLUMN "email" TYPE varchar(50) COLLATE "pg_catalog"."default";
