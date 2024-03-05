CREATE TABLE IF NOT EXISTS public.test (
                             id SERIAL PRIMARY KEY,
                             title VARCHAR(255) NOT NULL,
                             description TEXT,
                             specialty VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS public.question (
                                 id SERIAL PRIMARY KEY,
                                 question_txt TEXT
);

CREATE TABLE IF NOT EXISTS public.answer (
                               id SERIAL PRIMARY KEY,
                               answer_txt TEXT NOT NULL,
                               is_correct BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS public.test_questions (
                                       test_id INT REFERENCES public.test(id),
                                       question_id INT REFERENCES public.question(id),
                                       PRIMARY KEY (test_id, question_id)
);

CREATE TABLE IF NOT EXISTS public.question_answers (
                                         question_id INT REFERENCES public.question(id),
                                         answer_id INT REFERENCES public.answer(id),
                                         PRIMARY KEY (question_id, answer_id)
);
