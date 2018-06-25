CREATE TABLE exchangerate
(
  id            BIGINT NOT NULL
    CONSTRAINT exchangerate_pkey
    PRIMARY KEY,
      date      varchar(255),
  currency integer,
  value float
);