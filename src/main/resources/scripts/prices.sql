-- auto-generated definition
CREATE TABLE price
(
  id      BIGINT NOT NULL
          CONSTRAINT price_pkey
          PRIMARY KEY,
  cents         BIGINT,
  route_id      BIGINT NOT NULL
    CONSTRAINT fk_price_route_id
    REFERENCES route,
  contractor_id BIGINT NOT NULL
    CONSTRAINT fk_71lc5sc0qbgyqtm1c3fhmpbb6
    REFERENCES contractor
);

INSERT INTO price (id,route_id, cents, contractor_id) SELECT id, id, cents, 1 FROM route;

alter table route drop column cents;
-- auto-generated definition
drop TABLE booking;
CREATE TABLE booking
(
  agent                 BIGINT,
  bookingstatus         INTEGER,
  contractor            BIGINT,
  currency              INTEGER,
  date                  TIMESTAMP,
  datetext              VARCHAR(255),
  email                 VARCHAR(255),
  flightno              VARCHAR(255),
  instanziated          TIMESTAMP,
  landingtime           VARCHAR(255),
  name                  VARCHAR(255),
  ordertype             INTEGER,
  paidprice             INTEGER,
  parentid              BIGINT,
  pax                   INTEGER,
  pdf                   OID,
  rated                 BOOLEAN,
  ref                   VARCHAR(255),
  requirements          VARCHAR(255),
  shareprice            INTEGER,
  sharewanted           BOOLEAN,
  striperefusal         VARCHAR(255),
  surfboards            INTEGER,
  price_route_id        BIGINT NOT NULL
    CONSTRAINT booking_pkey
    PRIMARY KEY
    CONSTRAINT fk_hgkonrx93quc6tat2jqx5rl35
    REFERENCES price,
  booker_price_route_id BIGINT
    CONSTRAINT fk_eca3pyqqhv3m0vtkxh5cx8ou9
    REFERENCES booking,
  route_id              BIGINT
    CONSTRAINT fk_i8yfld4mqvgu14vb0ghmkhv0c
    REFERENCES route
);

UPDATE booking set price_route_id=206;
