-- Seed Company Schema -------------------------------------------------------------

create schema sc;

-- POSTS ----------------------------------------------------------

create table sc.posts_directory ( -- does not need to be secure
  id uuid primary key default common.uuid_generate_v4(),
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
  id uuid primary key default common.uuid_generate_v4(),

  directory uuid references sc.posts_directory(id), -- not null
  type sc.post_type, --not null,
  shareability sc.post_shareability, --not null,
  body text, --not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

-- ACCOUNTING TABLES --------------------------------------------------------

create table sc.funding_accounts (
  id uuid primary key default common.uuid_generate_v4(),

	account_number int unique, -- not null,
	name varchar(255),
	
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

-- LOCATION TABLES ----------------------------------------------------------

create table sc.field_zones (
	id uuid primary key default common.uuid_generate_v4(),

	director uuid references admin.people(id),
	name varchar(64) unique, -- not null
	
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create table sc.field_regions (
	id uuid primary key default common.uuid_generate_v4(),
	field_zone uuid references sc.field_zones(id),

	director uuid references admin.people(id),
	name varchar(32) unique, -- not null

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

-- extension table from commmon
create table sc.locations (
	id uuid unique not null references common.locations(id),

	default_region uuid references sc.field_regions(id),
	funding_account uuid references sc.funding_accounts(id),
	iso_alpha_3 char(3),
	name varchar(32) unique, -- not null
	type common.location_type, -- not null

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

-- ORGANIZATION TABLES

-- extension table from commmon
create table sc.organizations (
	id uuid primary key references common.organizations(id),
  
	address varchar(255),
	sensitivity common.sensitivity,
	root_directory uuid references common.directories(id),
	
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create table sc.organization_locations(
  id uuid primary key default common.uuid_generate_v4(),

	organization uuid not null references sc.organizations(id),
	location uuid not null references sc.locations(id),
	
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

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
	id uuid primary key references common.organizations(id),

	active bool,
	financial_reporting_types sc.financial_reporting_types[],
	is_innovations_client bool,
	pmc_entity_code varchar(32),
	point_of_contact uuid references admin.people(id),
	types sc.partner_types[],
	address varchar(255),
	sensitivity common.sensitivity,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

-- LANGUAGE TABLES ----------------------------------------------------------

create table sc.ethnologue (
  id uuid primary key default common.uuid_generate_v4(),
  language_index uuid not null references sil.language_index(id),
  code varchar(32),
  language_name varchar(75), -- override for language_index
  population int,
  provisional_code varchar(32),
  sensitivity common.sensitivity not null default 'High',

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
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
	id uuid primary key default common.uuid_generate_v4(),

  ethnologue uuid references sc.ethnologue(id), -- not null
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
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create table sc.language_goal_definitions (
	id uuid primary key default common.uuid_generate_v4(),

	-- todo

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create table sc.language_locations (
  id uuid primary key default common.uuid_generate_v4(),

	language uuid not null references sc.languages(id),
	location uuid not null references sc.locations(id),
	-- todo
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

	unique (language, location)
);

create table sc.language_goals (
  id uuid primary key default common.uuid_generate_v4(),

  language uuid not null references sc.languages(id),
	goal uuid not null references sc.language_goal_definitions(id),
	-- todo

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

	unique (language, goal)
);


-- USER TABLES --------------------------------------------------------------

create table sc.known_languages_by_person (
  id uuid primary key default common.uuid_generate_v4(),

  person uuid unique not null references admin.people(id),
  known_language uuid not null references sc.languages(id),
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

	unique (person, known_language)
);

-- extension table from commmon
create table sc.people (
  id uuid primary key references admin.people(id),

	skills varchar(32)[],
	status varchar(32),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create table sc.person_unavailabilities (
  id uuid primary key default common.uuid_generate_v4(),

  person uuid references admin.people(id),
	period_start timestamp not null default CURRENT_TIMESTAMP,
	period_end timestamp not null default CURRENT_TIMESTAMP,
	description text,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
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
  id uuid primary key default common.uuid_generate_v4(),

  status sc.change_to_plan_status,
  summary text,
  type sc.change_to_plan_type,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create table sc.periodic_reports_directory ( -- security not needed
  id uuid primary key default common.uuid_generate_v4(),
  created_at timestamp not null default CURRENT_TIMESTAMP
);

create table sc.periodic_reports (
  id uuid primary key default common.uuid_generate_v4(),

  directory uuid references sc.periodic_reports_directory(id),
  end_at timestamp,
  report_file uuid references common.files(id),
  start_at timestamp,
  type sc.periodic_report_type,
  skipped_reason text,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

-- extension table to common
create table sc.projects (
  id uuid primary key default common.uuid_generate_v4(),

	name varchar(32), -- not null
	change_to_plan uuid references sc.change_to_plans(id), -- not null
	active bool,
	department varchar(255),
	estimated_submission timestamp,
	field_region uuid references sc.field_regions(id),
	initial_mou_end timestamp,
	marketing_location uuid references sc.locations(id),
	mou_start timestamp,
	mou_end timestamp,
	owning_organization uuid references sc.organizations(id),
	periodic_reports_directory uuid references sc.periodic_reports_directory(id),
	posts_directory uuid references sc.posts_directory(id),
	primary_location uuid references sc.locations(id),
	root_directory uuid references common.directories(id),
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
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

	unique (id, change_to_plan)
);

create table sc.project_members (
  id uuid primary key default common.uuid_generate_v4(),

	project uuid references sc.projects(id), --not null
	person uuid references sc.people(id), --not null
	group_id uuid unique references admin.groups(id), --not null
	role uuid references admin.roles(id), --not null
	sensitivity common.sensitivity,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

  unique (project, person, group_id, role)
);

create table sc.pinned_projects (
  id uuid primary key default common.uuid_generate_v4(),
	person uuid unique references sc.people(id), -- not null
	project uuid references sc.projects(id), -- not null

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create type sc.partnership_agreement_status as enum (
		'NotAttached',
		'AwaitingSignature',
		'Signed'
);

create table sc.partnerships (
  id uuid primary key default common.uuid_generate_v4(),

  project uuid references sc.projects(id), -- not null
  partner uuid references sc.organizations(id), -- not null
  change_to_plan uuid references sc.change_to_plans(id), -- not null
  active bool,
  agreement_status sc.partnership_agreement_status,
  mou uuid references common.files(id),
  agreement uuid references common.files(id),
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
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)

);

-- PROJECT BUDGETS

create type common.budget_status as enum (
		'Pending',
		'Current',
		'Superseded',
		'Rejected'
);

create table sc.budgets (
  id uuid primary key default common.uuid_generate_v4(),

  change_to_plan uuid, -- not null
  project uuid references sc.projects(id), -- not null
  status common.budget_status,
  universal_template uuid references common.files(id),
  universal_template_file_url varchar(255),
  sensitivity common.sensitivity,
  total decimal,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

  unique (id, change_to_plan)
);

create table sc.budget_records (
  id uuid primary key default common.uuid_generate_v4(),

  budget uuid references sc.budgets(id), -- not null
  change_to_plan uuid references sc.change_to_plans(id), -- not null
  active bool,
  amount decimal,
  fiscal_year int,
  organization uuid references sc.organizations(id),
  sensitivity common.sensitivity,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),
  peer uuid references admin.peers(id)

);

create table sc.budget_records_partnerships (
  id uuid primary key default common.uuid_generate_v4(),
  budget_record uuid not null references sc.budget_records(id),
  partnership uuid not null references sc.partnerships(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)

);

-- PROJECT LOCATION

create table sc.project_locations (
  id uuid primary key default common.uuid_generate_v4(),

  active bool,
  change_to_plan uuid references sc.change_to_plans(id), -- not null
  location uuid references sc.locations(id), -- not null
  project uuid references sc.projects(id), -- not null
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

	unique (project, location, change_to_plan)
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
  id uuid primary key default common.uuid_generate_v4(),

	project uuid references sc.projects(id), -- not null
	language uuid references sc.languages(id), -- not null
--	ethnologue uuid references sc.ethnologue(id), -- not null
	change_to_plan uuid references sc.change_to_plans(id), -- not null
  active bool,
  is_open_to_investor_visit bool,
  communications_complete_date timestamp,
  complete_date timestamp,
  disbursement_complete_date timestamp,
  end_date timestamp,
  end_date_override timestamp,
  initial_end_date timestamp,
  is_first_scripture bool,
  is_luke_partnership bool,
  is_sent_printing bool,
  last_suspended_at timestamp,
  last_reactivated_at timestamp,
  paratext_registry varchar(32),
  periodic_reports_directory uuid references sc.periodic_reports_directory(id),
  pnp varchar(255),
  pnp_file uuid references common.files(id),
  product_engagement_tag common.project_engagement_tag,
  start_date timestamp,
  start_date_override timestamp,
  status common.engagement_status,
  status_modified_at timestamp,
  historic_goal varchar(255),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

	unique (project, language, change_to_plan)
);

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
  'AdobeAudition',
  'Audacity',
  'Craft2Tell',
  'Film',
  'OneStory',
  'OtherOralStories',
  'OtherOralTranslation',
  'OtherWritten',
  'OtherVisual',
  'Paratext',
  'Render',
  'SeedCompanyMethod',
  'SignLanguage',
  'StoryTogether'
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


create type sc.product_type as enum (
  'BibleStories',
  'EthnoArts',
  'Film',
  'FullBible',
  'Genesis',
  'Gospel',
  'IndividualBooks',
  'JesusFilm',
  'LiteracyMaterials',
  'NewTestamentFull',
  'OldTestamentPortions',
  'OldTestamentFull',
  'Songs',
  'Story'
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
    'Completed'
);


create table sc.products (
  id uuid primary key default common.uuid_generate_v4(),

  name varchar(64), -- not null
  change_to_plan uuid references sc.change_to_plans(id), -- not null
  active bool,
  mediums common.product_mediums[],
  methodology common.product_methodologies,
  approach common.product_approach,
  purposes common.product_purposes[],
  steps common.product_methodology_step[],
  progress_step_measurement common.progress_measurement,
  progress_target decimal,
  engagement uuid references sc.language_engagements(id),
  sensitivity common.sensitivity,
  describe_completion text,
  description text,
  type sc.product_type,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

  unique (id, change_to_plan)
);

create table sc.product_scripture_references (
  id uuid primary key default common.uuid_generate_v4(),
  product uuid references sc.products(id), -- not null
  scripture_reference uuid references common.scripture_references(id), -- not null
  change_to_plan uuid references sc.change_to_plans(id), -- not null
  active bool,
  
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

  unique (id)
);

create table sc.product_progress (
  id uuid primary key default common.uuid_generate_v4(),

  product uuid references sc.products(id),
  report uuid references sc.periodic_reports,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create table sc.step_progress (
  id uuid primary key default common.uuid_generate_v4(),

  step common.product_methodology_step,
  completed decimal,
  product_progress uuid references sc.product_progress(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
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
  id uuid primary key default common.uuid_generate_v4(),

	project uuid references sc.projects(id), -- not null
	change_to_plan uuid references sc.change_to_plans(id), -- not null
  active bool,
  communications_complete_date timestamp,
  complete_date timestamp,
  country_of_origin uuid references common.locations(id),
  disbursement_complete_date timestamp,
  end_date timestamp,
  end_date_override timestamp,
  growth_plan uuid references common.files(id), --references files, not file-versions in neo4j
  initial_end_date timestamp,
  intern uuid references admin.people(id),
  last_reactivated_at timestamp,
  mentor uuid references admin.people(id),
  methodologies common.product_methodologies[],
  paratext_registry varchar(32),
  periodic_reports_directory uuid references sc.periodic_reports_directory(id),
  position common.internship_position,
  sensitivity common.sensitivity,
  start_date timestamp,
  start_date_override timestamp,
  status common.engagement_status,
  status_modified_at timestamp,
  last_suspended_at timestamp,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

-- CEREMONIES

create type common.ceremony_type as enum (
  'Dedication',
  'Certification'
);

create table sc.ceremonies (
  id uuid primary key default common.uuid_generate_v4(),

  internship_engagement uuid references sc.internship_engagements(id),
  language_engagement uuid references sc.language_engagements(id),
  ethnologue uuid references sil.table_of_languages(id),
  actual_date timestamp,
  estimated_date timestamp,
  is_planned bool,
  type common.ceremony_type,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

-- PARTNER CRM STUFF ---------------------------------------------------------------------------------------------------

create type sc.partner_maturity_scale as enum (
  'Level 1', -- Non-Existent or Reactive
  'Level 2', -- Repeatable and Active
  'Level 3', -- Proactive and Defined
  'Level 4'  -- Fully Capable and Managed
);

create table sc.global_partner_assessments (
  id uuid primary key default common.uuid_generate_v4(),

  partner uuid not null references sc.organizations(id),

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
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create type sc.partner_performance_options as enum(
  '1', '2', '3', '4'
);

create table sc.global_partner_performance (
  id uuid primary key default common.uuid_generate_v4(),

  organization uuid unique not null references sc.organizations(id),

  reporting_performance sc.partner_performance_options,
  financial_performance sc.partner_performance_options,
  translation_performance sc.partner_performance_options,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id)
);

create type sc.global_partner_transition_options as enum(
  'Organization Assessment',
  'Development'
);

 create table sc.global_partner_transitions (
   id uuid primary key default common.uuid_generate_v4(),

   organization uuid unique not null references sc.organizations(id),
   transition_type sc.global_partner_transition_options not null,
   effective_date timestamp,

   created_at timestamp not null default CURRENT_TIMESTAMP,
   created_by uuid not null references admin.people(id),
   modified_at timestamp not null default CURRENT_TIMESTAMP,
   modified_by uuid not null references admin.people(id),
   owning_person uuid not null references admin.people(id),
   owning_group uuid not null references admin.groups(id)
 );

create type sc.global_partner_roles as enum (
  'A',
  'B'
);

create table sc.global_partner_engagements (
  id uuid primary key default common.uuid_generate_v4(),

  organization uuid not null references common.organizations(id),
  type common.involvement_options not null,
  mou_start timestamp,
  mou_end timestamp,
  sc_roles sc.global_partner_roles[],
  partner_roles sc.global_partner_roles[],

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

  unique (organization, type)
);

create table sc.global_partner_engagement_people (
  id uuid primary key default common.uuid_generate_v4(),

  engagement uuid not null references sc.global_partner_engagements(id),
  person uuid not null references admin.people(id),
  role common.people_to_org_relationship_type not null,

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by uuid not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by uuid not null references admin.people(id),
  owning_person uuid not null references admin.people(id),
  owning_group uuid not null references admin.groups(id),

  unique (engagement, person, role)
);
