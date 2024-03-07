CREATE TABLE IF NOT EXISTS public.user_test_result (
                                         id SERIAL PRIMARY KEY,
                                         user_id INT NOT NULL,
                                         test_id INT NOT NULL,
                                         score INT,
                                         complete_time BIGINT,
                                         FOREIGN KEY (user_id) REFERENCES public.users(id),
                                         FOREIGN KEY (test_id) REFERENCES public.test(id)
);

ALTER TABLE public.users
    ADD COLUMN user_test_result_id INT;

ALTER TABLE public.users
    ADD CONSTRAINT fk_user_test_result
        FOREIGN KEY (user_test_result_id) REFERENCES public.user_test_result(id);

ALTER TABLE public.question
    add column question_num INT;

alter table public.answer
    add column answer_num INT;