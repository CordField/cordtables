-- Seed Company Schema -------------------------------------------------------------

create schema sc;

-- POSTS ----------------------------------------------------------

create table sc.posts_directory ( -- does not need to be secure
  id serial primary key,
  created_at timestamp not null default CURRENT_TIMESTAMP
);

create type sc.post_shareability as enum (
  'Project Team',
  'Internal',
  'Ask to Share Externally',
  'External'
);


create type sc.post_type as enum (
  'Note',
  'Story',
  'Prayer'
);

create table sc.posts (
  id serial primary key,

  directory int not null references sc.posts_directory(id),
  type sc.post_type not null,
  shareability sc.post_shareability not null,
  body text not null,

	chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- ACCOUNTING TABLES --------------------------------------------------------

create table sc.funding_account (
  id serial primary key,
  neo4j_id varchar(32),

	account_number int unique not null,
	name varchar(32),

	chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- LOCATION TABLES ----------------------------------------------------------

create table sc.field_zone (
	id serial primary key,
	neo4j_id varchar(32),

	director int references admin.people(id),
	name varchar(32) unique not null,

	chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.field_regions (
	id serial primary key,
	neo4j_id varchar(32),

	director int references admin.people(id),
	name varchar(32) unique not null,

	chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- extension table from commmon
create table sc.locations (
	id int primary key not null references common.locations(id),
	neo4j_id varchar(32),

	default_region int references sc.field_regions(id),
	funding_account int references sc.funding_account(account_number),
	iso_alpha_3 char(3),
	name varchar(32) unique not null,
	type location_type not null,

	chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- ORGANIZATION TABLES

-- extension table from commmon
create table sc.organizations (
	id int primary key not null references common.organizations(id),
	neo4j_id varchar(32),

	address varchar(255),
	base64 varchar(32) unique not null,

	chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.organization_locations(
  id serial primary key,

	organization int not null references sc.organizations(id),
	location int not null references sc.locations(id),

	chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (organization, location)
);


create type sc.periodic_report_type as enum (
  'Financial',
  'Narrative'
);

DO $$ BEGIN
    create type sc.financial_reporting_types as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type sc.partner_types as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table sc.partners (
	id serial primary key,

	organization int not null references sc.organizations(id),
	active bool,
	financial_reporting_types sc.financial_reporting_types[],
	is_global_innovations_client bool,
	pmc_entity_code varchar(32),
	point_of_contact int references admin.people(id),
	types sc.partner_types[],

	chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- LANGUAGE TABLES ----------------------------------------------------------

create table sc.language_goal_definitions (
	id serial primary key,

	-- todo

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- NOT an extension table, but has a reference to common
-- sc languages may different from other org's language listings
create table sc.languages (
	id int primary key references sil.table_of_languages(id),

	display_name varchar(255) unique not null,
	is_dialect bool,
	is_sign_language bool,
	is_least_of_these bool,
	least_of_these_reason varchar(255),
	name varchar(255) unique not null,
	population_override int,
	registry_of_dialects_code varchar(32),
	sensitivity sensitivity,
	sign_language_code varchar(32),
	sponsor_estimated_eng_date timestamp,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.language_locations (
  id serial primary key,

	language int not null references sc.languages(id),
	location int not null references sc.locations(id),
	-- todo

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (language, location)
);

create table sc.language_goals (
  id serial primary key,

  language int not null references sc.languages(id),
	goal int not null references sc.language_goal_definitions(id),
	-- todo

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (language, goal)
);

create type sc.egids_scale as enum (
		'0',
		'1',
		'2',
		'3',
		'4',
		'5',
		'6a',
		'6b',
		'7',
		'8a',
		'8b',
		'9',
		'10'
);

create type sc.least_reached_progress_scale as enum (
    '0',
		'1',
		'2',
		'3',
		'4',
		'5'
);

create type sc.partner_interest_scale as enum (
		'No Partner Interest',
		'Some',
		'Significant',
		'Considerable'
);

create type sc.multiple_languages_leverage_linguistic_scale as enum (
		'None',
		'Some',
		'Significant',
		'Considerable',
		'Large',
		'Vast'
);

create type sc.multiple_languages_leverage_joint_training_scale as enum (
		'None',
		'Some',
		'Significant',
		'Considerable',
		'Large',
		'Vast'
);

create type sc.lang_comm_int_in_language_development_scale as enum (
		'No Interest',
		'Some',
		'Expressed Need',
		'Significant',
		'Considerable'
);

create type sc.lang_comm_int_in_scripture_translation_scale as enum (
		'No Interest',
		'Some',
		'Expressed Need',
		'Significant',
		'Considerable'
);

create type sc.access_to_scripture_in_lwc_scale as enum (
		'Full Access',
		'Vast Majority',
		'Large Majority',
		'Majority',
		'Significant',
		'Some',
		'Few'
);

create type sc.begin_work_geo_challenges_scale as enum (
		'None',
		'Very Difficult',
		'Difficult',
		'Moderate',
		'Easy'
);

create type sc.begin_work_rel_pol_obstacles_scale as enum (
		'None',
		'Very Difficult',
		'Difficult',
		'Moderate',
		'Easy'
);

create table sc.languages_ex(
	id serial primary key,

	language_name varchar(32),
	iso varchar(4),
	prioritization decimal generated always as (
	  population_value * 2 +
	  egids_value * 3 +
	  least_reached_value * 2 +
	  partner_interest_value * 2 +
	  multiple_languages_leverage_linguistic_value * 1 +
	  multiple_languages_leverage_joint_training_value * 1 +
	  lang_comm_int_in_language_development_value * 1 +
	  lang_comm_int_in_scripture_translation_value * 1 +
	  access_to_scripture_in_lwc_value * 1 +
	  begin_work_geo_challenges_value * 0.5 +
	  begin_work_rel_pol_obstacles_value * 0.5
	) stored,
	progress_bible bool,

  location_long text,
	island varchar(32),
	province varchar(32),

	first_language_population int NULL,
	population_value decimal default 0, -- calculated from first_language_population

	egids_level sc.egids_scale,
	egids_value decimal default 0, -- calculated from _level

	least_reached_progress_jps_level sc.least_reached_progress_scale,
	least_reached_value decimal default 0, -- calculated from _level

  partner_interest_level sc.partner_interest_scale,
	partner_interest_value decimal default 0, -- calculated from _level
	partner_interest_description text,
	partner_interest_source text,

  multiple_languages_leverage_linguistic_level sc.multiple_languages_leverage_linguistic_scale,
	multiple_languages_leverage_linguistic_value decimal default 0, -- calculated from _level
	multiple_languages_leverage_linguistic_description text,
	multiple_languages_leverage_linguistic_source text,

  multiple_languages_leverage_joint_training_level sc.multiple_languages_leverage_joint_training_scale,
	multiple_languages_leverage_joint_training_value decimal default 0, -- calculated from _level
  multiple_languages_leverage_joint_training_description text,
  multiple_languages_leverage_joint_training_source text,

  lang_comm_int_in_language_development_level sc.lang_comm_int_in_language_development_scale,
	lang_comm_int_in_language_development_value decimal default 0, -- calculated from _level
	lang_comm_int_in_language_development_description text,
	lang_comm_int_in_language_development_source text,

  lang_comm_int_in_scripture_translation_level sc.lang_comm_int_in_scripture_translation_scale,
	lang_comm_int_in_scripture_translation_value decimal default 0, -- calculated from _level
	lang_comm_int_in_scripture_translation_description text,
	lang_comm_int_in_scripture_translation_source text,

  access_to_scripture_in_lwc_level sc.access_to_scripture_in_lwc_scale,
	access_to_scripture_in_lwc_value decimal default 0, -- calculated from _level
	access_to_scripture_in_lwc_description text,
	access_to_scripture_in_lwc_source text,

  begin_work_geo_challenges_level sc.begin_work_geo_challenges_scale,
	begin_work_geo_challenges_value decimal default 0, -- calculated from _level
	begin_work_geo_challenges_description text,
	begin_work_geo_challenges_source text,

  begin_work_rel_pol_obstacles_level sc.begin_work_rel_pol_obstacles_scale,
	begin_work_rel_pol_obstacles_value decimal default 0, -- calculated from _level
  begin_work_rel_pol_obstacles_description text,
  begin_work_rel_pol_obstacles_source text,

	suggested_strategies text,
	comments text,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null,
  owning_person int not null,
  owning_group int not null,

  foreign key (created_by) references admin.people(id),
  foreign key (modified_by) references admin.people(id),
	foreign key (owning_person) references admin.people(id),
  foreign key (owning_group) references admin.groups(id)
);

-- USER TABLES --------------------------------------------------------------

create table sc.known_languages_by_person (
  id serial primary key,

  person int not null references admin.people(id),
  known_language int not null references sc.languages(id),

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (person, known_language)
);

-- extension table from commmon
create table sc.people (
  id int primary key references admin.people(id),
  neo4j_id varchar(32) unique,

	skills varchar(32)[],
	status varchar(32),

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.person_unavailabilities (
  id serial primary key,

  person int references admin.people(id),
	period_start timestamp not null,
	period_end timestamp not null,
	description text,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- FILES & DIRECTORIES ----------------------------------------------------------

create table sc.directories (
  id serial primary key,

	parent int references sc.directories(id),
  name varchar(255),
	-- todo

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.files (
  id serial primary key,

  directory int not null references sc.directories(id),
	name varchar(255),

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.file_versions (
  id serial primary key,

  category varchar(255),
  mime_type mime_type not null,
  name varchar(255) not null,
  file int not null references sc.files(id),
  file_url varchar(255) not null,
  file_size int, -- bytes

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- PROJECT TABLES ----------------------------------------------------------

-- todo
create type sc.project_step as enum (
		'A',
		'B',
		'C'
);

-- todo
create type sc.project_status as enum (
		'A',
		'B',
		'C'
);

-- todo
create type sc.change_to_plan_type as enum (
		'a',
		'b',
		'c'
);

-- todo
create type sc.change_to_plan_status as enum (
		'a',
		'b',
		'c'
);

create table sc.change_to_plans (
  id serial primary key,

  status sc.change_to_plan_status,
  summary text,
  type sc.change_to_plan_type,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.periodic_reports_directory ( -- security not needed
  id serial primary key,
  created_at timestamp not null default CURRENT_TIMESTAMP
);

create table sc.periodic_reports (
  id serial primary key,

  directory int not null references sc.periodic_reports_directory(id),
  end_at timestamp not null,
  reportFile int not null references sc.files(id),
  start_at timestamp not null,
  type sc.periodic_report_type not null,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- extension table to common
create table sc.projects (
  id int primary key references common.projects(id),
  neo4j_id varchar(32),

	base64 varchar(32) not null,
	change_to_plan int not null default 1 references sc.change_to_plans(id),
	active bool,
	department varchar(255),
	estimated_submission timestamp,
	field_region int references sc.field_regions(id),
	initial_mou_end timestamp,
	marketing_location int references sc.locations(id),
	mou_start timestamp,
	mou_end timestamp,
	owning_organization int references sc.organizations(id),
	periodic_reports_directory int references sc.periodic_reports_directory(id),
	posts_directory int references sc.posts_directory(id),
	primary_location int references sc.locations(id),
	root_directory int references sc.directories(id),
	status sc.project_status,
	status_changed_at timestamp,
	step sc.project_step,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (base64, change_to_plan)
);

create table sc.pinned_projects (
	person int not null references sc.people(id),
	project int not null references sc.projects(id)
);

create table sc.partnerships (
  id serial primary key,

  base64 varchar(32) unique not null,
  project int not null references sc.projects(id),
  partner int not null references sc.organizations(id),
  change_to_plan int not null default 1 references sc.change_to_plans(id),
  active bool,
  agreement int references sc.file_versions(id),

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (project, partner, change_to_plan)
);

-- PROJECT BUDGETS

-- todo
create type common.budget_status as enum (
		'A',
		'B',
		'C'
);

create table sc.budgets (
  id serial primary key,

  base64 varchar(32) not null,
  change_to_plan int not null default 1,
  project int not null references common.projects(id),
  status common.budget_status,
  universal_template int references sc.file_versions(id),
  universal_template_file_url varchar(255),

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (base64, change_to_plan)
);

create table sc.budget_records (
	id serial primary key,

  base64 varchar(32) not null,
  budget int not null references sc.budgets(id),
  change_to_plan int not null default 1 references sc.change_to_plans(id),
  active bool,
  amount decimal,
  fiscal_year int,
  partnership int references sc.partnerships(id),

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (budget, change_to_plan)
);

-- PROJECT LOCATION

create table sc.project_locations (
  id serial primary key,

  active bool,
  change_to_plan int not null default 1 references sc.change_to_plans(id),
  location int not null references sc.locations(id),
  project int not null references sc.projects(id),

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (project, location, change_to_plan)
);

-- LANGUAGE ENGAGEMENTS

-- todo
create type common.engagement_status as enum (
		'A',
		'B',
		'C'
);

-- todo
create type common.project_engagement_tag as enum (
		'A',
		'B',
		'C'
);

create table sc.language_engagements (
  id serial primary key,

  base64 varchar(32) not null,
	project int not null references sc.projects(id),
	ethnologue int not null references sil.table_of_languages(id),
	change_to_plan int not null default 1 references sc.change_to_plans(id),
  active bool,
	communications_complete_date timestamp,
	complete_date timestamp,
	disbursement_complete_date timestamp,
	end_date timestamp,
	end_date_override timestamp,
	initial_end_date timestamp,
	is_first_scripture bool,
	is_luke_partnership bool,
	is_sent_printing bool,
	last_reactivated_at timestamp,
	paratext_registry varchar(32),
	periodic_reports_directory int references sc.periodic_reports_directory(id),
	pnp varchar(255),
	pnp_file int references sc.file_versions(id),
	product_engagement_tag common.project_engagement_tag,
	start_date timestamp,
	start_date_override timestamp,
	status common.engagement_status,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (project, ethnologue, change_to_plan)
);

-- PRODUCTS

-- todo
create type common.product_mediums as enum (
  'A',
  'B',
  'C'
);

-- todo
create type common.product_methodologies as enum (
  'A',
  'B',
  'C'
);

-- todo
create type common.product_purposes as enum (
  'A',
  'B',
  'C'
);

-- todo
create type common.product_type as enum (
  'Film',
  'Literacy Material',
  'Scripture',
  'Song',
  'Story'
);

create table sc.products (
  id serial primary key,

  base64 varchar(32) not null,
  name varchar(64),
  change_to_plan int not null default 1 references sc.change_to_plans(id),
  active bool,
  mediums common.product_mediums[],
  methodologies common.product_methodologies[],
  purposes common.product_purposes[],
  type common.product_type,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (base64, change_to_plan)
);

create table sc.product_scripture_references (
  product int not null references sc.products(id),
  scripture_reference int not null references common.scripture_references(id),
  change_to_plan int not null default 1 references sc.change_to_plans(id),
  active bool,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  primary key (product, scripture_reference, change_to_plan)
);

-- INTERNSHIP ENGAGEMENTS

-- todo
create type common.internship_methodology as enum (
  'A',
  'B',
  'C'
);

-- todo
create type common.internship_position as enum (
  'A',
  'B',
  'C'
);

create table sc.internship_engagements (
  id serial primary key,

	project int not null references sc.projects(id),
	ethnologue int not null references sil.table_of_languages(id),
	change_to_plan int not null default 1 references sc.change_to_plans(id),
  active bool,
	communications_complete_date timestamp,
	complete_date timestamp,
	country_of_origin int references common.locations(id),
	disbursement_complete_date timestamp,
	end_date timestamp,
	end_date_override timestamp,
	growth_plan int references sc.file_versions(id),
	initial_end_date timestamp,
	intern int references admin.people(id),
	last_reactivated_at timestamp,
	mentor int references admin.people(id),
	methodology common.internship_methodology,
	paratext_registry varchar(32),
	periodic_reports_directory int references sc.periodic_reports_directory(id),
	position common.internship_position,
	start_date timestamp,
	start_date_override timestamp,
	status common.engagement_status,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (project, ethnologue, change_to_plan)
);

create table sc.ceremonies (
  id serial primary key,

  project int not null references sc.projects(id),
	ethnologue int not null references sil.table_of_languages(id),
	actual_date timestamp,
	estimated_date timestamp,
	is_planned bool,
	type varchar(255),

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- CRM TABLES, WIP ------------------------------------------------------------------

create type common.involvements as enum (
  'CIT',
  'Engagements'
);

create type admin.people_transitions as enum (
  'New Org',
  'Other'
);

create type common.org_transitions as enum (
  'To Manager',
  'To Other'
);

-- PARTNER CRM STUFF
--
--create table sc_org_to_org_rels (
--    from_sys_org varchar(32) not null,
--    to_sys_org varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (from_sys_org, to_sys_org),
--    foreign key (from_sys_org) references sc_organizations(sys_org),
--    foreign key (to_sys_org) references sc_organizations(sys_org)
--);
--
--create table sc_partner_performance (
--    sc_internal_org varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table sc_partner_finances (
--    sc_internal_org varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table sc_partner_reporting (
--    sc_internal_org varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table sc_partner_translation_progress (
--    sc_internal_org varchar(32) not null,
--    sc_internal_id varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org, sc_internal_id),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table sc_partner_notes (
--    sc_internal_org varchar(32) not null,
--    author_sys_person int not null,
--    note_text text not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org, author_sys_person, created_at),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org),
--    foreign key (author_sys_person) references sys_people(sys_person)
--);
--
--create table sc_org_transitions (
--    sc_internal_org varchar(32) not null,
--    transition_type sc_org_transitions not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table sc_roles (
--    sc_role serial primary key,
--    name varchar(32) unique not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP
--);
--
--create table sc_role_memberships (
--    sys_person int not null,
--    sc_role int not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sys_person, sc_role),
--    foreign key (sys_person) references sys_people(sys_person)
--);
--
--create table sc_person_to_person_rels (
--    from_sys_person int not null,
--    to_sys_person int not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (from_sys_person, to_sys_person),
--    foreign key (from_sys_person) references sys_people(sys_person),
--    foreign key (to_sys_person) references sys_people(sys_person)
--);
--
--create table sys_people_transitions (
--    sys_person int not null,
--    transition_type sc_people_transitions not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sys_person, transition_type),
--    foreign key (sys_person) references sys_people(sys_person)
--);
--
--create table sc_involvements (
--    sc_internal_org varchar(32) not null,
--    type sc_involvements not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--	primary key (sc_internal_org, type),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);