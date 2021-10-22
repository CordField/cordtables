create table common.organizations_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	name varchar(255) unique not null,
  sensitivity common.sensitivity,
  primary_location int,

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION organizations_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into common.organizations_history(id, name, sensitivity, primary_location, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.name, new.sensitivity, new.primary_location, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS organizations_history_insert_trigger ON common.organizations;
      CREATE TRIGGER organizations_history_insert_trigger
        AFTER INSERT
        ON common.organizations
        FOR EACH ROW
        EXECUTE PROCEDURE organizations_history_fn();

      DROP TRIGGER IF EXISTS organizations_history_update_trigger ON common.organizations;
      CREATE TRIGGER organizations_history_update_trigger
        AFTER UPDATE
        ON common.organizations
        FOR EACH ROW
        EXECUTE PROCEDURE organizations_history_fn();   
        
create table admin.users_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	person int,
  email varchar(255),
  password varchar(255),

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION users_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.users_history(id, person, email, password, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.person, new.email, new.password, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS users_history_insert_trigger ON admin.users;
      CREATE TRIGGER users_history_insert_trigger
        AFTER INSERT
        ON admin.users
        FOR EACH ROW
        EXECUTE PROCEDURE users_history_fn();

      DROP TRIGGER IF EXISTS users_history_update_trigger ON admin.users;
      CREATE TRIGGER users_history_update_trigger
        AFTER UPDATE
        ON admin.users
        FOR EACH ROW
        EXECUTE PROCEDURE users_history_fn();   