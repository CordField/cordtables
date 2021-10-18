CREATE OR REPLACE PROCEDURE sc.sc_migrate_ethnologue(
    in pIso_639 VARCHAR(3),
    in pLanguage_name varchar(64),
    in pPopulation int,
    in pProvisional_code varchar(32),
    in pSensitivity common.sensitivity,
    inout error_type varchar(32) default 'UnknownError'
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vLangId int;
BEGIN
  -- look up the iso, create if not found
  select id
  from sil.table_of_languages
  into vLangId
  where iso_639 = pIso_639;

  if found then
    -- update each cell only if there is a diff
    update sil.table_of_languages
    set language_name = pLanguage_name,
      modified_at = CURRENT_TIMESTAMP
    where iso_639 = pIso_639 and
      language_name != pLanguage_name;

    update sil.table_of_languages
    set population = pPopulation,
      modified_at = CURRENT_TIMESTAMP
    where iso_639 = pIso_639 and
      population != pPopulation;

    update sil.table_of_languages
    set provisional_code = pProvisional_code,
      modified_at = CURRENT_TIMESTAMP
    where iso_639 = pIso_639 and
      provisional_code != pProvisional_code;

    update sil.table_of_languages
    set sensitivity = pSensitivity,
      modified_at = CURRENT_TIMESTAMP
    where iso_639 = pIso_639 and
      sensitivity != pSensitivity;

    error_type := 'NoError';

  else
    insert into sil.table_of_languages(
      iso_639,
      language_name,
      population,
      provisional_code,
      sensitivity,
      created_by,
      modified_by,
      owning_person,
      owning_group
    ) values (
      pIso_639,
      pLanguage_name,
      pPopulation,
      pProvisional_code,
      pSensitivity,
      1,
      1,
      1,
      1
    );

    error_type := 'NoError';
  end if;

END; $$;