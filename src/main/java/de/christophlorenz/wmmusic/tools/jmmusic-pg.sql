-- database description for jmmusic DB

CREATE TABLE db_info (
	version character(10) NOT NULL
);
insert into db_info values('1.0');

CREATE TABLE artist (
	id SERIAL PRIMARY KEY,
    name text NOT NULL,
    print text NOT NULL,
    birthday date,
    country character(3),
    location text,
    url text,
    remarks text,
    timestamp timestamp NOT NULL
);

insert into artist (name,print,birthday,country,location,url,remarks,timestamp) values('unknown','unknown',null,null,null,null,null,now());


CREATE TABLE song (
    id SERIAL PRIMARY KEY,
    artist_id integer NOT NULL,
    title text NOT NULL,
    release character(3),
    year smallint,
    authors text,
    dance character(2),
    id3genre character varying(24),
    remarks text,
    timestamp timestamp NOT NULL,
    foreign key(artist_id) references artist(id)
);


CREATE TABLE medium (
	id SERIAL PRIMARY KEY,
	type smallint NOT NULL,
	code character(8) NOT NULL,
	artist_id integer,
	title text DEFAULT NULL,
	label text DEFAULT NULL,
	ordercode text DEFAULT NULL,
	p_year integer DEFAULT NULL,
	size smallint DEFAULT NULL,
	manufacturer text DEFAULT NULL,
	system text DEFAULT NULL,
	rec_begin_date date DEFAULT NULL,
	rec_begin_b date DEFAULT NULL,
	rec_end_date date DEFAULT NULL,
	burning_date date DEFAULT NULL,
	discid bigint DEFAULT NULL,
	track_offsets varchar(255) DEFAULT NULL,
	category varchar(16) DEFAULT NULL,
	id3_genre varchar(16) DEFAULT NULL,
	digital character(3) DEFAULT NULL,
	audio boolean DEFAULT NULL,
	rewritable boolean DEFAULT NULL,
	magic text DEFAULT NULL,
	files_type text DEFAULT NULL,
	buy_date date DEFAULT NULL,
	buy_price double precision DEFAULT NULL,
	remarks text DEFAULT NULL,
    timestamp timestamp NOT NULL
);


CREATE TABLE recording (
    id SERIAL PRIMARY KEY,
    song_id integer NOT NULL,
    medium_id integer NOT NULL,
    side character(1),
    track integer,
    counter text,
    time smallint,
    recording_year smallint,
    longplay character(2),
    quality integer,
    remarks text,
    special smallint,
    digital character(3),
    timestamp timestamp NOT NULL,
    foreign key(song_id) references song(id),
    foreign key(medium_id) references medium(id)
);


CREATE TABLE tag_medium (
	username varchar(64),
	mediumid integer,
	action varchar(64)
);

	
