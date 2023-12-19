CREATE TABLE refresh_sessions (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
  refresh_token UUID NOT NULL,
  expired_at TIMESTAMP(6) NOT NULL,
  issued_at TIMESTAMP(6) NOT NULL DEFAULT
);
