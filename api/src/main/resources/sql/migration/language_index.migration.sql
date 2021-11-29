CREATE OR REPLACE PROCEDURE sil.sil_migrate_language_index(
    in pLang VARCHAR(3),
    in pCountry VARCHAR(2),
    in pNameType VARCHAR(2),
    in pName VARCHAR(75)
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  vCommonId int;
  vPersonId int;
BEGIN
  select id from admin.people
  where sensitivity_clearance = 'High'
  into vPersonId;

  if vPersonId then
    insert into common.languages(created_by, modified_by, owning_person, owning_group)
    values (vPersonId, vPersonId, vPersonId, vPersonId)
    returning id
    into vCommonId;

    insert into sil.language_index(common_id, lang, country, name_type, name, created_by, modified_by, owning_person, owning_group)
    values (vCommonId, pLang, pCountry, pNameType::sil.language_name_type, pName, vPersonId, vPersonId, vPersonId, vPersonId);

  end if;

END; $$;