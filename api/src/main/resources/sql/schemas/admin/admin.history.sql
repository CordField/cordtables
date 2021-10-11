create table admin.people_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
  about text,
  phone varchar(32),
  picture varchar(255),
  private_first_name varchar(32),
  private_last_name varchar(32),
  public_first_name varchar(32),
  public_last_name varchar(32),
  primary_location int,
  private_full_name varchar(64),
  public_full_name varchar(64),
  sensitivity_clearance common.sensitivity,
  time_zone varchar(32),
  title varchar(255),
  status varchar(32),

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION people_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.people_history(id, about, phone, picture, private_first_name, private_last_name, public_first_name, public_last_name, primary_location, private_full_name, public_full_name, sensitivity_clearance, time_zone, title, status, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.about, new.phone, new.picture, new.private_first_name, new.private_last_name, new.public_first_name, new.public_last_name, new.primary_location, new.private_full_name, new.public_full_name, new.sensitivity_clearance, new.time_zone, new.title, new.status, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS people_history_insert_trigger ON admin.people;
      CREATE TRIGGER people_history_insert_trigger
        AFTER INSERT
        ON admin.people
        FOR EACH ROW
        EXECUTE PROCEDURE people_history_fn();

      DROP TRIGGER IF EXISTS people_history_update_trigger ON admin.people;
      CREATE TRIGGER people_history_update_trigger
        AFTER UPDATE
        ON admin.people
        FOR EACH ROW
        EXECUTE PROCEDURE people_history_fn();
        
create table admin.groups_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	name varchar(255),

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION groups_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.groups_history(id, name, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.name, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS groups_history_insert_trigger ON admin.groups;
      CREATE TRIGGER groups_history_insert_trigger
        AFTER INSERT
        ON admin.groups
        FOR EACH ROW
        EXECUTE PROCEDURE groups_history_fn();

      DROP TRIGGER IF EXISTS groups_history_update_trigger ON admin.groups;
      CREATE TRIGGER groups_history_update_trigger
        AFTER UPDATE
        ON admin.groups
        FOR EACH ROW
        EXECUTE PROCEDURE groups_history_fn();   
        
create table admin.group_row_access_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	group_id int,
  table_name admin.table_name,
  row int,

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION group_row_access_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.group_row_access_history(id, group_id, table_name, row, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.group_id, new.table_name, new.row, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS group_row_access_history_insert_trigger ON admin.group_row_access;
      CREATE TRIGGER group_row_access_history_insert_trigger
        AFTER INSERT
        ON admin.group_row_access
        FOR EACH ROW
        EXECUTE PROCEDURE group_row_access_history_fn();

      DROP TRIGGER IF EXISTS group_row_access_history_update_trigger ON admin.group_row_access;
      CREATE TRIGGER group_row_access_history_update_trigger
        AFTER UPDATE
        ON admin.group_row_access
        FOR EACH ROW
        EXECUTE PROCEDURE group_row_access_history_fn();      
        
create table admin.group_memberships_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	group_id int,
  person int,

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION group_memberships_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.group_memberships_history(id, group_id, person, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.group_id, new.person, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS group_memberships_history_insert_trigger ON admin.group_memberships;
      CREATE TRIGGER group_memberships_history_insert_trigger
        AFTER INSERT
        ON admin.group_memberships
        FOR EACH ROW
        EXECUTE PROCEDURE group_memberships_history_fn();

      DROP TRIGGER IF EXISTS group_memberships_history_update_trigger ON admin.group_memberships;
      CREATE TRIGGER group_memberships_history_update_trigger
        AFTER UPDATE
        ON admin.group_memberships
        FOR EACH ROW
        EXECUTE PROCEDURE group_memberships_history_fn(); 

create table admin.global_roles_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	name varchar(255),

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION global_roles_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.global_roles_history(id, name, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.name, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS global_roles_history_insert_trigger ON admin.global_roles;
      CREATE TRIGGER global_roles_history_insert_trigger
        AFTER INSERT
        ON admin.global_roles
        FOR EACH ROW
        EXECUTE PROCEDURE global_roles_history_fn();

      DROP TRIGGER IF EXISTS global_roles_history_update_trigger ON admin.global_roles;
      CREATE TRIGGER global_roles_history_update_trigger
        AFTER UPDATE
        ON admin.global_roles
        FOR EACH ROW
        EXECUTE PROCEDURE global_roles_history_fn();

create table admin.global_role_column_grants_history(
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	global_role int,
	table_name admin.table_name,
	column_name varchar(64),
	access_level admin.access_level,

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
  owning_group int
);

      CREATE OR REPLACE FUNCTION global_role_column_grants_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.global_role_column_grants_history(id, global_role, table_name, column_name, access_level, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.global_role, new.table_name, new.column_name, new.access_level, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS global_role_column_grants_history_insert_trigger ON admin.global_role_column_grants;
      CREATE TRIGGER global_role_column_grants_history_insert_trigger
        AFTER INSERT
        ON admin.global_role_column_grants
        FOR EACH ROW
        EXECUTE PROCEDURE global_role_column_grants_history_fn();

      DROP TRIGGER IF EXISTS global_role_column_grants_history_update_trigger ON admin.global_role_column_grants;
      CREATE TRIGGER global_role_column_grants_history_update_trigger
        AFTER UPDATE
        ON admin.global_role_column_grants
        FOR EACH ROW
        EXECUTE PROCEDURE global_role_column_grants_history_fn();

create table admin.global_role_table_permissions_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	global_role int,
  table_name admin.table_name,
  table_permission admin.table_permission,

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION global_role_table_permissions_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.global_role_table_permissions_history(id, global_role, table_name, table_permission, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.global_role, new.table_name, new.table_permission, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS global_role_table_permissions_history_insert_trigger ON admin.global_role_table_permissions;
      CREATE TRIGGER global_role_table_permissions_history_insert_trigger
        AFTER INSERT
        ON admin.global_role_table_permissions
        FOR EACH ROW
        EXECUTE PROCEDURE global_role_table_permissions_history_fn();

      DROP TRIGGER IF EXISTS global_role_table_permissions_history_update_trigger ON admin.global_role_table_permissions;
      CREATE TRIGGER global_role_table_permissions_history_update_trigger
        AFTER UPDATE
        ON admin.global_role_table_permissions
        FOR EACH ROW
        EXECUTE PROCEDURE global_role_table_permissions_history_fn();

create table admin.global_role_memberships_history (
  _history_id serial primary key,
  _history_created_at timestamp not null default CURRENT_TIMESTAMP,

	id int,
	global_role int,
  person int,

	created_at timestamp,
	created_by int,
	modified_at timestamp,
  modified_by int,
  owning_person int,
	owning_group int
);

      CREATE OR REPLACE FUNCTION global_role_memberships_history_fn()
        RETURNS TRIGGER
        LANGUAGE PLPGSQL
        AS $$
      begin
          insert into admin.global_role_memberships_history(id, global_role, person, created_at, created_by, modified_at, modified_by, owning_person, owning_group)
          values (new.id, new.global_role, new.person, new.created_at, new.created_by, new.modified_at, new.modified_by, new.owning_person, new.owning_group);
        RETURN NEW;
      end; $$;

      DROP TRIGGER IF EXISTS global_role_memberships_history_insert_trigger ON admin.global_role_memberships;
      CREATE TRIGGER global_role_memberships_history_insert_trigger
        AFTER INSERT
        ON admin.global_role_memberships
        FOR EACH ROW
        EXECUTE PROCEDURE global_role_memberships_history_fn();

      DROP TRIGGER IF EXISTS global_role_memberships_history_update_trigger ON admin.global_role_memberships;
      CREATE TRIGGER global_role_memberships_history_update_trigger
        AFTER UPDATE
        ON admin.global_role_memberships
        FOR EACH ROW
        EXECUTE PROCEDURE global_role_memberships_history_fn();