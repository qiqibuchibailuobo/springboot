-- auto-generated definition
create table COMMENT
(
  ID            BIGINT  default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_B78F8850_1860_49B3_AD6F_F9717B52CFE4)
    primary key,
  PARENT_ID     BIGINT  not null,
  TYPE          INTEGER not null,
  COMMENTATOR   BIGINT  not null,
  GMT_CREATE    BIGINT  not null,
  GMT_MODIFIED  BIGINT  not null,
  LIKE_COUNT    BIGINT  default 0,
  CONTENT       VARCHAR(1024),
  COMMENT_COUNT INTEGER default 0
);

comment on column COMMENT.PARENT_ID
is '父类id';

comment on column COMMENT.TYPE
is '父类类型';

comment on column COMMENT.GMT_CREATE
is '创建时间';

comment on column COMMENT.GMT_MODIFIED
is '更新时间';

comment on column COMMENT.LIKE_COUNT
is '点赞数';

comment on column COMMENT.CONTENT
is '评论';

