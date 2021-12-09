-- PRAYER --------------------------------------------------------------
create schema if not exists up;

create type up.prayer_type as enum (
  'Request',
  'Update',
  'Celebration'
);

create table up.prayer_requests(
	id serial primary key,

  language_id int references common.languages(id),
  sensitivity common.sensitivity default 'High',
  parent int references up.prayer_requests(id),
  translator int references admin.people(id),
  location varchar(64),
  title varchar(64) not null,
  content text not null,
  reviewed bool default false,
  prayer_type up.prayer_type default 'Request',

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);

create table up.prayer_notifications(
	id serial primary key,

  request int references up.prayer_requests(id),
  person int references admin.people(id),

  created_at timestamp not null default CURRENT_TIMESTAMP,
  created_by int not null references admin.people(id),
  modified_at timestamp not null default CURRENT_TIMESTAMP,
  modified_by int not null references admin.people(id),
  owning_person int not null references admin.people(id),
  owning_group int not null references admin.groups(id)
);
