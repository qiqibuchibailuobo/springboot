-- auto-generated definition
create table NOTIFICATION
(
  ID            BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_3E3B0981_3F7C_4AD4_A09B_00EDB6CD0D44)
    primary key,
  NOTIFIER      BIGINT            not null,
  RECEIVER      BIGINT            not null,
  OUTERID       BIGINT            not null,
  TYPE          INTEGER           not null,
  GMT_CREATE    BIGINT            not null,
  STATUS        INTEGER default 0 not null,
  NOTIFIER_NAME VARCHAR(100),
  OUTER_TITLE   VARCHAR(256)
);

