create table hardware (
    id int primary key,
    name VARCHAR2(100),
    logo VARCHAR2(300)
);

create table housing (
  id int primary key,
  name VARCHAR2(100)
);

create table room (
  id int primary key,
  name VARCHAR2(100),
  housing int references housing(id)
);

create table roomHAShardware (
    id int primary key,
    hardwareID int references hardware(id),
    roomID int REFERENCES room(id)
);

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
  id int primary key,
  uname VARCHAR2(100),
  udesc VARCHAR2(1000)
);

create table allowedUser (
   id int primary key,
   part int references roomHAShardware(id),
   isAD Number(1,0),
   uname int references avail_User(id)
);

CREATE OR REPLACE TRIGGER checkUser
  BEFORE INSERT OR UPDATE ON allowedUser
  FOR EACH ROW
  DECLARE
      numberOfAD number;
  BEGIN
      select count(part) into numberOfAD FROM allowedUser where isAD = 1 AND part=:new.part group by part;
      if numberOfAD = 1 then
        Raise_application_error(-20201, 'AD already defined!');
      end if;

      if :new.isAD = 1 AND uname != NULL then
        Raise_application_error(-20202, 'AD and user can not be set at the same time!');
      end if;
END;

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