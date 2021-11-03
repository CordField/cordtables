create extension postgis with schema sc;
alter table sc.languages add column coordinates sc.geography;
-- india centre
update sc.languages set coordinates = 'SRID=4326;POINT(77.5 12.9)' where id = 13
select coordinates from sc.languages;

