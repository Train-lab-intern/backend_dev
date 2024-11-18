ALTER TABLE public.user_test_result
    ADD COLUMN if not exists date DATE,
    ADD COLUMN if not exists level VARCHAR(255);

CREATE TABLE public.user_stats (
                                   id SERIAL PRIMARY KEY,
                                   user_id BIGINT NOT NULL,
                                   level VARCHAR(255),
                                   specialty VARCHAR(255),
                                   score INTEGER,
                                   pre_score INTEGER,
                                   test_count INTEGER,
                                   FOREIGN KEY (user_id) REFERENCES public.users(id)
);