/*-----indicators-----*/

ALTER TABLE "osmosys"."indicators" ADD COLUMN "core_indicator" bool;

ALTER TABLE "osmosys"."indicators" ADD COLUMN "regional_code" varchar(255) COLLATE "pg_catalog"."default";

ALTER TABLE "osmosys"."indicators" ADD COLUMN "result_manager" int8;

ALTER TABLE "osmosys"."indicators" ADD COLUMN "agg_rule_comment" varchar(255) COLLATE "pg_catalog"."default";

ALTER TABLE "osmosys"."indicators" ADD COLUMN "quarter_report_calc" varchar(255) COLLATE "pg_catalog"."default";

ALTER TABLE "osmosys"."indicators" ADD CONSTRAINT "fk_indicator_result_manager" FOREIGN KEY ("result_manager") REFERENCES "security"."user" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "osmosys"."indicators" ADD CONSTRAINT "uk_aowtlp29x5c1kfdn8k337bwhy" UNIQUE ("regional_code");

/*-----focal_point_assignation-----*/

CREATE SEQUENCE "osmosys"."focal_point_assignation_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE "osmosys"."focal_point_assignation" (
  "id" int8 NOT NULL DEFAULT nextval('"osmosys".focal_point_assignation_id_seq'::regclass),
  "main_focal_pointer" bool,
  "state" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "focal_pointer_id" int8 NOT NULL,
  "project_id" int8 NOT NULL,
  CONSTRAINT "focal_point_assignation_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "osmosys"."focal_point_assignation" ADD CONSTRAINT "fk_focalponter_project" FOREIGN KEY ("focal_pointer_id") REFERENCES "security"."user" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "osmosys"."focal_point_assignation" ADD CONSTRAINT "fk_por_indicator" FOREIGN KEY ("project_id") REFERENCES "osmosys"."projects" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER SEQUENCE "osmosys"."focal_point_assignation_id_seq"
OWNED BY "osmosys"."focal_point_assignation"."id";

/*-----projects-----*/

ALTER TABLE "osmosys"."projects" ADD COLUMN "focal_point_assignation_id" int8;

ALTER TABLE "osmosys"."projects" ADD COLUMN "partner_manager" int8;

ALTER TABLE "osmosys"."projects" ADD CONSTRAINT "fk_indicator_partner_manager" FOREIGN KEY ("partner_manager") REFERENCES "security"."user" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "osmosys"."projects" ADD CONSTRAINT "fk_project_focal_point_assignation" FOREIGN KEY ("focal_point_assignation_id") REFERENCES "osmosys"."focal_point_assignation" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

/*-----statements-----*/

ALTER TABLE "osmosys"."statements" ALTER COLUMN "situation_id" DROP NOT NULL;

/*-----audits-----*/

CREATE SEQUENCE "osmosys"."audits_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE "osmosys"."audits" (
  "id" int8 NOT NULL DEFAULT nextval('"osmosys".audits_id_seq'::regclass),
  "action" varchar(22) COLLATE "pg_catalog"."default" NOT NULL,
  "change_date" timestamp(6),
  "entity" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "indicator_code" varchar(255) COLLATE "pg_catalog"."default",
  "new_data" text COLLATE "pg_catalog"."default" NOT NULL,
  "old_data" text COLLATE "pg_catalog"."default",
  "project_code" varchar(255) COLLATE "pg_catalog"."default",
  "state" varchar(12) COLLATE "pg_catalog"."default" NOT NULL,
  "responsible_user_id" int8,
  CONSTRAINT "audits_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "osmosys"."audits" ADD CONSTRAINT "fk_audit_user_responsible" FOREIGN KEY ("responsible_user_id") REFERENCES "security"."user" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER SEQUENCE "osmosys"."audits_id_seq"
OWNED BY "osmosys"."audits"."id";

/*-----core_indicators-----*/

CREATE SEQUENCE "osmosys"."core_indicators_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE "osmosys"."core_indicators" (
  "id" int8 NOT NULL DEFAULT nextval('"osmosys".core_indicators_id_seq'::regclass),
  "area_code" varchar(255) COLLATE "pg_catalog"."default",
  "code" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "frecuency" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "guiadance" varchar(255) COLLATE "pg_catalog"."default",
  "measure_type" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "state" varchar(12) COLLATE "pg_catalog"."default" NOT NULL,
  CONSTRAINT "core_indicators_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "uk_64o9d4nykap6j90pjtucyaln8" UNIQUE ("code")
)
;

ALTER SEQUENCE "osmosys"."core_indicators_id_seq"
OWNED BY "osmosys"."core_indicators"."id";

/*-----tags-----*/

CREATE SEQUENCE "osmosys"."tags_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;


CREATE TABLE "osmosys"."tags" (
  "id" int8 NOT NULL DEFAULT nextval('"osmosys".tags_id_seq'::regclass),
  "description" text COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "state" varchar(12) COLLATE "pg_catalog"."default" NOT NULL,
  "operation" text COLLATE "pg_catalog"."default",
  CONSTRAINT "tags_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "uk_iebbcvbkf45fj5fukm9qjxhk6" UNIQUE ("description"),
  CONSTRAINT "uk_t48xdq560gs3gap9g7jg36kgc" UNIQUE ("name")
)
;

