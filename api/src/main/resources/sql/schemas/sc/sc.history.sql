create table sc.languages_ex_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
  lang_name varchar(32),
  lang_code varchar(16),
  location text,
  first_lang_population int,
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
  progress_bible int,

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION languages_ex_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into sc.languages_ex_history(id, lang_name, lang_code, location, first_lang_population, population, egids_level, egids_value, least_reached_progress_jps_scale, least_reached_value, partner_interest, partner_interest_description, partner_interest_source, multi_lang_leverage, multi_lang_leverage_description, multi_lang_leverage_source, community_interest, community_interest_description, community_interest_source, community_interest_value, community_interest_scripture_description, community_interest_scripture_source, lwc_scripture_access, lwc_scripture_description, lwc_scripture_source, access_to_begin, access_to_begin_description, access_to_begin_source, suggested_strategies, comments, prioritization, progress_bible, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.lang_name, new.lang_code, new.location, new.first_lang_population, new.population, new.egids_level, new.egids_value, new.least_reached_progress_jps_scale, new.least_reached_value, new.partner_interest, new.partner_interest_description, new.partner_interest_source, new.multi_lang_leverage, new.multi_lang_leverage_description, new.multi_lang_leverage_source, new.community_interest, new.community_interest_description, new.community_interest_source, new.community_interest_value, new.community_interest_scripture_description, new.community_interest_scripture_source, new.lwc_scripture_access, new.lwc_scripture_description, new.lwc_scripture_source, new.access_to_begin, new.access_to_begin_description, new.access_to_begin_source, new.suggested_strategies, new.comments, new.prioritization, new.progress_bible, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS languages_ex_history_insert_trigger ON sc.languages_ex;
      CREATE TRIGGER languages_ex_history_insert_trigger
        AFTER INSERT
        ON sc.languages_ex
        FOR EACH ROW
        EXECUTE PROCEDURE languages_ex_history_fn();

      DROP TRIGGER IF EXISTS languages_ex_history_update_trigger ON sc.languages_ex;
      CREATE TRIGGER languages_ex_history_update_trigger
        AFTER UPDATE
        ON sc.languages_ex
        FOR EACH ROW
        EXECUTE PROCEDURE languages_ex_history_fn();