-- OBSERVAÇÃO: Esse arquivo não pode ser alterado

CREATE TABLE enrollment (
   date_time TIMESTAMP,
   student_id BIGINT NOT NULL,
   course_id BIGINT NOT NULL,
   CONSTRAINT pk_enrollment PRIMARY KEY (student_id, course_id)
);