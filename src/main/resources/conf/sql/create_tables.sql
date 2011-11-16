CREATE TABLE ws_users (
  id_user  int(10) NOT NULL, 
  nickname varchar(40) NOT NULL UNIQUE, 
  password varchar(255) NOT NULL, 
  email    varchar(255) NOT NULL UNIQUE, 
  CONSTRAINT pk_user 
    PRIMARY KEY (id_user));

CREATE TABLE ws_roles (
  id_role int(10) NOT NULL, 
  name    varchar(40) NOT NULL UNIQUE, 
  CONSTRAINT pk_role 
    PRIMARY KEY (id_role));

CREATE TABLE ws_users_roles (
  users_id_user int(10) NOT NULL, 
  roles_id_role int(10) NOT NULL, 
  PRIMARY KEY (users_id_user, roles_id_role));

CREATE TABLE ws_worldcities (
    country_code varchar(2) NOT NULL,
    country_name varchar(40) NOT NULL,
    city         varchar(40) NOT NULL,
    region       varchar(3) NOT NULL,
    latitude     float NOT NULL,
    longitude    float NOT NULL,
    UNIQUE (country_code, city, latitude, longitude));

ALTER TABLE ws_users_roles ADD CONSTRAINT FKusers_role1 FOREIGN KEY (roles_id_role) REFERENCES ws_roles (id_role);
ALTER TABLE ws_users_roles ADD CONSTRAINT FKusers_role2 FOREIGN KEY (users_id_user) REFERENCES ws_users (id_user);

