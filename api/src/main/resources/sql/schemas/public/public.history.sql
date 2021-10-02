create table if not exists public.global_roles_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	name varchar(255),

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_org int
);

      CREATE OR REPLACE FUNCTION global_roles_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into public.global_roles_history(id, name, created_at, created_by, modified_at, modified_by, owning_person, owning_org)
          values (new.id, new.name, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_org);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS global_roles_history_insert_trigger ON public.global_roles;
      CREATE TRIGGER global_roles_history_insert_trigger
        AFTER INSERT
        ON public.global_roles
        FOR EACH ROW
        EXECUTE PROCEDURE global_roles_history_fn();

      DROP TRIGGER IF EXISTS global_roles_history_update_trigger ON public.global_roles;
      CREATE TRIGGER global_roles_history_update_trigger
        AFTER UPDATE
        ON public.global_roles
        FOR EACH ROW
        EXECUTE PROCEDURE global_roles_history_fn();

create table if not exists public.global_role_column_grants_history(
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	access_level access_level,
	column_name varchar(64),
	global_role int,
	table_name public.table_name,

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
  owning_org int
);

      CREATE OR REPLACE FUNCTION global_role_column_grant_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into public.global_role_column_grant_history(id, created_at, created_by, modified_at, modified_by, name, org)
          values (new.id, new.created_at, new.created_by, new.modified_at, new.modified_by, new.name, new.org);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS global_role_column_grant_history_insert_trigger ON public.global_role_column_grant;
      CREATE TRIGGER global_role_column_grant_history_insert_trigger
        AFTER INSERT
        ON public.global_role_column_grant
        FOR EACH ROW
        EXECUTE PROCEDURE global_role_column_grant_history_fn();

      DROP TRIGGER IF EXISTS global_role_column_grant_history_update_trigger ON public.global_role_column_grant;
      CREATE TRIGGER global_role_column_grant_history_update_trigger
        AFTER UPDATE
        ON public.global_role_column_grant
        FOR EACH ROW
        EXECUTE PROCEDURE global_role_column_grant_history_fn();