create table sc.languages_ex_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,

	language_name varchar(32),
	iso varchar(4),
	prioritization int,
	progress_bible bool,

	island varchar(32),
	province varchar(32),

	first_language_population int,
	population_value decimal, -- calculated from first_language_population

	egids_level sc.egids_scale,
	egids_value decimal, -- calculated from egids_level

	least_reached_progress_jps_level sc.least_reached_progress_scale,
	least_reached_value decimal, -- calculated from least_reached_progress_jps_scale

  partner_interest_level sc.partner_interest_scale,
	partner_interest_value decimal,
	partner_interest_description text,
	partner_interest_source text,

  multiple_languages_leverage_linguistic_level sc.multiple_languages_leverage_linguistic_scale,
	multiple_languages_leverage_linguistic_value decimal,
	multiple_languages_leverage_linguistic_description text,
	multiple_languages_leverage_linguistic_source text,

  multiple_languages_leverage_joint_training_level sc.multiple_languages_leverage_joint_training_scale,
	multiple_languages_leverage_joint_training_value decimal,
  multiple_languages_leverage_joint_training_description text,
  multiple_languages_leverage_joint_training_source text,

  lang_comm_int_in_language_development_level sc.lang_comm_int_in_language_development_scale,
	lang_comm_int_in_language_development_value decimal,
	lang_comm_int_in_language_development_description text,
	lang_comm_int_in_language_development_source text,

  lang_comm_int_in_scripture_translation_level sc.lang_comm_int_in_scripture_translation_scale,
	lang_comm_int_in_scripture_translation_value decimal,
	lang_comm_int_in_scripture_translation_description text,
	lang_comm_int_in_scripture_translation_source text,

  access_to_scripture_in_lwc_level sc.access_to_scripture_in_lwc_scale,
	access_to_scripture_in_lwc_value decimal,
	access_to_scripture_in_lwc_description text,
	access_to_scripture_in_lwc_source text,

  begin_work_geo_challenges_level sc.begin_work_geo_challenges_scale,
	begin_work_geo_challenges_value decimal,
	begin_work_geo_challenges_description text,
	begin_work_geo_challenges_source text,

  begin_work_rel_pol_obstacles_level sc.begin_work_rel_pol_obstacles_scale,
	begin_work_rel_pol_obstacles_value decimal,
  begin_work_rel_pol_obstacles_description text,
  begin_work_rel_pol_obstacles_source text,

	suggested_strategies text,
	comments text,

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
          insert into sc.languages_ex_history(
            id ,
            language_name ,
            iso ,
            prioritization ,
            progress_bible ,
            island ,
            province ,
            first_language_population ,
            population_value ,
            egids_level ,
            egids_value ,
            least_reached_progress_jps_level ,
            least_reached_value ,
            partner_interest_level ,
            partner_interest_value ,
            partner_interest_description ,
            partner_interest_source ,
            multiple_languages_leverage_linguistic_level ,
            multiple_languages_leverage_linguistic_value ,
            multiple_languages_leverage_linguistic_description ,
            multiple_languages_leverage_linguistic_source ,
            multiple_languages_leverage_joint_training_level ,
            multiple_languages_leverage_joint_training_value ,
            multiple_languages_leverage_joint_training_description ,
            multiple_languages_leverage_joint_training_source ,
            lang_comm_int_in_language_development_level ,
            lang_comm_int_in_language_development_value ,
            lang_comm_int_in_language_development_description ,
            lang_comm_int_in_language_development_source ,
            lang_comm_int_in_scripture_translation_level ,
            lang_comm_int_in_scripture_translation_value ,
            lang_comm_int_in_scripture_translation_description ,
            lang_comm_int_in_scripture_translation_source ,
            access_to_scripture_in_lwc_level,
            access_to_scripture_in_lwc_value ,
            access_to_scripture_in_lwc_description ,
            access_to_scripture_in_lwc_source ,
            begin_work_geo_challenges_level ,
            begin_work_geo_challenges_value ,
            begin_work_geo_challenges_description ,
            begin_work_geo_challenges_source ,
            begin_work_rel_pol_obstacles_level ,
            begin_work_rel_pol_obstacles_value ,
            begin_work_rel_pol_obstacles_description ,
            begin_work_rel_pol_obstacles_source ,
            suggested_strategies ,
            comments ,
            created_at ,
            created_by ,
            modified_at ,
            modified_by ,
            owning_person ,
            owning_group
          )
          values (
            new.id ,
            new.language_name ,
            new.iso ,
            new.prioritization ,
            new.progress_bible ,
            new.island ,
            new.province ,
            new.first_language_population ,
            new.population_value ,
            new.egids_level ,
            new.egids_value ,
            new.least_reached_progress_jps_level ,
            new.least_reached_value ,
            new.partner_interest_level ,
            new.partner_interest_value ,
            new.partner_interest_description ,
            new.partner_interest_source ,
            new.multiple_languages_leverage_linguistic_level ,
            new.multiple_languages_leverage_linguistic_value ,
            new.multiple_languages_leverage_linguistic_description ,
            new.multiple_languages_leverage_linguistic_source ,
            new.multiple_languages_leverage_joint_training_level ,
            new.multiple_languages_leverage_joint_training_value ,
            new.multiple_languages_leverage_joint_training_description ,
            new.multiple_languages_leverage_joint_training_source ,
            new.lang_comm_int_in_language_development_level ,
            new.lang_comm_int_in_language_development_value ,
            new.lang_comm_int_in_language_development_description ,
            new.lang_comm_int_in_language_development_source ,
            new.lang_comm_int_in_scripture_translation_level ,
            new.lang_comm_int_in_scripture_translation_value ,
            new.lang_comm_int_in_scripture_translation_description ,
            new.lang_comm_int_in_scripture_translation_source ,
            new.access_to_scripture_in_lwc_level,
            new.access_to_scripture_in_lwc_value ,
            new.access_to_scripture_in_lwc_description ,
            new.access_to_scripture_in_lwc_source ,
            new.begin_work_geo_challenges_level ,
            new.begin_work_geo_challenges_value ,
            new.begin_work_geo_challenges_description ,
            new.begin_work_geo_challenges_source ,
            new.begin_work_rel_pol_obstacles_level ,
            new.begin_work_rel_pol_obstacles_value ,
            new.begin_work_rel_pol_obstacles_description ,
            new.begin_work_rel_pol_obstacles_source ,
            new.suggested_strategies ,
            new.comments ,
            new.created_at ,
            new.created_by ,
            new.modified_at ,
            new.modified_by ,
            new.owning_person ,
            new.owning_group
          );
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