alter table common.locations add not null constraint (name);
alter table common.locations add not null constraint (type);
alter table common.organizations add not null constraint (name);
alter table common.files add not null constraint (name);
alter table common.file_versions add not null constraint (mime_type);
alter table common.file_versions add not null constraint (name);
alter table common.file_versions add not null constraint (file);
alter table common.file_versions add not null constraint (file_url);
