# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table device (
  id                        bigint auto_increment not null,
  arn                       varchar(255),
  uuid                      varchar(255),
  owner_id                  bigint,
  type                      varchar(7),
  first_registered          datetime,
  last_registered           datetime,
  constraint ck_device_type check (type in ('APPLE','ANDROID','WINDOWS')),
  constraint pk_device primary key (id))
;

create table friendship (
  id                        bigint auto_increment not null,
  nickname                  varchar(255),
  owner_id                  bigint,
  friend_id                 bigint,
  peer_id                   bigint,
  status                    varchar(8),
  constraint ck_friendship_status check (status in ('PENDING','INVITED','ACCEPTED','DECLINED','BLOCKED','DELETED')),
  constraint pk_friendship primary key (id))
;

create table location (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  latitude                  double,
  longitude                 double,
  constraint pk_location primary key (id))
;

create table session (
  id                        bigint auto_increment not null,
  uuid                      varchar(255),
  user_id                   bigint,
  expires                   datetime,
  constraint uq_session_uuid unique (uuid),
  constraint pk_session primary key (id))
;

create table shout (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  description               varchar(255),
  begins                    datetime,
  location_id               bigint,
  constraint pk_shout primary key (id))
;

create table shout_membership (
  id                        bigint auto_increment not null,
  shout_id                  bigint,
  user_id                   bigint,
  status                    varchar(8),
  role                      varchar(7),
  constraint ck_shout_membership_status check (status in ('INVITED','ACCEPTED','MAYBE','DECLINED')),
  constraint ck_shout_membership_role check (role in ('CREATOR','MEMBER','ADMIN')),
  constraint pk_shout_membership primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  region                    varchar(255),
  email                     varchar(255),
  hash                      varchar(255),
  status                    varchar(8),
  constraint ck_user_status check (status in ('INACTIVE','ACTIVE','BANNED')),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

alter table device add constraint fk_device_owner_1 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_device_owner_1 on device (owner_id);
alter table friendship add constraint fk_friendship_owner_2 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_friendship_owner_2 on friendship (owner_id);
alter table friendship add constraint fk_friendship_friend_3 foreign key (friend_id) references user (id) on delete restrict on update restrict;
create index ix_friendship_friend_3 on friendship (friend_id);
alter table friendship add constraint fk_friendship_peer_4 foreign key (peer_id) references friendship (id) on delete restrict on update restrict;
create index ix_friendship_peer_4 on friendship (peer_id);
alter table session add constraint fk_session_user_5 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_session_user_5 on session (user_id);
alter table shout add constraint fk_shout_location_6 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_shout_location_6 on shout (location_id);
alter table shout_membership add constraint fk_shout_membership_shout_7 foreign key (shout_id) references shout (id) on delete restrict on update restrict;
create index ix_shout_membership_shout_7 on shout_membership (shout_id);
alter table shout_membership add constraint fk_shout_membership_user_8 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_shout_membership_user_8 on shout_membership (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table device;

drop table friendship;

drop table location;

drop table session;

drop table shout;

drop table shout_membership;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

