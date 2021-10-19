-- sil tables are copied from SIL schema docs and adorned with columns this system needs
-- https://www.ethnologue.com/codes/code-table-structure
-- http://www.ethnologue.com/sites/default/files/Ethnologue-19-Global%20Dataset%20Doc.pdf

create schema sil;

CREATE TABLE sil.language_codes (
  id serial primary key,

  lang char(3) not null,  -- Three-letter code
  country char(2) not null,  -- Main country where used
  lang_status char(1) not null,  -- L(iving), (e)X(tinct)
  name varchar(75) not null,   -- Primary name in that country

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

CREATE TABLE sil.country_codes (
  id serial primary key,

  country char(2) not null,  -- Two-letter code from ISO3166
  name varchar(75) not null,  -- Country name
  area varchar(10) not null, -- World area

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

-- L(anguage), LA(lternate),
-- D(ialect), DA(lternate)
-- LP,DP (a pejorative alternate)
create type sil.language_name_type as enum (
  'L', 'LA', 'D', 'DA', 'LP', 'DP'
);

CREATE TABLE sil.language_index (
  id serial primary key,

  lang char(3) not null,      -- Three-letter code for language
  country char(2) not null,   -- Country where this name is used
  name_type sil.language_name_type not null,
  name  varchar(75) not null,

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);

create table sil.table_of_languages (
  id serial primary key,

  iso_639 char(3) unique,
  language_name varchar(64),
  population int,
  provisional_code varchar(32),
  sensitivity common.sensitivity not null default 'High',

  chat int references common.chats(id),
  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id),
  peer int references admin.peers(id)
);