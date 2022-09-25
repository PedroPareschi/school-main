insert into User (username, email, role) values ('alex', 'alex@email.com', 'INSTRUCTOR');
insert into User (username, email, role) values ('ana', 'ana@email.com', 'STUDENT');

insert into Course (code, name, description) values ('java-1', 'Java OO', 'Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.');
insert into Course (code, name, description) values ('java-2', 'Java Collections', 'Java Collections: Lists, Sets, Maps and more.');

insert into Section (code, title, author_id, course_id) values ('flutter-cores-dinamicas', 'Flutter: Configurando cores dinâmicas', 1, 1);

insert into video (section_id, url) values (1, 'https://www.youtube.com/watch?v=gI4-vj0WpKM');

insert into enrollment (date_time, course_id, student_id) values (now(), 1, 2);