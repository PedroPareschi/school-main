package br.com.alura.school.course;

import br.com.alura.school.sections.Section;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(max = 10)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String code;

    @Size(max = 20)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "course")
    private List<Section> sections;

    @Deprecated
    protected Course() {
    }

    public Course(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.sections = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Section> getSections() {
        return sections;
    }
}
