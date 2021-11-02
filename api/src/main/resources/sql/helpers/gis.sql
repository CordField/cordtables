create extension if not exists postgis with schema sc;
alter table sc.languages add column if not exists coordinates sc.geography;
delete from sc.languages;
insert into sc.languages(id,name,display_name,owning_person,owning_group, created_by, modified_by, coordinates)
values
(1,'Kannada', 'KND', 1,1,1,1,'SRID=4326;POINT(77.5 12.9)'),
(2,'English', 'ENG',1,1,1,1, 'SRID=4326;POINT(-96.1 32.7)');


select round(sc.ST_Distance
(
	(select coordinates from sc.languages where id = 1)::sc.geography,
	(select coordinates from sc.languages where id = 2)::sc.geography
)/1000) || ' km' as distance;

