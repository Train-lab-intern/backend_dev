create type userLevel as enum ('BA', 'FRONT', 'BACK', 'DESIGN', 'AQA', 'QA', 'PM');
create  type speciality as enum ('RAW','RARE','MEDIUM_RARE','MEDIUM','MEDIUM_WELL','WELL_DONE');

alter table users
    add firstname varchar(256),
    add secondname varchar(256),
    add userlevel userLevel,
    add speciality speciality;