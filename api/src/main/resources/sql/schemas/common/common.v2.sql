alter table common.locations alter column name set unique not null;
alter table common.locations alter column type set not null;
alter table common.organizations alter column name set not null;
alter table common.files alter column directory set not null;
alter table common.files alter column name set not null;
alter table common.file_versions alter column name set not null;
alter table common.file_versions alter column mime_type set not null;
alter table common.file_versions alter column file set not null;
alter table common.file_versions alter column file_url set not null;
