# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table device (
  id                        bigint not null,
  arn                       varchar(255),
  uuid                      varchar(255),
  owner_id                  bigint,
  type                      varchar(7),
  first_registered          timestamp,
  last_registered           timestamp,
  constraint ck_device_type check (type in ('APPLE','ANDROID','WINDOWS')),
  constraint pk_device primary key (id))
;

create table friendship (
  id                        bigint not null,
  nickname                  varchar(255),
  owner_id                  bigint,
  friend_id                 bigint,
  peer_id                   bigint,
  status                    varchar(8),
  constraint ck_friendship_status check (status in ('PENDING','INVITED','ACCEPTED','DECLINED','BLOCKED','DELETED')),
  constraint pk_friendship primary key (id))
;

create table location (
  id                        bigint not null,
  name                      varchar(255),
  latitude                  float,
  longitude                 float,
  constraint pk_location primary key (id))
;

create table session (
  id                        bigint not null,
  uuid                      varchar(191),
  user_id                   bigint,
  expires                   timestamp,
  constraint uq_session_uuid unique (uuid),
  constraint pk_session primary key (id))
;

create table shout (
  id                        bigint not null,
  title                     varchar(255),
  description               varchar(255),
  begins                    timestamp,
  location_id               bigint,
  constraint pk_shout primary key (id))
;

create table shout_membership (
  id                        bigint not null,
  shout_id                  bigint,
  user_id                   bigint,
  status                    varchar(8),
  role                      varchar(7),
  constraint ck_shout_membership_status check (status in ('INVITED','ACCEPTED','MAYBE','DECLINED','DELETED')),
  constraint ck_shout_membership_role check (role in ('CREATOR','MEMBER','ADMIN')),
  constraint pk_shout_membership primary key (id))
;

create table bro_user (
  id                        bigint not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  region                    varchar(255),
  email                     varchar(191),
  hash                      varchar(255),
  status                    varchar(8),
  constraint ck_bro_user_status check (status in ('INACTIVE','ACTIVE','BANNED')),
  constraint uq_bro_user_email unique (email),
  constraint pk_bro_user primary key (id))
;

create sequence device_seq;

create sequence friendship_seq;

create sequence location_seq;

create sequence session_seq;

create sequence shout_seq;

create sequence shout_membership_seq;

create sequence bro_user_seq;

alter table device add constraint fk_device_owner_1 foreign key (owner_id) references bro_user (id);
create index ix_device_owner_1 on device (owner_id);
alter table friendship add constraint fk_friendship_owner_2 foreign key (owner_id) references bro_user (id);
create index ix_friendship_owner_2 on friendship (owner_id);
alter table friendship add constraint fk_friendship_friend_3 foreign key (friend_id) references bro_user (id);
create index ix_friendship_friend_3 on friendship (friend_id);
alter table friendship add constraint fk_friendship_peer_4 foreign key (peer_id) references friendship (id);
create index ix_friendship_peer_4 on friendship (peer_id);
alter table session add constraint fk_session_user_5 foreign key (user_id) references bro_user (id);
create index ix_session_user_5 on session (user_id);
alter table shout add constraint fk_shout_location_6 foreign key (location_id) references location (id);
create index ix_shout_location_6 on shout (location_id);
alter table shout_membership add constraint fk_shout_membership_shout_7 foreign key (shout_id) references shout (id);
create index ix_shout_membership_shout_7 on shout_membership (shout_id);
alter table shout_membership add constraint fk_shout_membership_user_8 foreign key (user_id) references bro_user (id);
create index ix_shout_membership_user_8 on shout_membership (user_id);



# --- !Downs

drop table if exists device cascade;

drop table if exists friendship cascade;

drop table if exists location cascade;

drop table if exists session cascade;

drop table if exists shout cascade;

drop table if exists shout_membership cascade;

drop table if exists bro_user cascade;

drop sequence if exists device_seq;

drop sequence if exists friendship_seq;

drop sequence if exists location_seq;

drop sequence if exists session_seq;

drop sequence if exists shout_seq;

drop sequence if exists shout_membership_seq;

drop sequence if exists bro_user_seq;

