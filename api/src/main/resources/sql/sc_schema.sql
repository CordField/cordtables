-- Seed Company Schema -------------------------------------------------------------

create schema if not exists sc;



-- POSTS ----------------------------------------------------------

create table if not exists sc.posts_directory ( -- does not need to be secure
    id serial primary key,
    created_at timestamp not null default CURRENT_TIMESTAMP
);

create table if not exists sc.posts (
    id serial primary key,
    directory int not null,
    type public.post_type not null,
    shareability public.post_shareability not null,
    body text not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    created_by int not null default 0,
    foreign key (created_by) references public.people(id)
    -- foreign key (directory) references public.posts_directory(id)
);

-- ACCOUNTING TABLES --------------------------------------------------------

create table if not exists sc.funding_account (
  	id serial primary key,
  	neo4j_id varchar(32),
	account_number int unique not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	name varchar(32),
	foreign key (created_by) references public.people(id),
	foreign key (modified_by) references public.people(id)
);

-- LOCATION TABLES ----------------------------------------------------------

create table if not exists sc.field_zone (
	id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	director int,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	name varchar(32) unique not null,
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (director) references public.people(id)
);

create table if not exists sc.field_regions (
	id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	director int,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	name varchar(32) unique not null,
	foreign key (created_by) references public.people(id),
	foreign key (modified_by) references public.people(id),
	foreign key (director) references public.people(id)
);

create table if not exists sc.locations (
	id int primary key not null,
	neo4j_id varchar(32),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	default_region int,
	funding_account int,
	iso_alpha_3 char(3),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	name varchar(32) unique not null,
	type location_type not null,
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (id) references public.locations(id),
	foreign key (default_region) references sc.field_regions(id),
	foreign key (funding_account) references sc.funding_account(account_number)
);

-- ORGANIZATION TABLES

create table if not exists sc.organizations (
	id int primary key not null,
	address varchar(255),
	base64 varchar(32) unique not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	internal varchar(32) unique not null,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (id) references public.organizations(id)
);

create table if not exists sc.organization_locations(
  id serial primary key,
	organization varchar(32) not null,
	location int not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
	modified_by int not null default 0,
	unique (organization, location),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (organization) references sc.organizations(base64),
	foreign key (location) references public.locations(id)
);

DO $$ BEGIN
    create type public.financial_reporting_types as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.partner_types as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists sc.partners (
	id serial primary key,
	organization varchar(32) not null,
	active bool,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	financial_reporting_types public.financial_reporting_types[],
	is_global_innovations_client bool,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	pmc_entity_code varchar(32),
	point_of_contact int,
	types public.partner_types[],
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (point_of_contact) references public.people(id),
	foreign key (organization) references sc.organizations(base64)
);

-- LANGUAGE TABLES ----------------------------------------------------------

create table if not exists sc.language_goal_definitions (
	id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	foreign key (created_by) references public.people(id),
	foreign key (modified_by) references public.people(id)
	-- todo
);

create table if not exists sc.languages (
	id int primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	is_dialect bool,
	is_sign_language bool,
	is_least_of_these bool,
	display_name varchar(255) unique not null,
	least_of_these_reason varchar(255),
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	name varchar(255) unique not null,
	population_override int,
	registry_of_dialects_code varchar(32),
	sensitivity sensitivity,
	sign_language_code varchar(32),
	sponsor_estimated_eng_date timestamp,
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (id) references sil.table_of_languages(id)
);

create table if not exists sc.language_locations (
  id serial primary key,
	ethnologue int not null,
	location int not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	unique (ethnologue, location),
	foreign key (location) references public.locations(id),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (ethnologue) references sil.table_of_languages(id)
	-- todo
);

create table if not exists sc.language_goals (
  id serial primary key,
  ethnologue int not null,
	goal int not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	unique (ethnologue, goal),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (ethnologue) references sil.table_of_languages(id),
	foreign key (goal) references sc.language_goal_definitions(id)
	-- todo
);

