ALTER TABLE  users
    ADD COLUMN if not exists specialties VARCHAR(50) ARRAY;

CREATE TABLE if not exists user_specialties (
                                  user_id BIGINT NOT NULL,
                                  specialty VARCHAR(50) NOT NULL,
                                  FOREIGN KEY (user_id) REFERENCES users(id)
);