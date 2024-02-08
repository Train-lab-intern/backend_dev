CREATE TABLE IF NOT EXISTS speciality(
    speciality_id SERIAL PRIMARY KEY,
    speciality_name VARCHAR(128) NOT NULL UNIQUE
);

ALTER TABLE users ADD COLUMN speciality_id INT;
ALTER TABLE users ADD FOREIGN KEY(speciality_id) REFERENCES speciality(speciality_id) ON DELETE CASCADE;
ALTER TABLE users ADD COLUMN level_id INT;
ALTER TABLE users ADD FOREIGN KEY(level_id) REFERENCES user_level(id) ON DELETE CASCADE;
ALTER TABLE users ADD COLUMN surname VARCHAR(256);

ALTER TABLE users ADD COLUMN generated_name VARCHAR(256) UNIQUE;
ALTER TABLE users ALTER COLUMN generated_name SET NOT NULL;

INSERT INTO speciality(speciality_name)
VALUES ('BA/SA'),
       ('FRONT-END'),
       ('BACK-END'),
       ('DESIGN'),
       ('AQA'),
       ('QA'),
       ('PM');

CREATE TABLE IF NOT EXISTS user_level(
    id SERIAL PRIMARY KEY,
    level VARCHAR(128) NOT NULL UNIQUE
);

INSERT INTO user_level(level)
VALUES ('RAW'),
       ('RARE'),
       ('MEDIUM_RARE'),
       ('MEDIUM'),
       ('MEDIUM_WELL'),
       ('WELL_DONE');