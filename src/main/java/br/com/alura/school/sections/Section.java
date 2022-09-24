package br.com.alura.school.sections;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String code;

    @Size(min = 5)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Deprecated
    protected Section() {
    }

    public Section(String code, String title, Course course, User author) {
        this.code = code;
        this.title = title;
        this.course = course;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public Course getCourse() {
        return course;
    }

    public User getAuthor() {
        return author;
    }
}
