alter table admin.users alter column person set not null;
alter table admin.users alter column email set not null;
alter table admin.users and unique (person);
alter table admin.users and unique (email);
