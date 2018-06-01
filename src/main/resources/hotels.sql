drop table hotel;
create table hotel
(
  id       bigint not null
    constraint hotel_pkey
    primary key,
  address1 varchar(255),
  address2 varchar(255),
  address3 varchar(255),
  address4 varchar(255),
  email    varchar(255),
  link    varchar(255),
  web    varchar(255),
  logo    varchar(255),
  facebook    varchar(255),
  mobile   bigint,
  name     varchar(255),
   location_id bigint
    references location
);

INSERT INTO hotel (id, name,email, web, logo,facebook,link,location_id) VALUES (1000, 'Tea Tree Resort','theteatree.resort@gmail.com', 'http://theteatree.lk/', 'https://goo.gl/4MZPQb','https://www.facebook.com/theteatree.lk/','the-tea-tree',1003);

