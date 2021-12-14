alter table common.locations alter column name set unique not null;
alter table common.locations alter column type set not null;
alter table common.scripture_references and unique (neo4j_id);
alter table common.scripture_references and unique (book_start, book_end, chapter_start, chapter_end, verse_start, verse_end)
alter table common.organizations alter column name set not null;
alter table common.organizations and unique (name);
alter table common.directories and unique (neo4j_id);
alter table common.files alter column directory set not null;
alter table common.files alter column name set not null;
alter table common.files and unique (neo4j_id);
alter table common.file_versions alter column name set not null;
alter table common.file_versions alter column mime_type set not null;
alter table common.file_versions alter column file set not null;
alter table common.file_versions alter column file_url set not null;