create table if not exists sc.languages_ex(
	id serial primary key,
	lang_name varchar(32),
	lang_code varchar(16) NOT NULL,
	location text,
	first_lang_population int NULL,
	population int,
	egids_level int,
	egids_value int,
	least_reached_progress_jps_scale int,
	least_reached_value int,
	partner_interest int,
	partner_interest_description text,
	partner_interest_source text,
	multi_lang_leverage int,
	multi_lang_leverage_description text,
	multi_lang_leverage_source text,
	community_interest int,
	community_interest_description text,
	community_interest_source text,
	community_interest_value int,
	community_interest_scripture_description text,
	community_interest_scripture_source text,
	lwc_scripture_access int,
	lwc_scripture_description text,
	lwc_scripture_source text,
	access_to_begin int,
	access_to_begin_description text,
	access_to_begin_source text,
	suggested_strategies text,
	comments text,
	prioritization int,
	progress_bible int
);

-- USER TABLES --------------------------------------------------------------

create table if not exists sc.known_languages_by_person (
  id serial primary key,
  person int not null,
  known_language int not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	unique (person, known_language),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (person) references public.people(id),
	foreign key (known_language) references sil.table_of_languages(id)
);

create table if not exists sc.people (
  id int primary key,
  internal varchar(32) unique,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	skills varchar(32)[],
	status varchar(32),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (id) references public.people(id)
);

create table if not exists sc.person_unavailabilities (
  id int primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	description text,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	period_end timestamp not null,
	period_start timestamp not null,
	foreign key (created_by) references public.people(id),
	foreign key (modified_by) references public.people(id),
	foreign key (id) references public.people(id)
);

-- FILES & DIRECTORIES ----------------------------------------------------------

create table if not exists sc.directories (
  id serial primary key,
	parent int not null,
  name varchar(255),
	created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null default 0,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (parent) references sc.directories(id)
	-- todo
);

create table if not exists sc.files (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
  directory int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	name varchar(255),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (directory) references sc.directories(id)
);

create table if not exists sc.file_versions (
  id serial primary key,
  category varchar(255),
	created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null default 0,
  mime_type mime_type not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
  name varchar(255) not null,
  file int not null,
  file_url varchar(255) not null,
  file_size int, -- bytes
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (file) references sc.files(id)
);

-- PROJECT TABLES ----------------------------------------------------------

