# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table chat_room (
  id                        bigint not null,
  constraint pk_chat_room primary key (id))
;

create table chat_room_member (
  id                        bigint not null,
  user_id                   bigint,
  chat_room_id              bigint,
  constraint pk_chat_room_member primary key (id))
;

create table chat_room_message (
  id                        bigint not null,
  message                   varchar(255),
  posted                    timestamp,
  chat_room_id              bigint,
  constraint pk_chat_room_message primary key (id))
;

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
  chat_room_id              bigint,
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

create table support_request (
  id                        bigint not null,
  email                     varchar(255),
  message                   text,
  handled                   boolean,
  posted                    timestamp,
  user_id                   bigint,
  constraint pk_support_request primary key (id))
;

create table bro (
  id                        bigint not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  region                    varchar(255),
  email                     varchar(191),
  hash                      varchar(255),
  status                    varchar(8),
  constraint ck_bro_status check (status in ('INACTIVE','ACTIVE','BANNED')),
  constraint uq_bro_email unique (email),
  constraint pk_bro primary key (id))
;

create sequence chat_room_seq;

create sequence chat_room_member_seq;

create sequence chat_room_message_seq;

create sequence device_seq;

create sequence friendship_seq;

create sequence location_seq;

create sequence session_seq;

create sequence shout_seq;

create sequence shout_membership_seq;

create sequence support_request_seq;

create sequence bro_seq;

alter table chat_room_member add constraint fk_chat_room_member_user_1 foreign key (user_id) references bro (id);
create index ix_chat_room_member_user_1 on chat_room_member (user_id);
alter table chat_room_member add constraint fk_chat_room_member_chatRoom_2 foreign key (chat_room_id) references chat_room (id);
create index ix_chat_room_member_chatRoom_2 on chat_room_member (chat_room_id);
alter table chat_room_message add constraint fk_chat_room_message_chatRoom_3 foreign key (chat_room_id) references chat_room (id);
create index ix_chat_room_message_chatRoom_3 on chat_room_message (chat_room_id);
alter table device add constraint fk_device_owner_4 foreign key (owner_id) references bro (id);
create index ix_device_owner_4 on device (owner_id);
alter table friendship add constraint fk_friendship_owner_5 foreign key (owner_id) references bro (id);
create index ix_friendship_owner_5 on friendship (owner_id);
alter table friendship add constraint fk_friendship_friend_6 foreign key (friend_id) references bro (id);
create index ix_friendship_friend_6 on friendship (friend_id);
alter table friendship add constraint fk_friendship_peer_7 foreign key (peer_id) references friendship (id);
create index ix_friendship_peer_7 on friendship (peer_id);
alter table session add constraint fk_session_user_8 foreign key (user_id) references bro (id);
create index ix_session_user_8 on session (user_id);
alter table shout add constraint fk_shout_location_9 foreign key (location_id) references location (id);
create index ix_shout_location_9 on shout (location_id);
alter table shout add constraint fk_shout_chatRoom_10 foreign key (chat_room_id) references chat_room (id);
create index ix_shout_chatRoom_10 on shout (chat_room_id);
alter table shout_membership add constraint fk_shout_membership_shout_11 foreign key (shout_id) references shout (id);
create index ix_shout_membership_shout_11 on shout_membership (shout_id);
alter table shout_membership add constraint fk_shout_membership_user_12 foreign key (user_id) references bro (id);
create index ix_shout_membership_user_12 on shout_membership (user_id);
alter table support_request add constraint fk_support_request_user_13 foreign key (user_id) references bro (id);
create index ix_support_request_user_13 on support_request (user_id);



# --- !Downs

drop table if exists chat_room cascade;

drop table if exists chat_room_member cascade;

drop table if exists chat_room_message cascade;

drop table if exists device cascade;

drop table if exists friendship cascade;

drop table if exists location cascade;

drop table if exists session cascade;

drop table if exists shout cascade;

drop table if exists shout_membership cascade;

drop table if exists support_request cascade;

drop table if exists bro cascade;

drop sequence if exists chat_room_seq;

drop sequence if exists chat_room_member_seq;

drop sequence if exists chat_room_message_seq;

drop sequence if exists device_seq;

drop sequence if exists friendship_seq;

drop sequence if exists location_seq;

drop sequence if exists session_seq;

drop sequence if exists shout_seq;

drop sequence if exists shout_membership_seq;

drop sequence if exists support_request_seq;

drop sequence if exists bro_seq;

