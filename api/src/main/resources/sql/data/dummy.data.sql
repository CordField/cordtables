-- sc.languages
--insert into sc.languages(name, display_name,island, coordinates, owning_person,owning_group, created_by, modified_by)
--values
--('English', 'English', 'USA', 'SRID=4326;POINT(-96.1 32.7)',  1,1,1,1),
--('Spanish', 'Spanish', 'MEX', null, 1,1,1,1),
--('Hindi', 'Hindi', 'IND', null,1,1,1,1),
--('Kannada', 'Kannada', 'BLR','SRID=4326;POINT(77.5 12.9)', 1,1,1,1);


CREATE OR REPLACE PROCEDURE load_dummy_data()
LANGUAGE PLPGSQL
AS $$
DECLARE
  vEnglishEthId int;
  vSpanishEthId int;
  vHindiEthId int;
  vKannadaEthId int;
BEGIN
  -- todo

  call common.create_sc_ethnologue();
  call create_language(vEnglishEthId ,2,3, 'asdf');
END; $$;

CREATE OR REPLACE PROCEDURE common.create_sc_ethnologue()
LANGUAGE PLPGSQL
AS $$
DECLARE
  rec record;
  population_number int;
  provisional_code text;
  sensitivity sc.ethnologue.sensitivity%type;
BEGIN
  -- loop that selects the data from sil.language_index and inserts into rec
  	for rec in select id, lang, name, created_by, modified_by, owning_person, owning_group
  		from sil.language_index
  	loop
      -- Selects a random number between 40k and 100k to insert as the population number
	    SELECT floor(random()*(100000-40000+1))+40000 into population_number;
	    -- Selects a random combination of string composed by 4 characters to insert as the provisional code
	    SELECT array_to_string(ARRAY(SELECT chr((97+ round(random() * 25)) :: integer) from generate_series(1,4)), '' ) into provisional_code;
      -- Selects a random value from the predefined array to insert as the sensitivity
	    SELECT (array['Low','Medium','High'])[floor(random() * 3 + 1)] into sensitivity;
  	  insert into sc.ethnologue (language_index,code,language_name, population, provisional_code, sensitivity, created_by, modified_by, owning_person, owning_group) values (rec.id, rec.lang, rec.name, population_number, provisional_code, sensitivity, rec.created_by, rec.modified_by, rec.owning_person, rec.owning_group);
  	end loop;
END; $$;

CREATE OR REPLACE PROCEDURE create_language()
LANGUAGE PLPGSQL
AS $$
DECLARE
  vEnglishEthId int;
  vSpanishEthId int;
  vHindiEthId int;
  vKannadaEthId int;
BEGIN
  -- todo

END; $$;

call load_dummy_data();
