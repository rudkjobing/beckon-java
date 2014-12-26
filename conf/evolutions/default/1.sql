# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table beckon (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  creator_id                bigint,
  starts                    datetime,
  ends                      datetime,
  location_id               bigint,
  constraint pk_beckon primary key (id))
;

create table beckon_membership (
  id                        bigint auto_increment not null,
  beckon_id                 bigint,
  user_id                   bigint,
  status                    varchar(8),
  constraint ck_beckon_membership_status check (status in ('INVITED','ACCEPTED','DECLINED')),
  constraint pk_beckon_membership primary key (id))
;

create table device (
  id                        bigint auto_increment not null,
  arn                       varchar(255),
  owner_id                  bigint,
  constraint pk_device primary key (id))
;

create table friendship (
  id                        bigint auto_increment not null,
  nickname                  varchar(255),
  owner_id                  bigint,
  friend_id                 bigint,
  status                    varchar(8),
  constraint ck_friendship_status check (status in ('INVITED','ACCEPTED','DECLINED','BLOCKED','DELETED')),
  constraint pk_friendship primary key (id))
;

create table location (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  latitude                  double,
  longitude                 double,
  constraint pk_location primary key (id))
;

create table security_context (
  id                        bigint auto_increment not null,
  hash                      varchar(255),
  attempt                   varchar(255),
  autherized_user_id        bigint,
  pin_code                  varchar(255),
  cookie                    varchar(255),
  constraint pk_security_context primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  phone_number              varchar(255),
  email                     varchar(255),
  region                    varchar(255),
  constraint uq_user_phone_number unique (phone_number),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;


create table user_beckon_membership (
  user_id                        bigint not null,
  beckon_membership_id           bigint not null,
  constraint pk_user_beckon_membership primary key (user_id, beckon_membership_id))
;
alter table beckon add constraint fk_beckon_creator_1 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_beckon_creator_1 on beckon (creator_id);
alter table beckon add constraint fk_beckon_location_2 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_beckon_location_2 on beckon (location_id);
alter table beckon_membership add constraint fk_beckon_membership_beckon_3 foreign key (beckon_id) references beckon (id) on delete restrict on update restrict;
create index ix_beckon_membership_beckon_3 on beckon_membership (beckon_id);
alter table beckon_membership add constraint fk_beckon_membership_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_beckon_membership_user_4 on beckon_membership (user_id);
alter table device add constraint fk_device_owner_5 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_device_owner_5 on device (owner_id);
alter table friendship add constraint fk_friendship_owner_6 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_friendship_owner_6 on friendship (owner_id);
alter table friendship add constraint fk_friendship_friend_7 foreign key (friend_id) references user (id) on delete restrict on update restrict;
create index ix_friendship_friend_7 on friendship (friend_id);
alter table security_context add constraint fk_security_context_autherizedUser_8 foreign key (autherized_user_id) references user (id) on delete restrict on update restrict;
create index ix_security_context_autherizedUser_8 on security_context (autherized_user_id);



alter table user_beckon_membership add constraint fk_user_beckon_membership_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_beckon_membership add constraint fk_user_beckon_membership_beckon_membership_02 foreign key (beckon_membership_id) references beckon_membership (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table beckon;

drop table beckon_membership;

drop table device;

drop table friendship;

drop table location;

drop table security_context;

drop table user;

drop table user_beckon_membership;

SET FOREIGN_KEY_CHECKS=1;

