
ALTER TABLE users ADD COLUMN if not exists surname VARCHAR(256);

ALTER TABLE users ADD COLUMN if not exists  generated_name VARCHAR(256) UNIQUE;
-- ALTER TABLE users ALTER COLUMN generated_name SET NOT NULL;

ALTER TABLE users ADD COLUMN if not exists specialty VARCHAR(20);
ALTER  TABLE  users ADD COLUMN if not exists  user_level VARCHAR(20);
