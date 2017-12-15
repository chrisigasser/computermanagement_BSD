drop table hardware cascade constraints;
drop table housing cascade constraints;
drop table room cascade constraints;
drop table roomHAShardware cascade constraints;
drop table networkInfo cascade constraints;
drop table anwendung cascade constraints;
drop table hasAnwendung cascade constraints;
drop table avail_User cascade constraints;
drop table allowedUser cascade constraints;
drop table works cascade constraints;
drop table furtherInformation cascade constraints;

create table hardware (
    id int primary key,
    name VARCHAR2(100),
    logo VARCHAR2(300),
	hdesc VARCHAR2(500)
);

create table housing (
  id int primary key,
  name VARCHAR2(100)
);

create table room (
  id int primary key,
  name VARCHAR2(100),
  shape SDO_GEOMETRY,
  housing int references housing(id)
);

delete from user_sdo_geom_metadata
where table_name = 'room' 
or table_name = 'ROOM';

INSERT INTO user_sdo_geom_metadata
( TABLE_NAME,
COLUMN_NAME,
DIMINFO,
SRID
)
VALUES
( 'room',
'shape',
SDO_DIM_ARRAY(
SDO_DIM_ELEMENT('X', 0, 200, 1),
SDO_DIM_ELEMENT('Y', 0, 200, 1)
),
NULL
);

create table roomHAShardware (
    id int primary key,
    hardwareID int references hardware(id),
    roomID int REFERENCES room(id),
	name varchar2(100),
	rhdesc varchar2(500)
);

alter table roomHAShardware add constraint uniqueRoomName unique (name, roomID);

create table networkInfo (
  part int references roomHAShardware(id),
  isDHCP Number(1,0),
  additionalInfo VARCHAR2(1000),
  PRIMARY KEY (part)
);

create table anwendung (
  id int primary key,
  name VARCHAR2(100),
  adesc VARCHAR2(1000)
);

create table hasAnwendung (
  AnwendungsID int references anwendung(id),
  part int references roomHAShardware(id),
  primary key(AnwendungsID, part)
);

create table avail_User (
  id number primary key,
  uname VARCHAR2(100),
  udesc VARCHAR2(1000)
);

create table allowedUser (
   part int references roomHAShardware(id),
   isAD Number(1,0),
   uname number references avail_User(id),
   primary key(part, isAD, uname)
);

create table works (
  part int references roomHAShardware(id),
  working Number(1,0),
  primary key(part)
);

create table furtherInformation (
  part int references roomHAShardware(id),
  info VARCHAR2(2000),
  primary key(part)
);


CREATE OR REPLACE TRIGGER checkUser
  BEFORE INSERT OR UPDATE ON allowedUser
  FOR EACH ROW
  DECLARE
      numberOfAD number;
	  numberOfUser number;
  BEGIN
	BEGIN
		select count(part) into numberOfAD FROM allowedUser where isAD = 1 AND part=:new.part group by part;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        numberOfAD := 0;
    END;
	BEGIN
		select count(part) into numberOfUser FROM allowedUser where NVL(isAD, 0) = 0 AND part=:new.part group by part;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        numberOfUser := 0;
    END;
	
      if numberOfAD = 1 then
        Raise_application_error(-20201, 'AD already defined!');
      end if;
	  
	  if :new.isAD = 1 AND numberOfUser <> 0 then
		Raise_application_error(-20203, 'Please delete the AD Entry first!');
	  end if;

      if :new.isAD = 1 AND NVL(:new.uname,0) != 0 then
        Raise_application_error(-20202, 'AD and user can not be set at the same time!');
      end if;
END;