-- todo
DO $$ BEGIN
    create type public.project_step as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.project_status as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.change_to_plan_type as enum (
		'a',
		'b',
		'c'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.change_to_plan_status as enum (
		'a',
		'b',
		'c'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists sc.change_to_plans (
  id serial primary key,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
  status public.change_to_plan_status,
  summary text,
  type public.change_to_plan_type,
	foreign key (created_by) references public.people(id),
	foreign key (modified_by) references public.people(id)
);

create table if not exists sc.periodic_reports_directory ( -- security not needed
    id serial primary key,
    created_at timestamp not null default CURRENT_TIMESTAMP
);

create table if not exists sc.periodic_reports (
    id serial primary key,
    directory int not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    created_by int not null default 0,
    end_at timestamp not null,
    reportFile int not null,
    start_at timestamp not null,
    type public.periodic_report_type not null,
    foreign key (created_by) references public.people(id),
    foreign key (reportFile) references sc.files(id),
    foreign key (directory) references sc.periodic_reports_directory(id)
);

create table if not exists sc.projects (
  id serial primary key,
  project int not null,
	base64 varchar(32) not null,
	change_to_plan int not null default 0,
	active bool,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	department varchar(255),
	estimated_submission timestamp,
	field_region int,
	initial_mou_end timestamp,
	marketing_location int,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	mou_start timestamp,
	mou_end timestamp,
	owning_organization varchar(32),
	periodic_reports_directory int,
	posts_directory int,
	primary_location int,
	root_directory int,
	status public.project_status,
	status_changed_at timestamp,
	step public.project_step,
	unique (base64, change_to_plan),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (change_to_plan) references sc.change_to_plans(id),
	foreign key (field_region) references sc.field_regions(id),
	foreign key (marketing_location) references public.locations(id),
	foreign key (owning_organization) references sc.organizations(base64),
	foreign key (periodic_reports_directory) references sc.periodic_reports_directory(id),
	foreign key (posts_directory) references sc.posts_directory(id),
	foreign key (primary_location) references public.locations(id),
	foreign key (project) references public.projects(id),
	foreign key (root_directory) references sc.directories(id)
);

create table if not exists sc.pinned_projects (
	person int not null,
	project int not null,
	foreign key (person) references public.people(id),
	foreign key (project) references public.projects(id)
);

create table if not exists sc.partnerships (
  id serial primary key,
  base64 varchar(32) unique not null,
  project int not null,
  partner varchar(32) not null,
  change_to_plan int not null default 0,
  active bool,
  agreement int,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	unique (project, partner, change_to_plan),
	foreign key (agreement) references sc.file_versions(id),
	foreign key (change_to_plan) references sc.change_to_plans(id),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (partner) references sc.organizations(base64),
	foreign key (project) references public.projects(id)
);

-- PROJECT BUDGETS

-- todo
DO $$ BEGIN
    create type public.budget_status as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists sc.budgets (
  id serial primary key,
  base64 varchar(32) not null,
  change_to_plan int not null default 0,
  project int not null,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
  status public.budget_status,
  universal_template int,
  universal_template_file_url varchar(255),
  unique (base64, change_to_plan),
  foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
  foreign key (project) references public.projects(id),
	foreign key (universal_template) references sc.file_versions(id)
);

create table if not exists sc.budget_records (
	id serial primary key,
  base64 varchar(32) not null,
  budget int not null,
  change_to_plan int not null default 0,
  active bool,
  amount decimal,
  fiscal_year int,
  partnership varchar(32),
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	unique (budget, change_to_plan),
	foreign key (budget) references sc.budgets(id),
	foreign key (change_to_plan) references sc.change_to_plans(id),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (partnership) references sc.partnerships(base64)
);

-- PROJECT LOCATION

create table if not exists sc.project_locations (
  id serial primary key,
  active bool,
  change_to_plan int not null default 0,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
  location int not null,
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
  project int not null,
	unique (project, location, change_to_plan),
	foreign key (change_to_plan) references sc.change_to_plans(id),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (location) references sc.locations(id),
	foreign key (project) references public.projects(id)
);

-- LANGUAGE ENGAGEMENTS

-- todo
DO $$ BEGIN
    create type public.engagement_status as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.project_engagement_tag as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists sc.language_engagements (
  id serial primary key,
  base64 varchar(32) not null,
	project int not null,
	ethnologue int not null,
	change_to_plan int not null default 0,
  active bool,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
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
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	paratext_registry varchar(32),
	periodic_reports_directory int,
	pnp varchar(255),
	pnp_file int,
	product_engagement_tag public.project_engagement_tag,
	start_date timestamp,
	start_date_override timestamp,
	status public.engagement_status,
	unique (project, ethnologue, change_to_plan),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (change_to_plan) references sc.change_to_plans(id),
	foreign key (ethnologue) references sil.table_of_languages(id),
	foreign key (periodic_reports_directory) references sc.periodic_reports_directory(id),
	foreign key (pnp_file) references sc.file_versions(id),
	foreign key (project) references public.projects(id)
);

-- PRODUCTS

-- todo
DO $$ BEGIN
    create type public.product_mediums as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.product_methodologies as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.product_purposes as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.product_type as enum (
		'Film',
		'Literacy Material',
		'Scripture',
		'Song',
		'Story'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists sc.products (
    id serial primary key,
    base64 varchar(32) not null,
    change_to_plan int not null default 0,
    active bool,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    created_by int not null default 0,
    mediums public.product_mediums[],
    methodologies public.product_methodologies[],
    modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int not null default 0,
    purposes public.product_purposes[],
    type public.product_type,
    name varchar(64),
    unique (base64, change_to_plan),
    foreign key (created_by) references public.people(id),
    foreign key (modified_by) references public.people(id),
    foreign key (change_to_plan) references sc.change_to_plans(id)
);

create table if not exists sc.product_scripture_references (
    product int not null,
    scripture_reference int not null,
    change_to_plan int not null default 0,
    active bool,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    created_by int not null default 0,
    modified_at timestamp not null default CURRENT_TIMESTAMP,
    modified_by int not null default 0,
    primary key (product, scripture_reference, change_to_plan),
    foreign key (created_by) references public.people(id),
    foreign key (modified_by) references public.people(id),
    foreign key (product) references sc.products(id),
    foreign key (scripture_reference) references public.scripture_references(id),
    foreign key (change_to_plan) references sc.change_to_plans(id)
);

-- INTERNSHIP ENGAGEMENTS

-- todo
DO $$ BEGIN
    create type public.internship_methodology as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

-- todo
DO $$ BEGIN
    create type public.internship_position as enum (
		'A',
		'B',
		'C'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

create table if not exists sc.internship_engagements (
  id serial primary key,
	project int not null,
	ethnologue int not null,
	change_to_plan int not null default 0,
  active bool,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	communications_complete_date timestamp,
	complete_date timestamp,
	country_of_origin int,
	disbursement_complete_date timestamp,
	end_date timestamp,
	end_date_override timestamp,
	growth_plan int,
	initial_end_date timestamp,
	intern int,
	last_reactivated_at timestamp,
	mentor int,
	methodology public.internship_methodology,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	paratext_registry varchar(32),
	periodic_reports_directory int,
	position public.internship_position,
	start_date timestamp,
	start_date_override timestamp,
	status public.engagement_status,
	unique (project, ethnologue, change_to_plan),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (change_to_plan) references sc.change_to_plans(id),
	foreign key (ethnologue) references sil.table_of_languages(id),
	foreign key (project) references public.projects(id),
	foreign key (country_of_origin) references public.locations(id),
	foreign key (growth_plan) references sc.file_versions(id),
	foreign key (intern) references public.people(id),
	foreign key (mentor) references public.people(id),
	foreign key (periodic_reports_directory) references sc.periodic_reports_directory(id)
);

create table if not exists sc.ceremonies (
  id serial primary key,
  project int not null,
	ethnologue int not null,
	actual_date timestamp,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	created_by int not null default 0,
	estimated_date timestamp,
	is_planned bool,
	modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null default 0,
	type varchar(255),
	foreign key (created_by) references public.people(id),
  foreign key (modified_by) references public.people(id),
	foreign key (ethnologue) references sil.table_of_languages(id),
  foreign key (project) references public.projects(id)
);

-- CRM TABLES, WIP ------------------------------------------------------------------

DO $$ BEGIN
    create type public.involvements as enum (
		'CIT',
		'Engagements'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

DO $$ BEGIN
    create type public.people_transitions as enum (
		'New Org',
		'Other'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

DO $$ BEGIN
    create type public.org_transitions as enum (
		'To Manager',
		'To Other'
	);
	EXCEPTION
	WHEN duplicate_object THEN null;
END; $$;

--
--create table if not exists sc_org_to_org_rels (
--    from_sys_org varchar(32) not null,
--    to_sys_org varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (from_sys_org, to_sys_org),
--    foreign key (from_sys_org) references sc_organizations(sys_org),
--    foreign key (to_sys_org) references sc_organizations(sys_org)
--);
--
--create table if not exists sc_partner_performance (
--    sc_internal_org varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table if not exists sc_partner_finances (
--    sc_internal_org varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table if not exists sc_partner_reporting (
--    sc_internal_org varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table if not exists sc_partner_translation_progress (
--    sc_internal_org varchar(32) not null,
--    sc_internal_id varchar(32) not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org, sc_internal_id),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table if not exists sc_partner_notes (
--    sc_internal_org varchar(32) not null,
--    author_sys_person int not null,
--    note_text text not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org, author_sys_person, created_at),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org),
--    foreign key (author_sys_person) references sys_people(sys_person)
--);
--
--create table if not exists sc_org_transitions (
--    sc_internal_org varchar(32) not null,
--    transition_type sc_org_transitions not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sc_internal_org),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);
--
--create table if not exists sc_roles (
--    sc_role serial primary key,
--    name varchar(32) unique not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP
--);
--
--create table if not exists sc_role_memberships (
--    sys_person int not null,
--    sc_role int not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sys_person, sc_role),
--    foreign key (sys_person) references sys_people(sys_person)
--);
--
--create table if not exists sc_person_to_person_rels (
--    from_sys_person int not null,
--    to_sys_person int not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (from_sys_person, to_sys_person),
--    foreign key (from_sys_person) references sys_people(sys_person),
--    foreign key (to_sys_person) references sys_people(sys_person)
--);
--
--create table if not exists sys_people_transitions (
--    sys_person int not null,
--    transition_type sc_people_transitions not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--    primary key (sys_person, transition_type),
--    foreign key (sys_person) references sys_people(sys_person)
--);
--
--create table if not exists sc_involvements (
--    sc_internal_org varchar(32) not null,
--    type sc_involvements not null,
--    created_at timestamp not null default CURRENT_TIMESTAMP,
--	primary key (sc_internal_org, type),
--    foreign key (sc_internal_org) references sc_organizations(sc_internal_org)
--);