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
  neo4j_id varchar(32) unique,

  directory int references sc.posts_directory(id), -- not null
  type sc.post_type, --not null,
  shareability sc.post_shareability, --not null,
  body text, --not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- ACCOUNTING TABLES --------------------------------------------------------

create table sc.funding_accounts (
  id serial primary key,
  neo4j_id varchar(32) unique,

	account_number int unique, -- not null,
	name varchar(255),
	
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- LOCATION TABLES ----------------------------------------------------------

create table sc.field_zones (
	id serial primary key,
	neo4j_id varchar(32) unique,

	director int references admin.people(id),
	name varchar(32) unique, -- not null
	
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
	field_zone int references sc.field_zones(id),

	director int references admin.people(id),
	name varchar(32) unique, -- not null

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
	neo4j_id varchar(32) unique,

	default_region int references sc.field_regions(id),
	funding_account int references sc.funding_accounts(id),
	iso_alpha_3 char(3),
	name varchar(32) unique, -- not null
	type common.location_type, -- not null

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
	neo4j_id varchar(32) unique,
  
	address varchar(255),
	sensitivity common.sensitivity,
	root_directory int references common.directories(id),
	
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
  'Narrative',
  'Progress'
);

DO $$ BEGIN
    create type sc.financial_reporting_types as enum (
		'Funded',
		'FieldEngaged'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;


DO $$ BEGIN
    create type sc.partner_types as enum (
		'Managing',
		'Funding',
		'Impact',
		'Technical',
		'Resource'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table sc.partners (
	id serial primary key,

  neo4j_id varchar(32) unique,
	organization int references sc.organizations(id), -- not null
	active bool,
	financial_reporting_types sc.financial_reporting_types[],
	is_innovations_client bool,
	pmc_entity_code varchar(32),
	point_of_contact int references admin.people(id),
	types sc.partner_types[],
	address varchar(255),
	sensitivity common.sensitivity,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- LANGUAGE TABLES ----------------------------------------------------------

create table sc.ethnologue (
  id serial primary key,

  neo4j_id varchar(32) unique,
  language_index int not null references sil.language_index(id),
  code varchar(32),
  language_name varchar(64), -- override for language_index
  population int,
  provisional_code varchar(32),
  sensitivity common.sensitivity not null default 'High',

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
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
		'NoPartnerInterest',
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
		'NoInterest',
		'Some',
		'Significant',
		'Considerable'
);

create type sc.lang_comm_int_in_scripture_translation_scale as enum (
		'NoInterest',
		'Some',
		'ExpressedNeed',
		'Significant',
		'Considerable'
);

create type sc.access_to_scripture_in_lwc_scale as enum (
		'FullAccess',
		'VastMajority',
		'LargeMajority',
		'Majority',
		'Significant',
		'Some',
		'Few'
);

create type sc.begin_work_geo_challenges_scale as enum (
		'None',
		'VeryDifficult',
		'Difficult',
		'Moderate',
		'Easy'
);

create type sc.begin_work_rel_pol_obstacles_scale as enum (
		'None',
		'VeryDifficult',
		'Difficult',
		'Moderate',
		'Easy'
);

create table sc.languages(
	id serial primary key,
  neo4j_id varchar(32) unique,

  ethnologue int references sc.ethnologue(id), -- not null
  name varchar(255) unique, -- not null
  display_name varchar(255) unique, -- not null
  display_name_pronunciation varchar(255),
  tags text[],
  preset_inventory bool,
  is_dialect bool,
  is_sign_language bool,
  is_least_of_these bool,
  least_of_these_reason varchar(255),
  population_override int,
  registry_of_dialects_code varchar(32),
  sensitivity common.sensitivity,
  sign_language_code varchar(32),
  sponsor_start_date timestamp,
  sponsor_estimated_end_date timestamp,
  has_external_first_scripture bool,

--	language_name varchar(32),
--	iso varchar(4),

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

	first_language_population int,
	population_value decimal default 0, -- calculated from first_language_population

	egids_level common.egids_scale,
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

  coordinates common.geography,
	suggested_strategies text,
	comments text,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.language_goal_definitions (
	id serial primary key,

	-- todo

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

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (language, goal)
);


-- USER TABLES --------------------------------------------------------------

create table sc.known_languages_by_person (
  id serial primary key,

  person int not null references admin.people(id),
  known_language int not null references sc.languages(id),
  
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

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.person_unavailabilities (
  id serial primary key,
  neo4j_id varchar(32) unique,

  person int references admin.people(id),
	period_start timestamp not null default CURRENT_TIMESTAMP,
	period_end timestamp not null default CURRENT_TIMESTAMP,
	description text,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);


-- PROJECT TABLES ----------------------------------------------------------

create type sc.project_step as enum (
		'EarlyConversations',
		'PendingConceptApproval',
		'PrepForConsultantEndorsement',
		'PendingConsultantEndorsement',
		'PrepForFinancialEndorsement',
		'PendingFinancialEndorsement',
		'FinalizingProposal',
		'PendingRegionalDirectorApproval',
		'PendingZoneDirectorApproval',
		'PendingFinanceConfirmation',
		'OnHoldFinanceConfirmation',
		'DidNotDevelop',
		'Rejected',
		'Active',
		'ActiveChangedPlan',
		'DiscussingChangeToPlan',
		'PendingChangeToPlanApproval',
		'PendingChangeToPlanConfirmation',
		'DiscussingSuspension',
		'PendingSuspensionApproval',
		'Suspended',
		'DiscussingReactivation',
		'PendingReactivationApproval',
		'DiscussingTermination',
		'PendingTerminationApproval',
		'FinalizingCompletion',
		'Terminated',
		'Completed'
);

create type sc.project_status as enum (
		'InDevelopment',
		'Active',
		'Terminated',
		'Completed',
		'DidNotDevelop'
);

create type sc.project_type as enum (
        'Translation',
        'Internship'
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
  neo4j_id varchar(32),

  status sc.change_to_plan_status,
  summary text,
  type sc.change_to_plan_type,
  
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

  neo4j_id varchar(32),
  directory int references sc.periodic_reports_directory(id),
  -- TODO: not sure how to put 'parent' from neo4j...
  due: timestamp,
  end_at timestamp,
  received_date timestamp,
  report_file int references common.files(id),
  sensitivity common.sensitivity,
  skipped_reason text,
  start_at timestamp,
  type sc.periodic_report_type,

  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- extension table to common
create table sc.projects (
  id serial primary key,

	name varchar(32), -- not null
	change_to_plan int default 1 references sc.change_to_plans(id), -- not null
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
	root_directory int references common.directories(id),
	status sc.project_status,
	status_changed_at timestamp,
	step sc.project_step,
	step_changed_at timestamp,
	sensitivity common.sensitivity,
	tags text[],
	preset_inventory bool,
	type sc.project_type,
	report_received_at timestamp,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (id, change_to_plan)
);

create table sc.project_members (
  id serial primary key,
  neo4j_id varchar(32),
	project int references sc.projects(id), --not null
	person int references sc.people(id), --not null
	group_id int  references admin.groups(id), --not null
	role int references admin.roles(id), --not null
	sensitivity common.sensitivity,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (project, person, group_id, role)
);

create table sc.pinned_projects (
  id serial primary key,
	person int references sc.people(id), -- not null
	project int references sc.projects(id), -- not null

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create type sc.partnership_agreement_status as enum (
		'NotAttached',
		'AwaitingSignature',
		'Signed'
);

create table sc.partnerships (
  id serial primary key,

  neo4j_id varchar(32) unique,
  project int references sc.projects(id), -- not null
  partner int references sc.organizations(id), -- not null
  change_to_plan int default 1 references sc.change_to_plans(id), -- not null
  active bool,
  agreement_status sc.partnership_agreement_status,
  mou int references common.files(id),
  agreement int references common.files(id),
  mou_status sc.partnership_agreement_status,
  mou_start timestamp,
  mou_end timestamp,
  mou_start_override timestamp,
  mou_end_override timestamp,
  financial_reporting_type sc.financial_reporting_types,
  is_primary bool,
  sensitivity common.sensitivity,

  types sc.partner_types[],  -- added because exists in neo4j
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)

);

create table sc.partnerships_producing_mediums (

  medium references common.product_mediums,
  partnership references common.partnerships(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
)

-- PROJECT BUDGETS

create type common.budget_status as enum (
		'Pending',
		'Current',
		'Superseded',
		'Rejected'
);

create table sc.budgets (
  id serial primary key,

  neo4j_id varchar(32) unique,
  change_to_plan int default 1, -- not null   //TODO: this column doesn't exist in neo4j
  project int references sc.projects(id), -- not null   // TODO: column doesn't exist in neo4j
  status common.budget_status,
  universal_template int references common.files(id),
  --universal_template_file_url varchar(255),       -- // TODO: Shouldn't this come from file_versions -> common.files?
  sensitivity common.sensitivity,
  total decimal,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (id, change_to_plan)
);

create table sc.budget_records (
  id serial primary key,

  neo4j_id varchar(32) unique,
  budget int references sc.budgets(id), -- not null
  change_to_plan int default 1 references sc.change_to_plans(id), -- not null //TODO: column doesn't exist in neo4j
  active bool, -- // TODO: shouldn't ACTIVE be in every table? it's a BaseNode prop...
  amount decimal,
  fiscal_year int,
  organization int references sc.organizations(id),
  sensitivity common.sensitivity,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)

);

-- TODO: this relationship doesn't technically live in neo4j. the relationship between a budget and a partnership lives in a project.
create table sc.budget_records_partnerships (
  id serial primary key,
  budget_record int not null references sc.budget_records(id),
  partnership int not null references sc.partnerships(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)

);

-- PROJECT LOCATION
-- TODO: does this map to project's 'otherLocations' in neo4j?
create table sc.project_locations (
  id serial primary key,

  active bool,
  change_to_plan int default 1 references sc.change_to_plans(id), -- not null
  location int references sc.locations(id), -- not null
  project int references sc.projects(id), -- not null
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (project, location, change_to_plan)
);

-- CEREMONIES

create type common.ceremony_type as enum (
  'Dedication',
  'Certification'
);

create table sc.ceremonies (
  id serial primary key,
  neo4j_id varchar(32),
 -- project int  references sc.projects(id),     --TODO: this should probably be taken out. ceremony is on Language engagement, which
  ethnologue int references sil.table_of_languages(id),  -- TODO: (cont) does this for us.
  actual_date timestamp,
  estimated_date timestamp,
  is_planned bool,
  type common.ceremony_type,
  sensitivity common.sensitivity,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- LANGUAGE ENGAGEMENTS

create type common.engagement_status as enum (
		'InDevelopment',
		'DidNotDevelop',
		'Active',
		'DiscussingTermination',
		'DiscussingReactivation',
		'DiscussingChangeToPlan',
		'DiscussingSuspension',
		'FinalizingCompletion',
		'ActiveChangedPlan',
		'Suspended',
		'Terminated',
		'Completed',
		'Converted',
		'Unapproved',
		'Transferred',
		'NotRenewed',
		'Rejected'
);

-- todo
create type common.project_engagement_tag as enum (
		'A',
		'B',
		'C'
);

create table sc.language_engagements (
  id serial primary key,

  --TODO: neo4j also has the following columns not covered in this table:
  --TODO                   changeset   -- not sure about this one
  --TODO                   dateRange (derived, so probably don't need it here)    // can do `select as date ranges SELECT tsrange(start_date, end_date)`
  --TODO                   dateRangeOverride (derived)                            // can do `select as date ranges SELECT tsrange(start_date_override, end_date_override)`



  neo4j_id varchar(32) unique,
	project int references sc.projects(id), -- not null
	ethnologue int references sc.ethnologue(id), -- not null  //TODO: referencing ethnologue or sc.languages or language_index or common.language? neo4j just references language
	change_to_plan int default 1 references sc.change_to_plans(id), -- not null   //TODO: column doesn't exist in neo4j
	current_progress_report_due references common.files(id),
  active bool,
  ceremony int references sc.ceremonies(id),
  is_open_to_investor_visit bool,
  communications_complete_date timestamp,   -- //TODO: column doesn't exist in neo4j
  complete_date timestamp,
  disbursement_complete_date timestamp,
  end_date timestamp,
  end_date_override timestamp,
  initial_end_date timestamp,
  is_first_scripture bool,
  is_luke_partnership bool,
  is_sent_printing bool,     -- //TODO: should probably be sent_printing_date. in neo4j has sentPrintingDate
  latest_progress_report_submitted references sc.periodic_reports(id),
  last_suspended_at timestamp,
  last_reactivated_at timestamp,
  nextProgressReportDue references sc.periodic_reports(id),
  paratext_registry varchar(32),
  partnerships_producing_mediums sc.partnerships_producing_mediums(id) []
  periodic_reports_directory int references sc.periodic_reports_directory(id),   -- TODO: neo4j has this as 'progressReports' and its a list of PeriodicReports
  pnp varchar(255),   -- TODO: does this map to pnpData in neo4j? if so, it's marked as deprecated.
  pnp_file int references common.files(id),   --TODO: this should probably be file_version instead.
  product_engagement_tag common.project_engagement_tag,   --TODO: is this synonymous to the products field in LanguageEngagement? If so, this is already covered in the products table. If not, this column doesn't exist in neo4j
  sent_printing_date timestamp,
  start_date timestamp,
  start_date_override timestamp,
  status common.engagement_status,
  status_modified_at timestamp,
  historic_goal varchar(255),
  sensitivity common.sensitivity,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

	unique (project, ethnologue, change_to_plan)
);


create table sc.language_engagements_partnerships_producing_mediums (
  id serial primary key,
  language_engagement references sc.language_engagements(id),
  partnerships_producing_mediums references sc.partnerships_producing_mediums(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
)


-- PRODUCTS

create type common.product_mediums as enum (
  'Print',
  'Web',
  'EBook',
  'App',
  'TrainedStoryTellers',
  'Audio',
  'Video',
  'Other'
);


create type common.product_methodologies as enum (
  'Paratext',
  'OtherWritten',
  'Render',
  'Audacity',
  'AdobeAudition',
  'OtherOralTranslation',
  'StoryTogether',
  'SeedCompanyMethod',
  'OneStory',
  'Craft2Tell',
  'OtherOralStories',
  'Film',
  'SignLanguage',
  'OtherVisual'
 );

create type common.product_approach as enum (
  'OralStories',
  'OralTranslation',
  'Visual',
  'Written'
);


create type common.product_purposes as enum (
  'EvangelismChurchPlanting',
  'ChurchLife',
  'ChurchMaturity',
  'SocialIssues',
  'Discipleship'
);


create type common.product_type as enum (
  'BibleStories',
  'JesusFilm',
  'Songs',
  'LiteracyMaterials',
  'EthnoArts',
  'OldTestamentPortions',
  'OldTestamentFull',
  'Gospel',
  'NewTestamentPortions',
  'NewTestamentFull',
  'FullBible',
  'IndividualBooks',
  'Genesis'
);
create type common.progress_measurement as enum (
  'Percent',
  'Number',
  'Boolean'
);

create type common.product_methodology_step as enum (
    'ExegesisAndFirstDraft',
    'TeamCheck',
    'CommunityTesting',
    'BackTranslation',
    'ConsultantCheck',
    'InternalizationAndDrafting',
    'PeerRevision',
    'ConsistencyCheckAndFinalEdits',
    'Craft',
    'Test',
    'Check',
    'Record',
    'Develop',
    'Translate',
    'Completed'
);


create type sc.producible_type as enum (
  'DirectScriptureProduct',
  'DerivativeScriptureProduct',
  'OtherProduct'
);

create table sc.producible (
  id serial primary key,
  neo4j_id varchar(32) unique,
  name varchar(64),
  type sc.producible_type,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table sc.producible_scripture_references (
  id serial primary key,
  producible int references sc.producible(id), -- not null
  scripture_reference int references common.scripture_references(id), -- not null
  change_to_plan int default 1 references sc.change_to_plans(id), -- not null   //TODO: column doesn't exist in neo4j
  active bool,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (id)
);

create table sc.products (
  id serial primary key,

  neo4j_id varchar(32) unique,
  active bool,

  approach common.product_approach,
  available_steps common.product_methodology_step[],
  category varchar(32),
  change_to_plan int default 1 references sc.change_to_plans(id), -- not null  //TODO: column not exist in neo4j
  describe_completion text,
  description text,            -- TODO: column not exist in neo4j
  engagement int references sc.language_engagements(id),
  label varchar(32),
  mediums common.product_mediums[],
  methodology common.product_methodologies,
  name varchar(64), -- not null     // TODO: column not exist in neo4j
  produces int references sc.producible,
  progress_step_measurement common.progress_measurement,
  progress_target decimal,
  purposes common.product_purposes[],
  sensitivity common.sensitivity,
  steps common.product_methodology_step[],
  type common.product_type,                 -- TODO: named 'legacyType' in neo4j

  -- TODO: need to discuss if this is a better route for representing scripture ranges....
  --   see types common.scripture_range and common.scripture_reference
  scripture_references common.scripture_range[],

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (id, change_to_plan)
);

-- extension tables of products representing whether it's a derivative, direct, and other
-- TODO: need a blessing on these
create table product_derivative_scripture {
  product references sc.products(id),    -- not null

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
}

create table product_direct_scripture {
  product int references sc.products(id),    -- not null
  unspecified_scripture common.unspecified_scripture,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
}

create table product_other{
  product int  references sc.products(id),    -- not null
  title varchar(255),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
}


-- TODO: would it be best to describe a scripture reference type? see types common.scripture_reference and common.scripture_range
create table sc.product_scripture_references (
  id serial primary key,
  product int references sc.products(id), -- not null
  scripture_reference int references common.scripture_references(id), -- not null
  change_to_plan int default 1 references sc.change_to_plans(id), -- not null
  active bool,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (id)
);

-- todo: not sure if this is what the sc.step_progress covers or not...
create table sc.product_progress_of_current_report_due (
  id serial primary key,
  neo4j_id varchar(32),

  product int references sc.products(id),
  report int references sc.periodic_reports,
  steps common.product_methodology_step[]
)

create table sc.product_progress (
  id serial primary key,
  neo4j_id varchar(32),
  product int references sc.products(id),
  report int references sc.periodic_reports,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);


-- todo: is this progress_of_current_reports_due?
create table sc.step_progress (
  id serial primary key,
  neo4j_id varchar(32),
  step common.product_methodology_step,
  completed decimal,
  product_progress int references sc.product_progress(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- INTERNSHIP ENGAGEMENTS

-- todo
create type common.internship_methodology as enum (
  'A',
  'B',
  'C'
);

create type common.internship_position as enum (
  'ConsultantInTraining',
  'ExegeticalFacilitator',
  'LeadershipDevelopment',
  'Mobilization',
  'Personnel',
  'Communication',
  'Administration',
  'Technology',
  'Finance',
  'LanguageProgramManager',
  'Literacy',
  'TranslationFacilitator',
  'OralityFacilitator',
  'ScriptureEngagement',
  'OtherAttached',
  'OtherTranslationCapacity',
  'OtherPartnershipCapacity'
);

create table sc.internship_engagements (
  id serial primary key,

  neo4j_id varchar(32) unique,

  -- TODO: Missing the following columns from neo4j: (see language engagements for comments on these)
    -- TODO                 changeset
    -- TODO                 date_range
    -- TODO                 date_range_override
    -- TODO
  active bool,
	ceremony int references sc.ceremonies(id),
	change_to_plan int default 1 references sc.change_to_plans(id), -- not null  //TODO: column doesn't exist in neo4j
  communications_complete_date timestamp,     -- TODO: doesn't exist in neo4j
  complete_date timestamp,
  country_of_origin int references common.locations(id),
  disbursement_complete_date timestamp,
  end_date timestamp,
  end_date_override timestamp,
  growth_plan int references common.files(id), --references files, not file-versions in neo4j
  initial_end_date timestamp,
  intern int references admin.people(id),
  last_reactivated_at timestamp,
  last_suspended_at timestamp,
  mentor int references admin.people(id),
  methodologies common.product_methodologies[],
  paratext_registry varchar(32),         -- TODO: col doesn't exist in neo4j
  periodic_reports_directory int references sc.periodic_reports_directory(id),   --TODO: column doesn't exist in neo4j
  position common.internship_position,
  project int references sc.projects(id), -- not null
  sensitivity common.sensitivity,
  start_date timestamp,
  start_date_override timestamp,
  status common.engagement_status,
  status_modified_at timestamp,


  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

-- PARTNER CRM STUFF ---------------------------------------------------------------------------------------------------

create type sc.partner_maturity_scale as enum (
  'Level 1', -- Non-Existent or Reactive
  'Level 2', -- Repeatable and Active
  'Level 3', -- Proactive and Defined
  'Level 4'  -- Fully Capable and Managed
);

create table sc.global_partner_assessments (
  id serial primary key,

  partner int not null references sc.organizations(id),

  governance_trans          sc.partner_maturity_scale,
  director_trans            sc.partner_maturity_scale,
  identity_trans            sc.partner_maturity_scale,
  growth_trans              sc.partner_maturity_scale,
  comm_support_trans        sc.partner_maturity_scale,
  systems_trans             sc.partner_maturity_scale,
  fin_management_trans      sc.partner_maturity_scale,
  hr_trans                  sc.partner_maturity_scale,
  it_trans                  sc.partner_maturity_scale,
  program_design_trans      sc.partner_maturity_scale,
  tech_translation_trans    sc.partner_maturity_scale,
  director_opp              sc.partner_maturity_scale,
  financial_management_opp  sc.partner_maturity_scale,
  program_design_opp        sc.partner_maturity_scale,
  tech_translation_opp      sc.partner_maturity_scale,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create type sc.partner_performance_options as enum(
  '1', '2', '3', '4'
);

create table sc.global_partner_performance (
  id serial primary key,

  organization int unique not null references sc.organizations(id),

  reporting_performance sc.partner_performance_options,
  financial_performance sc.partner_performance_options,
  translation_performance sc.partner_performance_options,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create type sc.global_partner_transition_options as enum(
  'Organization Assessment',
  'Development'
);

 create table sc.global_partner_transitions (
   id serial primary key,

   organization int unique not null references sc.organizations(id),
   transition_type sc.global_partner_transition_options not null,
   effective_date timestamp,

   created_at timestamp not null default CURRENT_TIMESTAMP,
   created_by int not null references admin.people(id),
   modified_at timestamp not null default CURRENT_TIMESTAMP,
   modified_by int not null references admin.people(id),
   owning_person int not null references admin.people(id),
   owning_group int not null references admin.groups(id)
 );

create type sc.global_partner_roles as enum (
  'A',
  'B'
);

create table sc.global_partner_engagements (
  id serial primary key,

  organization int not null references common.organizations(id),
  type common.involvement_options not null,
  mou_start timestamp,
  mou_end timestamp,
  sc_roles sc.global_partner_roles[],
  partner_roles sc.global_partner_roles[],

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (organization, type)
);

create table sc.global_partner_engagement_people (
  id serial primary key,

  engagement int not null references sc.global_partner_engagements(id),
  person int not null references admin.people(id),
  role common.people_to_org_relationship_type not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),

  unique (engagement, person, role)
);
