CREATE OR REPLACE PROCEDURE sc.sc_migrate_language(
    in pIso_639 char(3) unique,
    in pNeo4j_id VARCHAR(32),
    in pName varchar(255),
    in pDisplay_name varchar(255),
    in pDisplay_name_pronunciation varchar(255),
    in pIs_dialect bool,
    in pPopulation_override int,
    in pRegistry_of_dialects_code varchar(32),
    in pIs_least_of_these bool,
    in pLeast_of_these_reason varchar(255),
    in pSign_language_code varchar(32),
    in pSponsor_estimated_end_date timestamp,
    in pSensitivity common.sensitivity,
    in pIs_sign_language bool,
    in pHas_external_first_scripture bool,
    in pTags varchar(32),
    in pPreset_inventory bool,
    inout error_type varchar(32) default 'UnknownError'
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vLangId int;
BEGIN
  select id
  from sc.languages
  into vLangId
  where neo4j_id = pNeo4j_id;

  if found then
    -- update each cell only if there is a diff
    update sc.languages
    set name = pName,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
      name != pName;

    update sc.languages
    set display_name = pDisplay_name,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
      display_name != pDisplay_name;

    update sc.languages
    set display_name_pronunciation = pDisplay_name_pronunciation,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
      display_name_pronunciation != pDisplay_name_pronunciation;

    update sc.languages
    set tags = pTags,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
      tags != pTags;

    update sc.languages
    set preset_inventory = pPreset_inventory,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       preset_inventory != pPreset_inventory;

    update sc.languages
    set is_dialect = pIs_dialect,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       is_dialect != pIs_dialect;

    update sc.languages
    set is_sign_language = pIs_sign_language,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       is_sign_language != pIs_sign_language;


    update sc.languages
    set is_least_of_these = pIs_least_of_these,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       is_least_of_these != pIs_least_of_these;



    update sc.languages
    set least_of_these_reason = pLeast_of_these_reason,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       least_of_these_reason != pLeast_of_these_reason;

    update sc.languages
    set population_override = pPopulation_override,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       population_override != pPopulation_override;

    update sc.languages
    set registry_of_dialects_code = pRegistry_of_dialects_code,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       registry_of_dialects_code != pRegistry_of_dialects_code;

    update sc.languages
    set sensitivity = pSensitivity,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       sensitivity != pSensitivity;

    update sc.languages
    set sign_language_code = pSign_language_code,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       sign_language_code != pSign_language_code;

    update sc.languages
    set sponsor_estimated_end_date = pSponsor_estimated_end_date,
      modified_at = CURRENT_TIMESTAMP
    where neo4j_id = pNeo4j_id and
       sponsor_estimated_end_date != pSponsor_estimated_end_date;

    error_type := 'NoError';

  else
    insert into sc.languages(
      neo4j_id
      name,
      display_name,
      display_name_pronunciation,
      tags,
      preset_inventory,
      is_dialect,
      is_sign_language,
      is_least_of_these,
      least_of_these_reason,
      population_override,
      registry_of_dialects_code,
      sensitivity,
      sign_language_code,
      sponsor_estimated_end_date,
      created_by,
      modified_by,
      owning_person,
      owning_group
    ) values (
      pNeo4j_id
      pName,
      pDisplay_name,
      pDisplay_name_pronunciation,
      pTags,
      pPreset_inventory,
      pIs_dialect,
      pIs_sign_language,
      pIs_least_of_these,
      pLeast_of_these_reason,
      pPopulation_override,
      pRegistry_of_dialects_code,
      pSensitivity,
      pSign_language_code,
      pSponsor_estimated_end_date,
      1,
      1,
      1,
      1
    );

    error_type := 'NoError';
  end if;

END; $$;