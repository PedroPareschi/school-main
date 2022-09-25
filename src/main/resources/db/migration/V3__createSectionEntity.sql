-- OBSERVAÇÃO: Esse arquivo não pode ser alterado

CREATE TABLE Section (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    title VARCHAR(100) NOT NULL,
    course_id BIGINT,
    author_id BIGINT,
    FOREIGN KEY (course_id) references Course(id),
    FOREIGN KEY (author_id) references User(id)
);