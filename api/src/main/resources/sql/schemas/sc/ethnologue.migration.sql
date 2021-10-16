CREATE OR REPLACE PROCEDURE sc_migrate_ethnologue(
    in code VARCHAR(3),
    in name varchar(64),
    in population int,
    in provisionalCode varchar(16),
    in sensitivity varchar(16),
    inout error_type varchar(32) default 'UnknownError'
)
LANGUAGE PLPGSQL
AS $$
DECLARE

BEGIN


END; $$;