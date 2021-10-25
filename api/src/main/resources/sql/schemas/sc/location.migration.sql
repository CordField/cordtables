CREATE OR REPLACE PROCEDURE sc.sc_migrate_location(
    in pNeo4j_id VARCHAR(32),
    in pName varchar(255),
    in pDefault_field_region VARCHAR(32),
    in pFunding_account VARCHAR(32),
    in pIso_alpha3 char(3),
    in pCountry_numeric int,
    in pCountry_country VARCHAR(255),
    in pCountry_alpha3 CHAR(3),
    in pCountry_alpha2 CHAR(2),
    in ptype common.location_type,
    inout error_type varchar(32) default 'UnknownError'
)
LANGUAGE PLPGSQL
AS $$
DECLARE
  scNeo4j_id int;
  scId int;
--    defFieldRegion int;  TODO: later
--    fundingAccount int;  TODO: later
BEGIN
  select id
  from sc.locations
  into scId
  where neo4j_id = pNeo4j_id;

  if found then
    -- update each cell only if there is a diff
    -- sc locations
    update sc.locations
    set iso_alpha_3 = pIso_alpha3,
      modified_at = CURRENT_TIMESTAMP
    where id = scId and
      (
          (iso_alpha_3 != pIso_alpha3)
          OR
          (iso_alpha_3 IS NULL AND pIso_alpha3 IS NOT NULL)
          OR
          (iso_alpha_3 IS NOT NULL AND pIso_alpha3 IS NULL )
      );

    -- common locations
    update common.locations
    set name = pName,
      modified_at = CURRENT_TIMESTAMP
    where id = scId and
      name != pName;

    update common.locations
    set type = pType,
        modified_at = CURRENT_TIMESTAMP
    where id = scId and
        type != pType;

    -- isoCountry
    update sc.location_iso_country
    set inumeric = pCountry_numeric,
        modified_at = CURRENT_TIMESTAMP
    where id = scId and
        (
            (inumeric != pCountry_numeric)
            OR
            (inumeric IS NULL AND pCountry_numeric IS NOT NULL)
            OR
            (inumeric IS NOT NULL AND pCountry_numeric IS NULL )
        );

    update sc.location_iso_country
    set country = pCountry_country,
        modified_at = CURRENT_TIMESTAMP
    where id = scId and
        (
            (country != pCountry_country)
            OR
            (country IS NULL AND pCountry_country IS NOT NULL)
            OR
            (country IS NOT NULL AND pCountry_country IS NULL )
        );

    update sc.location_iso_country
    set alpha3 = pCountry_alpha3,
        modified_at = CURRENT_TIMESTAMP
    where id = scId and
        (
            (alpha3 != pCountry_alpha3)
            OR
            (alpha3 IS NULL AND pCountry_alpha3 IS NOT NULL)
            OR
            (alpha3 IS NOT NULL AND pCountry_alpha3 IS NULL )
        );

    update sc.location_iso_country
    set alpha2 = pCountry_alpha2,
        modified_at = CURRENT_TIMESTAMP
    where id = scId and
        (
            (alpha2 != pCountry_alpha2)
            OR
            (alpha2 IS NULL AND pCountry_alpha2 IS NOT NULL)
            OR
            (alpha2 IS NOT NULL AND pCountry_alpha2 IS NULL )
        );

    error_type := 'NoError';

  else
  -- common
    insert into common.locations(
        name,
        type,
        created_by,
        modified_by,
        owning_person,
        owning_group
    ) values (
        pName,
        pType,
        1,
        1,
        1,
        1
    );
    -- sc
    INSERT INTO sc.locations(
        id,
        neo4j_id,
        iso_alpha_3,
        created_by,
        modified_by,
        owning_group,
        owning_person
    ) values (
        (SELECT id from common.locations where name = pName),
        pNeo4j_id,
        pIso_alpha3,
        1,
        1,
        1,
        1
    );
    -- isoCountry
    INSERT INTO sc.location_iso_country (
        id,
        inumeric,
        country,
        alpha3,
        alpha2,
        created_by,
        modified_by,
        owning_group,
        owning_person
    ) VALUES (
        (SELECT id from sc.locations where neo4j_id = pNeo4j_id),
        pCountry_numeric,
        pCountry_country,
        pCountry_alpha3,
        pCountry_alpha2,
        1,
        1,
        1,
        1
    );
    error_type := 'NoError';
  end if;
END; $$;