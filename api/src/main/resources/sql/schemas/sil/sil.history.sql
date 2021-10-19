create table sil.table_of_languages_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
  iso_639 char(3),
  language_name varchar(64),
  population int,
  provisional_code varchar(32),
  sensitivity common.sensitivity,

  chat int,
	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION sil.table_of_languages_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into sil.table_of_languages_history(
          	id,
            iso_639,
            language_name,
            population,
            provisional_code,
            sensitivity,
            chat,
          	created_at,
          	created_by,
          	modified_at,
            modified_by,
            owning_person,
          	owning_group
          ) values (
            new.id,
            new.iso_639,
            new.language_name,
            new.population,
            new.provisional_code,
            new.sensitivity,
            new.chat,
            new.created_at,
            new.created_by,
            new.modified_at,
            new.modified_by,
            new.owning_person,
            new.owning_group
          );
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS sil_table_of_languages_history_insert_trigger ON sil.table_of_languages;
      CREATE TRIGGER sil_table_of_languages_history_insert_trigger
        AFTER INSERT
        ON sil.table_of_languages
        FOR EACH ROW
        EXECUTE PROCEDURE sil.table_of_languages_history_fn();

      DROP TRIGGER IF EXISTS sil_table_of_languages_history_update_trigger ON sil.table_of_languages;
      CREATE TRIGGER sil_table_of_languages_history_update_trigger
        AFTER UPDATE
        ON sil.table_of_languages
        FOR EACH ROW
        EXECUTE PROCEDURE sil.table_of_languages_history_fn();
