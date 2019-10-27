-- auto-generated definition
create table USER
(
  ID           BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_50A52061_5625_4499_A090_A83FB2C9F91D)
    primary key,
  ACCOUNT_ID   VARCHAR(100),
  NAME         VARCHAR(50),
  TOKEN        CHAR(36),
  GMT_CREATE   BIGINT,
  GMT_MODIFIED BIGINT,
  BIO          VARCHAR(256),
  AVATAR_URL   VARCHAR(100)
);

