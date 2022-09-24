-- OBSERVAÇÃO: Esse arquivo não pode ser alterado

CREATE TABLE video (
    section_id BIGINT,
    url VARCHAR(255),
    FOREIGN KEY (section_id) references Course(id)
);