ALTER SEQUENCE "osmosys"."tags_id_seq"
OWNED BY "osmosys"."tags"."id";

/*-----indicator_tag_asignation-----*/

CREATE SEQUENCE "osmosys"."indicator_tag_asignation_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE "osmosys"."indicator_tag_asignation" (
  "id" int8 NOT NULL DEFAULT nextval('"osmosys".indicator_tag_asignation_id_seq'::regclass),
  "state" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "indicator_id" int8 NOT NULL,
  "tag_id" int8 NOT NULL,
  CONSTRAINT "indicator_tag_asignation_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "indicator_tag_ids_unique" UNIQUE ("indicator_id", "tag_id")
)
;

ALTER TABLE "osmosys"."indicator_tag_asignation" ADD CONSTRAINT "indicator_tag_asignation_indicator" FOREIGN KEY ("indicator_id") REFERENCES "osmosys"."indicators" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "osmosys"."indicator_tag_asignation" ADD CONSTRAINT "indicator_tag_asignation_tag" FOREIGN KEY ("tag_id") REFERENCES "osmosys"."tags" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER SEQUENCE "osmosys"."indicator_tag_asignation_id_seq"
OWNED BY "osmosys"."indicator_tag_asignation"."id";

/*-----period_tag_asignations-----*/

CREATE SEQUENCE "osmosys"."period_tag_asignations_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE "osmosys"."period_tag_asignations" (
  "id" int8 NOT NULL DEFAULT nextval('"osmosys".period_tag_asignations_id_seq'::regclass),
  "state" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "period_id" int8 NOT NULL,
  "tag_id" int8 NOT NULL,
  CONSTRAINT "period_tag_asignations_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "period_tag_ids_unique" UNIQUE ("period_id", "tag_id")
)
;

ALTER TABLE "osmosys"."period_tag_asignations" ADD CONSTRAINT "period_tag_asignation_period" FOREIGN KEY ("period_id") REFERENCES "osmosys"."periods" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "osmosys"."period_tag_asignations" ADD CONSTRAINT "period_tag_asignation_tag" FOREIGN KEY ("tag_id") REFERENCES "osmosys"."tags" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER SEQUENCE "osmosys"."period_tag_asignations_id_seq"
OWNED BY "osmosys"."period_tag_asignations"."id";


/*-----result_manager_indicators-----*/

CREATE SEQUENCE "osmosys"."result_manager_indicators_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE "osmosys"."result_manager_indicators" (
  "id" int8 NOT NULL DEFAULT nextval('"osmosys".result_manager_indicators_id_seq'::regclass),
  "is_confirmed" bool NOT NULL,
  "quarter_year_order" int4 NOT NULL,
  "indicator_id" int8 NOT NULL,
  "period_id" int8 NOT NULL,
  "population_diss_option_id" int8,
  "report_value" int4,
  CONSTRAINT "result_manager_indicators_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "osmosys"."result_manager_indicators" ADD CONSTRAINT "fk_result_manager_ind_period" FOREIGN KEY ("period_id") REFERENCES "osmosys"."periods" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "osmosys"."result_manager_indicators" ADD CONSTRAINT "fk_result_manager_indicator" FOREIGN KEY ("indicator_id") REFERENCES "osmosys"."indicators" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "osmosys"."result_manager_indicators" ADD CONSTRAINT "fk_result_manager_population_diss_opt" FOREIGN KEY ("population_diss_option_id") REFERENCES "dissagregations"."standard_dissagregation_options" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER SEQUENCE "osmosys"."result_manager_indicators_id_seq"
OWNED BY "osmosys"."result_manager_indicators"."id";

/*-----result_manager_indicator_quarter_report-----*/

CREATE SEQUENCE "osmosys"."result_manager_indicator_quarter_report_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE "osmosys"."result_manager_indicator_quarter_report" (
  "id" int8 NOT NULL DEFAULT nextval('"osmosys".result_manager_indicator_quarter_report_id_seq'::regclass),
  "quarter_year_order" int4 NOT NULL,
  "report_comment" varchar(255) COLLATE "pg_catalog"."default",
  "indicator_id" int8 NOT NULL,
  "period_id" int8 NOT NULL,
  CONSTRAINT "result_manager_indicator_quarter_report_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "osmosys"."result_manager_indicator_quarter_report" ADD CONSTRAINT "fk_result_man_ind_quarter_report" FOREIGN KEY ("indicator_id") REFERENCES "osmosys"."indicators" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "osmosys"."result_manager_indicator_quarter_report" ADD CONSTRAINT "fk_result_man_ind_quarter_report_period" FOREIGN KEY ("period_id") REFERENCES "osmosys"."periods" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER SEQUENCE "osmosys"."result_manager_indicator_quarter_report_id_seq"
OWNED BY "osmosys"."result_manager_indicator_quarter_report"."id";

/*-----email-----*/

ALTER TABLE "security"."user" ALTER COLUMN "email" TYPE varchar(100) COLLATE "pg_catalog"."default";

ALTER TABLE "dissagregations"."standard_dissagregation_options" ADD COLUMN "region_group_name" varchar(255) COLLATE "pg_catalog"."default";

ALTER TABLE "dissagregations"."standard_dissagregation_options" ADD COLUMN "other_group_name" varchar(255) COLLATE "pg_catalog"."default";